package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.projectile.EntityMagicArrow;
import electroblob.wizardry.entity.projectile.EntityMagicProjectile;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.WizardryParticleType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.PRESSURE;

public class EntityCycloneBolt extends EntityMagicBolt  {

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
	public void onBlockHit(){
		// Adds a particle effect when the ice lance hits a block.
		if(this.world.isRemote){
			for(int j = 0; j < 10; j++){
				double x = this.posX - 0.25d + (rand.nextDouble() / 2);
				double y = this.posY - 0.25d + (rand.nextDouble() / 2);
				double z = this.posZ - 0.25d + (rand.nextDouble() / 2);
				world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, x, y, z);
			}
		}
		// Parameters for sound: sound event name, volume, pitch.
		this.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, rand.nextFloat() * 0.4F + 1.2F);

	}

}
