package com.favouritedragon.arcaneessentials.common;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityLightningVortex extends EntityMagicConstruct {

	public EntityLightningVortex(World par1World) {
		super(par1World);
		this.height = 7.0f;
		this.width = 3.0f;
	}

	public EntityLightningVortex(World world, double x, double y, double z, EntityLivingBase caster, int lifetime,
								 float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		this.height = 7.0f;
		this.width = 3.0f;
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
		if (ticksExisted % 10 == 0) {
			world.playSound(posX, posY, posZ, WizardrySounds.SPELL_LOOP_LIGHTNING, SoundCategory.AMBIENT, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
		}
		if (!this.world.isRemote) {

			List<EntityLivingBase> targets = WizardryUtilities.getEntitiesWithinRadius(3.0d, this.posX, this.posY,
					this.posZ, this.world);

			for (EntityLivingBase target : targets) {

				if (this.isValidTarget(target)) {

					double velY = target.motionY;

					double dx = this.posX - target.posX > 0 ? 0.5 - (this.posX - target.posX) / 8
							: -0.5 - (this.posX - target.posX) / 8;
					double dz = this.posZ - target.posZ > 0 ? 0.5 - (this.posZ - target.posZ) / 8
							: -0.5 - (this.posZ - target.posZ) / 8;
					target.setFire(4);
					if (this.getCaster() != null) {
						target.attackEntityFrom(
								MagicDamage.causeIndirectMagicDamage(this, getCaster(), MagicDamage.DamageType.SHOCK),
								1 * damageMultiplier);
					} else {
						target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1 * damageMultiplier);
					}

					target.motionX = dx;
					target.motionY = velY + 0.2;
					target.motionZ = dz;

					ArcaneUtils.applyPlayerKnockback(target);

				}
			}
			ArcaneUtils.spawnSpinningVortex(world, 360, 7, 120, WizardryParticleType.SPARK,
					new Vec3d(posX, posY, posZ), new Vec3d(0.15, 0.05, 0.15), 2, 0, 0, 0);
		}



	}
}
