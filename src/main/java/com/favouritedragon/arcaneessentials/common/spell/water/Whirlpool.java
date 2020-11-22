package com.favouritedragon.arcaneessentials.common.spell.water;

import com.favouritedragon.arcaneessentials.common.entity.EntityWhirlpool;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Whirlpool extends ArcaneSpell {

    public Whirlpool() {
        super("whirlpool", EnumAction.BOW, false);
        addProperties(RANGE, DAMAGE);
    }

    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
        double range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
        float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, range, true);
        Vec3d pos;
        if (result != null) {
            pos = result.hitVec;
        } else {
            pos = caster.getPositionVector();
        }
        EntityWhirlpool pool = new EntityWhirlpool(world);
        pool.setOwner(caster);
        pool.setPosition(pos.x, pos.y, pos.z);
        pool.setCaster(caster);
        pool.lifetime = 80 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade);
        pool.damageMultiplier = damage;
        pool.setRenderSize(2);
        pool.setVortexHeight(3);
        EntityUtils.playSoundAtPlayer(caster, SoundEvents.BLOCK_WATER_AMBIENT, 2.0F,
                world.rand.nextFloat() * 0.2F + 1.0F);
        EntityUtils.playSoundAtPlayer(caster, SoundEvents.ENTITY_PLAYER_SPLASH, 1F,
                world.rand.nextFloat() * 0.2F + 1.0F);
        EntityUtils.playSoundAtPlayer(caster, SoundEvents.ENTITY_GENERIC_SPLASH, 2.0F,
                world.rand.nextFloat() * 0.2F + 1.0F);
        if (!world.isRemote)
            return world.spawnEntity(pool);
        return false;
    }

    @Override
    public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
        double range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
        float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, range, true);
        Vec3d pos;
        if (result != null) {
            pos = result.hitVec;
        } else {
            pos = caster.getPositionVector();
        }
        EntityWhirlpool pool = new EntityWhirlpool(world);
        pool.setOwner(caster);
        pool.setPosition(pos.x, pos.y, pos.z);
        pool.setCaster(caster);
        pool.lifetime = 80 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade);
        pool.damageMultiplier = damage;
        pool.setRenderSize(2);
        pool.setVortexHeight(3);
        world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.HOSTILE, 2.0F,
                world.rand.nextFloat() * 0.2F + 1.0F, true);
        world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.HOSTILE, 1F,
                world.rand.nextFloat() * 0.2F + 1.0F, true);
        world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.HOSTILE, 2.0F,
                world.rand.nextFloat() * 0.2F + 1.0F, true);
        if (!world.isRemote)
            return world.spawnEntity(pool);
        return false;
    }

}
