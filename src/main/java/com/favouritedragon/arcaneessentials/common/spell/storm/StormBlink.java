package com.favouritedragon.arcaneessentials.common.spell.storm;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class StormBlink extends ArcaneSpell {

	public StormBlink() {
		super("storm_blink", EnumAction.BOW, false);
		addProperties(EFFECT_RADIUS, RANGE, DAMAGE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float radius = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		//TODO: Adjust particle amount. Add a config, and add trailing particlew when teleporting to make it feel more realistic
		RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, getProperty(RANGE).intValue() * modifiers.get(WizardryItems.range_upgrade), false);
		if (!world.isRemote) {
			if (result != null) {
				BlockPos pos = result.getBlockPos();
				if (caster.attemptTeleport(pos.getX(), pos.getY() + 1, pos.getZ())) {
					WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_LIGHTNING_IMPACT, WizardrySounds.SPELLS,
							2.0F + world.rand.nextFloat(), 1.0F + world.rand.nextFloat());
					WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, SoundCategory.AMBIENT, 4.0F, 1.0F);
					List<EntityLivingBase> targets = WizardryUtilities
							.getEntitiesWithinRadius(radius, caster.posX, caster.posY, caster.posZ, world);
					targets.remove(caster);
					for (EntityLivingBase target : targets) {
						if (MagicDamage.isEntityImmune(MagicDamage.DamageType.SHOCK, target)) {
							caster.sendMessage(new TextComponentTranslation("spell.resist", target.getName(),
									this.getNameForTranslationFormatted()));
						} else {
							if (target != caster) {
								target.attackEntityFrom(
										MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.SHOCK),
										getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));
								double dx = target.posX - caster.posX;
								double dz = target.posZ - caster.posZ;
								// Normalises the velocity.
								double vectorLength = MathHelper.sqrt(dx * dx + dz * dz);
								dx /= vectorLength;
								dz /= vectorLength;

								target.motionX = 2 * dx;
								target.motionY = 0.2;
								target.motionZ = 2 * dz;

								// Player motion is handled on that player's client so it needs packets
								if (target instanceof EntityPlayerMP) {
									((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityTeleport(target));
								}
							}


						}
					}

				}
				return true;
			}
		} else {
			double x, y, z;
			for (double theta = 0; theta <= 180; theta += 1) {
				double dphi = 40 / Math.sin(Math.toRadians(theta));

				for (double phi = 0; phi < 360; phi += dphi) {
					double rphi = Math.toRadians(phi);
					double rtheta = Math.toRadians(theta);

					x = radius * Math.cos(rphi) * Math.sin(rtheta);
					y = radius * Math.sin(rphi) * Math.sin(rtheta);
					z = radius * Math.cos(rtheta);

					double px, py, pz;

					for(int i = 0; i < 4; i++) {
						px = x + world.rand.nextDouble() - 0.5;
						py = y + world.rand.nextDouble() - 0.5;
						pz = z + world.rand.nextDouble() - 0.5;
						ParticleBuilder.create(ParticleBuilder.Type.SPARK).pos(px + caster.posX, py + caster.getEntityBoundingBox().minY, pz + caster.posZ)
								.vel(world.rand.nextDouble() * 0.3 * x, world.rand.nextDouble() * 0.3 * y, world.rand.nextDouble() * 0.3 * z).time(5).spawn(world);
					}
				}
			}

		}

		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		float radius = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		//TODO: Adjust particle amount. Add a config, and add trailing particlew when teleporting to make it feel more realistic
		RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, getProperty(RANGE).intValue() * modifiers.get(WizardryItems.range_upgrade), false);
		if (!world.isRemote) {
			if (result != null) {
				BlockPos pos = result.getBlockPos();
				if (caster.attemptTeleport(pos.getX(), pos.getY() + 1, pos.getZ())) {
					world.playSound(null, caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, SoundCategory.AMBIENT, 4.0F, 2.0F);
					List<EntityLivingBase> targets = WizardryUtilities
							.getEntitiesWithinRadius(radius, caster.posX, caster.posY, caster.posZ, world);
					targets.remove(caster);
					for (EntityLivingBase hit : targets) {
						if (MagicDamage.isEntityImmune(MagicDamage.DamageType.SHOCK, hit)) {
							caster.sendMessage(new TextComponentTranslation("spell.resist", hit.getName(),
									this.getNameForTranslationFormatted()));
						} else {
							if (hit != caster) {
								hit.attackEntityFrom(
										MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.SHOCK),
										getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));
								double dx = hit.posX - caster.posX;
								double dz = hit.posZ - caster.posZ;
								// Normalises the velocity.
								double vectorLength = MathHelper.sqrt(dx * dx + dz * dz);
								dx /= vectorLength;
								dz /= vectorLength;

								hit.motionX = 2 * dx;
								hit.motionY = 0.2;
								hit.motionZ = 2 * dz;

								// Player motion is handled on that player's client so it needs packets
								if (hit instanceof EntityPlayerMP) {
									((EntityPlayerMP) hit).connection.sendPacket(new SPacketEntityTeleport(hit));
								}
							}


						}
					}

				}
				return true;
			}
		} else {
			double x, y, z;
			for (double theta = 0; theta <= 180; theta += 1) {
				double dphi = 40 / Math.sin(Math.toRadians(theta));

				for (double phi = 0; phi < 360; phi += dphi) {
					double rphi = Math.toRadians(phi);
					double rtheta = Math.toRadians(theta);

					x = radius * Math.cos(rphi) * Math.sin(rtheta);
					y = radius * Math.sin(rphi) * Math.sin(rtheta);
					z = radius * Math.cos(rtheta);

					double px, py, pz;

					for(int i = 0; i < 4; i++) {
						px = x + world.rand.nextDouble() - 0.5;
						py = y + world.rand.nextDouble() - 0.5;
						pz = z + world.rand.nextDouble() - 0.5;
						ParticleBuilder.create(ParticleBuilder.Type.SPARK).pos(px + caster.posX, py + caster.getEntityBoundingBox().minY, pz + caster.posZ)
								.vel(world.rand.nextDouble() * 0.3 * x, world.rand.nextDouble() * 0.3 * y, world.rand.nextDouble() * 0.3 * z).time(5).spawn(world);
					}
				}
			}

		}

		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
