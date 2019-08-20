package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.world.World;

public class EntityFireball extends EntityMagicBolt {

	private float damage;
	private int lifetime;


	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	public EntityFireball(World world) {
		super(world);
	}

	@Override
	public double getDamage() {
		return damage;
	}

	@Override
	public int getLifetime() {
		return lifetime;
	}
}
