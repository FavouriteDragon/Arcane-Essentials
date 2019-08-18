package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MagicBurst extends Spell {

	public MagicBurst(String name, EnumAction action, boolean isContinuous) {
		super(name, action, isContinuous);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
