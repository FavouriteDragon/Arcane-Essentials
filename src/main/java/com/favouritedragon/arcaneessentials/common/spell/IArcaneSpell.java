package com.favouritedragon.arcaneessentials.common.spell;

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
}
