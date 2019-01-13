package com.favouritedragon.arcaneessentials.common.spell;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ThunderBurst extends Spell {

	public ThunderBurst() {
		super(Tier.MASTER, 100, Element.LIGHTNING, "thunder_burst", SpellType.ATTACK, 300, EnumAction.BOW, false);
	}
	@Override
	public boolean cast(World world, EntityPlayer entityPlayer, EnumHand enumHand, int i, SpellModifiers spellModifiers) {
		if (!world.isRemote) {

		}
		return false;
	}
}
