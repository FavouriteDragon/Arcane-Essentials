package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.util.MagicDamage;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityFireball extends EntityMagicBolt {


	private static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityFireball.class,
			DataSerializers.FLOAT);
	private float damage;
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
