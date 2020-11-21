package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityThunderBurst extends EntityMagicConstruct {

    public EntityThunderBurst(World world) {
        super(world);
        setSize(1, 1);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        double speed = 1;
        if (!world.isRemote) {
            List<EntityLivingBase> targets = EntityUtils
                    .getEntitiesWithinRadius((this.ticksExisted * speed), this.posX, this.posY, this.posZ, world, EntityLivingBase.class);

            // In this particular instance, the caster is completely unaffected because they will always be in the
            // centre.
            targets.remove(this.getCaster());

            for (EntityLivingBase target : targets) {
                if (target != this.getCaster()) {
                    if (this.isValidTarget(target)) {
                        target.attackEntityFrom(
                                MagicDamage.causeIndirectMagicDamage(this, this.getCaster(), MagicDamage.DamageType.SHOCK),
                                10 * this.damageMultiplier);
                        double dx = target.posX - getCaster().posX;
                        double dz = target.posZ - getCaster().posZ;
                        // Normalises the velocity.
                        double vectorLength = MathHelper.sqrt(dx * dx + dz * dz);
                        dx /= vectorLength;
                        dz /= vectorLength;

                        target.motionX = 2 * dx;
                        target.motionY = 0.2;
                        target.motionZ = 2 * dz;

                        // Player motion is handled on that player's client so needs packets
                        if (target instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
                        }
                    }


                }
            }
            if (ticksExisted * speed > 8) {
                this.setDead();
            }
        }

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }


}
