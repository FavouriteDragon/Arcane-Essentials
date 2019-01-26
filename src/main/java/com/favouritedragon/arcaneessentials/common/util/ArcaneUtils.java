package com.favouritedragon.arcaneessentials.common.util;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ArcaneUtils {
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
	public static void spawnDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double minRadius, double radiusScale, WizardryParticleType particle, double posX, double posY, double posZ,
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
				Wizardry.proxy.spawnParticle(particle, world, pos.x + posX + direction.x, pos.y + posY + direction.y,
						pos.z + posZ + direction.z, velX, velY, velZ, maxAge, r, g, b);
			} else {
				Wizardry.proxy.spawnParticle(particle, world, x + posX, y + posY,
						z + posZ, velX, velY, velZ, maxAge, r, g, b);
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
	 * @param maxAge        The maximum age of the particle. Wizardry particles already have a predetermined age, this just adds onto it.
	 * @param r             The amount of red in the particle. G and B are self-explanatory (green and blue).
	 */
	public static void spawnSpinningDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double minRadius, double radiusScale, WizardryParticleType particle, Vec3d position,
													  Vec3d particleSpeed, int maxAge, float r, float g, float b) {
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
				pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
				pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
				Wizardry.proxy.spawnParticle(particle, world, pos.x + position.x + direction.x, pos.y + position.y + direction.y,
						pos.z + position.z + direction.z, particleSpeed.x * radius * omega * Math.cos(angle2), particleSpeed.y, particleSpeed.z * radius * omega * Math.sin(angle2), maxAge, r, g, b);
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
	public static void spawnSpinningVortex(World world, int maxAngle, double vortexHeight, double minRadius, double radiusScale, WizardryParticleType particle, Vec3d position,
										   Vec3d particleSpeed, Vec3d entitySpeed, int maxAge, float r, float g, float b) {
		for (int angle = 0; angle < maxAngle; angle++) {
			double angle2 = world.rand.nextDouble() * Math.PI * 2;
			double radius = minRadius + (angle / radiusScale);
			double x = radius * cos(angle);
			double y = angle / (maxAngle / vortexHeight);
			double z = radius * sin(angle);
			double speed = world.rand.nextDouble() * 2 + 1;
			double omega = Math.signum(speed * ((Math.PI * 2) / 20 - speed / (20 * radius)));
			angle2 += omega;
			Wizardry.proxy.spawnParticle(particle, world, x + position.x, y + position.y, z + position.z,
					(particleSpeed.x * radius * omega * Math.cos(angle2)) + entitySpeed.x, particleSpeed.y + entitySpeed.y, (particleSpeed.z * radius * omega * Math.sin(angle2)) + entitySpeed.z, maxAge, r, g, b);

		}
	}

	public static void spawnDirectionalHelix(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double radius, EnumParticleTypes particle, double posX, double posY, double posZ,
											 double velX, double velY, double velZ) {
		for (int angle = 0; angle < maxAngle; angle++) {
			double x = radius * cos(angle);
			double y = angle / (maxAngle / vortexLength);
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

	public static void spawnDirectionHelix(World world, EntityLivingBase entity, Vec3d direction, int maxAngle, double vortexLength, double radius, WizardryParticleType particle, double posX, double posY, double posZ,
										   double velX, double velY, double velZ, int maxAge, float r, float g, float b) {
		for (int angle = 0; angle < maxAngle; angle++) {
			double x = radius * cos(angle);
			double y = angle / (maxAngle / vortexLength);
			double z = radius * sin(angle);
			Vec3d pos = new Vec3d(x, y, z);
			if (entity != null && direction != null) {
				pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
				pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
				Wizardry.proxy.spawnParticle(particle, world, pos.x + posX + direction.x, pos.y + posY + direction.y,
						pos.z + posZ + direction.z, velX, velY, velZ, maxAge, r, g, b);
			} else {
				Wizardry.proxy.spawnParticle(particle, world, x + posX, y + posY,
						z + posZ, velX, velY, velZ, maxAge);
			}
		}
	}

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
			RayTraceResult result = ArcaneUtils.standardEntityRayTrace(world, entity, startPos, vortexLength, maxRadius);
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
	public static RayTraceResult standardEntityRayTrace(World world, EntityLivingBase entity, Vec3d startPos, Vec3d endPos, float borderSize) {
		HashSet<Entity> hashset = new HashSet<>(1);
		hashset.add(entity);
		return WizardryUtilities.tracePath(world, (float) startPos.x,
				(float) startPos.y, (float) startPos.z,
				(float) endPos.x, (float) endPos.y, (float) endPos.z,
				borderSize, hashset, false);
	}

	@Nullable
	public static RayTraceResult standardEntityRayTrace(World world, EntityLivingBase entity, Vec3d startPos, double range, float borderSize) {
		float dx = (float) entity.getLookVec().x * (float) range;
		float dy = (float) entity.getLookVec().y * (float) range;
		float dz = (float) entity.getLookVec().z * (float) range;
		HashSet<Entity> hashset = new HashSet<>(1);
		hashset.add(entity);
		return WizardryUtilities.tracePath(world, (float) startPos.x,
				(float) startPos.y, (float) startPos.z,
				(float) startPos.x + dx, (float) startPos.y + dy, (float) startPos.z + dz,
				borderSize, hashset, false);
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
		return new Vec3d((double) (f1 * f2), (double) f3, (double) (f * f2));
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

	public static void handlePiercingBeamCollision(World world, EntityLivingBase entity, Vec3d startPos, Vec3d endPos, float borderSize, MagicDamage.DamageType damageType,
												   double damage, Vec3d knockBack) {
		
	}
}

