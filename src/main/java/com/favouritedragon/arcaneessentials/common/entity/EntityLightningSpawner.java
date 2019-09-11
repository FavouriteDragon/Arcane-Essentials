package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.registry.WizardrySounds;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityLightningSpawner extends EntityMagicSpawner {

	private int burnTime;

	public EntityLightningSpawner(World world) {
		super(world);
	}

	public void setBurnTime(int time) {
		this.burnTime = time;
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

	}

	@Override
	protected int getFrequency() {
		return 2;
	}

	@Override
	protected boolean spawnEntity() {
		assert getCaster() != null;
		EntityMagicLightning bolt = new EntityMagicLightning(world);
		bolt.setDamage(damageMultiplier);
		bolt.setBurnTime(burnTime);
		bolt.setKnockback(new Vec3d(motionX, motionY, motionZ).scale(0.25).add(0, 0.1, 0));
		bolt.setOwner(getCaster());
		bolt.setPosition(posX, posY, posZ);
		bolt.motionX = bolt.motionY = bolt.motionZ = 0;
		return world.spawnEntity(bolt);
	}

	@Override
	public void playSound() {
		world.playSound(posX, posY, posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.PLAYERS, 1.2F + world.rand.nextFloat(),
				0.8F + world.rand.nextFloat(), false);
		world.playSound(posX, posY, posZ, WizardrySounds.ENTITY_LIGHTNING_DISC_HIT, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				0.8F + world.rand.nextFloat() / 10, false);
	}
}
