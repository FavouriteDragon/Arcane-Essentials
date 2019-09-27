package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMagicSpawner extends EntityMagicConstruct {


	public EntityMagicSpawner(World world) {
		super(world);
	}

	public EntityMagicSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world);
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.setOwner(caster);
		this.setCaster(caster);
		this.lifetime = lifetime;
		this.damageMultiplier = damageMultiplier;
	}

	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int time) {
		this.lifetime = time;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		//Using this does hella weird stuff with the positioning
		//this.move(MoverType.SELF, motionX, 0, motionZ);
		setPosition(posX + motionX, posY, posZ + motionZ);
		BlockPos pos = new BlockPos(posX, getEntityBoundingBox().minY, posZ);
		boolean inSolid = world.getBlockState(getPosition()).getBlock() != Blocks.AIR &&
				world.getBlockState(getPosition()).isFullBlock();
		//Ensures that if the size is greater than 1 it doesn't think it's in a solid
		//inSolid &= world.getBlockState(getPosition().add(0, Math.round(getSize() - 1), 0)).getBlock() != Blocks.AIR &&
		//		world.getBlockState(getPosition().add(0, Math.round(getSize() - 1), 0)).isFullBlock();
			if (inSolid || (!world.getBlockState(getPosition().down()).isFullBlock())) {
				this.setDead();
		}
		if (getCaster() != null && ticksExisted % getFrequency() == 0) {
			if (spawnEntity()) {
				playSound();
			}
		}
	}

	protected boolean spawnEntity() {
		return false;
	}

	protected int getFrequency() {
		return 6;
	}

	public void playSound() {

	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

}
