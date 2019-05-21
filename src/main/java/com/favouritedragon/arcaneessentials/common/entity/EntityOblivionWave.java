package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityOblivionWave extends EntityMagicConstruct {

	private static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityOblivionWave.class, DataSerializers.FLOAT);

	private int soulsReaped;

	public EntityOblivionWave(World par1World) {
		super(par1World);
	}

	public EntityOblivionWave(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		this.soulsReaped = 0;
	}


	public void setSize(float size) {
		dataManager.set(SYNC_SIZE, size);
	}

	public float getSize() {
		return dataManager.get(SYNC_SIZE);
	}
 	@Override
	protected void entityInit() {
		dataManager.register(SYNC_SIZE, 1F);

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
		if (!world.isRemote) {
			List<EntityLivingBase> targets = WizardryUtilities
					.getEntitiesWithinRadius((this.ticksExisted * speed), this.posX, this.posY, this.posZ, world);

			// In this particular instance, the caster is completely unaffected because they will always be in the
			// centre.
			targets.remove(this.getCaster());

			for (EntityLivingBase target : targets) {
				if (target != this.getCaster()) {
					if (this.isValidTarget(target)) {
						if (target.getHealth() <= 10 * this.damageMultiplier) {
							soulsReaped ++;
						}
						target.attackEntityFrom(
								MagicDamage.causeIndirectMagicDamage(this, this.getCaster(), MagicDamage.DamageType.WITHER),
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
			if (soulsReaped > 0 && !world.isRemote && getCaster() != null) {
				for (int i = 0; i < soulsReaped; i++) {
					getCaster().addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH));
					getCaster().addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, soulsReaped * 800, soulsReaped));
					getCaster().addPotionEffect(new PotionEffect(MobEffects.REGENERATION, soulsReaped * 300, soulsReaped));
					soulsReaped--;
				}
			}
			setSize((float) speed * ticksExisted);
			if (ticksExisted * speed > 8) {
				this.setDead();
			}
		}
	}
}
