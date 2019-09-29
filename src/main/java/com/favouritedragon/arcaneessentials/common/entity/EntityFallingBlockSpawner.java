package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.entity.data.Behaviour;
import com.favouritedragon.arcaneessentials.common.entity.data.MagicConstructBehaviour;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.EARTH;

public class EntityFallingBlockSpawner extends EntityMagicSpawner {

	//TODO: Rewrite projectile code
	public EntityFallingBlockSpawner(World world) {
		super(world);
	}

	public EntityFallingBlockSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		//Sizing causes hella weird positioning shenanigans, so it's better not to mess with it, which is why render size is used.
		List<Entity> nearby = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow((getRenderSize() - 1) / 2));
		nearby.remove(getCaster());
		if (!nearby.isEmpty()) {
			for (Entity hit : nearby) {
				if (hit != this && AllyDesignationSystem.isValidTarget(getCaster(), hit) && hit.canBePushed() && hit.canBeCollidedWith()) {
					if (!world.isRemote) {
						hit.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster(), EARTH), damageMultiplier);
						hit.addVelocity(motionX / 4 * getRenderSize(), 0.1 * getRenderSize(), motionZ / 4 * getRenderSize());
					}
				}
			}
		}
		setRenderSize(getRenderSize() * 1.005F);
		if (ticksExisted % 2 == 0) {
			playSound(world.getBlockState(getPosition().down()).getBlock().getSoundType().getBreakSound(), 0.8F + world.rand.nextFloat() / 10F, 0.8F + world.rand.nextFloat() / 10F);
		}
	}

	@Override
	protected int getFrequency() {
		return 2;
	}

	@Override
	protected boolean spawnEntity() {
		EntityFloatingBlock block = new EntityFloatingBlock(world, posX, posY + getRenderSize(), posZ, getCaster(),
				damageMultiplier, (int) (10 * getRenderSize()), world.getBlockState(getPosition().down()).getBlock());
		//Fall ticks upwards, so if you make it positive, it'll stay for a long time.
		//block.fallTime = MathHelper.clamp((int) (-10 * getRenderSize() + getRenderSize() > 0 ? (10 * getRenderSize()) % 10 : 0), -40, -10);
		block.motionX = block.motionZ = 0;
		block.motionY = 0.5 * MathHelper.clamp(0.3 * getRenderSize(), 0.25F, 1.5F);
		block.setBehaviour(new FallingBlockBehaviour());
		//block.shouldDropItem = false;
		if (!world.isRemote) {
			return world.spawnEntity(block);
		}
		else return false;
	}

	@Override
	public void playSound() {
		playSound(SoundEvents.BLOCK_SAND_FALL, 1.0F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10F);
	}

	public static class FallingBlockBehaviour extends MagicConstructBehaviour {

		@Override
		public Behaviour onUpdate(EntityMagicConstruct entity) {
			if (entity instanceof EntityFloatingBlock) {
				entity.motionY -= 0.0184;
				//Gravity is 9.2 m/s^2 on Earth
				if (entity.collided && !entity.world.isRemote) {
					entity.setDead();

				}
			}
			return this;
		}

		@Override
		public void fromBytes(PacketBuffer buf) {

		}

		@Override
		public void toBytes(PacketBuffer buf) {

		}

		@Override
		public void load(NBTTagCompound nbt) {

		}

		@Override
		public void save(NBTTagCompound nbt) {

		}
	}
}
