package com.favouritedragon.arcaneessentials.common.entity;

public class EntityMagicTornado {/*} extends EntityMagicConstruct {

	public EntityLightningVortex(World world) {
		super(world);
	}

	public EntityLightningVortex(World world, double x, double y, double z, Vec3d velocity, EntityLivingBase caster, int lifetime,
								 float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		this.height = 7.0f;
		this.width = 3.0f;
		this.motionX = velocity.x;
		this.motionY = velocity.y;
		this.motionZ = velocity.z;
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
		return isBurning();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.setSize(this.width, 7);
		Block belowBlock = world.getBlockState(getPosition()).getBlock();
		if (belowBlock != Blocks.AIR && motionY != 0) {
			motionY = 0;
		}
		if (belowBlock != Blocks.AIR && world.getBlockState(getPosition().add(0, 1, 0)).getBlock() != Blocks.AIR && motionY < 0) {
			motionY *= -1;
		}
		if (belowBlock == Blocks.LAVA || belowBlock == Blocks.FIRE) {
			this.setFire(100);
		}
		if (this.isBurning()) ArcaneUtils.spawnSpinningVortex(world, 120, 7, 0.25, 40, WizardryParticleType.MAGIC_FIRE,
				new Vec3d(posX, posY, posZ), new Vec3d(0.3, 0.1, 0.3), new Vec3d(motionX, motionY, motionZ), 10, 1, 0, 0);

		this.move(MoverType.SELF, motionX, motionY / 2, motionZ);
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

		}


	}**/
}
