package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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

	public void setLifetime(int time) {
		this.lifetime = time;
	}

	public int getLifetime() {
		return lifetime;
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
