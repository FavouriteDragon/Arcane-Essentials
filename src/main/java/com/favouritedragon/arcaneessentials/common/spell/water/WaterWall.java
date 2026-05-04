package com.favouritedragon.arcaneessentials.common.spell.water;

import com.favouritedragon.arcaneessentials.common.entity.EntityWaterWall;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.LIFETIME;
import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.SIZE;

public class WaterWall extends ArcaneSpell {

    public WaterWall() {
        super("water_wall", EnumAction.BOW, false);
        addProperties(SIZE, LIFETIME);
        this.npcSelector = npcSelector.or((entityLiving, aBoolean) -> entityLiving != null);
    }

    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse,
                        SpellModifiers modifiers) {
        return cast(world, (EntityLivingBase) caster, modifiers);
    }

    @Override
    public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse,
                        EntityLivingBase target, SpellModifiers modifiers) {
        return cast(world, (EntityLivingBase) caster, modifiers);
    }

    private boolean cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
        float size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        int lifetime = (int) (getProperty(LIFETIME).floatValue()
                * modifiers.get(WizardryItems.duration_upgrade));

        // Place the wall at the raytrace hit point, or 3 blocks in front of the caster
        double dist = Math.max(3, size * 1.5);
        RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, dist, true);
        Vec3d pos;
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            pos = result.hitVec;
        } else {
            float yawRad = caster.rotationYaw * (float) Math.PI / 180f;
            pos = caster.getPositionVector().add(
                    -MathHelper.sin(yawRad) * dist,
                    0,
                    MathHelper.cos(yawRad) * dist);
        }

        world.playSound(null, caster.posX, caster.posY, caster.posZ,
                SoundEvents.ENTITY_GENERIC_SPLASH, caster.getSoundCategory(), 1.5f,
                0.8f + world.rand.nextFloat() * 0.2f);
        world.playSound(null, caster.posX, caster.posY, caster.posZ,
                SoundEvents.BLOCK_WATER_AMBIENT, caster.getSoundCategory(), 1.5f,
                0.9f + world.rand.nextFloat() * 0.2f);

        if (!world.isRemote) {
            EntityWaterWall wall = new EntityWaterWall(world);
            wall.setCaster(caster);
            // Orient the wall perpendicular to the caster's facing direction
            wall.rotationYaw = caster.rotationYaw;
            wall.setSize(size);
            wall.lifetime = lifetime;
            wall.setPosition(pos.x, pos.y, pos.z);
            return world.spawnEntity(wall);
        }
        return false;
    }
}
