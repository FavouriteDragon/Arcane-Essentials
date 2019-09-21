package com.favouritedragon.arcaneessentials.common.spell.earth;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityMagicConstruct;
import com.favouritedragon.arcaneessentials.common.entity.EntityMagicSpawner;
import com.favouritedragon.arcaneessentials.common.entity.data.Behaviour;
import com.favouritedragon.arcaneessentials.common.entity.data.MagicConstructBehaviour;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static com.favouritedragon.arcaneessentials.RegisterHandler.quake;

public class Quake extends Spell {

	public Quake() {
		super(ArcaneEssentials.MODID, "quake", EnumAction.BOW, false);
		addProperties(DAMAGE, DURATION, RANGE, BLAST_RADIUS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (world.getBlockState(caster.getPosition().offset(EnumFacing.DOWN)).getBlock() != Blocks.AIR) {
			Vec3d look = caster.getLookVec();

			EntityMagicSpawner spawner = new EntityMagicSpawner(world);
			spawner.setSize(getProperty(BLAST_RADIUS).floatValue() * 2);
			spawner.setLifetime(getProperty(DURATION).intValue() * 20 + (int) (10 * modifiers.get(WizardryItems.duration_upgrade)));
			spawner.damageMultiplier = modifiers.get(SpellModifiers.POTENCY) * getProperty(DAMAGE).floatValue();
			spawner.setPosition(caster.posX + look.x, caster.getEntityBoundingBox().minY, caster.posZ + look.z);
			look = look.scale(getProperty(RANGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));
			spawner.motionX = look.x;
			spawner.motionY = 0;
			spawner.motionZ = look.z;
			spawner.setOwner(caster);
			spawner.setBehaviour(new QuakeBehaviour());
			caster.swingArm(hand);
			if (!world.isRemote)
				return world.spawnEntity(spawner);
		}
		return false;
	}


	public static class QuakeBehaviour extends MagicConstructBehaviour {

		@Override
		public Behaviour onUpdate(EntityMagicConstruct entity) {
			if (entity instanceof EntityMagicSpawner) {
				World world = entity.world;
				if (entity.ticksExisted % 3 == 0) {
					EntityFallingBlock block = new EntityFallingBlock(entity.world);
					block.setOrigin(entity.getPosition().add(0, 1, 0));
					block.setHurtEntities(true);
					if (!world.isRemote)
						world.spawnEntity(block);
				}
				if (world.isRemote && entity.ticksExisted % 3 == 0) {
					world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, entity.posX, entity.posY, entity.posZ, world.rand.nextGaussian() / 90, world.rand.nextFloat() / 60,
							world.rand.nextGaussian() / 90, Block.getStateId(world.getBlockState(entity.getPosition())));
				}
				List<Entity> nearby = world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox().grow(quake.getProperty(BLAST_RADIUS).floatValue()));
				nearby.remove(entity.getCaster());
				if (!nearby.isEmpty()) {
					if (!world.isRemote) {
						for (Entity hit : nearby) {
							if (AllyDesignationSystem.isValidTarget(entity, hit)) {
								if (entity.canBeCollidedWith() && entity.canBePushed()) {
									hit.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(entity, entity.getCaster()), entity.damageMultiplier);
									hit.addVelocity(entity.motionX / 2, entity.motionY / 2 + 0.15, entity.motionZ / 2);
								}
							}
						}
					}
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
