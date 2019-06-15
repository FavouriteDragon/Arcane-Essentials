package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MagicMirror extends Spell {
	public MagicMirror(Tier tier, int cost, Element element, String name, SpellType type, int cooldown, EnumAction action, boolean isContinuous, String modID) {
		super(tier, cost, element, name, type, cooldown, action, isContinuous, modID);
	}

	@Override
	public boolean cast(World world, EntityPlayer entityPlayer, EnumHand enumHand, int i, SpellModifiers spellModifiers) {
		return false;
	}
}
