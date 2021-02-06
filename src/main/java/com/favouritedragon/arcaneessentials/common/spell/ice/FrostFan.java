package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.common.item.weapon.ItemMagicSword;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.entity.projectile.EntityIceLance;
import electroblob.wizardry.entity.projectile.EntityMagicArrow;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import static electroblob.wizardry.entity.projectile.EntityMagicArrow.LAUNCH_Y_OFFSET;

public class FrostFan extends ArcaneSpell {

	private static final String FROST_FANS = "frost_fans";
	private static final float DISPENSER_INACCURACY = 1; // This is the same as for players
	private static final float FALLBACK_VELOCITY = 2; // 2 seems to be a pretty standard value

	public FrostFan() {
		super("frost_fan", EnumAction.NONE, false);
		addProperties(DAMAGE, RANGE, FROST_FANS, EFFECT_STRENGTH);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!(caster.getHeldItem(hand).getItem() instanceof ItemMagicSword))
			caster.swingArm(hand);
		return cast(world, caster, modifiers);
	}

	private boolean cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
		int fans = getProperty(FROST_FANS).intValue();
		//noinspection IntegerDivisionInFloatingPointContext
		double totalAngle = fans % 2 == 0 ? (float) fans / 2 * 15 - 7.5 : fans / 2 * 15;
			for (//noinspection IntegerDivisionInFloatingPointContext
					double angle = fans % 2 == 0 ? (float) fans / 2F * -15 + 7.5 : (fans / 2) * -15; angle <= totalAngle; angle += 15) {
				EntityIceLance lance = new EntityIceLance(world);
				aim(caster.rotationPitch, caster.rotationYaw + (float) angle, caster, calculateVelocity(lance, modifiers, caster.getEyeHeight()
						- (float)EntityMagicArrow.LAUNCH_Y_OFFSET), lance);
				lance.damageMultiplier = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
				if (!world.isRemote) {
					world.spawnEntity(lance);
				}
			}
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (!(caster.getHeldItem(hand).getItem() instanceof ItemMagicSword))
			caster.swingArm(hand);
		return cast(world, caster, modifiers);
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		return super.cast(world, x, y, z, direction, ticksInUse, duration, modifiers);
	}

	@Override
	public boolean castRightClick() {
		return false;
	}

	/**
	 * Computes the velocity the projectile should be launched at to achieve the required range.
	 */
	// Long story short, it doesn't make much sense to me to have the JSON file specify the velocity - even less so if
	// the velocity is masquerading under the tag 'range' - so we'll let the code do the heavy lifting so people can
	// input something meaningful.
	protected float calculateVelocity(EntityMagicArrow projectile, SpellModifiers modifiers, float launchHeight) {
		// The required range
		float range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);

		if (!projectile.doGravity()) {
			// No sensible spell will do this - range is meaningless if the particle has no gravity or lifetime
			if (projectile.getLifetime() <= 0) return FALLBACK_VELOCITY;
			// Speed = distance/time (trivial, I know, but I've put it here for the sake of completeness)
			return range / projectile.getLifetime();
		} else {
			// Arrows have gravity 0.05
			float g = 0.05f;
			// Assume horizontal projection
			return range / MathHelper.sqrt(2 * launchHeight / g);
		}
	}

	private void aim(float rotationPitch, float rotationYaw, EntityLivingBase caster, float speed, EntityMagicArrow projectile) {
		projectile.setCaster(caster);

		projectile.setLocationAndAngles(caster.posX, caster.getEntityBoundingBox().minY + (double) caster.getEyeHeight() - LAUNCH_Y_OFFSET,
				caster.posZ, rotationYaw, rotationPitch);

		projectile.posX -= (double)(MathHelper.cos(projectile.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		projectile.posY -= 0.10000000149011612D;
		projectile.posZ -= (double)(MathHelper.sin(projectile.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);

		projectile.setPosition(projectile.posX, projectile.posY, projectile.posZ);

		// yOffset was set to 0 here, but that has been replaced by getYOffset(), which returns 0 in Entity anyway.
		projectile.motionX = (double)(-MathHelper.sin(projectile.rotationYaw / 180.0F * (float)Math.PI)
				* MathHelper.cos(projectile.rotationPitch / 180.0F * (float)Math.PI));
		projectile.motionY = (double)(-MathHelper.sin(projectile.rotationPitch / 180.0F * (float)Math.PI));
		projectile.motionZ = (double)(MathHelper.cos(projectile.rotationYaw / 180.0F * (float)Math.PI)
				* MathHelper.cos(projectile.rotationPitch / 180.0F * (float)Math.PI));

		projectile.shoot(projectile.motionX, projectile.motionY, projectile.motionZ, speed * 1.5F, 1.0F);
	}
}
