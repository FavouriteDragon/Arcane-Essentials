package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.PRESSURE;

public class EntityCycloneShield extends EntityMagicConstruct {

    private static final DataParameter<Float> SYNC_RADIUS = EntityDataManager.createKey(EntityCycloneShield.class, DataSerializers.FLOAT);

    public EntityCycloneShield(World par1World) {
        super(par1World);
    }

    public EntityCycloneShield(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier, float radius) {
        super(world);
        this.setOwner(caster);
        this.setPosition(x, y, z);
        this.setCaster(caster);
        this.lifetime = lifetime;
        this.damageMultiplier = damageMultiplier;
        setRadius(radius);
    }

    private void Dissipate() {
        if (!world.isRemote && getCaster() != null) {
            double x = posX;
            double y = posY + getCaster().getEyeHeight();
            double z = posZ;
            List<EntityLivingBase> targets = EntityUtils.getEntitiesWithinRadius(getRadius() * 1.25F, x, y,
                    z, world, EntityLivingBase.class);
            for (EntityLivingBase target : targets) {
                if (isValidTarget(target)) {
                    target.addVelocity((target.posX - x),
                            (target.posY - (y)) + 0.1, (target.posZ - z));
                    // Player motion is handled on that player's client so needs packets
                    if (!MagicDamage.isEntityImmune(PRESSURE, target)) {
                        target.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster(), PRESSURE), 4 * damageMultiplier);
                    }
                    target.setEntityInvulnerable(false);
                    ArcaneUtils.applyPlayerKnockback(target);
                }
            }
            AxisAlignedBB box = new AxisAlignedBB(x + getRadius() * 1.5F, y + getRadius() * 1.5F, z + getRadius() * 1.5F, x - getRadius() * 1.5F,
                    y - getRadius() * 1.5F, z - getRadius() * 1.5F);
            List<Entity> projectiles = world.getEntitiesWithinAABB(Entity.class, box);
            for (Entity projectile : projectiles) {
                if ((projectile.canBeCollidedWith() && projectile.canBePushed() || projectile instanceof EntityArrow || projectile instanceof EntityThrowable) &&
                        !(projectile instanceof EntityLivingBase)) {
                    projectile.motionX = projectile.posX - x;
                    projectile.motionY = projectile.posY - y;
                    projectile.motionZ = projectile.posZ - z;
                }
            }
        }

    }

    public float getRadius() {
        return dataManager.get(SYNC_RADIUS);
    }

    public void setRadius(float radius) {
        dataManager.set(SYNC_RADIUS, radius);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(SYNC_RADIUS, 1F);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && getCaster() != null) {
            double x = posX;
            double y = posY + getCaster().getEyeHeight();
            double z = posZ;
            List<EntityLivingBase> targets = EntityUtils.getEntitiesWithinRadius(getRadius(), x, y,
                    z, world, EntityLivingBase.class);
            for (EntityLivingBase target : targets) {
                if (target != getCaster()) {
                    if (this.isValidTarget(target)) {
                        boolean b = world.rand.nextBoolean();
                        double multiplier = (getRadius() - target.getDistance(x, y, z)) * 0.0025;
                        if (b) {
                            target.addVelocity((target.posX - x) * multiplier,
                                    (target.posY - (y)) * multiplier, (target.posZ - z) * multiplier);
                        } else {
                            target.addVelocity((x - target.posX) * multiplier,
                                    (target.posY - (y)) * multiplier, (z - target.posZ) * multiplier);
                        }
                        if (!MagicDamage.isEntityImmune(PRESSURE, target)) {
                            target.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster(), PRESSURE), 0.5F * damageMultiplier);
                        }
                        target.setEntityInvulnerable(false);
                        ArcaneUtils.applyPlayerKnockback(target);
                    }
                }
            }
            AxisAlignedBB box = new AxisAlignedBB(x + getRadius() * 1.25F, y + getRadius() * 1.25F, z + getRadius() * 1.25F, x - getRadius() * 1.25F,
                    y - getRadius() * 1.25F, z - getRadius() * 1.25F);
            List<Entity> projectiles = world.getEntitiesWithinAABB(Entity.class, box);
            for (Entity projectile : projectiles) {
                if ((projectile.canBeCollidedWith() && projectile.canBePushed() || projectile instanceof EntityArrow || projectile instanceof EntityThrowable) &&
                        !(projectile instanceof EntityLivingBase)) {
                    double multiplier = (getRadius() - projectile.getDistance(x, y, z)) * 0.0025;
                    projectile.motionX = (projectile.posX - x) * multiplier;
                    projectile.motionY = (projectile.posY - (y)) * multiplier;
                    projectile.motionZ = (projectile.posZ - z) * multiplier;
                }
            }

        }
        if (ticksExisted % 4 == 0 && getCaster() != null) {
            world.playSound(getCaster().posX, getCaster().posY, getCaster().posZ, WizardrySounds.ENTITY_BLIZZARD_AMBIENT, SoundCategory.PLAYERS, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 20, true);
        }
    }

    @Override
    public void setDead() {
        Dissipate();
        super.setDead();
    }


}
