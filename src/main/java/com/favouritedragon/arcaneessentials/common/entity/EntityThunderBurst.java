package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
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
		setSize(10, 10);
	}

	public EntityThunderBurst(World world, double x, double y, double z, EntityLivingBase caster, int lifetime,
							  float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		setSize(1, 1);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		double speed = 1;
		if (!world.isRemote) {
			List<EntityLivingBase> targets = WizardryUtilities
					.getEntitiesWithinRadius((this.ticksExisted * speed), this.posX, this.posY, this.posZ, world);

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
		double x, y, z;
		//Creates a sphere.
		for (double theta = 0; theta <= 180; theta += 1) {
			double dphi = 15 / Math.sin(Math.toRadians(theta));

			for (double phi = 0; phi < 360; phi += dphi) {
				double rphi = Math.toRadians(phi);
				double rtheta = Math.toRadians(theta);

				x = ticksExisted * 1 * Math.cos(rphi) * Math.sin(rtheta);
				y = ticksExisted * 1 * Math.sin(rphi) * Math.sin(rtheta);
				z = ticksExisted * 1 * Math.cos(rtheta);

				if (world.isRemote) {
					Wizardry.proxy.spawnParticle(WizardryParticleType.SPARK, world, x + this.posX, y + this.posY, z + this.posZ, 0, 0, 0, 10);
				}
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
