package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityLightningSpawner extends EntityMagicSpawner {

	private int burnTime;

	public EntityLightningSpawner(World world) {
		super(world);
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

	}

	@Override
	protected int getFrequency() {
		return 3;
	}

	@Override
	protected boolean spawnEntity() {
		assert getCaster() != null;
		EntityMagicLightning bolt = new EntityMagicLightning(world);
		bolt.setDamage(damageMultiplier);
		bolt.setBurnTime(burnTime);
		bolt.setKnockbackMult((float) ArcaneUtils.getMagnitude(new Vec3d(motionX, motionY, motionZ)) / 2);
		bolt.setOwner(getCaster());
		bolt.setPosition(posX, posY, posZ);
		bolt.motionX = bolt.motionY = bolt.motionZ = 0;
		return world.spawnEntity(bolt);
	}
}
