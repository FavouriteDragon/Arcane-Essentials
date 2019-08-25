package com.favouritedragon.arcaneessentials.common.spell.fire;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityFlamePillar;
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
import net.minecraft.world.World;

public class InfernoPillar extends Spell {

	public InfernoPillar() {
			super(ArcaneEssentials.MODID, "inferno_pillar", EnumAction.BOW, false);
			addProperties(DAMAGE, EFFECT_RADIUS, EFFECT_DURATION, RANGE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		int lifetime = getProperty(EFFECT_DURATION).intValue() * 20 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade);
		float radius = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float height = getProperty(RANGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		world.spawnEntity(new EntityFlamePillar(world, caster.posX, caster.posY, caster.posZ, caster, lifetime,
				damage, radius, height, 120));
		WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_GHAST_SHOOT, 2 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_FIRE_RING_AMBIENT, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_FIRE_SIGIL_TRIGGER, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10);

		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		int lifetime = getProperty(EFFECT_DURATION).intValue() * 20 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade);
		float radius = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float height = getProperty(RANGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		world.spawnEntity(new EntityFlamePillar(world, caster.posX, caster.posY, caster.posZ, caster, lifetime,
				damage, radius, height, 120));
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL,
				2 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FIRE_RING_AMBIENT, SoundCategory.NEUTRAL,
				1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FIRE_SIGIL_TRIGGER, SoundCategory.NEUTRAL,
				1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
		return true;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
