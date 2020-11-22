package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class IcicleStorm extends SpellRay {

    public IcicleStorm() {
        super("icicle_storm", true, EnumAction.NONE);
        this.particleVelocity(1);
        this.particleJitter(0.3);
        this.particleSpacing(0.125);
        addProperties(DAMAGE, EFFECT_STRENGTH, EFFECT_DURATION);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (target instanceof EntityLivingBase) {

            if (MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, target)) {
                if (!world.isRemote && ticksInUse == 1 && caster instanceof EntityPlayer) ((EntityPlayer) caster)
                        .sendStatusMessage(new TextComponentTranslation("spell.resist", target.getName(),
                                this.getNameForTranslationFormatted()), true);
                // This now only damages in line with the maxHurtResistantTime. Some mods don't play nicely and fiddle
                // with this mechanic for their own purposes, so this line makes sure that doesn't affect wizardry.
            } else if (ticksInUse % ((EntityLivingBase) target).maxHurtResistantTime == 1) {
                ((EntityLivingBase) target).addPotionEffect(new PotionEffect(WizardryPotions.frost,
                        getProperty(EFFECT_DURATION).intValue(), getProperty(EFFECT_STRENGTH).intValue(), false, false));
                EntityUtils.attackEntityWithoutKnockback(target,
                        MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST),
                        getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));
            }
        }

        return true;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return true;
    }

    @Override
    protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
        ParticleBuilder.create(ParticleBuilder.Type.ICE).pos(x, y, z).vel(vx, vy, vz).scale(2 + world.rand.nextFloat()).collide(true).spawn(world);
        ParticleBuilder.create(ParticleBuilder.Type.ICE).pos(x, y, z).vel(vx, vy, vz).scale(2 + world.rand.nextFloat()).collide(true).spawn(world);
    }

    @Override
    public void playSound(World world, EntityLivingBase caster) {
        world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_SHARD_SMASH,
                SoundCategory.PLAYERS, 0.4F + world.rand.nextFloat() / 10, 0.6F + world.rand.nextFloat() / 10,
                true);
        world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_BLIZZARD_AMBIENT,
                SoundCategory.PLAYERS, 0.2F + world.rand.nextFloat() / 10, 0.6F + world.rand.nextFloat() / 10,
                true);

    }

    @Override
    public boolean isAxeCastable() {
        return false;
    }

    @Override
    public boolean isShieldCastable() {
        return false;
    }

    @Override
    public boolean castRightClick() {
        return true;
    }
}