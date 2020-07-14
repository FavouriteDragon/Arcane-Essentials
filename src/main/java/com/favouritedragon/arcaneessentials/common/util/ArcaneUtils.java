package com.favouritedragon.arcaneessentials.common.util;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ArcaneUtils {
    //NOTE: ONLY USE ENUMPARTICLETYPE SPAWN METHODS IN RENDERING FILES. DUE TO VANILLA'S WEIRD PARTICLE SPAWNING SYSTEM,
    //YOU CANNOT SPAWN PARTICLES IN ENTITY CLASSES AND SUCH RELIABLY. CUSTOM PARTICLES IN THIS CASE ARE FINE, THOUGH.

    //TODO: Fix most particle spawning methods
    public static Vec3d rotateAroundAxisX(Vec3d v, double angle) {
        angle = Math.toRadians(angle);
        double y, z, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        y = v.y * cos - v.z * sin;
        z = v.y * sin + v.z * cos;
        return new Vec3d(v.x, y, z);
    }

    public static Vec3d rotateAroundAxisY(Vec3d v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        double x, z, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        x = v.x * cos + v.z * sin;
        z = v.x * -sin + v.z * cos;
        return new Vec3d(x, v.y, z);
    }

    public static Vec3d rotateAroundAxisZ(Vec3d v, double angle) {
        angle = Math.toRadians(angle);
        double x, y, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        x = v.x * cos - v.y * sin;
        y = v.x * sin + v.y * cos;
        return new Vec3d(x, y, v.z);
    }

    //Returns the exact middle of the entity.
    public static Vec3d getMiddleOfEntity(Entity entity) {
        double x = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
        double y = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY;
        double z = entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ;

        return new Vec3d(x / 2 + entity.getEntityBoundingBox().minX, y / 2 + entity.getEntityBoundingBox().minY,
                z / 2 + entity.getEntityBoundingBox().minZ);
    }

    //Returns the exact pos, with the y value being the minY and the x and z being the centre of it.
    public static Vec3d getEntityPos(Entity entity) {
        double x = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
        double z = entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ;

        return new Vec3d(x / 2 + entity.getEntityBoundingBox().minX, entity.getEntityBoundingBox().minY,
                z / 2 + entity.getEntityBoundingBox().minZ);
    }

    //NOTE: ONLY USE ENUMPARTICLETYPE SPAWN METHODS IN RENDERING FILES. DUE TO VANILLA'S WEIRD PARTICLE SPAWNING SYSTEM,
    //YOU CANNOT SPAWN PARTICLES IN ENTITY CLASSES AND SUCH RELIABLY. CUSTOM PARTICLES IN THIS CASE ARE FINE, THOUGH.

    //Spawns a directional sword beam.

    /**
     * @param world              The world the blade will be spawned in.
     * @param entity             The entity that will be spawning the blade beam.
     * @param direction          The direction vector that will offset how it's spawned.
     * @param particleController How many particles to spawn per location. Keeping it lower spawns more particles, while higher spawns less.
     * @param bladeLength        How long the blade is horizontally.
     * @param entityLifetime     How long the entity spawning it has been alive. Good for getting finer angle increments.
     * @param type               The particle type.
     * @param pos                The normal position.
     * @param vel                The velocity of the particles.
     * @param rgb                The array of floats to use for r, g, b, in that order.
     * @param size               The size of the particle. Also used for height.
     * @param lifetime           The lifetime of the particles.
     */
    public static void spawnDirectionalHorizontalBlade(World world, Entity entity, @Nullable Vec3d direction, double particleController, double bladeLength, int entityLifetime,
                                                       ResourceLocation type, Vec3d pos, Vec3d vel, float[] rgb, float size, int lifetime) {
        if (!world.isRemote) return;

        //TODO: Rotate the axis so it actually works.
        direction = direction == null ? Vec3d.ZERO : direction;
        int amount = (int) (30 * Math.round(bladeLength) + entityLifetime);
        for (double i = 0; i < amount; i += particleController) {
            Vec3d spawnPos = ArcaneUtils.toRectangular(Math.toRadians(entity.rotationPitch - amount / 2F + i), 0);
            if (rgb[0] == -1 && rgb[1] == -1 && rgb[2] == -1)
                ParticleBuilder.create(type).time(lifetime).vel(vel).pos(spawnPos.add(pos.add(direction))).scale(size).collide(true).spawn(world);
            else
                ParticleBuilder.create(type).time(lifetime).vel(vel).pos(spawnPos.add(pos.add(direction))).scale(size).clr(rgb[0], rgb[1], rgb[2]).collide(true).spawn(world);
        }
    }

    public static void spawnDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int particleAmount, double vortexLength, double minRadius, double radiusScale, EnumParticleTypes particle, double posX, double posY, double posZ,
                                              double velX, double velY, double velZ) {
        for (int angle = 0; angle < particleAmount; angle++) {
            double radius = minRadius + (angle / radiusScale);
            double x = radius * cos(angle);
            double y = angle / (particleAmount / vortexLength);
            double z = radius * sin(angle);
            Vec3d pos = new Vec3d(x, y, z);
            if (entity != null && direction != null) {
                pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
                pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
                world.spawnParticle(particle, true, pos.x + posX + direction.x, pos.y + posY + direction.y,
                        pos.z + posZ + direction.z, velX, velY, velZ);
            } else {
                world.spawnParticle(particle, false, x + posX, y + posY,
                        z + posZ, velX, velY, velZ);
            }
        }
    }

    //TODO: Reduce velocity and position into a vec3d
    public static void spawnDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double minRadius, double radiusScale, ResourceLocation particle, double posX, double posY, double posZ,
                                              double velX, double velY, double velZ, int maxAge, float r, float g, float b) {
        for (int angle = 0; angle < maxAngle; angle++) {
            double radius = minRadius + (angle / radiusScale);
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(angle);
            Vec3d pos = new Vec3d(x, y, z);
            if (entity != null && direction != null) {
                pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
                pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
                //Wizardry.proxy.spawnParticle(particle, world, pos.x + posX + direction.x, pos.y + posY + direction.y,
                //		pos.z + posZ + direction.z, velX, velY, velZ, maxAge, r, g, b);
            } else {
                //	Wizardry.proxy.spawnParticle(particle, world, x + posX, y + posY,
                //			z + posZ, velX, velY, velZ, maxAge, r, g, b);
            }
        }
    }


    /**
     * Spawns a directional vortex that has rotating particles.
     *
     * @param world         World the vortex spawns in.
     * @param entity        Entity that's spawning the vortex.
     * @param direction     The direction that the vortex is spawning in. Although it's just used for proper positioning, use entity.getLookVec()
     *                      or some other directional Vec3d.
     * @param maxAngle      The amount of particles/the maximum angle that the circle ticks to. 240 would mean there are 240 particles spiraling away.
     * @param vortexLength  How long the vortex is. This is initially used at the height, before rotating the vortex.
     * @param radiusScale   The maximum radius and how much the radius increases by. Always use your value for the maxAngle here-
     *                      otherwise you can get some funky effects. Ex: maxAngle/1.5 would give you a max radius of 1.5 blocks.
     *                      Note: It might only be a diamater of 1.5 blocks- if so, uhhh... My bad.
     * @param particle      The wizardry particle type. I had to create two methods- for for normal particles, one for wizardry ones.
     * @param position      The starting/reference position of the vortex. Used along with the direction position to determine the actual starting position.
     * @param particleSpeed How fast the particles are spinning. You don't need to include complex maths here- that's all handled by this method.
     * @param entitySpeed   The speed of the entity that is rendering these particles. If the player/entity spawns a tornado, this is the speed of the tornado. If there's no entity,
     *                      do Vec3d.ZERO.
     * @param maxAge        The maximum age of the particle. Wizardry particles already have a predetermined age, this just adds onto it.
     * @param r             The amount of red in the particle. G and B are self-explanatory (green and blue).
     */
    public static void spawnSpinningDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double minRadius, double radiusScale, ResourceLocation particle, Vec3d position,
                                                      Vec3d particleSpeed, Vec3d entitySpeed, int maxAge, float r, float g, float b) {
        if (!world.isRemote) return;

        for (int angle = 0; angle < maxAngle; angle++) {
            double newAngle = Math.toRadians(angle);
            double angle2 = world.rand.nextDouble() * Math.PI * 2;
            double radius = minRadius + (newAngle / radiusScale);
            double x = radius * cos(newAngle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(newAngle);
            double speed = world.rand.nextDouble() * 2 + 1;
            double omega = Math.signum(speed * ((Math.PI * 2) / 20 - speed / (20 * radius)));
            angle2 += omega;
            Vec3d pos = new Vec3d(x, y, z);
            if (entity != null && direction != null) {
                Vec3d pVel = new Vec3d(particleSpeed.x * radius * omega * Math.cos(angle2), particleSpeed.y, particleSpeed.z * radius * omega * Math.sin(angle2));
                pVel = ArcaneUtils.rotateAroundAxisX(pVel, entity.rotationPitch - 90);
                pVel = ArcaneUtils.rotateAroundAxisY(pVel, entity.rotationYaw);
                pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
                pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
                if (r == -1 && g == -1 && b == -1) {
                    ParticleBuilder.create(particle).pos(pos.add(position).add(direction)).vel(pVel.add(entitySpeed)).time(maxAge).spawn(world);
                } else {
                    ParticleBuilder.create(particle).pos(pos.add(position).add(direction)).
                            clr(r, g, b).vel(pVel.add(entitySpeed)).time(maxAge).spawn(world);
                }
            }
        }
    }

    /**
     * Spawns a directional vortex that has rotating particles.
     *
     * @param world         World the vortex spawns in.
     * @param entity        Entity that's spawning the vortex.
     * @param direction     The direction that the vortex is spawning in. Although it's just used for proper positioning, use entity.getLookVec()
     *                      or some other directional Vec3d.
     * @param maxAngle      The amount of particles/the maximum angle that the circle ticks to. 240 would mean there are 240 particles spiraling away.
     * @param vortexLength  How long the vortex is. This is initially used at the height, before rotating the vortex.
     * @param radiusScale   The maximum radius and how much the radius increases by. Always use your value for the maxAngle here-
     *                      otherwise you can get some funky effects. Ex: maxAngle/1.5 would give you a max radius of 1.5 blocks.
     *                      Note: It might only be a diamater of 1.5 blocks- if so, uhhh... My bad.
     * @param particle      The wizardry particle type. I had to create two methods- for for normal particles, one for wizardry ones.
     * @param position      The starting/reference position of the vortex. Used along with the direction position to determine the actual starting position.
     * @param particleSpeed How fast the particles are spinning. You don't need to include complex maths here- that's all handled by this method.
     * @param entitySpeed   The speed of the entity that is rendering these particles. If the player/entity spawns a tornado, this is the speed of the tornado. If there's no entity,
     *                      do Vec3d.ZERO.
     * @param maxAge        The maximum age of the particle. Wizardry particles already have a predetermined age, this just adds onto it.
     * @param r             The amount of red in the particle. G and B are self-explanatory (green and blue).
     */
    public static void spawnSpinningDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double minRadius, double radiusScale, ResourceLocation particle, Vec3d position,
                                                      Vec3d particleSpeed, Vec3d entitySpeed, int maxAge, float r, float g, float b, float scale) {
        if (!world.isRemote) return;

        for (int angle = 0; angle < maxAngle; angle++) {
            double angle2 = world.rand.nextDouble() * Math.PI * 2;
            double radius = minRadius + (angle / radiusScale);
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(angle);
            double speed = world.rand.nextDouble() * 2 + 1;
            double omega = Math.signum(speed * ((Math.PI * 2) / 20 - speed / (20 * radius)));
            angle2 += omega;
            Vec3d pos = new Vec3d(x, y, z);
            if (entity != null && direction != null) {
                Vec3d pVel = new Vec3d(particleSpeed.x * radius * omega * Math.cos(angle2), particleSpeed.y, particleSpeed.z * radius * omega * Math.sin(angle2));
                pVel = ArcaneUtils.rotateAroundAxisX(pVel, entity.rotationPitch - 90);
                pVel = ArcaneUtils.rotateAroundAxisY(pVel, entity.rotationYaw);
                pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
                pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
                if (r == -1 && g == -1 && b == -1) {
                    ParticleBuilder.create(particle).pos(pos.add(position).add(direction)).vel(pVel.add(entitySpeed)).time(maxAge).collide(true).scale(scale).spawn(world);
                } else {
                    ParticleBuilder.create(particle).pos(pos.add(position).add(direction)).
                            clr(r, g, b).vel(pVel.add(entitySpeed)).time(maxAge).scale(scale).collide(true).spawn(world);
                }
            }
        }
    }

    /**
     * Spawns a directional vortex that has rotating particles.
     *
     * @param world         World the vortex spawns in.
     * @param entity        Entity that's spawning the vortex.
     * @param direction     The direction that the vortex is spawning in. Although it's just used for proper positioning, use entity.getLookVec()
     *                      or some other directional Vec3d.
     * @param maxAngle      The amount of particles/the maximum angle that the circle ticks to. 240 would mean there are 240 particles spiraling away.
     * @param vortexLength  How long the vortex is. This is initially used at the height, before rotating the vortex.
     * @param radiusScale   The maximum radius and how much the radius increases by. Always use your value for the maxAngle here-
     *                      otherwise you can get some funky effects. Ex: maxAngle/1.5 would give you a max radius of 1.5 blocks.
     *                      Note: It might only be a diamater of 1.5 blocks- if so, uhhh... My bad.
     * @param particle      The wizardry particle type. I had to create two methods- for for normal particles, one for wizardry ones.
     * @param position      The starting/reference position of the vortex. Used along with the direction position to determine the actual starting position.
     * @param particleSpeed How fast the particles are spinning. You don't need to include complex maths here- that's all handled by this method.
     * @param entitySpeed   The speed of the entity that is rendering these particles. If the player/entity spawns a tornado, this is the speed of the tornado. If there's no entity,
     *                      do Vec3d.ZERO.
     */
    public static void spawnSpinningDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double minRadius, double radiusScale, EnumParticleTypes particle, Vec3d position,
                                                      Vec3d particleSpeed, Vec3d entitySpeed) {
        for (int angle = 0; angle < maxAngle; angle++) {
            double angle2 = world.rand.nextDouble() * Math.PI * 2;
            double radius = minRadius + (angle / radiusScale);
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(angle);
            double speed = world.rand.nextDouble() * 2 + 1;
            double omega = Math.signum(speed * ((Math.PI * 2) / 20 - speed / (20 * radius)));
            angle2 += omega;
            Vec3d pos = new Vec3d(x, y, z);
            if (entity != null && direction != null) {
                Vec3d pVel = new Vec3d(particleSpeed.x * radius * omega * Math.cos(angle2), particleSpeed.y, particleSpeed.z * radius * omega * Math.sin(angle2));
                pVel = ArcaneUtils.rotateAroundAxisX(pVel, entity.rotationPitch - 90);
                pVel = ArcaneUtils.rotateAroundAxisY(pVel, entity.rotationYaw);
                pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
                pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
                world.spawnParticle(particle, true, pos.x + position.x + direction.x, pos.y + position.y + direction.y,
                        pos.z + position.z + direction.z, pVel.x + entitySpeed.x, pVel.y + entitySpeed.y, pVel.z + entitySpeed.z);
            }
        }
    }

    /**
     * Spawns a directional vortex that has rotating particles.
     *
     * @param world         World the vortex spawns in.
     * @param maxAngle      The amount of particles/the maximum angle that the circle ticks to. 240 would mean there are 240 particles spiraling away.
     * @param vortexHeight  How tall the vortex is.
     * @param radiusScale   The maximum radius and how much the radius increases by. Always use your value for the maxAngle here-
     *                      otherwise you can get some funky effects. Ex: maxAngle/1.5 would give you a max radius of 1.5 blocks.
     *                      Note: It might only be a diamater of 1.5 blocks- if so, uhhh... My bad.
     * @param particle      The wizardry particle type. I had to create two methods- for for normal particles, one for wizardry ones.
     * @param position      The starting/reference position of the vortex. Used along with the direction position to determine the actual starting position.
     * @param particleSpeed How fast the particles are spinning. You don't need to include complex maths here- that's all handled by this method.
     * @param entitySpeed   The speed of the entity that is spawning the particles. If this is used for a quickburst, just make this 0. This is so
     *                      particles move with the entity that's directly spawning it.
     * @param maxAge        The maximum age of the particle. Wizardry particles already have a predetermined age, this just adds onto it.
     * @param r             The amount of red in the particle. G and B are self-explanatory (green and blue).
     */
    public static void spawnSpinningVortex(World world, int maxAngle, double vortexHeight, double minRadius, double radiusScale, ResourceLocation particle, Vec3d position,
                                           Vec3d particleSpeed, Vec3d entitySpeed, int maxAge, float r, float g, float b) {
        if (!world.isRemote) return;

        Vec3d prevpos = position;
        for (int angle = 0; angle < maxAngle; angle++) {
            double radius = minRadius + (angle / radiusScale);
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexHeight);
            double z = radius * sin(angle);
            Vec3d pos = new Vec3d(position.x + x, position.y + y, position.z + z);
            if (particle.equals(ParticleBuilder.Type.LIGHTNING)) {
                ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING).vel(entitySpeed).spin(radius, ArcaneUtils.getMagnitude(particleSpeed))
                        .time(maxAge).clr(r, g, b).target(prevpos).pos(pos).spawn(world);
                prevpos = pos;
            } else {
                ParticleBuilder.create(particle).vel(entitySpeed).spin(radius, ArcaneUtils.getMagnitude(particleSpeed))
                        .time(maxAge).clr(r, g, b).pos(pos).spawn(world);
            }
        }
    }


    /**
     * Spawns a directional vortex that has rotating particles.
     *
     * @param world         World the vortex spawns in.
     * @param maxAngle      The amount of particles/the maximum angle that the circle ticks to. 240 would mean there are 240 particles spiraling away.
     * @param vortexHeight  How tall the vortex is.
     * @param radiusScale   The maximum radius and how much the radius increases by. Always use your value for the maxAngle here-
     *                      otherwise you can get some funky effects. Ex: maxAngle/1.5 would give you a max radius of 1.5 blocks.
     *                      Note: It might only be a diamater of 1.5 blocks- if so, uhhh... My bad.
     * @param particle      The wizardry particle type. I had to create two methods- for for normal particles, one for wizardry ones.
     * @param position      The starting/reference position of the vortex. Used along with the direction position to determine the actual starting position.
     * @param particleSpeed How fast the particles are spinning. You don't need to include complex maths here- that's all handled by this method.
     * @param entitySpeed   The speed of the entity that is spawning the particles. If this is used for a quickburst, just make this 0. This is so
     *                      particles move with the entity that's directly spawning it.
     * @param maxAge        The maximum age of the particle. Wizardry particles already have a predetermined age, this just adds onto it.
     * @param r             The amount of red in the particle. G and B are self-explanatory (green and blue).
     */
    public static void spawnSpinningVortex(World world, int maxAngle, double vortexHeight, double minRadius, double radiusScale, ResourceLocation particle, Vec3d position,
                                           Vec3d particleSpeed, Vec3d entitySpeed, int maxAge, float r, float g, float b, float scale) {
        if (!world.isRemote) return;

        Vec3d prevpos = position;
        for (int angle = 0; angle < maxAngle; angle++) {
            double radius = minRadius + (angle / radiusScale);
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexHeight);
            double z = radius * sin(angle);
            Vec3d pos = new Vec3d(position.x + x, position.y + y, position.z + z);
            if (particle.equals(ParticleBuilder.Type.LIGHTNING)) {
                ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING).vel(entitySpeed).spin(radius, ArcaneUtils.getMagnitude(particleSpeed))
                        .time(maxAge).clr(r, g, b).target(prevpos).pos(pos).scale(scale).spawn(world);
                prevpos = pos;
            } else {
                ParticleBuilder.create(particle).vel(entitySpeed).spin(radius, ArcaneUtils.getMagnitude(particleSpeed))
                        .time(maxAge).clr(r, g, b).pos(pos).scale(scale).collide(true).spawn(world);
            }
        }
    }


    /**
     * Spawns a directional vortex that has rotating particles.
     * Same as the one above, except it uses the particle's natural texture/colour.
     *
     * @param world         World the vortex spawns in.
     * @param maxAngle      The amount of particles/the maximum angle that the circle ticks to. 240 would mean there are 240 particles spiraling away.
     * @param vortexHeight  How tall the vortex is.
     * @param radiusScale   The maximum radius and how much the radius increases by. Always use your value for the maxAngle here-
     *                      otherwise you can get some funky effects. Ex: maxAngle/1.5 would give you a max radius of 1.5 blocks.
     *                      Note: It might only be a diamater of 1.5 blocks- if so, uhhh... My bad.
     * @param particle      The wizardry particle type. I had to create two methods- for for normal particles, one for wizardry ones.
     * @param position      The starting/reference position of the vortex. Used along with the direction position to determine the actual starting position.
     * @param particleSpeed How fast the particles are spinning. You don't need to include complex maths here- that's all handled by this method.
     * @param entitySpeed   The speed of the entity that is spawning the particles. If this is used for a quickburst, just make this 0. This is so
     *                      particles move with the entity that's directly spawning it.
     * @param maxAge        The maximum age of the particle. Wizardry particles already have a predetermined age, this just adds onto it.
     */
    public static void spawnSpinningVortex(World world, int maxAngle, double vortexHeight, double minRadius, double radiusScale, ResourceLocation particle, Vec3d position,
                                           double particleSpeed, Vec3d entitySpeed, int maxAge) {
        if (!world.isRemote) return;

        Vec3d prevpos = position;
        for (int angle = 0; angle < maxAngle; angle++) {
            double radius = minRadius + (angle / radiusScale);
            double x = radius * cos(angle);
            double y = vortexHeight * (angle / (float) maxAngle);
            y = MathHelper.clamp(y, 0, vortexHeight);
            double z = radius * sin(angle);
            Vec3d pos = new Vec3d(position.x + x, position.y + y, position.z + z);
            if (particle.equals(ParticleBuilder.Type.LIGHTNING)) {
                ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING).vel(entitySpeed).spin(radius, particleSpeed)
                        .time(maxAge).target(prevpos).pos(pos).spawn(world);
                prevpos = pos;
            } else {
                ParticleBuilder.create(particle).vel(entitySpeed).spin(radius, particleSpeed)
                        .time(maxAge).pos(pos).spawn(world);
            }
        }
    }

    public static void spawnSpinningVortex(World world, int maxAngle, double vortexHeight, double minRadius, double radiusScale, EnumParticleTypes particle, Vec3d position,
                                           Vec3d particleSpeed, Vec3d entitySpeed) {
        for (int angle = 0; angle < maxAngle; angle++) {
            double angle2 = world.rand.nextDouble() * Math.PI * 2;
            double radius = minRadius + (angle / radiusScale);
            double x = radius * cos(angle);
            double y = vortexHeight * (angle / (float) maxAngle);
            double z = radius * sin(angle);
            double speed = world.rand.nextDouble() * 2 + 1;
            double omega = Math.signum(speed * ((Math.PI * 2) / 20 - speed / (20 * radius)));
            angle2 += omega;
            world.spawnParticle(particle, false, x + position.x, y + position.y, z + position.z,
                    (particleSpeed.x * radius * omega * Math.cos(angle2)) + entitySpeed.x, particleSpeed.y + entitySpeed.y, (particleSpeed.z * radius * omega * Math.sin(angle2)) + entitySpeed.z);

        }
    }


    public static void spawnDirectionalHelix(World world, Entity entity, Vec3d direction, int maxAngle, double vortexLength, double radius, EnumParticleTypes particle, Vec3d position,
                                             Vec3d particleSpeed) {
        if (!world.isRemote) return;

        for (int angle = 0; angle < maxAngle; angle++) {
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(angle);
            Vec3d pos = new Vec3d(x, y, z);
            if (entity != null && direction != null) {
                pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
                pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
                world.spawnParticle(particle, true, pos.x + position.x + direction.x, pos.y + position.z + direction.y,
                        pos.z + position.z + direction.z, particleSpeed.z, particleSpeed.y, particleSpeed.z);
            } else {
                world.spawnParticle(particle, false, x + position.x, y + position.y,
                        z + position.z, particleSpeed.z, particleSpeed.y, particleSpeed.z);
            }
        }
    }

    public static void spawnDirectionalHelix(World world, Entity entity, Vec3d direction, int maxAngle, double vortexLength, double radius, ResourceLocation particle, Vec3d position,
                                             Vec3d particleSpeed, int maxAge, float r, float g, float b) {
        if (!world.isRemote) return;

        for (int angle = 0; angle < maxAngle; angle++) {
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(angle);
            Vec3d pos = new Vec3d(x, y, z);
            if (entity != null && direction != null) {
                pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
                pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
                if (r == -1 && g == -1 && b == -1) {
                    ParticleBuilder.create(particle).pos(pos.x + position.x + direction.x, pos.y + position.y + direction.y,
                            pos.z + position.z + direction.z).vel(particleSpeed).time(maxAge).spawn(world);
                } else {
                    ParticleBuilder.create(particle).pos(pos.x + position.x + direction.x, pos.y + position.y + direction.y,
                            pos.z + position.z + direction.z).vel(particleSpeed).time(maxAge).clr(r, g, b).spawn(world);
                }
            } else {
            }
        }
    }

    public static void spawnSpinningDirectionalHelix(World world, Entity entity, Vec3d direction, Vec3d entitySpeed, int maxAngle, double vortexLength, double radius, ResourceLocation particle, Vec3d position,
                                                     Vec3d particleSpeed, int maxAge, float r, float g, float b) {
        if (!world.isRemote) return;

        Vec3d prevPos = position;
        for (int angle = 0; angle < maxAngle; angle++) {
            double angle2 = world.rand.nextDouble() * Math.PI * 2;
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(angle);
            double speed = world.rand.nextDouble() * 2 + 1;
            double omega = Math.signum(speed * ((Math.PI * 2) / 20 - speed / (20 * radius)));
            angle2 += omega;
            Vec3d pos = new Vec3d(x, y, z);
            if (entity != null && direction != null) {
                Vec3d pVel = new Vec3d(particleSpeed.x * radius * omega * Math.cos(angle2), particleSpeed.y, particleSpeed.z * radius * omega * Math.sin(angle2));
                pVel = ArcaneUtils.rotateAroundAxisX(pVel, entity.rotationPitch + 90);
                pVel = ArcaneUtils.rotateAroundAxisY(pVel, entity.rotationYaw);
                pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
                pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
                if (r == -1 && b == -1 && g == -1) {
                    if (particle == ParticleBuilder.Type.LIGHTNING || particle == ParticleBuilder.Type.BEAM || particle == ParticleBuilder.Type.VINE) {
                        ParticleBuilder.create(particle).pos(position.add(pos).add(direction))
                                .vel(pVel.add(entitySpeed)).entity(entity).time(maxAge).target(prevPos).spawn(world);
                        prevPos = position.add(pos).add(direction);
                    } else {
                        ParticleBuilder.create(particle).pos(position.add(pos).add(direction))
                                .vel(pVel.add(entitySpeed)).time(maxAge).spawn(world);
                    }
                } else {
                    if (particle == ParticleBuilder.Type.LIGHTNING || particle == ParticleBuilder.Type.BEAM || particle == ParticleBuilder.Type.VINE) {
                        ParticleBuilder.create(particle).pos(position.add(pos).add(direction))
                                .vel(pVel.add(entitySpeed)).time(maxAge).entity(entity).target(prevPos).clr(r, g, b).spawn(world);
                        prevPos = position.add(pos).add(direction);
                    } else {
                        ParticleBuilder.create(particle).pos(position.add(pos).add(direction))
                                .vel(pVel.add(entitySpeed)).time(maxAge).clr(r, g, b).spawn(world);
                    }
                }
            }
        }
    }

    public static void spawnSpinningHelix(World world, int maxAngle, double vortexLength, double radius, ResourceLocation particle, Vec3d position,
                                          double particleSpeed, Vec3d entitySpeed, int maxAge, float r, float g, float b, float scale) {
        if (!world.isRemote) return;

        for (int angle = 0; angle < maxAngle; angle++) {
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(angle);
            if (r == -1 && g == -1 && b == -1) {
                ParticleBuilder.create(particle).pos(x + position.x, y + position.y, z + position.z).spin(radius, particleSpeed)
                        .vel(entitySpeed.scale(particleSpeed)).time(maxAge).scale(scale).spawn(world);
            } else {
                ParticleBuilder.create(particle).pos(x + position.x, y + position.y, z + position.z).clr(r, g, b).spin(radius, particleSpeed)
                        .vel(entitySpeed.scale(particleSpeed)).time(maxAge).scale(scale).spawn(world);

            }
        }
    }

    public static void spawnSpinningHelix(World world, int maxAngle, double vortexLength, double radius, EnumParticleTypes particle, Vec3d position,
                                          Vec3d particleSpeed, Vec3d entitySpeed) {
        for (int angle = 0; angle < maxAngle; angle++) {
            double angle2 = world.rand.nextDouble() * Math.PI * 2;
            double x = radius * cos(angle);
            double y = angle / (maxAngle / vortexLength);
            double z = radius * sin(angle);
            double speed = world.rand.nextDouble() * 2 + 1;
            double omega = Math.signum(speed * ((Math.PI * 2) / 20 - speed / (20 * radius)));
            angle2 += omega;
            world.spawnParticle(particle, x + position.x, y + position.y,
                    z + position.z, (particleSpeed.x * radius * omega * Math.cos(angle2)) + entitySpeed.x, particleSpeed.y + entitySpeed.y, (particleSpeed.z * radius * omega * Math.sin(angle2)) + entitySpeed.z);
        }
    }

	/*public static void spawnSpinningCircles(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, int maxCircles, double pillarHeight, double radius, EnumParticleTypes particle, Vec3d position,
											Vec3d particleSpeed, Vec3d entitySpeed, boolean isDirectional) {
		for (int circle = 0; circle < maxCircles; circle += pillarHeight/maxCircles) {
			for (int angle = 0; angle < maxAngle; angle++) {
				double angle2 = world.rand.nextDouble() * Math.PI * 2;
				double x = radius * cos(angle);
				double z = radius * sin(angle);
				double speed = world.rand.nextDouble() * 2 + 1;
				double omega = Math.signum(speed * ((Math.PI * 2) / 20 - speed / (20 * radius)));
				angle2 += omega;
				Vec3d pos = new Vec3d(x, circle, z);
				if (entity != null && direction != null && isDirectional) {
					Vec3d pVel = new Vec3d(particleSpeed.x * radius * omega * Math.cos(angle2), particleSpeed.y, particleSpeed.z * radius * omega * Math.sin(angle2));
					pVel = ArcaneUtils.rotateAroundAxisX(pVel, entity.rotationPitch - 90);
					pVel = ArcaneUtils.rotateAroundAxisY(pVel, entity.rotationYaw);
					pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
					pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
					world.spawnParticle(particle, true, pos.x + position.x + direction.x, pos.y + position.y + direction.y,
							pos.z + position.z + direction.z, pVel.x + entitySpeed.x, pVel.y + entitySpeed.y, pVel.z + entitySpeed.z);
				}
				else {
					if (direction != null) {
						Vec3d pVel = new Vec3d(particleSpeed.x * radius * omega * Math.cos(angle2), particleSpeed.y, particleSpeed.z * radius * omega * Math.sin(angle2));
						world.spawnParticle(particle, true, pos.x + position.x + direction.x, pos.y + position.y + direction.y,
								pos.z + position.z + direction.z, pVel.x + entitySpeed.x, pVel.y + entitySpeed.y, pVel.z + entitySpeed.z);
					}
				}
			}
		}

	}**/

    public static void vortexEntityCollision(World world, EntityLivingBase entity, Entity spellEntity, Vec3d startPos, Vec3d endPos, float maxRadius,
                                             float damage, Vec3d knockBack, MagicDamage.DamageType damageSource, boolean directDamage) {
        if (entity != null) {
            AxisAlignedBB hitBox = new AxisAlignedBB(startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z);
            hitBox = hitBox.grow(maxRadius);
            List<Entity> hit = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
            if (!hit.isEmpty()) {
                for (Entity e : hit) {
                    if (e != entity) {
                        if (directDamage) {
                            e.attackEntityFrom(MagicDamage.causeDirectMagicDamage(entity, damageSource), damage);
                            e.motionX += knockBack.x;
                            e.motionY += knockBack.y + 0.2;
                            e.motionZ += knockBack.z;
                            ArcaneUtils.applyPlayerKnockback(e);
                        } else if (spellEntity != null) {
                            e.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(spellEntity, entity, damageSource), damage);
                            e.motionX += knockBack.x;
                            e.motionY += knockBack.y + 0.2;
                            e.motionZ += knockBack.z;
                            ArcaneUtils.applyPlayerKnockback(e);
                        }
                    }
                }
            }

        }
    }

    public static void vortexEntityRaytrace(World world, EntityLivingBase entity, Entity spellEntity, Vec3d startPos, double vortexLength, float maxRadius,
                                            float damage, Vec3d knockBack, MagicDamage.DamageType damageSource, boolean directDamage) {
        if (entity != null) {
            RayTraceResult result = ArcaneUtils.standardEntityRayTrace(world, entity, spellEntity, startPos, vortexLength, false, maxRadius, true, false);
            if (result != null && result.entityHit != null) {
                if (result.entityHit instanceof EntityLivingBase) {
                    EntityLivingBase hit = (EntityLivingBase) result.entityHit;
                    if (hit != entity) {
                        if (directDamage) {
                            hit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(entity, damageSource), damage);
                            hit.motionX += knockBack.x;
                            hit.motionY += knockBack.y + 0.2;
                            hit.motionZ += knockBack.z;
                            ArcaneUtils.applyPlayerKnockback(hit);
                        } else if (spellEntity != null) {
                            hit.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(spellEntity, entity, damageSource), damage);
                            hit.motionX += knockBack.x;
                            hit.motionY += knockBack.y + 0.2;
                            hit.motionZ += knockBack.z;
                            ArcaneUtils.applyPlayerKnockback(hit);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public static RayTraceResult standardEntityRayTrace(World world, Vec3d startPos, Vec3d endPos, Predicate<? super Entity> filter,
                                                        boolean hitLiquids, float borderSize, boolean ignoreUncollidables, boolean returnLastUncollidable) {
        return RayTracer.rayTrace(world, startPos, endPos, borderSize,
                hitLiquids, ignoreUncollidables, returnLastUncollidable, Entity.class, filter);
    }

    @Nullable
    public static RayTraceResult standardEntityRayTrace(World world, EntityLivingBase entity, Entity spellEntity, Vec3d startPos, double range,
                                                        boolean hitLiquids, float borderSize, boolean ignoreUncollidables, boolean returnLastUncollidable) {
        float dx = (float) entity.getLookVec().x * (float) range;
        float dy = (float) entity.getLookVec().y * (float) range;
        float dz = (float) entity.getLookVec().z * (float) range;
        return RayTracer.rayTrace(world, startPos,
                new Vec3d(startPos.x + dx, startPos.y + dy, startPos.z + dz), borderSize,
                hitLiquids, ignoreUncollidables, returnLastUncollidable, Entity.class, entity1 -> entity1 == entity || entity1 == spellEntity);
    }

    public static void applyPlayerKnockback(Entity target) {
        if (target instanceof EntityPlayerMP) {
            ((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
        }
    }

    public static Vec3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static Vec3d toRectangular(double yaw, double pitch) {
        return new Vec3d(-sin(yaw) * cos(pitch), -sin(pitch), cos(yaw) * cos(pitch));
    }

    public static Vec3d getDirectionalVortexEndPos(EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double radiusScale, double posX, double posY, double posZ) {
        double radius = maxAngle / radiusScale;
        double x = radius * cos(maxAngle);
        double z = radius * sin(maxAngle);
        Vec3d pos = new Vec3d(x, vortexLength, z);
        if (entity != null && direction != null) {
            pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
            pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
            return new Vec3d(pos.x + posX + direction.x, pos.y + posY + direction.y,
                    pos.z + posZ + direction.z);
        } else {
            return new Vec3d(x + posX, vortexLength + posY, z + posZ);
        }
    }

    /**
     * @param world        The world the raytrace is in.
     * @param caster       The caster of the spell. This is so mobs don't attack each other when you use raytraces from mobs.
     *                     All damage is done by the caster.
     * @param startPos     Where the raytrace starts.
     * @param endPos       Where the raytrace ends.
     * @param borderSize   The width of the raytrace.
     * @param spellEntity  The entity that's using this method, if applicable. If this method is directly used in a spell, just make this null.
     * @param directDamage If the damage caused should be direct or not. Set to true if this method is directly used by the caster in a spell.
     * @param damageType   The damagetype of the damage.
     * @param damage       The amount of damage.
     * @param knockBack    The amount of knockback.
     * @param fireTime     How long to set an enemy on fire.
     * @param lifeSteal    The percent of damage to heal the caster by. Between 0 and 1.
     */

    public static void handlePiercingBeamCollision(World world, EntityLivingBase caster, Vec3d startPos, Vec3d endPos, float borderSize, Entity spellEntity, boolean directDamage, MagicDamage.DamageType damageType,
                                                   float damage, Vec3d knockBack, boolean invulnerable, int fireTime, float radius, float lifeSteal,
                                                   Predicate<? super Entity> filter) {
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
                if (!(hit instanceof EntityPlayer && ((EntityPlayer) hit).isCreative()) && hit.getIsInvulnerable())
                    hit.setEntityInvulnerable(false);
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
                            filter = filter.or(e -> e == secondHit);
                        }
                    }
                    if (secondHit.getTeam() == caster.getTeam()) {
                        if (damageType == MagicDamage.DamageType.RADIANT) {
                            if (secondHit instanceof EntityLivingBase) {
                                ((EntityLivingBase) secondHit).heal(damage);
                            }
                        }
                    }
                }
            } else {
                handlePiercingBeamCollision(world, caster, pos, endPos, borderSize, spellEntity, directDamage,
                        damageType, damage, knockBack, invulnerable, fireTime, radius, lifeSteal, filter);

            }

        }
    }

    /**
     * This method is like the other method, it just uses vanilla damage sources.
     *
     * @param world        The world the raytrace is in.
     * @param caster       The caster of the spell. This is so mobs don't attack each other when you use raytraces from mobs.
     *                     All damage is done by the caster.
     * @param startPos     Where the raytrace starts.
     * @param endPos       Where the raytrace ends.
     * @param borderSize   The width of the raytrace.
     * @param spellEntity  The entity that's using this method, if applicable. If this method is directly used in a spell, just make this null.
     * @param damageSource The damage source.
     * @param damage       The amount of damage.
     * @param knockBack    The amount of knockback.
     * @param fireTime     How long to set an enemy on fire.
     */

    public static void handlePiercingBeamCollision(World world, EntityLivingBase caster, Vec3d startPos, Vec3d endPos, float borderSize, Entity spellEntity, DamageSource damageSource,
                                                   float damage, Vec3d knockBack, boolean invulnerable, int fireTime, float radius, float lifeSteal,
                                                   Predicate<? super Entity> filter) {
        filter = filter.or(entity1 -> entity1 == caster || entity1 == spellEntity);
        RayTraceResult result = standardEntityRayTrace(world, startPos, endPos, filter, false, borderSize, true, false);
        if (result != null && result.entityHit instanceof EntityLivingBase && result.entityHit != caster && result.entityHit != spellEntity) {
            EntityLivingBase hit = (EntityLivingBase) result.entityHit;
            hit.setFire(fireTime);
            caster.heal(damage * lifeSteal);
            hit.attackEntityFrom(damageSource, damage);
            filter = filter.or(entity1 -> entity1 == hit);
            Vec3d kM = endPos.subtract(startPos).scale(.01);
            hit.motionX += knockBack.x * kM.x;
            hit.motionY += knockBack.y * kM.y;
            hit.motionZ += knockBack.z * kM.z;
            hit.setEntityInvulnerable(invulnerable);
            applyPlayerKnockback(hit);

            Vec3d pos = result.hitVec;
            AxisAlignedBB hitBox = new AxisAlignedBB(pos.x + radius, pos.y + radius, pos.z + radius, pos.x - radius, pos.y - radius, pos.z - radius);
            List<Entity> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
            nearby.removeIf(filter);
            //This is so it doesn't count the entity that was hit by the raytrace and mess up the chain
            if (!nearby.isEmpty()) {
                for (Entity e : nearby) {
                    if (e != caster && e != hit && e.getTeam() != caster.getTeam()) {
                        e.setFire(fireTime);
                        e.attackEntityFrom(damageSource, damage);
                        e.motionX += knockBack.x;
                        e.motionY += knockBack.y;
                        e.motionZ += knockBack.z;
                        applyPlayerKnockback(e);
                        filter = filter.or(entity1 -> entity1 == e);
                    }
                }
            } else {
                handlePiercingBeamCollision(world, caster, pos, endPos, borderSize, spellEntity,
                        damageSource, damage, knockBack, invulnerable, fireTime, radius, lifeSteal, filter);

            }
        }

    }

    //This method is like the piercing method, except it just stops.

    /**
     * @param world        The world the raytrace is in.
     * @param caster       The caster of the spell. This is so mobs don't attack each other when you use raytraces from mobs.
     *                     All damage is done by the caster.
     * @param startPos     Where the raytrace starts.
     * @param endPos       Where the raytrace ends.
     * @param borderSize   The width of the raytrace.
     * @param spellEntity  The entity that's using this method, if applicable. If this method is directly used in a spell, just make this null.
     * @param directDamage If the damage caused should be direct or not. Set to true if this method is directly used by the caster in a spell.
     * @param damageType   The damagetype of the damage.
     * @param damage       The amount of damage.
     * @param knockBack    The amount of knockback.
     * @param fireTime     How long to set an enemy on fire.
     * @param lifeSteal    The percent of damage to heal the caster by. Between 0 and 1.
     */

    public static void handleBeamCollision(World world, EntityLivingBase caster, Vec3d startPos, Vec3d endPos, float borderSize, Entity spellEntity, boolean directDamage, MagicDamage.DamageType damageType,
                                           float damage, Vec3d knockBack, boolean invulnerable, int fireTime, float radius, float lifeSteal,
                                           Predicate<? super Entity> filter) {
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
                            filter = filter.or(e -> e == secondHit);
                        }
                    }
                    if (secondHit.getTeam() == caster.getTeam()) {
                        if (damageType == MagicDamage.DamageType.RADIANT) {
                            if (secondHit instanceof EntityLivingBase) {
                                ((EntityLivingBase) secondHit).heal(damage);
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * Method for ray tracing entities (the useless default method doesn't work, despite EnumHitType having an ENTITY
     * field...) You can also use this for seeking.
     *
     * @param world                  The world the raytrace is in.
     * @param x                      startX
     * @param y                      startY
     * @param z                      startZ
     * @param tx                     endX
     * @param ty                     endY
     * @param tz                     endZ
     * @param borderSize             extra area to examine around line for entities
     * @param excluded               any excluded entities (the player, spell entities, previously hit entities, etc)
     * @param raytraceNonSolidBlocks This controls whether or not the raytrace goes through non-solid blocks, such as grass, fences, trapdoors, cobwebs, e.t.c.
     * @return a RayTraceResult of either the block hit (no entity hit), the entity hit (hit an entity), or null for
     * nothing hit
     */
    @Nullable
    public static RayTraceResult tracePath(World world, float x, float y, float z, float tx, float ty, float tz,
                                           float borderSize, HashSet<Entity> excluded, boolean collideablesOnly, boolean raytraceNonSolidBlocks) {
        Vec3d startVec = new Vec3d(x, y, z);
        Vec3d endVec = new Vec3d(tx, ty, tz);
        float minX = Math.min(x, tx);
        float minY = Math.min(y, ty);
        float minZ = Math.min(z, tz);
        float maxX = Math.max(x, tx);
        float maxY = Math.max(y, ty);
        float maxZ = Math.max(z, tz);
        AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).grow(borderSize, borderSize,
                borderSize);
        List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(null, bb);
        RayTraceResult blockHit = world.rayTraceBlocks(startVec, endVec);
        if (blockHit != null && !world.getBlockState(blockHit.getBlockPos()).isFullBlock() && !raytraceNonSolidBlocks) {
            blockHit = null;
        }
        startVec = new Vec3d(x, y, z);
        endVec = new Vec3d(tx, ty, tz);
        float maxDistance = (float) endVec.distanceTo(startVec);
        if (blockHit != null) {
            maxDistance = (float) blockHit.hitVec.distanceTo(startVec);
        }
        Entity closestHitEntity = null;
        float closestHit = maxDistance;
        float currentHit;
        AxisAlignedBB entityBb;// = ent.getBoundingBox();
        RayTraceResult intercept;
        for (Entity ent : allEntities) {
            if ((ent.canBeCollidedWith() || !collideablesOnly)
                    && (excluded == null || !excluded.contains(ent))) {
                float entBorder = ent.getCollisionBorderSize();
                entityBb = ent.getEntityBoundingBox();
                entityBb = entityBb.grow(entBorder, entBorder, entBorder);
                if (borderSize != 0) entityBb = entityBb.grow(borderSize, borderSize, borderSize);
                intercept = entityBb.calculateIntercept(startVec, endVec);
                if (intercept != null) {
                    currentHit = (float) intercept.hitVec.distanceTo(startVec);
                    if (currentHit < closestHit || currentHit == 0) {
                        closestHit = currentHit;
                        closestHitEntity = ent;
                    }
                }
            }
        }
        if (closestHitEntity != null) {
            blockHit = new RayTraceResult(closestHitEntity);
        }
        return blockHit;
    }

    public static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    //Pretty performance heavy
    public static double getMagnitude(Vec3d vector) {
        return Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
    }

    //Copied from the original method, except it doesn't spawn particles
    //Goes directly to the ground
    public static boolean attemptGroundedTeleport(EntityLivingBase entity, double x, double y, double z) {
        double d0 = entity.posX;
        double d1 = entity.posY;
        double d2 = entity.posZ;
        entity.posX = x;
        entity.posY = y;
        entity.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entity);
        World world = entity.world;

        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --entity.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

                if (world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entity.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            entity.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {

            if (entity instanceof EntityCreature) {
                ((EntityCreature) entity).getNavigator().clearPath();
            }

            return true;
        }
    }

    //Copied from the original method, except it doesn't spawn particles
    //Goes directly to the ground
    public static boolean attemptDimensionalGroundedTeleport(EntityLivingBase entity, double x, double y, double z, int targetDimension) {
        double d0 = entity.posX;
        double d1 = entity.posY;
        double d2 = entity.posZ;
        entity.posX = x;
        entity.posY = y;
        entity.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entity);
        World world = entity.world;

        entity.changeDimension(targetDimension);

        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --entity.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

                if (world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entity.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            entity.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {

            if (entity instanceof EntityCreature) {
                ((EntityCreature) entity).getNavigator().clearPath();
            }

            return true;
        }
    }


    public static boolean attemptTeleport(EntityLivingBase entity, double x, double y, double z) {
        double d0 = entity.posX;
        double d1 = entity.getEntityBoundingBox().minY;
        double d2 = entity.posZ;
        entity.posX = x;
        entity.posY = y - 1;
        entity.posZ = z;
        boolean flag = false;
        World world = entity.world;

        entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

        if (world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entity.getEntityBoundingBox())) {
            flag = true;
        }

        if (!flag) {
            entity.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {
            if (entity instanceof EntityCreature) {
                ((EntityCreature) entity).getNavigator().clearPath();
            }

            return true;
        }
    }

    /**
     * Shorthand for {@link WizardryUtilities#getEntitiesWithinRadius(double, double, double, double, World, Class)}
     * with EntityLivingBase as the entity type. This is by far the most common use for that method.
     *
     * @param radius The search radius
     * @param x      The x coordinate to search around
     * @param y      The y coordinate to search around
     * @param z      The z coordinate to search around
     * @param world  The world to search in
     */
    public static List<Entity> getEntitiesWithinRadius(double radius, double x, double y, double z,
                                                       World world) {
        return WizardryUtilities.getEntitiesWithinRadius(radius, x, y, z, world, Entity.class);
    }

    //Sound stuff
    public static SoundEvent createSound(String name) {
        // All the setRegistryName methods delegate to this one, it doesn't matter which you use.
        return new SoundEvent(new ResourceLocation(ArcaneEssentials.MODID, name)).setRegistryName(name);
    }


    public static Entity getEntityFromStringID(World world, String UUID) {
        return WizardryUtilities.getEntityByUUID(world, java.util.UUID.fromString(UUID));
    }

    public static EntityPlayer getPlayerFromStringID(String UUID) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(java.util.UUID.fromString(UUID));
    }

    public static EntityPlayer getPlayerFromUsername(String username) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(username);
    }

}

