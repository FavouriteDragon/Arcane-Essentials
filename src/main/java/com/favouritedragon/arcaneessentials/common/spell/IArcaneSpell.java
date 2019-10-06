package com.favouritedragon.arcaneessentials.common.spell;

import net.minecraft.util.EnumHand;

//Temporary interface until the proper spell contexts are implemented
public interface IArcaneSpell {

	default boolean isSwordCastable() {
		return false;
	}

	default boolean isWandCastable() {
		return true;
	}

	default boolean isAxeCastable() {
		return false;
	}

	default boolean isShieldCastable() {
		return false;
	}

	default boolean castRightClick() {
		return true;
	}
}
