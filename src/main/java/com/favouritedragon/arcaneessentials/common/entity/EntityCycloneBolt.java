package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.PRESSURE;

public class EntityCycloneBolt extends EntityMagicBolt {

	public EntityCycloneBolt(World world) {
		super(world);
		this.setKnockbackStrength(1);
	}

	public EntityCycloneBolt(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityCycloneBolt(World world, EntityLivingBase caster, Entity target, float speed, float aimingError, float damageMultiplier) {
		super(world, caster, target, speed, aimingError, damageMultiplier);
	}

	public EntityCycloneBolt(World world, EntityLivingBase caster, float speed, float damageMultiplier, int knockBackStrength) {
		super(world, caster, speed, damageMultiplier);
		this.setKnockbackStrength(knockBackStrength);
		setSize(1, 1);
	}

	@Override
	public double getDamage() {
		return 4;
	}

	@Override
	public MagicDamage.DamageType getDamageType() {
		return PRESSURE;
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	@Override
	public void tickInGround() {
		setDead();
	}

	@Override
	public boolean doGravity() {
		return false;
	}

	@Override
	public void onBlockHit() {
		setDead();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.motionX == 0 && motionY == 0 && motionZ == 0) {
			setDead();
		}
		if (this.ticksExisted >= 150) {
			setDead();
		}
	}
}
