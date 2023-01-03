package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityMagicShockwave extends EntityMagicConstruct {


	public EntityMagicShockwave(World world) {
		super(world);
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
		return getCaster().getUniqueID();
	}

	@Nullable
	@Override
	public Entity getOwner() {
		return getCaster();
	}
}
