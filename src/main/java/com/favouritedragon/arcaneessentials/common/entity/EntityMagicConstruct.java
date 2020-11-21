package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.entity.data.MagicConstructBehaviour;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
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
    /**
     * The time in ticks this magical construct lasts for; defaults to 600 (30 seconds). If this is -1 the construct
     * doesn't despawn.
     */
    public int lifetime = 600;
    /**
     * The damage multiplier for this construct, determined by the wand with which it was cast.
     */
    public float damageMultiplier = 1.0f;
    /**
     * The UUID of the caster. As of Wizardry 4.2, this <b>is</b> synced, and rather than storing the caster
     * instance via a weak reference, it is fetched from the UUID each time it is needed in
     * {@link electroblob.wizardry.entity.construct.EntityMagicConstruct#getCaster()}.
     */
    private UUID casterUUID;


    public EntityMagicConstruct(World world) {
        super(world);
        this.height = 1.0f;
        this.width = 1.0f;
        this.noClip = true;
    }

    public float getSize() {
        return dataManager.get(SYNC_SIZE);
    }

    public void setSize(float size) {
        dataManager.set(SYNC_SIZE, size);
    }

    public float getRenderSize() {
        return dataManager.get(SYNC_RENDER_SIZE);
    }

    public void setRenderSize(float size) {
        dataManager.set(SYNC_RENDER_SIZE, size);
    }

    public MagicConstructBehaviour getBehaviour() {
        return dataManager.get(SYNC_BEHAVIOUR);
    }

    public void setBehaviour(MagicConstructBehaviour behaviour) {
        dataManager.set(SYNC_BEHAVIOUR, behaviour);
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    protected void entityInit() {
        //Random UUID
        dataManager.register(SYNC_OWNER_ID, "cb2e7444-3287-4b97-adf1-e5e7ec266331");

        dataManager.register(SYNC_SIZE, 1.0F);
        dataManager.register(SYNC_RENDER_SIZE, 1.0F);
        dataManager.register(SYNC_BEHAVIOUR, new MagicConstructBehaviour.Idle());
    }

    // Overrides the original to stop the entity moving when it intersects stuff. The default arrow does this to allow
    // it to stick in blocks.
    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        setBehaviour((MagicConstructBehaviour) getBehaviour().onUpdate(this));

        if (this.ticksExisted > lifetime && lifetime != -1) {
            this.despawn();
        }

        setSize(getSize(), getSize());


    }

    /**
     * Defaults to just setDead() in EntityMagicConstruct, but is provided to allow subclasses to override this e.g.
     * bubble uses it to dismount the entity inside it and play the 'pop' sound before calling super(). You should
     * always call super() when overriding this method, in case it changes. There is no need, therefore, to call
     * setDead() when overriding.
     */
    public void despawn() {
        this.setDead();
    }


    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        casterUUID = nbttagcompound.getUniqueId("casterUUID");
        lifetime = nbttagcompound.getInteger("lifetime");
        damageMultiplier = nbttagcompound.getFloat("damageMultiplier");
    }

    //Caster is null client side
    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound nbttagcompound) {
        if (this.getCaster() != null) {
            nbttagcompound.setUniqueId("casterUUID", this.getCaster().getUniqueID());
        }
        nbttagcompound.setInteger("lifetime", lifetime);
        nbttagcompound.setFloat("damageMultiplier", damageMultiplier);
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeInt(lifetime);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        lifetime = data.readInt();
    }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return casterUUID;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return getCaster(); // Delegate to getCaster
    }

    public void setOwner(Entity entity) {
        dataManager.set(SYNC_OWNER_ID, entity.getUniqueID().toString());
    }

    /**
     * Returns the EntityLivingBase that created this construct, or null if it no longer exists. Cases where the entity
     * may no longer exist are: entity died or was deleted, mob despawned, player logged out, entity teleported to
     * another dimension, or this construct simply had no caster in the first place.
     */
    @Nullable
    public EntityLivingBase getCaster() { // Kept despite the above method because it returns an EntityLivingBase

        Entity entity = EntityUtils.getEntityByUUID(world, getOwnerId());

        if (entity != null && !(entity instanceof EntityLivingBase)) { // Should never happen
            Wizardry.logger.warn("{} has a non-living owner!", this);
            entity = null;
        }

        return (EntityLivingBase) entity;
    }

    public void setCaster(@Nullable EntityLivingBase caster) {
        this.casterUUID = caster == null ? null : caster.getUniqueID();
    }

    /**
     * Shorthand for {@link AllyDesignationSystem#isValidTarget(Entity, Entity)}, with the owner of this construct as the
     * attacker. Also allows subclasses to override it if they wish to do so.
     */
    public boolean isValidTarget(Entity target) {
        return AllyDesignationSystem.isValidTarget(this.getCaster(), target);
    }

}
