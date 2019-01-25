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

	public static void spawnDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int particleAmount, double vortexLength, double radiusScale, EnumParticleTypes particle, double posX, double posY, double posZ,
											  double particleSpeed) {

	}

	public static void spawnDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int particleAmount, double vortexLength, double radiusScale, WizardryParticleType particle, double posX, double posY, double posZ,
											  double velX, double velY, double velZ, int maxAge) {
		;
		for (int angle = 0; angle < particleAmount; angle++) {
			double radius = angle / radiusScale;
			double x = radius * cos(angle);
			double y = angle / (particleAmount / vortexLength);
			double z = radius * sin(angle);
			Vec3d pos = new Vec3d(x, y, z);
			if (entity != null && direction != null) {
				pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
				pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
				Wizardry.proxy.spawnParticle(particle, world, pos.x + posX + direction.x, pos.y + posY + direction.y,
						pos.z + posZ + direction.z, velX, velY, velZ, maxAge);
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
}

