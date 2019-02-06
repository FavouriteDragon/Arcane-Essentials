package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class EntityMagicSpawner extends EntityMagicConstruct {

	public EntityMagicSpawner(World par1World) {
		super(par1World);
	}

	public EntityMagicSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
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
}
