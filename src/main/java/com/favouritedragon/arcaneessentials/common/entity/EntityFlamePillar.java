package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.entity.projectile.EntityMagicProjectile;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityFlamePillar extends EntityMagicConstruct {

	private static final DataParameter<Float> SYNC_RADIUS = EntityDataManager.createKey(EntityFlamePillar.class, DataSerializers.FLOAT);

	public EntityFlamePillar(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier, float radius) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		setSize(1, 1);
		setRadius(radius);

	}

	public float getRadius() {
		return dataManager.get(SYNC_RADIUS);
	}

	public void setRadius(float radius) {
		dataManager.set(SYNC_RADIUS, radius);
	}

	@Override
	protected void entityInit() {
		dataManager.register(SYNC_RADIUS, 1F);

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		world.playSound(posX, posY, posZ, WizardrySounds.SPELL_LOOP_FIRE, SoundCategory.HOSTILE, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, false);
		assert getCaster() != null;
		AxisAlignedBB hitBox = new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX, this.posY + 15, this.posZ);
		hitBox = hitBox.grow(getRadius(), 0, getRadius());
		List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, hitBox);
		if (!hit.isEmpty()) {
			for (Entity e : hit) {
				if (this.isValidTarget(e)) {
					if (e instanceof EntityLivingBase) {
						if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.FIRE, e)) {
							e.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster()), 0.5F * damageMultiplier);
							e.motionX += 0.025;
							e.motionY += 0.25;
							e.motionZ += 0.025;
						}
					}
					if (e instanceof EntityMagicProjectile || e instanceof EntityThrowable || e instanceof EntityArrow) {
						e.setVelocity(e.motionX * -1.1, e.motionY, e.motionZ * -1.1);
					}
				}
			}
		}
	}
}
