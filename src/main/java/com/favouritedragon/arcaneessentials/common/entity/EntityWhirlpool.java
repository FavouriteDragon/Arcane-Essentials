package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneEnums.WATER;

public class EntityWhirlpool extends EntityMagicConstruct {

	public EntityWhirlpool(World world) {
		super(world);
		this.height = 1F;
		this.width = 1F;
	}

	public EntityWhirlpool(World world, double x, double y, double z, EntityLivingBase caster, int lifetime,
						   float damageMultiplier, float width, float height) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		this.height = height;
		this.width = width;
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
	public boolean canRenderOnFire() {
		return false;
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

			List<EntityLivingBase> targets = WizardryUtilities.getEntitiesWithinRadius(3.0d, this.posX, this.posY,
					this.posZ, this.world);

			for (EntityLivingBase target : targets) {

				if (this.isValidTarget(target)) {

					double velY = target.motionY;

					double dx = this.posX - target.posX > 0 ? 0.5 - (target.posX - this.posX) / 8
							: -0.5 - (this.posX - target.posX) / 8;
					double dz = this.posZ - target.posZ > 0 ? 0.5 - (target.posZ - this.posZ) / 8
							: -0.5 - (this.posZ - target.posZ) / 8;
					if (this.getCaster() != null) {
						target.attackEntityFrom(
								MagicDamage.causeIndirectMagicDamage(this, getCaster(), WATER),
								1 * damageMultiplier);
					} else {
						target.attackEntityFrom(DamageSource.DROWN, 0.25F * damageMultiplier);
					}

					target.motionX = dx / 4;
					target.motionY = velY + 0.2;
					target.motionZ = dz / 4;

					ArcaneUtils.applyPlayerKnockback(target);

				}
			}
			ArcaneUtils.spawnSpinningVortex(world, 180, 2, 0.25, 60, WizardryParticleType.MAGIC_BUBBLE,
					new Vec3d(posX, posY, posZ), new Vec3d(0.15, 0.05, 0.15), Vec3d.ZERO, 2, 0, 0, 0);

		}


	}

	@Override
	public void despawn() {
		if (!world.isRemote) {
			List<EntityLivingBase> targets = WizardryUtilities.getEntitiesWithinRadius(3.0d, this.posX, this.posY,
					this.posZ, this.world);

			for (EntityLivingBase target : targets) {

				if (this.isValidTarget(target)) {

					double velY = target.motionY;

					double dx = this.posX - target.posX > 0 ? 0.5 - (this.posX - target.posX) / 8
							: -0.5 - (this.posX - target.posX) / 8;
					double dz = this.posZ - target.posZ > 0 ? 0.5 - (this.posZ - target.posZ) / 8
							: -0.5 - (this.posZ - target.posZ) / 8;
					if (this.getCaster() != null) {
						target.attackEntityFrom(
								MagicDamage.causeIndirectMagicDamage(this, getCaster(), WATER),
								1 * damageMultiplier);
					} else {
						target.attackEntityFrom(DamageSource.DROWN, 0.25F * damageMultiplier);
					}
					target.motionX = dx * 2;
					target.motionY = velY + 0.2;
					target.motionZ = dz * 2;
					ArcaneUtils.applyPlayerKnockback(target);
					ArcaneUtils.spawnSpinningVortex(world, 180, 5, 0.25, 60, WizardryParticleType.MAGIC_BUBBLE,
							new Vec3d(posX, posY, posZ), new Vec3d(0.4, 0.2, 0.4), Vec3d.ZERO, 20, 0, 0, 0);
				}

			}
		}
		this.setDead();
	}
}