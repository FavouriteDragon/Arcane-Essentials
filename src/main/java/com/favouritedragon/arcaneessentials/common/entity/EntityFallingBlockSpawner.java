package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.world.World;

public class EntityFallingBlockSpawner extends EntityMagicSpawner {

	public EntityFallingBlockSpawner(World world) {
		super(world);
	}

	public EntityFallingBlockSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
	}

	@Override
	protected int getFrequency() {
		return 4;
	}

	@Override
	protected boolean spawnEntity() {
		EntityFallingBlock block = new EntityFallingBlock(world, posX, posY + 1, posZ, world.getBlockState(getPosition().down()));
		block.setHurtEntities(true);
		if (!world.isRemote)
			return world.spawnEntity(block);
		else return false;
	}
}
