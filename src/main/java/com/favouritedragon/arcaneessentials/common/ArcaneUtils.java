package com.favouritedragon.arcaneessentials.common;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.util.WizardryParticleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ArcaneUtils {
	public static Vec3d rotateAroundAxisX(Vec3d v, double angle) {
		angle = Math.toRadians(angle);
		double y, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		y = v.y * cos - v.z * sin;
		z = v.y * sin + v.z * cos;
		return new Vec3d(v.x, y, z);
	}

	public static Vec3d rotateAroundAxisY(Vec3d v, double angle) {
		angle = -angle;
		angle = Math.toRadians(angle);
		double x, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.x * cos + v.z * sin;
		z = v.x * -sin + v.z * cos;
		return new Vec3d(x, v.y, z);
	}

	public static Vec3d rotateAroundAxisZ(Vec3d v, double angle) {
		angle = Math.toRadians(angle);
		double x, y, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.x * cos - v.y * sin;
		y = v.x * sin + v.y * cos;
		return new Vec3d(x, y, v.z);
	}

	public static void spawnDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int particleAmount, double vortexLength, double radiusScale, EnumParticleTypes particle, double posX, double posY, double posZ,
									   double particleSpeed) {

	}

	public static void spawnDirectionalVortex(World world, EntityLivingBase entity, Vec3d direction, int particleAmount, double vortexLength, double radiusScale, WizardryParticleType particle, double posX, double posY, double posZ,
									   double velX, double velY, double velZ, int maxAge) { ;
		for (int angle = 0; angle < particleAmount; angle++) {
			double radius = angle / radiusScale;
			double x = radius * Math.cos(angle);
			double y = angle / (180/vortexLength);
			double z = radius * Math.sin(angle);
			Vec3d pos = new Vec3d(x, y, z);
			if (entity != null && direction != null) {
				pos = ArcaneUtils.rotateAroundAxisX(pos, entity.rotationPitch + 90);
				pos = ArcaneUtils.rotateAroundAxisY(pos, entity.rotationYaw);
				Wizardry.proxy.spawnParticle(particle, world, pos.x + posX + direction.x, pos.y + posY + direction.y,
						pos.z + posZ + direction.z, velX, velY, velZ, maxAge);
			}
			else {
				Wizardry.proxy.spawnParticle(particle, world, x + posX, y + posY,
						z + posZ, velX, velY, velZ, maxAge);
			}
		}
	}
}
