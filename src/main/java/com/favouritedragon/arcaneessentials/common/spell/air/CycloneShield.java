package com.favouritedragon.arcaneessentials.common.spell.air;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneShield;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class CycloneShield extends Spell {
	public CycloneShield() {
		super(ArcaneEssentials.MODID, "cyclone_shield", EnumAction.BOW, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_LOOP_WIND, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_SHOCKWAVE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		if (!world.isRemote) {
			caster.swingArm(hand);
			int lifetime = 40 + 5 * (int) modifiers.get(WizardryItems.duration_upgrade);
			float damageMultiplier = 2 * modifiers.get(SpellModifiers.DAMAGE);
			float radius = 4 * modifiers.get(WizardryItems.range_upgrade);
			world.spawnEntity(new EntityCycloneShield(world, caster.posX, caster.posY, caster.posZ, caster, lifetime, damageMultiplier, radius));
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_LOOP_WIND, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_SHOCKWAVE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);

		if (!world.isRemote) {
			caster.swingArm(hand);
			int lifetime = 40 + 5 * (int) modifiers.get(WizardryItems.duration_upgrade);
			float damageMultiplier = 2 * modifiers.get(SpellModifiers.DAMAGE);
			float radius = 4 * modifiers.get(WizardryItems.range_upgrade);
			world.spawnEntity(new EntityCycloneShield(world, caster.posX, caster.posY, caster.posZ, caster, lifetime, damageMultiplier, radius));
			return true;
		}
		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
