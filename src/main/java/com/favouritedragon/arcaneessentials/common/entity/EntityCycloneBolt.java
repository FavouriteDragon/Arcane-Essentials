package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
	protected void onBlockHit(RayTraceResult hit) {
		if (canCollideWithSolid(hit))
			Dissipate();
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
		if (!world.isRemote) {
			world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
					0.8F + world.rand.nextFloat() / 10F);
			if (world instanceof WorldServer) {
				WorldServer World = (WorldServer) world;
				World.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 12,
						0, 0, 0, 0.1);
			}
			List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox());
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
		setDead();
	}

	@Override
	protected void onEntityHit(EntityLivingBase entityHit) {
		if (canCollideWithEntity(entityHit))
			Dissipate();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (getLifetime() >= 100 && ticksExisted >= getLifetime()) {
			Dissipate();
		}
		if (ArcaneUtils.getMagnitude(new Vec3d(motionX, motionY, motionZ)) <= 0.4F) {
			Dissipate();
		}
	}
}
