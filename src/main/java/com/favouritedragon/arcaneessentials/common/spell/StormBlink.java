package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class StormBlink extends Spell {
	public StormBlink() {
		super(Tier.MASTER, 80, Element.LIGHTNING, "storm_blink", SpellType.UTILITY, 280, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float radius = 2 * modifiers.get(WizardryItems.range_upgrade);
		//TODO: Adjust particle amount. Add a config, and add trailing particlew when teleporting to make it feel more realistic
		if (!world.isRemote) {
			RayTraceResult result = WizardryUtilities.rayTrace(80, world, caster, false);
			if (result != null) {
				BlockPos pos = result.getBlockPos();
				if (caster.attemptTeleport(pos.getX(), pos.getY() + 1, pos.getZ())) {
					WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_LIGHTNING, SoundCategory.AMBIENT, 4.0F, 2.0F);
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
										4 * modifiers.get(WizardryItems.blast_upgrade));
								double dx = target.posX - caster.posX;
								double dz = target.posZ - caster.posZ;
								// Normalises the velocity.
								double vectorLength = MathHelper.sqrt(dx * dx + dz * dz);
								dx /= vectorLength;
								dz /= vectorLength;

								target.motionX = 2 * dx;
								target.motionY = 0.2;
								target.motionZ = 2 * dz;

								// Player motion is handled on that player's client so needs packets
								if (target instanceof EntityPlayerMP) {
									((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
								}
							}


						}
					}
					double x, y, z;
					for (double theta = 0; theta <= 180; theta += 1) {
						double dphi = 15 / Math.sin(Math.toRadians(theta));

						for (double phi = 0; phi < 360; phi += dphi) {
							double rphi = Math.toRadians(phi);
							double rtheta = Math.toRadians(theta);

							x = radius * Math.cos(rphi) * Math.sin(rtheta);
							y = radius * Math.sin(rphi) * Math.sin(rtheta);
							z = radius * Math.cos(rtheta);

							Wizardry.proxy.spawnParticle(WizardryParticleType.SPARK, world, x + caster.posX, y + caster.posY, z + caster.posZ, 0, 0, 0, 10);
						}

					}
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		float radius = 4 * modifiers.get(WizardryItems.range_upgrade);

		if (!world.isRemote) {
			RayTraceResult result = WizardryUtilities.rayTrace(60 * modifiers.get(WizardryItems.range_upgrade), world, caster, false);
			if (result != null) {
				BlockPos pos = result.getBlockPos();
				if (caster.attemptTeleport(pos.getX(), pos.getY() + 1, pos.getZ())) {
					world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_LIGHTNING, SoundCategory.AMBIENT, 4.0F, 2.0F, true);
					List<EntityLivingBase> targets = WizardryUtilities
							.getEntitiesWithinRadius(radius, caster.posX, caster.posY, caster.posZ, world);
					targets.remove(caster);
					for (EntityLivingBase target1 : targets) {
						if (MagicDamage.isEntityImmune(MagicDamage.DamageType.SHOCK, target1)) {
							caster.sendMessage(new TextComponentTranslation("spell.resist", target1.getName(),
									this.getNameForTranslationFormatted()));
						} else {
							if (target1 != caster) {
								target1.attackEntityFrom(
										MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.SHOCK),
										4 * modifiers.get(WizardryItems.blast_upgrade));
								double dx = target1.posX - caster.posX;
								double dz = target1.posZ - caster.posZ;
								// Normalises the velocity.
								double vectorLength = MathHelper.sqrt(dx * dx + dz * dz);
								dx /= vectorLength;
								dz /= vectorLength;

								target1.motionX = 2 * dx;
								target1.motionY = 0.2;
								target1.motionZ = 2 * dz;

								// Player motion is handled on that player's client so needs packets
								if (target instanceof EntityPlayerMP) {
									((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
								}
							}


						}
					}
					double x, y, z;
					for (double theta = 0; theta <= 180; theta += 1) {
						double dphi = 15 / Math.sin(Math.toRadians(theta));

						for (double phi = 0; phi < 360; phi += dphi) {
							double rphi = Math.toRadians(phi);
							double rtheta = Math.toRadians(theta);

							x = radius * Math.cos(rphi) * Math.sin(rtheta);
							y = radius * Math.sin(rphi) * Math.sin(rtheta);
							z = radius * Math.cos(rtheta);

							Wizardry.proxy.spawnParticle(WizardryParticleType.SPARK, world, x + caster.posX, y + caster.posY, z + caster.posZ, 0, 0, 0, 10);
						}

					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
