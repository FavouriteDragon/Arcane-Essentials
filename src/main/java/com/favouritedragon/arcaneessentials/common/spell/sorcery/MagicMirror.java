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


	public MagicMirror(String name, EnumAction action, boolean isContinuous) {
		super(name, action, isContinuous);
	}

	@Override
	public boolean cast(World world, EntityPlayer entityPlayer, EnumHand enumHand, int i, SpellModifiers spellModifiers) {
		return false;
	}
}
