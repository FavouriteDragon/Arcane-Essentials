package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ThunderBurst extends Spell {

	public ThunderBurst() {
		super(Tier.MASTER, 100, Element.LIGHTNING, "thunder_burst", SpellType.ATTACK, 300, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}
	@Override
	public boolean cast(World world, EntityPlayer entityPlayer, EnumHand enumHand, int i, SpellModifiers spellModifiers) {
		if (!world.isRemote) {
				entityPlayer.swingArm(enumHand);
				WizardryUtilities.playSoundAtPlayer(entityPlayer, WizardrySounds.SPELL_LIGHTNING, 1.0f, 1.0f);
				WizardryUtilities.playSoundAtPlayer(entityPlayer, WizardrySounds.SPELL_SHOCKWAVE, 2.0f, 1.0f);
				world.spawnEntity(new EntityThunderBurst(world, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ,
						entityPlayer, 30, 1));
				return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (!world.isRemote) {
				caster.swingArm(hand);
				world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_LIGHTNING, SoundCategory.HOSTILE, 1.0f, 1.0f, false);
				world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_SHOCKWAVE, SoundCategory.HOSTILE, 2.0f, 1.0f, true);
				world.spawnEntity(new EntityThunderBurst(world, caster.posX, caster.posY, caster.posZ,
						caster, 30, 1));
				return true;

		}
		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
