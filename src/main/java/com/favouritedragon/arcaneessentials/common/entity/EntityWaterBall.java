package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class EntityWaterBall extends EntityMagicBolt {

	private float damage = 5;
	private boolean spawnWhirlPool;
	private boolean spawnGeysers;
	private boolean splashed;

	public EntityWaterBall(World world) {
		super(world);
		this.blockBoxX = blockBoxY = blockBoxZ = getSize() / 10;
	}

	public void setSpawnWhirlPool(boolean whirlPool) {
		this.spawnWhirlPool = whirlPool;
	}

	public void setSpawnGeysers(boolean spawnGeysers) {
		this.spawnGeysers = spawnGeysers;
	}

	@Override
	protected boolean doGravity() {
		return true;
	}

	@Override
	public double getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	@Override
	public boolean doDeceleration() {
		return false;
	}

	@Override
	public int getLifetime() {
		return 200;
	}

	private void Splash() {
		if (splashed) return;
		splashed = true;

		world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				0.8F + world.rand.nextFloat() / 10F);
		List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow(getSize() / 2));
		if (!hit.isEmpty()) {
			for (Entity target : hit) {
				if (target != this && target != getCaster()) {
					if (target.canBeCollidedWith()) {
						if (!world.isRemote) {
							target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(getCaster(),
									getDamageType()), (float) getDamage() * 0.4F);
							target.addVelocity(motionX / 4 * getSize(), motionY / 4 * getSize(), motionZ / 4 * getSize());
							ArcaneUtils.applyPlayerKnockback(target);
						}
					}
				}
			}
		}
		if (spawnWhirlPool) {
			EntityWhirlpool pool = new EntityWhirlpool(world);
			pool.setOwner(getCaster());
			pool.setPosition(posX, posY, posZ);
			pool.setCaster(getCaster());
			pool.lifetime = 20 + (int) (getSize() * 10);
			pool.damageMultiplier = damage / 7;
			pool.setSize(getSize());
			pool.setRenderSize(getSize() * 1.5F);
			pool.setVortexHeight(getSize() * 1.5F);
			if (!world.isRemote)
				world.spawnEntity(pool);
		}

		if (spawnGeysers) {
			for (int i = 0; i < 5; i++) {
				Vec3d pos = ArcaneUtils.toRectangular(rotationYaw + i * 72, 0);
				pos = pos.scale(3);
				EntityWhirlpool pool = new EntityWhirlpool(world);
				pool.setOwner(getCaster());
				pool.setPosition(posX + pos.x, posY, posZ + pos.z);
				pool.setCaster(getCaster());
				pool.lifetime = 20 + (int) (getSize() * 10);
				pool.damageMultiplier = damage / 10;
				pool.setSize(getSize() / 4);
				pool.setRenderSize(getSize() / 2);
				pool.setVortexHeight(getSize() * 4);
				if (!world.isRemote)
					world.spawnEntity(pool);
			}
		}

		if (!world.isRemote) {
			if (world instanceof WorldServer) {
				((WorldServer) world).spawnParticle(EnumParticleTypes.WATER_SPLASH, true, posX, posY, posZ, 40 + (int) (getSize() * 5), 0, 0, 0,
						0.02D + getSize() / 50);
			}
		}

		if (world.isRemote) {
			for (int i = 0; i < 30 - getSize(); i++) {
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_BUBBLE).pos(getPositionVector()).time(10)
						.vel(world.rand.nextGaussian() / 8 * getSize(), world.rand.nextGaussian() / 8
								* getSize(), world.rand.nextGaussian() / 8 * getSize()).
						scale(0.25F + getSize() / 2 + world.rand.nextFloat()).spawn(world);
			}
			this.isDead = true;
		}
	}

	@Override
	protected void tickInAir() {
		if (world.isRemote) {
			float size = getSize();
			// Water drop trail — 1-2 droplets per tick, drifting slightly backward
			int drops = 1 + (int) size;
			for (int i = 0; i < drops; i++) {
				world.spawnParticle(EnumParticleTypes.WATER_DROP,
						posX + world.rand.nextGaussian() * 0.15 * size,
						posY + world.rand.nextGaussian() * 0.15 * size,
						posZ + world.rand.nextGaussian() * 0.15 * size,
						-motionX * 0.25, world.rand.nextDouble() * 0.05, -motionZ * 0.25);
			}
			// Splash droplets every other tick
			if (ticksExisted % 2 == 0) {
				world.spawnParticle(EnumParticleTypes.WATER_SPLASH,
						posX + world.rand.nextGaussian() * 0.2 * size,
						posY + world.rand.nextGaussian() * 0.2 * size,
						posZ + world.rand.nextGaussian() * 0.2 * size,
						world.rand.nextGaussian() * 0.04, 0.04 + world.rand.nextDouble() * 0.06,
						world.rand.nextGaussian() * 0.04);
			}
			// Rising magic bubbles every 3 ticks for a watery shimmer
			if (ticksExisted % 3 == 0) {
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_BUBBLE)
						.pos(getPositionVector())
						.vel(world.rand.nextGaussian() * 0.02 * size,
								0.02 + world.rand.nextDouble() * 0.025,
								world.rand.nextGaussian() * 0.02 * size)
						.time(6 + (int) (size * 2))
						.scale(0.1F + size * 0.12F)
						.spawn(world);
			}
		}
	}

	@Override
	public void setDead() {
		Splash();
		super.setDead();
	}

	@Override
	public boolean canCollideWithSolid(IBlockState hit) {
		return getSize() < 1 ? hit.getBlock() != Blocks.AIR && !(hit.getBlock() instanceof BlockBush) : super.canCollideWithSolid(hit);
	}
}
