package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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
		//Using this does hella weird stuff with the positioning
		//this.move(MoverType.SELF, motionX, 0, motionZ);
		this.setNoGravity(true);
		setPosition(posX + motionX, posY, posZ + motionZ);
		boolean inSolid = world.getBlockState(getPosition()).isFullCube() && world.getBlockState(getPosition()).getBlock() != Blocks.AIR;
		boolean solid = world.getBlockState(getPosition().down()).isFullBlock();
		if (!world.isRemote) {
			if (this.collided || !solid || inSolid) {
				this.setDead();
			}
		}
	}

}
