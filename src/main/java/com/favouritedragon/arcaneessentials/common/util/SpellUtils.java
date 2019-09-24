package com.favouritedragon.arcaneessentials.common.util;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import com.favouritedragon.arcaneessentials.common.spell.arcane.ElementArcane;
import electroblob.wizardry.api.WizardryEnumHelper;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.spell.Earthquake;
import electroblob.wizardry.spell.Meteor;
import electroblob.wizardry.spell.Shockwave;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellProperties;
import net.minecraftforge.fml.common.Mod;

public class SpellUtils {

	public static final String SIZE = "size";
	public static final String SPEED = "speed";
	public static final String LIFE_STEAL = "life_steal";
	public static final String LIFETIME = "lifetime";

	public static boolean isSwordCastable(Spell spell) {
		if (spell instanceof IArcaneSpell) {
			return ((IArcaneSpell) spell).isSwordCastable();
		}
		return true; //spell.isEnabled(SWORDS);
	}

	public static boolean isWandCastable(Spell spell) {
		if (spell instanceof IArcaneSpell) {
			return ((IArcaneSpell) spell).isWandCastable();
		}
		return true; //spell.isEnabled(SpellProperties.Context.WANDS);
	}

	public static boolean isAxeCastable(Spell spell) {
		if (spell instanceof IArcaneSpell) {
			return ((IArcaneSpell) spell).isAxeCastable();
		}
			return false; //spell.isEnabled(AXES);
	}

	//TODO: Add spell sources

	//public static final SpellProperties.Context SWORDS = WizardryEnumHelper.addSpellContext("swords", "swords");
	//public static final SpellProperties.Context AXES = WizardryEnumHelper.addSpellContext("axes", "axes");
	//public static final SpellProperties.Context BOWS = WizardryEnumHelper.addSpellContext("bows", "bows");
	//public static final SpellProperties.Context SHIELDS = WizardryEnumHelper.addSpellContext("shields", "shields");



}
