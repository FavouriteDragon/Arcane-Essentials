package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class FlashStep extends Spell implements IArcaneSpell {

	public FlashStep() {
		super(ArcaneEssentials.MODID, "flash_step", EnumAction.BOW, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		//RayTraceResult result = ArcaneUtils.standardEntityRayTrace(world, caster);
		if (!world.isRemote) {

		}
		if (world.isRemote) {

		}
		return false;
	}


}
