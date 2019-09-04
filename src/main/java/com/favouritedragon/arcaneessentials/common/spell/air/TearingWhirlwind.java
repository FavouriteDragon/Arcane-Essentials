package com.favouritedragon.arcaneessentials.common.spell.air;

import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

//Continuous spinning sword spell
public class TearingWhirlwind extends Spell implements IArcaneSpell {

	public TearingWhirlwind(String name, EnumAction action, boolean isContinuous) {
		super(name, action, isContinuous);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
