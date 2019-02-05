package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityFlamePillarSpawner extends EntityMagicConstruct {

	public EntityFlamePillarSpawner(World world) {
		super(world);
	}

	public EntityFlamePillarSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
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

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.move(MoverType.SELF, motionX, 0, motionZ);
		boolean solid = world.getBlockState(getPosition().offset(EnumFacing.DOWN)).isFullCube();
		boolean inSolid = world.getBlockState(getPosition()).isFullCube();
		if (!solid || inSolid) {
			this.setDead();
		}

		if (this.collided) {
			this.setDead();
		}
		assert getCaster() != null;
		if (ticksExisted % 7 == 0) {
			world.spawnEntity(new EntityFlamePillar(world, posX, posY, posZ, getCaster(), lifetime / 2, damageMultiplier, 0.75F));
		}
	}

}
