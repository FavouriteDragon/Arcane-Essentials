package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EntityFlamePillarSpawner extends EntityMagicSpawner {

	public EntityFlamePillarSpawner(World world) {
		super(world);
	}

	public EntityFlamePillarSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ticksExisted % 5 == 0 && getCaster() != null) {
			world.spawnEntity(new EntityFlamePillar(world, posX, posY, posZ, getCaster(), lifetime / 2, damageMultiplier, 1F, 7, 60));
			world.playSound(posX, posY, posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE,
					1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public void setDead() {
		super.setDead();
		if (!world.isRemote) {
			Thread.dumpStack();
		}
	}
}
