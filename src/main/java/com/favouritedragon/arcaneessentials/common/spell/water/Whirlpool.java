package com.favouritedragon.arcaneessentials.common.spell.water;

import com.favouritedragon.arcaneessentials.common.entity.EntityLightningVortex;
import com.favouritedragon.arcaneessentials.common.entity.EntityWhirlpool;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
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
		super(Tier.ADVANCED, 55, Element.EARTH, "whirlpool", SpellType.DEFENCE, 100, EnumAction.BOW, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = 3 + 1 * modifiers.get(WizardryItems.range_upgrade);
		float damage = 3 + 1 * modifiers.get(WizardryItems.blast_upgrade);
		Vec3d vel = caster.getLookVec().scale(range / 10);
		RayTraceResult result = WizardryUtilities.rayTrace(range, world, caster, true);
		if (result != null) {
			Vec3d pos = result.hitVec;
			world.spawnEntity(new EntityWhirlpool(world, pos.x, pos.y, pos.z,
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.BLOCK_WATER_AMBIENT, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_PLAYER_SPLASH, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_GENERIC_SPLASH, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			return true;
		} else {
			Vec3d pos = caster.getLookVec().scale(1.5).add(caster.getPositionVector());
			world.spawnEntity(new EntityWhirlpool(world, pos.x, pos.y, pos.z,
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
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
		double range = 3 + 1 * modifiers.get(WizardryItems.range_upgrade);
		float damage = 3 + 1 * modifiers.get(WizardryItems.blast_upgrade);
		RayTraceResult result = WizardryUtilities.rayTrace(range, world, caster, true);
		if (result != null) {
			Vec3d pos = result.hitVec;
			world.spawnEntity(new EntityWhirlpool(world, pos.x, pos.y, pos.z,
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.HOSTILE, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			return true;
		} else {
			Vec3d pos = caster.getLookVec().scale(1.5).add(caster.getPositionVector());
			world.spawnEntity(new EntityWhirlpool(world, pos.x, pos.y, pos.z,
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
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
