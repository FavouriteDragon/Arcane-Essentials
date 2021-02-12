package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.PRESSURE;

public class EntityCycloneBolt extends EntityMagicBolt {


	private static final DataParameter<Integer> SYNC_LIFETIME = EntityDataManager.createKey(EntityCycloneBolt.class,
			DataSerializers.VARINT);
	private float damage;


	public EntityCycloneBolt(World world) {
		super(world);
		this.setSize(0.75F, 0.75F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_LIFETIME, 150);
	}

	@Override
	public double getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}


	@Override
	public int getLifetime() {
		return dataManager.get(SYNC_LIFETIME);
	}

	public void setLifetime(int lifeTime) {
		dataManager.set(SYNC_LIFETIME, lifeTime);
	}

	@Override
	public MagicDamage.DamageType getDamageType() {
		return PRESSURE;
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	private void Dissipate() {
		world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				0.8F + world.rand.nextFloat() / 10F);
		if (world.isRemote)
			for (int i = 0; i < getSize() * 10; i++)
				ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(ArcaneUtils.getMiddleOfEntity(this)).collide(true).vel(world.rand.nextGaussian() / 20,
						world.rand.nextGaussian() / 20, world.rand.nextGaussian() / 20).scale(getSize() * 1.5F).spawn(world);


		List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox());
		if (!hit.isEmpty()) {
			for (Entity target : hit) {
				if (target != this && target != getCaster()) {
					if (target.canBeCollidedWith() && !world.isRemote) {
						target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(getCaster(),
								getDamageType()), (float) getDamage() * 0.4F);
						target.addVelocity(motionX / 4, motionY / 4, motionZ / 4);
						ArcaneUtils.applyPlayerKnockback(target);
					}
				}
			}
		}
		this.isDead = true;
	}


	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ArcaneUtils.getMagnitude(new Vec3d(motionX, motionY, motionZ)) <= 0.4F) {
			setDead();
		}

		if (world.isRemote) {
			for (int i = 0; i < getSize() * 10; i++) {
				if (world.isRemote) {
					AxisAlignedBB boundingBox = getEntityBoundingBox();
					double spawnX = boundingBox.getCenter().x + world.rand.nextGaussian() / 10;
					double spawnY = boundingBox.getCenter().y + world.rand.nextGaussian() / 10;
					double spawnZ = boundingBox.getCenter().z + world.rand.nextGaussian() / 10;
					ParticleBuilder.create(ParticleBuilder.Type.FLASH).vel(new Vec3d(motionX, motionY, motionZ).scale(world.rand.nextFloat() / 10))
							.pos(spawnX, spawnY, spawnZ).collide(true).time(5).clr(0.85F, 0.85F, 0.85F).scale(0.75F + getSize() / 2 + world.rand.nextFloat() / 2).spawn(world);
				}
			}
			//Particles
			ArcaneUtils.spawnSpinningDirectionalVortex(world, getCaster(), Vec3d.ZERO, 15,
					1, 0, 72, ParticleBuilder.Type.FLASH,
					getPositionVector().add(0, height / 2, 0), new Vec3d(0.4, 0.1, 0.4), new Vec3d(motionX * 1.05,
							motionY * 1.05, motionZ * 1.05), 8, 0.85F, 0.85F, 0.85F, 1.5F);

		}
	}

	@Override
	public void setDead() {
		Dissipate();
		super.setDead();
	}
}
