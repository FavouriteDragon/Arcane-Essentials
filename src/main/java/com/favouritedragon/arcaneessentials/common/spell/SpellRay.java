package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.SpellProperties;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell.SWORDS;

public abstract class SpellRay extends electroblob.wizardry.spell.SpellRay implements IArcaneSpell {

	public SpellRay(String name, boolean isContinuous, EnumAction action) {
		super(name, isContinuous, action);
		addProperties(EFFECT_RADIUS, SWORDS);
	}

	public SpellRay(String modID, String name, boolean isContinuous, EnumAction action) {
		super(modID, name, isContinuous, action);
		addProperties(EFFECT_RADIUS, SWORDS);
	}

	@Override
	protected boolean shootSpell(World world, Vec3d origin, Vec3d direction, @Nullable EntityPlayer caster, int ticksInUse, SpellModifiers modifiers) {

		if (isPiercing()) {
			return shootPiercingSpell(world, origin, direction, caster, ticksInUse, modifiers, RayTracer.ignoreEntityFilter(caster));
		} else {
			double range = getRange(world, origin, direction, caster, ticksInUse, modifiers);
			Vec3d endpoint = origin.add(direction.scale(range));
			Predicate<Entity> ignore = RayTracer.ignoreEntityFilter(caster);

			// Change the filter depending on whether living entities are ignored or not
			RayTraceResult rayTrace = RayTracer.rayTrace(world, origin, endpoint, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY), hitLiquids,
					ignoreUncollidables, false, Entity.class, ignoreLivingEntities ? WizardryUtilities::isLiving
							: ignore);

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
			if (caster != null)
				playSound(world, caster);

			return true;
		}
	}

	protected boolean shootPiercingSpell(World world, Vec3d origin, Vec3d direction, @Nullable EntityPlayer caster, int ticksInUse, SpellModifiers modifiers, Predicate<Entity> filter) {
		double range = getRange(world, origin, direction, caster, ticksInUse, modifiers);
		Vec3d endpoint = origin.add(direction.scale(range));

		// Change the filter depending on whether living entities are ignored or not
		RayTraceResult rayTrace = RayTracer.rayTrace(world, origin, endpoint, getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY), hitLiquids,
				ignoreUncollidables, false, Entity.class, ignoreLivingEntities ? WizardryUtilities::isLiving
						: filter);

		boolean flag = false;

		//TODO: Piercing
		if (rayTrace != null) {
			// Doesn't matter which way round these are, they're mutually exclusive
			if (rayTrace.typeOfHit == RayTraceResult.Type.ENTITY) {
				// Do whatever the spell does when it hits an entity
				flag = onEntityHit(world, rayTrace.entityHit, rayTrace.hitVec, caster, origin, ticksInUse, modifiers);
				// If the spell succeeded, clip the particles to the correct distance so they don't go through the entity
				if (flag) range = origin.distanceTo(rayTrace.hitVec);
				filter = filter.or(e -> e == rayTrace.entityHit);
				shootPiercingSpell(world, origin, direction, caster, ticksInUse, modifiers, filter);


			} else if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
				// Do whatever the spell does when it hits an block
				flag = onBlockHit(world, rayTrace.getBlockPos(), rayTrace.sideHit, rayTrace.hitVec, caster, origin, ticksInUse, modifiers);
				// Clip the particles to the correct distance so they don't go through the block
				// Unlike with entities, this is done regardless of whether the spell succeeded, since no spells go
				// through blocks (and in fact, even the ray tracer itself doesn't do that)
				range = origin.distanceTo(rayTrace.hitVec);
				// Particle spawning
				if (world.isRemote) {
					spawnParticleRay(world, origin, direction, caster, range);
				}
				if (caster != null)
					playSound(world, caster);
				return true;
			}
		}

		// If flag is false, either the spell missed or the relevant entity/block hit method returned false
		if (!flag && !onMiss(world, caster, origin, direction, ticksInUse, modifiers)) return false;

		// Particle spawning
		if (world.isRemote) {
			spawnParticleRay(world, origin, direction, caster, range);
		}

		if (caster != null)
			playSound(world, caster);

		return true;
	}


	public boolean isPiercing() {
		return false;
	}

	public abstract void playSound(World world, EntityLivingBase caster);

	@Override
	protected SoundEvent[] createSounds() {
		return new SoundEvent[]{ArcaneUtils.createSound("spell." + this.getRegistryName().getPath())};
	}

	@Override
	public boolean isSwordCastable() {
		return getProperty(SWORDS).equals(true);
	}

	@Override
	public boolean isWandCastable() {
		return isEnabled(SpellProperties.Context.WANDS);
	}
}
