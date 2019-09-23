package com.favouritedragon.arcaneessentials.common.spell.storm;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.data.IVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ThunderStrike extends ArcaneSpell {

	private static final IVariable<Boolean> IS_POWERED = new IVariable.Variable<Boolean>(Persistence.DIMENSION_CHANGE);
	public ThunderStrike() {
		super(ArcaneEssentials.MODID, "thunder_strike", EnumAction.BOW, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	public boolean isSwordCastable() {
		return true;
	}


}
