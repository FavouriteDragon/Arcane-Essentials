package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.world.World;

public class EntityFireball extends EntityMagicBolt {

	private float damage;

	private void setDamage(float damage) {
		this.damage = damage;
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
		return 0;
	}
}
