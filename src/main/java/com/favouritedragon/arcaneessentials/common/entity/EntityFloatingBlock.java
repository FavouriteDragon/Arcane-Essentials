package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EntityFloatingBlock extends EntityMagicConstruct {
	private static final DataParameter<Integer> SYNC_BLOCK = EntityDataManager.createKey(EntityFloatingBlock.class,
			DataSerializers.VARINT);

	private float damage;
	private double x, y, z;
	private int lifetime;
	/**
	 * Creates a new projectile in the given world.
	 *
	 * @param world
	 */
	public EntityFloatingBlock(World world) {
		super(world);
	}

	public BlockPos getOrigin() {
		return new BlockPos(x, y, z);
	}
	public EntityFloatingBlock(World world, double posX, double posY, double posZ, EntityLivingBase caster, float damage, int lifetime, Block block) {
		super(world);
		setOwner(caster);
		setCaster(caster);
		setBlock(block);
		this.lifetime = lifetime;
		this.damage = damage;
		x = posX;
		y = posY;
		z = posZ;
		setPositionAndUpdate(posX, posY, posZ);
	}

	public void setBlock(Block block) {
		dataManager.set(SYNC_BLOCK, Block.getStateId(block.getBlockState().getBaseState()));
	}

	public Block getBlock() {
		return Block.getStateById(dataManager.get(SYNC_BLOCK)).getBlock();
	}


	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_BLOCK, Block.getStateId(Blocks.DIRT.getDefaultState()));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		lifetime = tag.getInteger("Lifetime");
		damage = tag.getFloat("Damage");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("Lifetime", lifetime);
		tag.setFloat("Damage", damage);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.move(MoverType.SELF, motionX, motionY, motionZ);

		if (world.getBlockState(getPosition()).isFullBlock() || world.getBlockState(getPosition()).getBlock() == Blocks.AIR)
			setDead();

		List<Entity> nearby = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox());
		nearby.remove(getCaster());
		nearby.remove(this);
		/*if (!nearby.isEmpty()) {
			for (Entity hit : nearby) {
				if (AllyDesignationSystem.isValidTarget(this, hit) && hit != this && hit != getCaster() && hit.canBeCollidedWith() && hit.canBePushed()) {

				}
			}
		}**/
	}

}
