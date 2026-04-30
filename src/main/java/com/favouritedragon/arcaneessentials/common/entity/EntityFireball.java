package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityFireball extends EntityMagicBolt {

	private static final DataParameter<Boolean> SYNC_KA_FRIZZLE = EntityDataManager.createKey(EntityFireball.class, DataSerializers.BOOLEAN);

	private float damage;
	private int lifetime = 40;
	private int burnDuration;
	private boolean exploded;

	public EntityFireball(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_KA_FRIZZLE, false);
	}

	public boolean isKaFrizzle() {
		return dataManager.get(SYNC_KA_FRIZZLE);
	}

	public void setKaFrizzle(boolean value) {
		dataManager.set(SYNC_KA_FRIZZLE, value);
	}

	public void setBurnDuration(int duration) {
		this.burnDuration = duration;
	}

	@Override
	public double getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	@Override
	public MagicDamage.DamageType getDamageType() {
		return MagicDamage.DamageType.FIRE;
	}

	@Override
	public boolean doDeceleration() {
		return false;
	}

	@Override
	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}


	private void Explode() {
		if (exploded) return;
		exploded = true;

		boolean kaFrizzle = isKaFrizzle();
		boolean heavyOrb = kaFrizzle || getLifetime() >= 70;

		if (!world.isRemote) {
			world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
					0.8F + world.rand.nextFloat() / 10F);
			List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow(getSize() / 4));
			if (!hit.isEmpty()) {
				for (Entity target : hit) {
					if (target != this && target != getCaster()) {
						if (target.canBeCollidedWith()) {
							target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(getCaster(),
									getDamageType()), (float) getDamage() * 0.2F);
							target.addVelocity(motionX / 4, motionY / 4, motionZ / 4);
							target.setFire(burnDuration);
							ArcaneUtils.applyPlayerKnockback(target);
						}
					}
				}
			}
		}
		if (kaFrizzle) {
			EntityFlamePillar pillar = new EntityFlamePillar(world, posX, posY, posZ, getCaster(),
					(int) getSize() * 30, (float) getDamage() / 6F, getSize() / 2, getSize() * 5,
					60 + (int) (getSize() * 2));
			if (!world.isRemote)
				world.spawnEntity(pillar);
		}

		if (world.isRemote) {
			int explosionParticles = heavyOrb
					? Math.max(8, Math.min(20, 24 - (int) getSize()))
					: Math.max(16, 50 - (int) getSize());
			for (int i = 0; i < explosionParticles; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).pos(getPositionVector()).time(10)
						.vel(world.rand.nextGaussian() / 10 * getSize(), world.rand.nextGaussian() / 10
								* getSize(), world.rand.nextGaussian() / 10 * getSize()).
						scale(0.75F + getSize() / 2 + world.rand.nextFloat()).spawn(world);
			}
		}
		this.isDead = true;
	}


	@Override
	protected void tickInGround() {
		setDead();
	}

	@Override
	public void setDead() {
		Explode();
		super.setDead();
	}

	@Override
	protected void onEntityHit(EntityLivingBase entityHit) {
		super.onEntityHit(entityHit);
		if (canCollideWithEntity(entityHit))
			entityHit.setFire((int) getSize() * 5);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		boolean kaFrizzle = isKaFrizzle();
		boolean heavyOrb = kaFrizzle || getLifetime() >= 70;
			if (ticksExisted == 1) {
				if (world.isRemote) {
				double x1, y1, z1;
				Vec3d prevPos = Vec3d.ZERO;
				int shellBudget = heavyOrb ? 140 : 420;
				int spawned = 0;
				double thetaStep = heavyOrb ? 6 : 3;
				for (double theta = 0; theta <= 180 && spawned < shellBudget; theta += thetaStep) {
					double sinTheta = Math.sin(Math.toRadians(theta));
					if (Math.abs(sinTheta) < 1.0E-6) sinTheta = 1.0E-6;
					double dphi = (50 - getSize() * 6) / sinTheta;
					dphi = Math.max(dphi, heavyOrb ? 18 : 10);
					if (!Double.isFinite(dphi) || dphi <= 0) dphi = heavyOrb ? 18 : 10;
					for (double phi = 0; phi < 360; phi += dphi) {
						double rphi = Math.toRadians(phi);
						double rtheta = Math.toRadians(theta);

						//Making it spin increases the sphere size
						x1 = getSize() / 2.25 * Math.cos(rphi) * Math.sin(rtheta);
						y1 = getSize() / 2.25 * Math.sin(rphi) * Math.sin(rtheta);
						z1 = getSize() / 2.25 * Math.cos(rtheta);

						if (prevPos != Vec3d.ZERO)
							ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).collide(true).vel(motionX * 1.05 + world.rand.nextGaussian() / 160,
									motionY * 1.05 + world.rand.nextGaussian() / 160, motionZ * 1.05 + world.rand.nextGaussian() / 160).target(prevPos).scale(getSize())
									.time(lifetime).pos(ArcaneUtils.getMiddleOfEntity(this).add(new Vec3d(x1, y1, z1))).spin(0.1, world.rand.nextGaussian() / 40)
									.spawn(world);
						else ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).collide(true).vel(motionX * 1.05 + world.rand.nextGaussian() / 160,
								motionY * 1.05 + world.rand.nextGaussian() / 160, motionZ * 1.05 + world.rand.nextGaussian() / 160).scale(getSize())
								.time(lifetime).pos(ArcaneUtils.getMiddleOfEntity(this).add(new Vec3d(x1, y1, z1))).spin(0.1, world.rand.nextGaussian() / 40).spawn(world);
						prevPos = new Vec3d(x1, y1, z1).add(ArcaneUtils.getMiddleOfEntity(this));
						spawned++;
						if (spawned >= shellBudget) break;

					}
				}
			}
		}

		int trailParticles = (int) (getSize() * (heavyOrb ? 3 : 6));
		trailParticles = Math.max(2, Math.min(trailParticles, heavyOrb ? 5 : 12));
		for (int i = 0; i < trailParticles; i++) {
			if (world.isRemote) {
				AxisAlignedBB boundingBox = getEntityBoundingBox();
				double spawnX = boundingBox.minX + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxX - boundingBox.minX);
				double spawnY = boundingBox.minY + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxY - boundingBox.minY);
				double spawnZ = boundingBox.minZ + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxZ - boundingBox.minZ);
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).vel(new Vec3d(motionX, motionY, motionZ).scale(world.rand.nextFloat() / 10))
						.pos(spawnX, spawnY, spawnZ).collide(true).time(5).scale(0.75F + getSize() / 2 + world.rand.nextFloat() / 2).spawn(world);
			}
		}
	}
}
