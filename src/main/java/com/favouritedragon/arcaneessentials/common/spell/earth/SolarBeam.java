package com.favouritedragon.arcaneessentials.common.spell.earth;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class SolarBeam extends Spell {

	public SolarBeam(Tier tier, int cost, Element element, String name, SpellType type, int cooldown, EnumAction action, boolean isContinuous) {
		super(Tier.APPRENTICE, 50, Element.EARTH, "solar_beam", type, cooldown, action, isContinuous);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
