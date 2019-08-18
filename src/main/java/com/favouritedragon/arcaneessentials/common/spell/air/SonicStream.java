package com.favouritedragon.arcaneessentials.common.spell.air;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class SonicStream extends Spell {

	public SonicStream(String name, EnumAction action, boolean isContinuous) {
		super(name, action, isContinuous);
	}
	//This spell just gives you a massive speed boost, a way better jump, and a melee buff


	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
