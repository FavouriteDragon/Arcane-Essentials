package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.world.World;

import java.util.List;

public class EntityThunderBurst extends EntityMagicConstruct {

	public EntityThunderBurst(World world) {
		super(world);
		setSize(10, 10);
	}

	public EntityThunderBurst(World world, double x, double y, double z, EntityLivingBase caster, int lifetime,
							  float damageMultiplier){
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		setSize(10, 10);
	}


	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		double speed = 1;
		List<EntityLivingBase> targets = WizardryUtilities
				.getEntitiesWithinRadius((this.ticksExisted * speed), this.posX, this.posY, this.posZ, world);

		// In this particular instance, the caster is completely unaffected because they will always be in the
		// centre.
		targets.remove(this.getCaster());

		for(EntityLivingBase target : targets) {
			if (target != this.getCaster()) {


				// Knockback must be removed in this instance, or the target will fall into the floor.
				double motionX = target.motionX;
				double motionZ = target.motionZ;

				if (this.isValidTarget(target)) {
					target.attackEntityFrom(
							MagicDamage.causeIndirectMagicDamage(this, this.getCaster(), MagicDamage.DamageType.SHOCK),
							10 * this.damageMultiplier);
				}

				// All targets are thrown, even those immune to the damage, so they don't fall into the ground.
				target.motionX = motionX;
				target.motionY = 0.8; // Throws target into the air.
				target.motionZ = motionZ;

				// Player motion is handled on that player's client so needs packets
				if (target instanceof EntityPlayerMP) {
					((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
				}
			}
			if (ticksExisted * speed > this.width) {
				this.setDead();
			}
		}

	}
}
