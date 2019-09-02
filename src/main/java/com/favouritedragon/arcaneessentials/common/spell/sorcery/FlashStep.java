package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlashStep extends Spell {

	public FlashStep() {
		super(ArcaneEssentials.MODID, "flash_step", EnumAction.BOW, false);
		addProperties(RANGE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = getProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
		RayTraceResult result = ArcaneUtils.standardEntityRayTrace(world, caster, null, caster.getPositionVector().add(0, caster.getEyeHeight(), 0),
				 range, false, 0, true, false);
		Vec3d tPos = caster.getLookVec().scale(range).add(caster.getPositionVector().add(0, caster.getEyeHeight(), 0));
		if (result != null) {
			tPos = result.hitVec.add(0, 1, 0);
		}
		if (ArcaneUtils.attemptTeleport(caster, tPos.x, tPos.y, tPos.z)) {
			if (!world.isRemote) {


			}
			if (world.isRemote) {
				//spawn particles
			}
			return true;
		}


		return false;
	}

}
