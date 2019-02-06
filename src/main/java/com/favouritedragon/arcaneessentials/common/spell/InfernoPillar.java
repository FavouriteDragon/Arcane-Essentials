package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityFlamePillar;
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
import net.minecraft.world.World;

public class InfernoPillar extends Spell {

	public InfernoPillar() {
		super(Tier.ADVANCED, 60, Element.FIRE, "inferno_pillar", SpellType.DEFENCE, 180, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		world.spawnEntity(new EntityFlamePillar(world, caster.posX, caster.posY, caster.posZ, caster, 80 + 2 * (int) modifiers.get(WizardryItems.duration_upgrade),
				1 * modifiers.get(WizardryItems.blast_upgrade), 1.5F * modifiers.get(WizardryItems.range_upgrade), 15, 180));
		WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_GHAST_SHOOT, 2 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_LOOP_FIRE, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_SUMMONING, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10);

		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		world.spawnEntity(new EntityFlamePillar(world, caster.posX, caster.posY, caster.posZ, caster, 60 + 2 * (int) modifiers.get(WizardryItems.duration_upgrade),
				1 * modifiers.get(WizardryItems.blast_upgrade), 1.5F * modifiers.get(WizardryItems.range_upgrade), 15, 180));
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL,
				2 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_LOOP_FIRE, SoundCategory.NEUTRAL,
				1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_SUMMONING, SoundCategory.NEUTRAL,
				1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
		return true;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
