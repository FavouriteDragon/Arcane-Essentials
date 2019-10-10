package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class AbsoluteZero extends ArcaneSpell {

	//Freezes all enemies within a small radius, and slows enemies outside of it.
	public AbsoluteZero(String name, EnumAction action, boolean isContinuous) {
		super(name, action, isContinuous);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
