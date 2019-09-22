package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.entity.data.MagicConstructBehaviour;
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

	//SYNC_SIZE is used for setting the actual size of the entity. It's also used for rendering.
	private static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityMagicConstruct.class,
			DataSerializers.FLOAT);
	//This is used for when the entity renders too big to fit normal hitboxes.
	private static final DataParameter<Float> SYNC_RENDER_SIZE = EntityDataManager.createKey(EntityMagicConstruct.class,
			DataSerializers.FLOAT);

	private static final DataParameter<MagicConstructBehaviour> SYNC_BEHAVIOUR = EntityDataManager.createKey(EntityMagicConstruct.class,
			MagicConstructBehaviour.DATA_SERIALIZER);

	public EntityMagicConstruct(World world) {
		super(world);
	}

	public void setOwner(Entity entity) {
		dataManager.set(SYNC_OWNER_ID, entity.getUniqueID().toString());
	}

	public void setSize(float size) {
		dataManager.set(SYNC_SIZE, size);
	}

	public float getSize() {
		return dataManager.get(SYNC_SIZE);
	}

	public void setRenderSize(float size) {
		dataManager.set(SYNC_RENDER_SIZE, size);
	}

	public float getRenderSize() {
		return dataManager.get(SYNC_RENDER_SIZE);
	}

	public void setBehaviour(MagicConstructBehaviour behaviour) {
		dataManager.set(SYNC_BEHAVIOUR, behaviour);
	}

	public MagicConstructBehaviour getBehaviour() {
		return dataManager.get(SYNC_BEHAVIOUR);
	}

	@Override
	protected void entityInit() {
		//Random UUID
		dataManager.register(SYNC_OWNER_ID, "cb2e7444-3287-4b97-adf1-e5e7ec266331");

		dataManager.register(SYNC_SIZE, 1.0F);
		dataManager.register(SYNC_RENDER_SIZE, 1.0F);
		dataManager.register(SYNC_BEHAVIOUR, new MagicConstructBehaviour.Idle());
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
	public void onUpdate() {
		super.onUpdate();
		setSize(getSize(), getSize());
		setBehaviour(getBehaviour());

		if (getCaster() == null) {
			despawn();
		}

	}


	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound){
		lifetime = nbttagcompound.getInteger("lifetime");
		damageMultiplier = nbttagcompound.getFloat("damageMultiplier");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound){
		nbttagcompound.setInteger("lifetime", lifetime);
		nbttagcompound.setFloat("damageMultiplier", damageMultiplier);
	}

}
