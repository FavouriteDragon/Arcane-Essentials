package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.data.IVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlashStep extends Spell {

	private static final IVariable<Integer> TELEPORTS = new IVariable.Variable<>(Persistence.DIMENSION_CHANGE);
	private static final String TELEPORT_NUMBER = "teleport_count";

	public FlashStep() {
		super(ArcaneEssentials.MODID, "flash_step", EnumAction.BOW, false);
		addProperties(RANGE, TELEPORT_NUMBER);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (WizardData.get(caster) != null)
			if (WizardData.get(caster).getVariable(TELEPORTS) == null || WizardData.get(caster).getVariable(TELEPORTS) < 1)
				WizardData.get(caster).setVariable(TELEPORTS, getProperty(TELEPORT_NUMBER).intValue());

		double range = getProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
		RayTraceResult result = ArcaneUtils.standardEntityRayTrace(world, caster, null, caster.getPositionVector().add(0, caster.getEyeHeight(), 0),
				 range, false, 0, true, false);
		Vec3d tPos = caster.getLookVec().scale(range).add(caster.getPositionVector().add(0, caster.getEyeHeight(), 0));
		if (result != null) {
			tPos = result.hitVec.add(0, 1, 0);
		}
		if (ArcaneUtils.attemptTeleport(caster, tPos.x, tPos.y, tPos.z)) {
			if (world.isRemote) {
				//spawn particles, play sounds
				WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_FORCE_ORB_HIT_BLOCK, 1.0F + world.rand.nextFloat() / 10,
						0.8F + world.rand.nextFloat() / 10);
				caster.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.5F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10);

			}
		}
		WizardData.get(caster).setVariable(TELEPORTS, WizardData.get(caster).getVariable(TELEPORTS) - 1);

		return WizardData.get(caster).getVariable(TELEPORTS) < 1;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return false;
	}
}
