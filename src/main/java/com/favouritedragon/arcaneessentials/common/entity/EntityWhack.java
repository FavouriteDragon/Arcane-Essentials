package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.util.MagicDamage;
import net.minecraft.world.World;

public class EntityWhack extends EntityMagicBolt {

	private float damage;

	public EntityWhack(World world) {
		super(world);
		setSize(0.75F, 0.75F);
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	@Override
	public double getDamage() {
		return damage;
	}

	@Override
	public int getLifetime() {
		return 100;
	}

	@Override
	public MagicDamage.DamageType getDamageType() {
		return MagicDamage.DamageType.WITHER;
	}
}
