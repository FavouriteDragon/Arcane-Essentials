package com.favouritedragon.arcaneessentials.common.spell.necromancy;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class KaThwack extends Spell {
	/**
	 * The radius within which maximum damage is dealt and maximum repulsion velocity is applied.
	 */
	private static final double EPICENTRE_RADIUS = 1;
	private static final String DEATH_CHANCE = "death_chance";

	public KaThwack() {
		super(ArcaneEssentials.MODID, "kathwack", EnumAction.BOW, false);
		this.soundValues(2, 0.8f, 0.3F);
		addProperties(BLAST_RADIUS, DAMAGE, DEATH_CHANCE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

		double radius = getProperty(BLAST_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade);

		List<EntityLivingBase> targets = WizardryUtilities.getEntitiesWithinRadius(radius, caster.posX, caster.posY, caster.posZ, world);

		for (EntityLivingBase target : targets) {

			if (MagicDamage.isEntityImmune(MagicDamage.DamageType.WITHER, target)) {

				if (!world.isRemote) caster.sendStatusMessage(new TextComponentTranslation("spell.resist",
						target.getName(), this.getNameForTranslationFormatted()), true);
				return false;
			}

			if (AllyDesignationSystem.isValidTarget(caster, target)) {

				// Produces a linear profile from 0 at the edge of the radius to 1 at the epicentre radius, then
				// a constant value of 1 within the epicentre radius.
				float proximity = (float) (1 - (Math.max(target.getDistance(caster) - EPICENTRE_RADIUS, 0)) / (radius - EPICENTRE_RADIUS));

				// Death chance increases closer to player up to a maximum of 20% at full hearts (at 1 block distance).
				float chance = ArcaneUtils.getRandomNumberInRange(1, 100);
				float healthMod = target.getHealth() / target.getMaxHealth();
				if (chance > ((getProperty(DEATH_CHANCE).floatValue() + healthMod * 100) / (0.5 + proximity / 2)) ||
						target.getHealth() <= getProperty(DAMAGE).floatValue() * proximity * (modifiers.get(SpellModifiers.POTENCY))) {
					if (target.getHealth() > 0 || !target.isDead) {
						caster.heal(target.getHealth());
						WizardryUtilities.attackEntityWithoutKnockback(target, MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.WITHER),
								target.getHealth() + 1);
						if (world.isRemote) {
							for (int i = 0; i < 80; i++) {
								ParticleBuilder.create(ParticleBuilder.Type.DARK_MAGIC).pos(target.posX, target.getEntityBoundingBox().minY, target.posZ)
										.vel(world.rand.nextBoolean() ? world.rand.nextFloat() / 30 : -world.rand.nextFloat() / 30, world.rand.nextFloat() / 10,
												world.rand.nextBoolean() ? world.rand.nextFloat() / 30 : -world.rand.nextFloat() / 30).clr(36, 7, 71).spawn(world);
							}
							ParticleBuilder.spawnHealParticles(world, caster);
						}
					}
				}
				else {
					if (!world.isRemote)
						WizardryUtilities.attackEntityWithoutKnockback(target, MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.WITHER),
								getProperty(DAMAGE).floatValue() * proximity * modifiers.get(SpellModifiers.POTENCY));
				}
			}
		}

		if (world.isRemote) {

			double particleX, particleZ;

			for (int i = 0; i < 40; i++) {

				particleX = caster.posX - 1.0d + 2 * world.rand.nextDouble();
				particleZ = caster.posZ - 1.0d + 2 * world.rand.nextDouble();
				IBlockState block = WizardryUtilities.getBlockEntityIsStandingOn(caster);

				if (block != null) {
					world.spawnParticle(EnumParticleTypes.BLOCK_DUST, particleX, caster.getEntityBoundingBox().minY,
							particleZ, particleX - caster.posX, 0, particleZ - caster.posZ, Block.getStateId(block));
				}
			}

			for (int i = 0; i < 4; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.SPHERE)
						.pos(caster.posX, caster.getEntityBoundingBox().minY + 0.1, caster.posZ)
						.scale((float) radius * 0.5f)
						.clr(21, 0, 46)
						.shaded(true)
						.spawn(world);
			}

		}

		caster.swingArm(hand);
		playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}
}
