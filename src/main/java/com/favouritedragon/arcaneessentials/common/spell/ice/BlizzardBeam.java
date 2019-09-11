package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneUtils.applyPlayerKnockback;

public class BlizzardBeam extends SpellRay {

	public BlizzardBeam() {
		super(ArcaneEssentials.MODID, "blizzard_beam", false, EnumAction.BOW);
		addProperties(DAMAGE, EFFECT_DURATION, EFFECT_STRENGTH, DIRECT_EFFECT_STRENGTH);
	}

	/*@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		Vec3d look = caster.getLookVec();
		float damage = getProperty(DAMAGE).floatValue() + modifiers.get(WizardryItems.blast_upgrade);
		float range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		double eyepos = caster.getEntityBoundingBox().minY + caster.getEyeHeight() - 0.4F;
		float knockbackMult = getProperty(DIRECT_EFFECT_STRENGTH).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		Vec3d startPos = new Vec3d(caster.posX, eyepos, caster.posZ);
		Vec3d endPos = caster.getLookVec().scale(range).add(startPos);

		if (!world.isRemote) {
			Vec3d knockBack = new Vec3d(knockbackMult, knockbackMult, knockbackMult);
			handlePiercingBeamCollision(world, caster, startPos, endPos, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY), null, true,
					MagicDamage.DamageType.FROST, damage, knockBack, true, 0, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY),
					0, RayTracer.ignoreEntityFilter(caster), modifiers);
		}

		if (world.isRemote) {
			for (int i = 0; i < 80; i++) {
				boolean b = world.rand.nextBoolean();
				double x1 = caster.posX + look.x * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				double y1 = eyepos + look.y * i / 2
						+ world.rand.nextFloat() / 5 - 0.1f;
				double z1 = caster.posZ + look.z * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				ParticleBuilder.create(ParticleBuilder.Type.SNOW).pos(x1, y1, z1).vel(b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80,
						world.rand.nextDouble() / 40,
						b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80).time(15).spawn(world);

			}
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).scale(getProperty(EFFECT_RADIUS).floatValue() * 5).pos(startPos).target(endPos)
					.time(15).clr(174, 252, 255).fade(230, 253, 254).collide(true).spawn(world);
			ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), 180, range, getProperty(EFFECT_RADIUS).floatValue(),
					ParticleBuilder.Type.SNOW, startPos, new Vec3d(world.rand.nextGaussian() / 80, world.rand.nextGaussian() / 40, world.rand.nextGaussian() / 80),
					12, -1, -1, -1);

		}
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_SHARD_SMASH, WizardrySounds.SPELLS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_CHARGE_ICE, WizardrySounds.SPELLS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_GIANT_ATTACK, WizardrySounds.SPELLS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		Vec3d look = caster.getLookVec();
		float damage = getProperty(DAMAGE).floatValue() + modifiers.get(SpellModifiers.POTENCY);
		float range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		double eyepos = caster.getEntityBoundingBox().minY + caster.getEyeHeight() - 0.4F;
		float knockbackMult = getProperty(DIRECT_EFFECT_STRENGTH).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		Vec3d startPos = new Vec3d(caster.posX, eyepos, caster.posZ);
		Vec3d endPos = caster.getLookVec().scale(range).add(startPos);

		if (!world.isRemote) {
			Vec3d knockBack = new Vec3d(knockbackMult, knockbackMult, knockbackMult);
			handlePiercingBeamCollision(world, caster, startPos, endPos, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY), null, true,
					MagicDamage.DamageType.FROST, damage, knockBack, true, 0, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY),
					0, RayTracer.ignoreEntityFilter(caster), modifiers);
		}

		if (world.isRemote) {
			for (int i = 0; i < 80; i++) {
				boolean b = world.rand.nextBoolean();
				double x1 = caster.posX + look.x * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				double y1 = eyepos + look.y * i / 2
						+ world.rand.nextFloat() / 5 - 0.1f;
				double z1 = caster.posZ + look.z * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				ParticleBuilder.create(ParticleBuilder.Type.SNOW).pos(x1, y1, z1).vel(b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80,
						world.rand.nextDouble() / 40,
						b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80).time(15).clr(1.0F, 1.0F, 0.3F).spawn(world);

			}
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).scale(getProperty(EFFECT_RADIUS).floatValue() * 5).pos(startPos).entity(caster).target(endPos)
					.time(6).clr(174, 252, 255).fade(230, 253, 254).spawn(world);
			ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), 180, range, getProperty(EFFECT_RADIUS).floatValue(),
					ParticleBuilder.Type.SNOW, startPos, new Vec3d(world.rand.nextGaussian() / 80, world.rand.nextGaussian() / 40, world.rand.nextGaussian() / 80),
					12, -1, -1, -1);

		}
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_SHARD_SMASH, WizardrySounds.SPELLS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_CHARGE_ICE, WizardrySounds.SPELLS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_GIANT_ATTACK, WizardrySounds.SPELLS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		return true;
	}


	//Method handling everything
	private void handlePiercingBeamCollision(World world, EntityLivingBase caster, Vec3d startPos, Vec3d endPos, float borderSize, Entity spellEntity, boolean directDamage, MagicDamage.DamageType damageType,
											 float damage, Vec3d knockBack, boolean invulnerable, int fireTime, float radius, float lifeSteal,
											 Predicate<? super Entity> filter, SpellModifiers modifiers) {
		filter = filter.or(e -> e == caster);
		if (spellEntity != null) {
			filter = filter.or(e -> e == spellEntity);
		}

		RayTraceResult result = standardEntityRayTrace(world, startPos, endPos, filter, false, borderSize, true, false);

		if (result != null && result.entityHit instanceof EntityLivingBase && !filter.test(result.entityHit)) {
			EntityLivingBase hit = (EntityLivingBase) result.entityHit;
			if (!MagicDamage.isEntityImmune(damageType, hit)) {
				hit.setFire(fireTime);
				caster.heal(damage * lifeSteal);
				if (directDamage) {
					hit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, damageType), damage);
				} else if (spellEntity != null) {
					hit.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(spellEntity, caster, damageType), damage);

				}
				Vec3d kM = endPos.subtract(startPos).scale(.01);
				hit.motionX += knockBack.x * kM.x;
				hit.motionY += knockBack.y * kM.y;
				hit.motionZ += knockBack.z * kM.z;
				hit.setEntityInvulnerable(invulnerable);
				hit.addPotionEffect(new PotionEffect(WizardryPotions.frost, getProperty(EFFECT_DURATION).intValue() * (int) modifiers.get(WizardryItems.duration_upgrade),
						getProperty(EFFECT_STRENGTH).intValue() * (int) modifiers.get(SpellModifiers.POTENCY)));
				applyPlayerKnockback(hit);
				filter = filter.or(e -> e == hit);
			}
			Vec3d pos = hit.getPositionVector().add(0, hit.getEyeHeight(), 0);
			AxisAlignedBB hitBox = new AxisAlignedBB(pos.x + radius, pos.y + radius, pos.z + radius, pos.x - radius, pos.y - radius, pos.z - radius);
			List<Entity> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
			nearby.removeIf(filter);
			//This is so it doesn't count the entity that was hit by the raytrace and mess up the chain
			if (!nearby.isEmpty()) {
				for (Entity secondHit : nearby) {
					if (secondHit != caster && secondHit != hit && secondHit.getTeam() != caster.getTeam()) {
						if (!MagicDamage.isEntityImmune(damageType, secondHit)) {
							secondHit.setFire(fireTime);
							if (directDamage) {
								secondHit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, damageType), damage);
							} else if (spellEntity != null) {
								secondHit.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(spellEntity, caster, damageType), damage);
							}
							secondHit.motionX += knockBack.x;
							secondHit.motionY += knockBack.y;
							secondHit.motionZ += knockBack.z;
							applyPlayerKnockback(secondHit);
							if (secondHit instanceof EntityLivingBase)
								((EntityLivingBase) secondHit).addPotionEffect(new PotionEffect(WizardryPotions.frost, getProperty(EFFECT_DURATION).intValue() * (int) modifiers.get(WizardryItems.duration_upgrade),
										getProperty(EFFECT_STRENGTH).intValue() * (int) modifiers.get(SpellModifiers.POTENCY)));
							filter = filter.or(e -> e == secondHit);
						}
					}
				}
			} else {
				handlePiercingBeamCollision(world, caster, pos, endPos, borderSize, spellEntity, directDamage,
						damageType, damage, knockBack, invulnerable, fireTime, radius, lifeSteal, filter, modifiers);

			}

		}
	}**/

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		Predicate<Entity> filter = RayTracer.ignoreEntityFilter(caster);
		Vec3d knockBack = hit.subtract(origin).scale(.01 * getProperty(DIRECT_EFFECT_STRENGTH).floatValue());
		float damage = getProperty(DAMAGE).floatValue() + modifiers.get(SpellModifiers.POTENCY);
		if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, target) && target instanceof EntityLivingBase) {
			target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), damage);
			target.motionX += knockBack.x;
			target.motionY += knockBack.y;
			target.motionZ += knockBack.z;
			((EntityLivingBase) target).addPotionEffect(new PotionEffect(WizardryPotions.frost, getProperty(EFFECT_DURATION).intValue() * (int) modifiers.get(WizardryItems.duration_upgrade),
					getProperty(EFFECT_STRENGTH).intValue() * (int) modifiers.get(SpellModifiers.POTENCY)));
			applyPlayerKnockback(target);
			filter = filter.or(e -> e == target);
		}
		//This is actually just the area around the entity that gets hti
		float radius = getProperty(EFFECT_RADIUS).floatValue() / 2;
		Vec3d pos = target.getPositionVector().add(0, target.getEyeHeight(), 0);
		AxisAlignedBB hitBox = new AxisAlignedBB(pos.x + radius, pos.y + radius, pos.z + radius, pos.x - radius, pos.y - radius, pos.z - radius);
		List<Entity> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
		nearby.removeIf(filter);
		//This is so it doesn't count the entity that was hit by the raytrace and mess up the chain
		if (!nearby.isEmpty()) {
			for (Entity secondHit : nearby) {
				if (secondHit != caster && secondHit != target && AllyDesignationSystem.isValidTarget(caster, secondHit)) {
					if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, secondHit)) {
						secondHit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), damage);
						secondHit.motionX += knockBack.x;
						secondHit.motionY += knockBack.y;
						secondHit.motionZ += knockBack.z;
						applyPlayerKnockback(secondHit);
						if (secondHit instanceof EntityLivingBase)
							((EntityLivingBase) secondHit).addPotionEffect(new PotionEffect(WizardryPotions.frost, getProperty(EFFECT_DURATION).intValue() * (int) modifiers.get(WizardryItems.duration_upgrade),
									getProperty(EFFECT_STRENGTH).intValue() * (int) modifiers.get(SpellModifiers.POTENCY)));
						filter = filter.or(e -> e == secondHit);
					}
				}
			}
		}
		//shootSpell(world, hit.add(hit.subtract(origin).scale(0.5)), hit.subtract(origin), (EntityPlayer) caster, ticksInUse, modifiers);
		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance) {
		super.spawnParticleRay(world, origin, direction, caster, distance);
		Vec3d endPos = origin.add(direction.scale(distance));
		ParticleBuilder.create(ParticleBuilder.Type.BEAM).scale(getProperty(EFFECT_RADIUS).floatValue() * 5).pos(origin).target(endPos)
				.time(15).clr(174, 252, 255).fade(230, 253, 254).collide(true).spawn(world);
		ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), 180, distance, getProperty(EFFECT_RADIUS).floatValue(),
				ParticleBuilder.Type.SNOW, origin, new Vec3d(world.rand.nextGaussian() / 80, world.rand.nextGaussian() / 40, world.rand.nextGaussian() / 80),
				12, -1, -1, -1);

	}



	@Override
	public void playSound(World world, EntityLivingBase caster) {
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_SHARD_SMASH, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_CHARGE_ICE, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_GIANT_ATTACK, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
	}
}
