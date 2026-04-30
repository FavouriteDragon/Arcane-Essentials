package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.List;

public class EntitySaintessSun extends EntityMagicConstruct {

    private float damage;
    private int fireTime;
    private float knockback;

    public EntitySaintessSun(World world) {
        super(world);
        this.noClip = true;
        this.ignoreFrustumCheck = true;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getFireTime() {
        return this.fireTime;
    }

    public void setFireTime(int fireTime) {
        this.fireTime = fireTime;
    }

    public float getKnockback() {
        return this.knockback;
    }

    public void setKnockback(float knockback) {
        this.knockback = knockback;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase caster = getCaster();
        if (caster != null) {
            float orbitHeight = getSize() * 1.5F;
            Vec3d targetPos = ArcaneUtils.getEntityPos(caster).add(0, orbitHeight, 0);
            Vec3d entityPos = getPositionVector();
            Vec3d targetVelocity = targetPos.subtract(entityPos).scale(0.1);
            motionX = targetVelocity.x;
            motionY = targetVelocity.y;
            motionZ = targetVelocity.z;
            velocityChanged = true;

            List<Entity> targets = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow(orbitHeight * 1.25F));
            for (Entity target : targets) {
                if (isValidTarget(target) && target != this && target != caster) {
                    if (target instanceof EntityLivingBase && ((EntityLivingBase) target).hurtTime == 0)
                        shootBeam(caster, target);
                }
            }

            if (ticksExisted < 2)
                world.playSound(posX, posY, posZ, WizardrySounds.ENTITY_RADIANT_TOTEM_AMBIENT,
                        WizardrySounds.SPELLS, 3.0F, 1.5F, false);
        }
        move(MoverType.SELF, motionX, motionY, motionZ);
        if (world.isRemote) {
            for (double i = 0; i < width; i += 0.30) {
                Vec3d pos = ArcaneUtils.getMiddleOfEntity(this);
                double x = world.rand.nextDouble() / 2 * width * world.rand.nextGaussian();
                double y = world.rand.nextDouble() / 2 * height * world.rand.nextGaussian();
                double z = world.rand.nextDouble() / 2 * width * world.rand.nextGaussian();
                pos = pos.add(x, y, z);
                ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(pos).clr(1.0F, 1.0F, ArcaneUtils.getRandomNumberInRange(90, 255) / 255F)
                        .time(12 + ArcaneUtils.getRandomNumberInRange(2, 4)).scale(getSize() / 2).vel(world.rand.nextGaussian() / 20,
                        world.rand.nextGaussian() / 20, world.rand.nextGaussian() / 20)
                        .spawn(world);
                ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(pos).clr(1.0F, 1.0F, ArcaneUtils.getRandomNumberInRange(90, 255) / 255F)
                        .time(10 + ArcaneUtils.getRandomNumberInRange(2, 4)).scale(getSize()).vel(world.rand.nextGaussian() / 20,
                        world.rand.nextGaussian() / 20, world.rand.nextGaussian() / 20)
                        .spawn(world);
            }
        }
    }

    public void shootBeam(EntityLivingBase caster, Entity target) {
        if (caster != null && world.isRemote) {
            ParticleBuilder.create(ParticleBuilder.Type.BEAM).time(12 + ArcaneUtils.getRandomNumberInRange(0, 4))
                    .clr(1.0F, 1.0F, 0.3F).entity(caster).pos(ArcaneUtils.getMiddleOfEntity(this)
                    .subtract(caster.getPositionVector())).target(target.getPositionVector().add(0, target.getEyeHeight() / 2, 0))
                    .scale(getSize()).spawn(world);
            world.playSound(target.posX, target.posY, target.posZ, WizardrySounds.ENTITY_HAMMER_EXPLODE,
                    WizardrySounds.SPELLS, 1.5F + world.rand.nextFloat() / 4, 0.75F + world.rand.nextFloat() / 4, true);
            world.playSound(target.posX, target.posY, target.posZ, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT,
                    WizardrySounds.SPELLS, 1.5F + world.rand.nextFloat() / 4, 0.875F + world.rand.nextFloat() / 4, true);
        }
        if (caster != null && !world.isRemote) {
            DamageSource damageSource = MagicDamage.causeIndirectMagicDamage(this, caster, MagicDamage.DamageType.RADIANT);
            float damage = getDamage();
            if (!target.getIsInvulnerable() && !MagicDamage.isEntityImmune(MagicDamage.DamageType.RADIANT, target)
                    && target.attackEntityFrom(damageSource, damage / 4)) {
                target.setFire(getFireTime());
                Vec3d entityPos = target.getPositionVector();
                Vec3d sourcePos = getPositionVector();
                Vec3d vel = sourcePos.subtract(entityPos).scale(0.005);
                target.addVelocity(vel.x, vel.y + 0.15, vel.z);
            } else if (!target.attackEntityFrom(damageSource, damage) && target instanceof EntityDragon) {
                if (((EntityDragon) target).attackEntityFromPart(((EntityDragon) target).dragonPartBody, damageSource, damage)) {
                    target.setFire(getFireTime());
                }
            }
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void resetPositionToBB() {
        super.resetPositionToBB();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRenderInPass(int pass) {
        return super.shouldRenderInPass(pass) || pass == 1;
    }
}
