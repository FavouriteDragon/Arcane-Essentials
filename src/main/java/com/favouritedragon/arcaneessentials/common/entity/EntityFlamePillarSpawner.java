package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityFlamePillarSpawner extends EntityMagicSpawner {

	public EntityFlamePillarSpawner(World world) {
		super(world);
	}

	public EntityFlamePillarSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
	}



	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected int getFrequency() {
		return 5;
	}

	@Override
	protected boolean spawnEntity() {
		return 	world.spawnEntity(new EntityFlamePillar(world, posX, posY, posZ, getCaster(), getLifetime() / 2, damageMultiplier, 1F, 7, 60));
	}

	@Override
	public void playSound() {
		world.playSound(posX, posY, posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS,
				1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);

	}
}
