package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.world.World;

public class EntityFireball extends EntityMagicBolt {



	public EntityFireball(World world) {
		super(world);
	}

	@Override
	public double getDamage() {
		return this.damageMultiplier;
	}


	@Override
	public int getLifetime() {
		return 150;
	}
}
