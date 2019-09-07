package com.favouritedragon.arcaneessentials.common.spell.water;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityWhirlpool;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Whirlpool extends Spell {

	public Whirlpool() {
		super(ArcaneEssentials.MODID, "whirlpool", EnumAction.BOW, false);
		addProperties(RANGE, DAMAGE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, range,true);
		if (result != null) {
			Vec3d pos = result.hitVec;
			EntityWhirlpool pool = new EntityWhirlpool(world);
			pool.setOwner(caster);
			pool.setPosition(pos.x, pos.y, pos.z);
			pool.setCaster(caster);
			pool.lifetime = 80 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade);
			pool.damageMultiplier = damage;
			pool.setRenderSize(4);
			world.spawnEntity(pool);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.BLOCK_WATER_AMBIENT, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_PLAYER_SPLASH, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_GENERIC_SPLASH, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			return true;
		} else {
			Vec3d pos = caster.getPositionVector();
			EntityWhirlpool pool = new EntityWhirlpool(world);
			pool.setOwner(caster);
			pool.setPosition(pos.x, pos.y, pos.z);
			pool.setCaster(caster);
			pool.lifetime = 80 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade);
			pool.damageMultiplier = damage;
			pool.setRenderSize(4);
			pool.setVortexHeight(2);
			world.spawnEntity(pool);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.BLOCK_WATER_AMBIENT, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_PLAYER_SPLASH, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_GENERIC_SPLASH, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			return true;
		}
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		double range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, range,true);
		if (result != null) {
			Vec3d pos = result.hitVec;
			EntityWhirlpool pool = new EntityWhirlpool(world);
			pool.setPosition(pos.x, pos.y, pos.z);
			pool.setCaster(caster);
			pool.lifetime = 80 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade);
			pool.damageMultiplier = damage;
			pool.height = 3;
			pool.width = 3;
			world.spawnEntity(pool);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.HOSTILE, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			return true;
		} else {
			Vec3d pos = caster.getPositionVector();
			EntityWhirlpool pool = new EntityWhirlpool(world);
			pool.setPosition(pos.x, pos.y, pos.z);
			pool.setCaster(caster);
			pool.lifetime = 80 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade);
			pool.damageMultiplier = damage;
			pool.height = 3;
			pool.width = 3;
			world.spawnEntity(pool);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.HOSTILE, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			return true;
		}
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
