package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import electroblob.wizardry.spell.SpellBuff;
import net.minecraft.potion.Potion;

import java.util.function.Supplier;

public class GiantStrength extends SpellBuff {
	public GiantStrength(String name, float r, float g, float b, Supplier<Potion>... effects) {
		super(name, r, g, b, effects);
	}

	public GiantStrength(String modID, String name, float r, float g, float b, Supplier<Potion>... effects) {
		super(modID, name, r, g, b, effects);
	}
}
