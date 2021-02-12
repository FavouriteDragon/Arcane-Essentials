package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityFlamePillar extends EntityMagicConstruct {

	private static final DataParameter<Float> SYNC_RADIUS = EntityDataManager.createKey(EntityFlamePillar.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SYNC_HEIGHT = EntityDataManager.createKey(EntityFlamePillar.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> SYNC_PARTICLE_AMOUNT = EntityDataManager.createKey(EntityFlamePillar.class, DataSerializers.VARINT);

	public EntityFlamePillar(World world) {
		super(world);
	}

	public EntityFlamePillar(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier, float radius,
							 float vortexHeight, int particleAmount) {
		super(world);
		this.setPosition(x, y, z);
		this.setOwner(caster);
		this.setCaster(caster);
		this.lifetime = lifetime;
		this.damageMultiplier = damageMultiplier;
		setSize(1, 1);
		setRadius(radius);
		setVortexHeight(vortexHeight);
		setParticleAmount(particleAmount);

	}

	public float getRadius() {
		return dataManager.get(SYNC_RADIUS);
	}

	public void setRadius(float radius) {
		dataManager.set(SYNC_RADIUS, radius);
	}

	public float getVortexHeight() {
		return dataManager.get(SYNC_HEIGHT);
	}

	public void setVortexHeight(float height) {
		dataManager.set(SYNC_HEIGHT, height);
	}

	public int getParticleAmount() {
		return dataManager.get(SYNC_PARTICLE_AMOUNT);
	}

	public void setParticleAmount(int amount) {
		dataManager.set(SYNC_PARTICLE_AMOUNT, amount);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_RADIUS, 1F);
		dataManager.register(SYNC_PARTICLE_AMOUNT, 180);
		dataManager.register(SYNC_HEIGHT, 15F);

	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (world.isRemote) {
			ArcaneUtils.spawnSpinningHelix(world, getParticleAmount() * 2 / 3, getVortexHeight(),
					getRadius() * 0.5F, ParticleBuilder.Type.MAGIC_FIRE,getPositionVector(),
					-0.05, Vec3d.ZERO, 10, 1.0F, 1.0F, 1.0F,
					2.0F + world.rand.nextFloat());
		}

		if (ticksExisted % 5 == 0) {
			world.playSound(posX, posY, posZ, WizardrySounds.ENTITY_FIRE_RING_AMBIENT, SoundCategory.PLAYERS, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, false);
		}
		if (ticksExisted % 60 == 0) {
			world.playSound(posX, posY, posZ, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, false);
		}
		if (!world.isRemote) {
			assert getCaster() != null;
			AxisAlignedBB hitBox = new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX, this.posY + getVortexHeight(), this.posZ);
			hitBox = hitBox.grow(getRadius() * 1.1, 0, getRadius() * 1.1);
			List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, hitBox);
			if (!hit.isEmpty()) {
				for (Entity e : hit) {
					if (this.isValidTarget(e)) {
						if (e instanceof EntityLivingBase && e != getCaster() && AllyDesignationSystem.isValidTarget(this, e) && e.canBePushed() && e.canBeCollidedWith()) {
							if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.FIRE, e)) {
								e.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster()), 0.5F * damageMultiplier);
								e.motionX += 0.025;
								e.motionY += 0.1;
								e.motionZ += 0.025;
								e.setFire(4);
								e.setEntityInvulnerable(false);
							}
						}
						if (e instanceof EntityThrowable && !(e instanceof EntityMagicBolt) || e instanceof EntityArrow) {
							e.motionX *= -1.1;
							e.motionZ *= -1.1;
						}
					}
				}
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

}
