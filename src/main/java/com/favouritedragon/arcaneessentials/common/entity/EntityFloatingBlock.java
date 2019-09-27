package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.block.Block;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityFloatingBlock extends EntityMagicBolt {
	private static final DataParameter<Integer> SYNC_BLOCK = EntityDataManager.createKey(EntityFloatingBlock.class,
			DataSerializers.VARINT);

	/**
	 * Creates a new projectile in the given world.
	 *
	 * @param world
	 */
	public EntityFloatingBlock(World world) {
		super(world);
	}

	public void setBlock(Block block) {
		dataManager.set(SYNC_BLOCK, Block.getStateId(block.getBlockState().getBaseState()));
	}

	public Block getBlock() {
		return Block.getStateById(dataManager.get(SYNC_BLOCK)).getBlock();
	}

	@Override
	public double getDamage() {
		return 0;
	}

	@Override
	public int getLifetime() {
		return 0;
	}
}
