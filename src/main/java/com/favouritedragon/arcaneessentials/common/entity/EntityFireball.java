package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityFireball extends EntityMagicBolt {


	private static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityFireball.class,
			DataSerializers.FLOAT);
	private float damage;
	private int lifetime = 40;
	private int burnDuration;

	public EntityFireball(World world) {
		super(world);
	}

	public void setBurnDuration(int duration) {
		this.burnDuration = duration;
	}

	public float getSize() {
		return dataManager.get(SYNC_SIZE);
	}

	public void setSize(float size) {
		dataManager.set(SYNC_SIZE, size);
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

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_SIZE, 1.0F);
	}

	private void Explode() {
		if (!world.isRemote) {
			world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_GHAST_SHOOT, WizardrySounds.SPELLS, 1.0F + world.rand.nextFloat() / 10,
					0.8F + world.rand.nextFloat() / 10F);
			List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow(getSize() / 2));
			if (!hit.isEmpty()) {
				for (Entity target : hit) {
					if (target != this && target != getCaster()) {
						if (target.canBeCollidedWith()) {
							target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(getCaster(),
									getDamageType()), (float) getDamage() * 0.4F);
							target.addVelocity(motionX / 4, motionY / 4, motionZ / 4);
							ArcaneUtils.applyPlayerKnockback(target);
						}
					}
				}
			}
		}

		if (world.isRemote) {
			for (int i = 0; i < 60 - getSize(); i++) {
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).pos(getPositionVector()).time(10)
						.vel(world.rand.nextGaussian() / 10 * getSize(), world.rand.nextGaussian() / 10
								* getSize(), world.rand.nextGaussian() / 10 * getSize()).
						scale(0.75F + getSize() / 2 + world.rand.nextFloat()).spawn(world);
			}
		}

		setDead();
	}

	@Override
	protected void onEntityHit(EntityLivingBase entityHit) {
		super.onEntityHit(entityHit);
		if (entityHit != getCaster()) {
			entityHit.setFire(burnDuration);
			Explode();
		}
	}

	@Override
	protected void onBlockHit(RayTraceResult hit) {
		super.onBlockHit(hit);
		Explode();
	}

	@Override
	protected void tickInGround() {
		Explode();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ticksExisted >= getLifetime() && !world.isRemote) {
			Explode();
		}
		setSize(getSize(), getSize());
	}
}
