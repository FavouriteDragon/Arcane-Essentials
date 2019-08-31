package com.favouritedragon.arcaneessentials.common.util;

import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import electroblob.wizardry.spell.Earthquake;
import electroblob.wizardry.spell.Meteor;
import electroblob.wizardry.spell.Shockwave;
import electroblob.wizardry.spell.Spell;

public class SpellUtils {

	public static boolean isSwordCastable(Spell spell) {
		if (spell instanceof IArcaneSpell) {
			return ((IArcaneSpell) spell).isSwordCastable();
		}
		return false;
	}

	public static boolean isWandCastable(Spell spell) {
		if (spell instanceof IArcaneSpell) {
			return ((IArcaneSpell) spell).isWandCastable();
		}
		return false;
	}

	public static boolean isAxeCastable(Spell spell) {
		if (spell instanceof IArcaneSpell) {
			return ((IArcaneSpell) spell).isAxeCastable();
		}
		else {
			return spell instanceof Earthquake || spell instanceof Shockwave || spell instanceof Meteor;
		}
	}
}
