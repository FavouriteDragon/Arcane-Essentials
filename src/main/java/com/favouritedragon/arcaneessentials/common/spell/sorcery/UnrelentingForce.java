package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import com.favouritedragon.arcaneessentials.common.item.weapon.ItemMagicSword;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UnrelentingForce extends ArcaneSpell {

	public UnrelentingForce() {
		super("unrelenting_force", EnumAction.BOW, false);
		addProperties(DAMAGE, RANGE, EFFECT_STRENGTH, EFFECT_RADIUS);
		this.soundValues(5.0f, 1.125F, 0.0875F);
		this.npcSelector = npcSelector.or((entityLiving, aBoolean) -> entityLiving != null);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!(caster.getHeldItem(hand).getItem() instanceof ItemMagicSword))
			caster.swingArm(hand);
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return cast(world, caster, hand, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (!(caster.getHeldItem(hand).getItem() instanceof ItemMagicSword))
			caster.swingArm(hand);
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return cast(world, caster, hand, modifiers);
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
			for (int i = 0; i < 80; i++) {
				double x1 = caster.posX + look.x;
				double y1 = eyepos - 0.4F;
				double z1 = caster.posZ + look.z;

				//Using the random function each time ensures a different number for every value, making the ability "feel" better.
				ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(x1, y1, z1).vel(look.x * mult * ArcaneUtils.getRandomNumberInRange(1, 2),
						look.y * mult * ArcaneUtils.getRandomNumberInRange(1, 2),
						look.z * mult * ArcaneUtils.getRandomNumberInRange(1, 2)).clr(0x04ffb4).time(20).scale(getProperty(EFFECT_STRENGTH).floatValue() * 3).spawn(world);
			}
		}

		//Used through left-click on swords.
		//if (!caster.isSwingInProgress)
		//	caster.swingArm(hand);

		caster.playSound(WizardrySounds.ENTITY_FORCEFIELD_DEFLECT, 0.2F,
				world.rand.nextFloat() * 0.05F + 0.025F);
		caster.playSound(SoundEvents.ENTITY_LIGHTNING_IMPACT, 0.4F, world.rand.nextFloat() / 10 + 0.5F);

		if (!world.isRemote) {
			Vec3d startPos = new Vec3d(caster.posX, eyepos, caster.posZ);
			Vec3d endPos = startPos.add(caster.getLookVec().scale(mult * range));
			ArcaneUtils.vortexEntityCollision(world, caster, null, startPos, endPos, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY),
					damage, look.scale(mult / 0.6 * 2), MagicDamage.DamageType.FROST, true);

			return true;
		}

		return true;
	}

	@Override
	public boolean castRightClick() {
		return false;
	}
}