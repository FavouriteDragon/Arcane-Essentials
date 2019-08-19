package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

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
		this.setNoGravity(true);
		if (!world.isRemote) {
			if (this.collided || world.getBlockState(getPosition().add(0, 1, 0)).isFullCube()) {
				this.setDead();
			}
		}
	}

}
