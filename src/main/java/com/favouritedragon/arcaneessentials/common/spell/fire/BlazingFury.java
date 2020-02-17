package com.favouritedragon.arcaneessentials.common.spell.fire;

import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class BlazingFury extends Spell {
	public BlazingFury(String name, EnumAction action, boolean isContinuous) {
		super(name, action, isContinuous);
	}
	//Lowers cooldowns for a duration, increases power, increases power by the next attack that hits you.

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
