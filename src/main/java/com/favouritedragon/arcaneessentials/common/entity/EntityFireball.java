package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.spell.fire.KaFrizzle;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
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

	private float damage;
	private int lifetime = 40;
	private int burnDuration;

	public EntityFireball(World world) {
		super(world);
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
		if (getBehaviour() instanceof KaFrizzle.KaFrizzleBehaviour) {
			EntityFlamePillar pillar = new EntityFlamePillar(world, posX, posY, posZ, getCaster(),
					(int) getSize() * 30, (float) getDamage() / 6F, getSize() / 2, getSize() * 5,
					120 + (int) (getSize() * 5));
			if (!world.isRemote)
				world.spawnEntity(pillar);
		}

		if (world.isRemote) {
			for (int i = 0; i < 60 - getSize(); i++) {
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
		for (int i = 0; i < getSize() * 10; i++) {
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
