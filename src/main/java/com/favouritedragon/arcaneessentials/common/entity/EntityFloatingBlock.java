package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityFloatingBlock extends EntityMagicBolt {
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
	public EntityFloatingBlock(World world, double posX, double posY, double posZ, EntityLivingBase caster, float speed, float damage, int lifetime, Block block) {
		super(world);
		setCaster(caster);
		setBlock(block);
		this.lifetime = lifetime;
		this.damage = damage;
		x = posX;
		y = posY;
		z = posZ;
		this.noClip = true;
		//Makes the block face directly up
		this.setLocationAndAngles(x, y, z, caster.rotationYaw, (float) Math.PI);
		this.setPositionAndUpdate(posX, posY, posZ);
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.shoot(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 0);
	}

	public void setBlock(Block block) {
		dataManager.set(SYNC_BLOCK, Block.getStateId(block.getBlockState().getBaseState()));
	}

	public Block getBlock() {
		return Block.getStateById(dataManager.get(SYNC_BLOCK)).getBlock();
	}

	@Override
	public double getDamage() {
		return damage;
	}

	@Override
	public int getLifetime() {
		return lifetime;
	}

	@Override
	public boolean doDeceleration() {
		return false;
	}

	@Override
	protected boolean doGravity() {
		return true;
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
}
