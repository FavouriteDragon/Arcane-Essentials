package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.world.World;

public class EntityWaterBall extends EntityMagicBolt {

	public EntityWaterBall(World world) {
		super(world);
	}

	@Override
	public double getDamage() {
		return 0;
	}

	@Override
	public int getLifetime() {
		return 0;
	}
}
