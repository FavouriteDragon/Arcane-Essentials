package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityMagicLightning extends EntityMagicConstruct {

	private Vec3d knockback;
	private float damage;
	private int burnTime;

	public EntityMagicLightning(World world) {
		super(world);
	}

	public void setKnockback(Vec3d knockback) {
		this.knockback = knockback;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ticksExisted > 5) {
			setDead();
		}
		List<Entity> targets = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow(1));
		if (!targets.isEmpty()) {
			targets.remove(getCaster());
			for (Entity hit : targets) {
				if (!world.isRemote) {
					if (hit.canBeCollidedWith() && hit.canBePushed() && AllyDesignationSystem.isValidTarget(this, hit)) {
						hit.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster(), MagicDamage.DamageType.SHOCK), damage);
						Vec3d vel = knockback;
						hit.addVelocity(vel.x, vel.y, vel.z);
						hit.setFire(burnTime);
						ArcaneUtils.applyPlayerKnockback(hit);
					}
				}
			}
		}
	}

	@Override
	public boolean canBePushed() {
		return false;
	}
}
