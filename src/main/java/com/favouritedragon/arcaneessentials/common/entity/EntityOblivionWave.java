package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityOblivionWave extends EntityMagicConstruct {

	private static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityOblivionWave.class, DataSerializers.FLOAT);

	public EntityOblivionWave(World par1World) {
		super(par1World);
	}

	public void setSize(float size) {
		dataManager.set(SYNC_SIZE, size);
	}

	public float getSize() {
		return dataManager.get(SYNC_SIZE);
	}
 	@Override
	protected void entityInit() {
		dataManager.register(SYNC_SIZE, 1F);

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}
}
