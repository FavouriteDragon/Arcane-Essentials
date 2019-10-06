package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FrostBlast extends ArcaneSpell {

	public FrostBlast() {
		super(ArcaneEssentials.MODID, "frost_blast", EnumAction.BOW, false);
		addProperties(DAMAGE, RANGE, EFFECT_STRENGTH, EFFECT_RADIUS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return cast(world, caster, hand, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return cast(world, caster, hand, modifiers);
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}

	private boolean cast(World world, EntityLivingBase caster, EnumHand hand, SpellModifiers modifiers) {
		double range = getProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
		Vec3d look = caster.getLookVec();
		double mult = getProperty(EFFECT_STRENGTH).doubleValue() * 0.5 * modifiers.get(SpellModifiers.POTENCY);
		double eyepos = caster.getEyeHeight() + caster.getEntityBoundingBox().minY;
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		int min = -10 * getProperty(EFFECT_RADIUS).intValue();
		int max = 10 * getProperty(EFFECT_RADIUS).intValue();
		if (world.isRemote) {
			//Spawn particles
			for(int i = 0; i < 240; i++) {
				double x1 = caster.posX + look.x + world.rand.nextFloat() / 10 - 0.05f;
				double y1 = eyepos - 0.4F + world.rand.nextFloat() / 10 - 0.05f;
				double z1 = caster.posZ + look.z + world.rand.nextFloat() / 10 - 0.05f;

				//Using the random function each time ensures a different number for every value, making the ability "feel" better.
				ParticleBuilder.create(ParticleBuilder.Type.SNOW).pos(x1, y1, z1).vel(look.x * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 30
								+ ArcaneUtils.getRandomNumberInRange(min, max) / 40F,
						look.y * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 30
								+ ArcaneUtils.getRandomNumberInRange(min, max) / 40F,
						look.z * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 30
								+ ArcaneUtils.getRandomNumberInRange(min, max) / 40F)
						.face(caster.rotationYaw, caster.rotationPitch).spawn(world);
			}
		}

		//Used through left-click on swords.
		//if (!caster.isSwingInProgress)
		//	caster.swingArm(hand);

		if (!world.isRemote) {
			Vec3d startPos = new Vec3d(caster.posX, eyepos, caster.posZ);
			Vec3d endPos = startPos.add(caster.getLookVec().scale(mult * range));
			ArcaneUtils.vortexEntityCollision(world, caster, null, startPos, endPos, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY),
					damage, look.scale(mult / 0.6 * 2), MagicDamage.DamageType.FROST, true);
			caster.playSound(WizardrySounds.MISC_FREEZE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			caster.playSound(WizardrySounds.ENTITY_ICEBALL_HIT, 0.8F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			caster.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);

			return true;
		}

		return true;
	}

	@Override
	public boolean castRightClick() {
		return false;
	}
}
