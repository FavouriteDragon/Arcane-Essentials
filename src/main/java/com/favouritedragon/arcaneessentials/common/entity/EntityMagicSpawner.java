package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityMagicSpawner extends EntityMagicConstruct {


	public EntityMagicSpawner(World world) {
		super(world);
	}

	public EntityMagicSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world);
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.setCaster(caster);
		this.lifetime = lifetime;
		this.damageMultiplier = damageMultiplier;
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
	}

	@Nullable
	@Override
	public UUID getOwnerId() {
		return getCaster().getUniqueID();
	}

	@Nullable
	@Override
	public Entity getOwner() {
		return getOwner();
	}

}
