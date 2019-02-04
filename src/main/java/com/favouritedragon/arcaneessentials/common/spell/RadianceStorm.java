package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class RadianceStorm extends Spell {
	public RadianceStorm() {
		super(Tier.MASTER, 300, Element.HEALING, "radiance_storm", SpellType.ATTACK, 300, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float damage = 8 + 2 * modifiers.get(WizardryItems.blast_upgrade);
		int fireTime = 10 + 2 * (int) modifiers.get(WizardryItems.duration_upgrade);
		float beamRadius = 1 * modifiers.get(WizardryItems.blast_upgrade);
		for (int r = 0; r < 6; r++) {
			float radius = world.rand.nextInt((2 + 2 * (int) modifiers.get(WizardryItems.range_upgrade))) + world.rand.nextFloat() * modifiers.get(WizardryItems.blast_upgrade);
			double angle = world.rand.nextDouble() * Math.PI * 2;
			double x = caster.posX + radius * Math.cos(angle);
			double z = caster.posZ + radius * Math.sin(angle);
			double y = WizardryUtilities.getNearestFloorLevel(world, new BlockPos(x, caster.posY, z), 2 + 2 * (int) modifiers.get(WizardryItems.range_upgrade));
			Vec3d startPos = new Vec3d(x, caster.getEntityBoundingBox().minY + 30, z);
			Vec3d endPos = new Vec3d(x, y, z);
			Vec3d direction = endPos.subtract(startPos);
			spawnRadiantBeam(world, caster, startPos, endPos, beamRadius, damage, direction, fireTime);
			spawnSphericalExplosion(world, caster, endPos, beamRadius * 2, damage,
					new Vec3d(2, 0.1, 2).scale(modifiers.get(WizardryItems.blast_upgrade)), fireTime);
		}
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return super.cast(world, caster, hand, ticksInUse, target, modifiers);
	}

	private void spawnRadiantBeam(World world, EntityLivingBase caster, Vec3d startPos, Vec3d endPos, float radius, float damage, Vec3d knockBack, int fireTime) {
		if (!world.isRemote) {
			ArcaneUtils.handlePiercingBeamCollision(world, caster, startPos, endPos, radius, null, true, MagicDamage.DamageType.RADIANT,
					damage, knockBack, true, fireTime, radius);
		}
		if (world.isRemote) {
			ArcaneUtils.spawnSpinningHelix(world, 480, 30, radius, WizardryParticleType.SPARKLE, endPos,
					new Vec3d(0.025, -0.0025, 0.025), Vec3d.ZERO, 30, 1.0F, 1.0F, 0.3F);
		}
	}

	private void spawnSphericalExplosion(World world, EntityLivingBase caster, Vec3d position, float radius, float damage, Vec3d knockBackScale, int fireTime) {
		if (world.isRemote) {
			double x, y, z;
			for (double theta = 0; theta <= 180; theta += 1) {
				double dphi = 30 / Math.sin(Math.toRadians(theta));

				for (double phi = 0; phi < 360; phi += dphi) {
					double rphi = Math.toRadians(phi);
					double rtheta = Math.toRadians(theta);

					x = radius * Math.cos(rphi) * Math.sin(rtheta);
					y = radius * Math.sin(rphi) * Math.sin(rtheta);
					z = radius * Math.cos(rtheta);

					Wizardry.proxy.spawnParticle(WizardryParticleType.SPARKLE, world, position.x, position.y, position.z, x/20, y/20, z/20, 10, 1.0F, 1.0F, 0.3F);

				}
			}
		}
		if (!world.isRemote) {
			AxisAlignedBB hitBox = new AxisAlignedBB(position.x, position.y, position.z, position.x + radius, position.y + radius, position.z + radius);
			List<Entity> hit = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
			if (!hit.isEmpty()) {
				for (Entity e : hit) {
					if (WizardryUtilities.isValidTarget(caster, e)) {
						if (e != caster) {
							if ((e instanceof EntityPlayer && caster instanceof EntityPlayer && !WizardryUtilities.isPlayerAlly((EntityPlayer) caster,
									(EntityPlayer) e)) || e.getTeam() != caster.getTeam()) {
								if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.RADIANT, e)) {
									double dx = e.posX - caster.posX;
									double dy = e.posY - caster.posY;
									double dz = e.posZ - caster.posZ;
									// Normalises the velocity.
									double vectorLength = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
									dx /= vectorLength;
									dy /= vectorLength;
									dz /= vectorLength;

									e.motionX = knockBackScale.x * dx;
									e.motionY = knockBackScale.y * dy;
									e.motionZ = knockBackScale.z * dz;
									e.setFire(fireTime);
									e.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.RADIANT), damage);
									ArcaneUtils.applyPlayerKnockback(e);
								}
							}
						}
					}
				}
			}
		}
	}
}
