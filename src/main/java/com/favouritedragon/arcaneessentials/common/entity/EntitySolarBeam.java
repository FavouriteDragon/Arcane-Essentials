package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.util.WizardryParticleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.EARTH;

public class EntitySolarBeam extends EntityMagicConstruct {
	//This entity isn't actually a laser- it spawns slightly in front of the player, then spawns particles along a raytrace and multhits along it.
	//The entity pitch and yaw is set to the caster's when the entity is spawned, so the raytrace functions work correctly.

	private static final DataParameter<Float> SYNC_RADIUS = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SYNC_RANGE = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.FLOAT);

	public EntitySolarBeam(World par1World) {
		super(par1World);
	}

	public EntitySolarBeam(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
	}

	public void setRadius(float radius) {
		dataManager.set(SYNC_RADIUS, radius);
	}

	public void setRange(float range) {
		dataManager.set(SYNC_RANGE, range);
	}

	public float getRadius() {
		return dataManager.get(SYNC_RADIUS);
	}

	public float getRange() {
		return dataManager.get(SYNC_RANGE);
	}



	@Override
	protected void entityInit() {
		dataManager.register(SYNC_RADIUS, 1.0F);
		dataManager.register(SYNC_RANGE, 20.0F);
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
		if (!world.isRemote && getCaster() != null) {
			Vec3d endpos = getLookVec().scale(getRange()).add(getPositionVector());
			ArcaneUtils.handlePiercingBeamCollision(world, getCaster(), getPositionVector(), endpos, getRadius(), this, false, EARTH, 0.25F * damageMultiplier,
					new Vec3d(0.25, 0.1, 0.25), false, 0, getRadius());
		}
	}


}

