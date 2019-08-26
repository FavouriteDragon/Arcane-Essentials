package com.favouritedragon.arcaneessentials.common.spell.necromancy;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneSpellProperties.LIFE_STEAL;

public class Zam extends SpellRay {

	public Zam() {
		super(ArcaneEssentials.MODID, "zam", false, EnumAction.BOW);
		addProperties(DAMAGE, LIFE_STEAL, EFFECT_RADIUS, EFFECT_STRENGTH);
	}

	@Override
	protected boolean shootSpell(World world, Vec3d origin, Vec3d direction, @Nullable EntityPlayer caster, int ticksInUse, SpellModifiers modifiers) {

		double range = getRange(world, origin, direction, caster, ticksInUse, modifiers);
		Vec3d endpoint = origin.add(direction.scale(range));

		// Change the filter depending on whether living entities are ignored or not
		RayTraceResult rayTrace = RayTracer.rayTrace(world, origin, endpoint, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY), hitLiquids,
				ignoreUncollidables, false, Entity.class, ignoreLivingEntities ? WizardryUtilities::isLiving
						: RayTracer.ignoreEntityFilter(caster));

		boolean flag = false;

		if (rayTrace != null) {
			// Doesn't matter which way round these are, they're mutually exclusive
			if (rayTrace.typeOfHit == RayTraceResult.Type.ENTITY) {
				// Do whatever the spell does when it hits an entity
				flag = onEntityHit(world, rayTrace.entityHit, rayTrace.hitVec, caster, origin, ticksInUse, modifiers);
				// If the spell succeeded, clip the particles to the correct distance so they don't go through the entity
				if (flag) range = origin.distanceTo(rayTrace.hitVec);

			} else if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
				// Do whatever the spell does when it hits an block
				flag = onBlockHit(world, rayTrace.getBlockPos(), rayTrace.sideHit, rayTrace.hitVec, caster, origin, ticksInUse, modifiers);
				// Clip the particles to the correct distance so they don't go through the block
				// Unlike with entities, this is done regardless of whether the spell succeeded, since no spells go
				// through blocks (and in fact, even the ray tracer itself doesn't do that)
				range = origin.distanceTo(rayTrace.hitVec);
			}
		}

		// If flag is false, either the spell missed or the relevant entity/block hit method returned false
		if (!flag && !onMiss(world, caster, origin, direction, ticksInUse, modifiers)) return false;

		// Particle spawning
		if (world.isRemote) {
			spawnParticleRay(world, origin, direction, caster, range);
		}

		return true;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			if (target instanceof EntityLivingBase || target.canBeCollidedWith() && target.canBePushed()) {
				if (!world.isRemote) {
					if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.WITHER, target)) {
						float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
						if (target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.WITHER), damage)) {
							caster.heal(getProperty(LIFE_STEAL).floatValue() * damage * modifiers.get(SpellModifiers.POTENCY));
							Vec3d vel = hit.subtract(origin).scale(getProperty(EFFECT_STRENGTH).doubleValue()).add(0, 0.1F, 0);
							target.addVelocity(vel.x, vel.y, vel.z);
							return true;
						}

					}
				}
				if (world.isRemote) {
					ParticleBuilder.create(ParticleBuilder.Type.BUFF).entity(target).time(10).clr(33, 0, 71).spawn(world);
				}
			}
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance) {
		super.spawnParticleRay(world, origin, direction, caster, distance);
		ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING).entity(caster).pos(origin).target(origin.add(direction.scale(distance)))
				.clr(33, 0, 71).scale(getProperty(EFFECT_RADIUS).floatValue()).time(7).spawn(world);
		for (int i = 0; i < 15; i++)
			ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).pos(origin.add(direction.scale(distance))).clr(33, 0, 71)
					.scale(getProperty(EFFECT_RADIUS).floatValue()).time(14).spawn(world);
	}
}
