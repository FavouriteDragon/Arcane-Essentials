package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityThunderBurst extends EntityMagicConstruct {

	public EntityThunderBurst(World world) {
		super(world);
	}

	public EntityThunderBurst(World world, double x, double y, double z, EntityLivingBase caster, int lifetime,
							  float damageMultiplier){
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		this.height = 1.0f;
		this.width = 1.0f;
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

	@Override
	public void onUpdate() {
		super.onUpdate();

	}
}
