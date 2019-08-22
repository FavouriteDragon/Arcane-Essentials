package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.util.MagicDamage;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityFireball extends EntityMagicBolt {


	private static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityFireball.class,
			DataSerializers.FLOAT);

	public EntityFireball(World world) {
		super(world);
	}

	private float damage;

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setSize(float size) {
		dataManager.set(SYNC_SIZE, size);
	}

	public float getSize() {
		return dataManager.get(SYNC_SIZE);
	}
	@Override
	public double getDamage() {
		return damage;
	}

	@Override
	public MagicDamage.DamageType getDamageType() {
		return MagicDamage.DamageType.FIRE;
	}

	@Override
	public boolean doGravity() {
		return false;
	}

	@Override
	public boolean doDeceleration() {
		return false;
	}

	@Override
	public int getLifetime() {
		return 150;
	}


	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_SIZE, 1.0F);
	}
}
