package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.EARTH;

public class EntityFallingBlockSpawner extends EntityMagicSpawner {

	public EntityFallingBlockSpawner(World world) {
		super(world);
	}

	public EntityFallingBlockSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		List<Entity> nearby = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox());
		nearby.remove(getCaster());
		if (!nearby.isEmpty()) {
			for (Entity hit : nearby) {
				if (hit != this && AllyDesignationSystem.isValidTarget(getCaster(), hit) && hit.canBePushed() && hit.canBeCollidedWith()) {
					if (!world.isRemote) {
						hit.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster(), EARTH), damageMultiplier);
						hit.addVelocity(motionX / 2, motionY / 2 + 0.15F, motionZ / 2);
					}
				}
			}
		}
		if (ticksExisted % 2 == 0) {
			playSound(world.getBlockState(getPosition().down()).getBlock().getSoundType().getBreakSound(), 0.8F + world.rand.nextFloat() / 10F, 0.8F + world.rand.nextFloat() / 10F);
		}
	}

	@Override
	protected int getFrequency() {
		return 3;
	}

	@Override
	protected boolean spawnEntity() {
		EntityFallingBlock block = new EntityFallingBlock(world, posX, posY + 1, posZ, world.getBlockState(getPosition().down()));
		block.setHurtEntities(true);
		//Fall ticks upwards, so if you make it positive, it'll stay for a long time.
		block.fallTime = -4;
		if (!world.isRemote)
			return world.spawnEntity(block);
		else return false;
	}

	@Override
	public void playSound() {
		playSound(SoundEvents.BLOCK_SAND_FALL, 1.0F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10F);
	}
}
