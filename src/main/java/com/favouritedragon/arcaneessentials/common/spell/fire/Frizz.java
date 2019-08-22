package com.favouritedragon.arcaneessentials.common.spell.fire;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityFireball;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellProjectile;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.function.Function;

public class Frizz extends Spell {

	public Frizz() {
		super(ArcaneEssentials.MODID, "frizz", EnumAction.BOW, false);
	}


	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	private static void cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {

	}
}
