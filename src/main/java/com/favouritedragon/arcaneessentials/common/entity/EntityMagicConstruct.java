package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityMagicConstruct extends electroblob.wizardry.entity.construct.EntityMagicConstruct {

	public static final DataParameter<String> SYNC_OWNER_ID = EntityDataManager.createKey(EntityMagicConstruct.class,
			DataSerializers.STRING);

	public EntityMagicConstruct(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		dataManager.register(SYNC_OWNER_ID,);
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
