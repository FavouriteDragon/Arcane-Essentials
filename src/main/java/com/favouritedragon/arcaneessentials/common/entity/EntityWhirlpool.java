package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.SPLASH;

public class EntityWhirlpool extends EntityMagicConstruct {

    private static final DataParameter<Float> SYNC_VORTEX_HEIGHT = EntityDataManager.createKey(EntityWhirlpool.class,
            DataSerializers.FLOAT);

    public EntityWhirlpool(World world) {
        super(world);
        this.height = 1F;
        this.width = 1F;
    }

    public float getVortexHeight() {
        return dataManager.get(SYNC_VORTEX_HEIGHT);
    }

    public void setVortexHeight(float height) {
        dataManager.set(SYNC_VORTEX_HEIGHT, height);
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(SYNC_VORTEX_HEIGHT, 1.0F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Block belowBlock = world.getBlockState(getPosition()).getBlock();
        if (belowBlock == Blocks.FIRE) {
            world.setBlockToAir(getPosition());
        }
        if (belowBlock == Blocks.LAVA) {
            world.setBlockState(getPosition(), Blocks.STONE.getDefaultState());
        }


        if (ticksExisted % 10 == 0) {
            world.playSound(posX, posY, posZ, SoundEvents.ENTITY_PLAYER_SWIM, SoundCategory.AMBIENT, 2.0F,
                    world.rand.nextFloat() * 0.2F + 1.0F, true);
        }
        if (!this.world.isRemote) {

            List<EntityLivingBase> targets = EntityUtils.getEntitiesWithinRadius(3.0d, this.posX, this.posY,
                    this.posZ, this.world, EntityLivingBase.class);

            for (EntityLivingBase target : targets) {

                if (this.isValidTarget(target) && target != getOwner() && target != getCaster()) {

                    double velY = target.motionY;

                    double dx = this.posX - target.posX > 0 ? 0.5 - (target.posX - this.posX) / 8
                            : -0.5 - (this.posX - target.posX) / 8;
                    double dz = this.posZ - target.posZ > 0 ? 0.5 - (target.posZ - this.posZ) / 8
                            : -0.5 - (this.posZ - target.posZ) / 8;
                    if (this.getCaster() != null) {
                        target.attackEntityFrom(
                                MagicDamage.causeIndirectMagicDamage(this, getCaster(), SPLASH),
                                1 * damageMultiplier);
                    } else {
                        target.attackEntityFrom(DamageSource.DROWN, 0.25F * damageMultiplier);
                    }

                    target.motionX = dx / 4;
                    target.motionY = velY + 0.05;
                    target.motionZ = dz / 4;

                    ArcaneUtils.applyPlayerKnockback(target);

                }
            }

        } else {
            if (ticksExisted <= 2 || ticksExisted % 4 == 0) {
                int maxAngle = 120 + (int) (getRenderSize() * 60);
                ArcaneUtils.spawnSpinningVortex(world, maxAngle, getVortexHeight(), 0.01, maxAngle / getRenderSize(), ParticleBuilder.Type.MAGIC_BUBBLE,
                        new Vec3d(posX, posY, posZ), -2, Vec3d.ZERO, 20);
            }
        }
    }


    @Override
    public void setDead() {
        if (!world.isRemote) {
            List<EntityLivingBase> targets = EntityUtils.getEntitiesWithinRadius(3.0d, this.posX, this.posY,
                    this.posZ, this.world, EntityLivingBase.class);

            for (EntityLivingBase target : targets) {

                if (this.isValidTarget(target)) {

                    double velY = target.motionY;

                    double dx = this.posX - target.posX > 0 ? 0.5 - (this.posX - target.posX) / 8
                            : -0.5 - (this.posX - target.posX) / 8;
                    double dz = this.posZ - target.posZ > 0 ? 0.5 - (this.posZ - target.posZ) / 8
                            : -0.5 - (this.posZ - target.posZ) / 8;
                    if (this.getCaster() != null) {
                        target.attackEntityFrom(
                                MagicDamage.causeIndirectMagicDamage(this, getCaster(), SPLASH),
                                1 * damageMultiplier);
                    } else {
                        target.attackEntityFrom(DamageSource.DROWN, 1F * damageMultiplier);
                    }
                    target.motionX = dx * 2;
                    target.motionY = velY + 0.2;
                    target.motionZ = dz * 2;
                    ArcaneUtils.applyPlayerKnockback(target);
                    ArcaneUtils.spawnSpinningVortex(world, 180, 5, 0.25, 60, ParticleBuilder.Type.MAGIC_BUBBLE,
                            new Vec3d(posX, posY, posZ), new Vec3d(0.4, 0.2, 0.4), Vec3d.ZERO, 20, 0, 0, 0);
                }

            }
        }
        this.isDead = true;
    }
}