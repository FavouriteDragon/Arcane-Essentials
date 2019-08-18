package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityMagicShockwave extends EntityMagicConstruct {

	public EntityMagicShockwave(World par1World) {
		super(par1World);
	}

	public EntityMagicShockwave(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Nullable
	@Override
	public UUID getOwnerId() {
		return null;
	}

	@Nullable
	@Override
	public Entity getOwner() {
		return null;
	}
}
