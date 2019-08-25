package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityMagicConstruct extends electroblob.wizardry.entity.construct.EntityMagicConstruct {

	public static final DataParameter<String> SYNC_OWNER_ID = EntityDataManager.createKey(EntityMagicConstruct.class,
			DataSerializers.STRING);

	public EntityMagicConstruct(World world) {
		super(world);
	}

	public void setOwner(Entity entity) {
		dataManager.set(SYNC_OWNER_ID, entity.getUniqueID().toString());
	}

	@Override
	protected void entityInit() {
		//Random UUID
		dataManager.register(SYNC_OWNER_ID, "cb2e7444-3287-4b97-adf1-e5e7ec266331");
	}


	@Nullable
	@Override
	public UUID getOwnerId() {
		return UUID.fromString(dataManager.get(SYNC_OWNER_ID));
	}

	@Nullable
	@Override
	public Entity getOwner() {
		return getCaster();
	}

	@Override
	public void despawn() {
		super.despawn();
	}

}
