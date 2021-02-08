package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell.SWORDS;

public abstract class SpellRay extends electroblob.wizardry.spell.SpellRay implements IArcaneSpell {

    protected Predicate<Entity> targetSelector;

    public SpellRay(String name, EnumAction action, boolean isContinuous) {
        super(ArcaneEssentials.MODID, name, action, isContinuous);
        addProperties(EFFECT_RADIUS);
        this.targetSelector = Entity::canBeAttackedWithItem;
    }


    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
        this.targetSelector = targetSelector.and(entity -> !(entity == caster));
        playSound(world, caster);
        return super.cast(world, caster, hand, ticksInUse, modifiers);
    }

    @Override
    public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
        this.targetSelector = targetSelector.and(entity -> !(entity == caster));
        playSound(world, caster);
        return super.cast(world, caster, hand, ticksInUse, target, modifiers);
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
        return getProperty(SWORDS).floatValue() == 1;
    }

    @Override
    public boolean isWandCastable() {
        return isEnabled(SpellProperties.Context.WANDS);
    }

    //Copied from the super method; have to adjust to account for piercing
    @Override
    protected boolean shootSpell(World world, Vec3d origin, Vec3d direction, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
        double range = getRange(world, origin, direction, caster, ticksInUse, modifiers);
        Vec3d endpoint = origin.add(direction.scale(range));

        // Filter is negated because of weird raytrace shenanigans
        RayTraceResult rayTrace = RayTracer.rayTrace(world, origin, endpoint, aimAssist, hitLiquids,
                ignoreUncollidables, false, Entity.class, targetSelector.negate());

        boolean flag = false;

        if (rayTrace != null) {
            // Doesn't matter which way round these are, they're mutually exclusive
            if (rayTrace.typeOfHit == RayTraceResult.Type.ENTITY) {
                // Do whatever the spell does when it hits an entity
                // FIXME: Some spells (e.g. lightning web) seem to not render when aimed at item frames
                targetSelector = targetSelector.and(entity -> AllyDesignationSystem.isValidTarget(caster, entity));
                if (targetSelector.test(rayTrace.entityHit)) {
                    flag = onEntityHit(world, rayTrace.entityHit, rayTrace.hitVec, caster, origin, ticksInUse, modifiers);
                    // If the spell succeeded, clip the particles to the correct distance so they don't go through the entity
                    if (flag) range = origin.distanceTo(rayTrace.hitVec);
                    targetSelector = targetSelector.and(entity -> entity != rayTrace.entityHit);
                }

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

        //For piercing spells
        else if (!flag && onMiss(world, caster, origin, direction, ticksInUse, modifiers) && isPiercing())
            shootSpell(world, origin, direction, caster, ticksInUse, modifiers);


        // Particle spawning
        if (world.isRemote) {
            spawnParticleRay(world, origin, direction, caster, range);
        }

        return true;
    }
}
