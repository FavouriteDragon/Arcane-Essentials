package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.block.BlockStatue;
import electroblob.wizardry.registry.WizardryBlocks;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

import static electroblob.wizardry.util.SpellModifiers.POTENCY;

public class AbsoluteZero extends ArcaneSpell {

	//Freezes all enemies within a small radius, and slows enemies outside of it.
	public AbsoluteZero() {
		super("absolute_zero", EnumAction.BOW, false);
		addProperties(EFFECT_STRENGTH, EFFECT_RADIUS, EFFECT_DURATION);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(POTENCY);
		float particleSize = getProperty(EFFECT_STRENGTH).floatValue();


		List<EntityLivingBase> nearby = WizardryUtilities.getEntitiesWithinRadius(range, caster.posX, caster.getEntityBoundingBox().minY, caster.posZ, world);
		nearby.remove(caster);
		if (!nearby.isEmpty()) {
			for (EntityLivingBase target : nearby) {
				if (AllyDesignationSystem.isValidTarget(caster, target)) {
					((BlockStatue) WizardryBlocks.ice_statue).convertToStatue((EntityLiving) target, getProperty(EFFECT_DURATION).intValue() * 20);
				}
			}
		}
		if (world.isRemote) {
			for (int angle = 0; angle < 360; angle++) {
				double radians = Math.toRadians(angle);
				double x = Math.cos(radians);
				double z = Math.sin(radians);
				//On the client, the posY is the eye position.
				ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(x + caster.posX, caster.posY + 0.5, z + caster.posZ).vel(x * range / 10,
						world.rand.nextGaussian() / 10 * range, z * range / 10).time(14).clr(205, 254, 255).scale((float) (particleSize)).spawn(world);
			}
			ParticleBuilder.create(ParticleBuilder.Type.SPHERE).entity(caster).time(6).scale((float) range * 1.25F).clr(205, 254, 255).spawn(world);
		}
		caster.playSound(WizardrySounds.MISC_FREEZE, 3.0F, 1.0F);
		caster.playSound(WizardrySounds.ENTITY_ICE_WRAITH_AMBIENT, 2.0F, 1.0F);
		caster.swingArm(hand);

		return true;
	}
}
