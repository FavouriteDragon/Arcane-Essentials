package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class RadianceStorm extends Spell {
	public RadianceStorm() {
		super(Tier.MASTER, 125, Element.HEALING, "radiance_storm", SpellType.ATTACK, 300, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (world.canBlockSeeSky(new BlockPos(caster))) {
			float damage = 8 + 2 * modifiers.get(WizardryItems.blast_upgrade);
			int fireTime = 10 + 2 * (int) modifiers.get(WizardryItems.duration_upgrade);
			float beamRadius = 1 * modifiers.get(WizardryItems.blast_upgrade);
			for (int r = 0; r < 7; r++) {
				float radius = world.rand.nextInt((4 + 2 * (int) modifiers.get(WizardryItems.range_upgrade))) + world.rand.nextFloat() * modifiers.get(WizardryItems.blast_upgrade);
				double angle = world.rand.nextDouble() * Math.PI * 2;
				double x = caster.posX + radius * Math.cos(angle);
				double z = caster.posZ + radius * Math.sin(angle);
				double y = WizardryUtilities.getNearestFloorLevel(world, new BlockPos(x, caster.posY, z), 3 + 2 * (int) modifiers.get(WizardryItems.range_upgrade));
				Vec3d startPos = new Vec3d(x, caster.getEntityBoundingBox().minY + 30, z);
				Vec3d endPos = new Vec3d(x, y, z);
				Vec3d direction = endPos.subtract(startPos);
				spawnRadiantBeam(world, caster, startPos, endPos, beamRadius, damage, direction, fireTime);
				handleSphericalExplosion(world, caster, endPos, beamRadius * 2, damage,
						new Vec3d(2, 0.1, 2).scale(modifiers.get(WizardryItems.blast_upgrade)), fireTime);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (world.canBlockSeeSky(new BlockPos(caster))) {
			float damage = 8 + 2 * modifiers.get(WizardryItems.blast_upgrade);
			int fireTime = 10 + 2 * (int) modifiers.get(WizardryItems.duration_upgrade);
			float beamRadius = 1 * modifiers.get(WizardryItems.blast_upgrade);
			for (int r = 0; r < 6; r++) {
				float radius = world.rand.nextInt((4 + 2 * (int) modifiers.get(WizardryItems.range_upgrade))) * 1.5F + world.rand.nextFloat() * modifiers.get(WizardryItems.blast_upgrade);
				double angle = world.rand.nextDouble() * Math.PI * 2;
				double x = caster.posX + radius * Math.cos(angle);
				double z = caster.posZ + radius * Math.sin(angle);
				double y = WizardryUtilities.getNearestFloorLevel(world, new BlockPos(x, caster.posY, z), 4 + 2 * (int) modifiers.get(WizardryItems.range_upgrade));
				Vec3d startPos = new Vec3d(x, caster.getEntityBoundingBox().minY + 30, z);
				Vec3d endPos = new Vec3d(x, y, z);
				Vec3d direction = endPos.subtract(startPos);
				spawnRadiantBeam(world, caster, startPos, endPos, beamRadius, damage, direction, fireTime);
				handleSphericalExplosion(world, caster, endPos, beamRadius * 1.5F, damage,
						new Vec3d(2, 0.1, 2).scale(modifiers.get(WizardryItems.blast_upgrade)), fireTime);
			}
			return true;
		}
		return false;
	}

	private void spawnRadiantBeam(World world, EntityLivingBase caster, Vec3d startPos, Vec3d endPos, float radius, float damage, Vec3d knockBack, int fireTime) {
		if (!world.isRemote) {
			ArcaneUtils.handlePiercingBeamCollision(world, caster, startPos, endPos, radius, null, true, MagicDamage.DamageType.RADIANT,
					damage, knockBack, true, fireTime, radius);
		}
		if (world.isRemote) {
			ArcaneUtils.spawnSpinningHelix(world, 420, 30, radius, WizardryParticleType.SPARKLE, endPos,
					new Vec3d(0.0075, -0.0025, 0.0075), Vec3d.ZERO, 30, 1.0F, 1.0F, 0.3F);
		}
		world.playSound(endPos.x, endPos.y, endPos.z, WizardrySounds.SPELL_HEAL, SoundCategory.HOSTILE, 1.5F, 1F, true);
		world.playSound(endPos.x, endPos.y, endPos.z, WizardrySounds.SPELL_SHOCKWAVE, SoundCategory.HOSTILE, 1.5F, 1F, true);
	}

	private void handleSphericalExplosion(World world, EntityLivingBase caster, Vec3d position, float radius, float damage, Vec3d knockBackScale, int fireTime) {
		if (!world.isRemote) {
			AxisAlignedBB hitBox = new AxisAlignedBB(position.x - radius, position.y - radius, position.z - radius, position.x + radius, position.y + radius, position.z + radius);
			List<Entity> hit = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
			if (!hit.isEmpty()) {
				for (Entity e : hit) {
					if (WizardryUtilities.isValidTarget(caster, e)) {
						if (e != caster) {
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
