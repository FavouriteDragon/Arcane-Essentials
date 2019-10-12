package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//Entity for FlameCleave.
public class EntityFlameSlash extends EntityMagicBolt {

	private float damage;
	private int lifetime = 60;
	private int burnDuration;

	/**
	 * Creates a new projectile in the given world.
	 *
	 * @param world The world the entity is in.
	 */
	public EntityFlameSlash(World world) {
		super(world);
	}

	public void setBurnDuration(int burnDuration) {
		this.burnDuration = burnDuration;
	}

	@Override
	public double getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	@Override
	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	@Override
	public boolean doDeceleration() {
		return true;
	}

	@Override
	public boolean doOverpenetration() {
		return true;
	}

	@Override
	public MagicDamage.DamageType getDamageType() {
		return MagicDamage.DamageType.FIRE;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		//Render code. Uh-oh.
		if (world.isRemote) {
			if (ticksExisted >= 1) {
				//We don't want to override the default flame particle colouring.
				float[] rgb = new float[3];
				rgb[0] = -1;
				rgb[1] = -1;
				rgb[2] = -1;
				ArcaneUtils.spawnDirectionalHorizontalBlade(world, this, null, 8, getSize() * 6, ticksExisted,
						ParticleBuilder.Type.MAGIC_FIRE, ArcaneUtils.getMiddleOfEntity(this), Vec3d.ZERO, rgb, getSize() * 4, (int) (getSize() * 15));
			}
		}
	}

	private void Dissipate() {
		world.playSound(null, posX, posY, posZ, WizardrySounds.ENTITY_FIREBOMB_FIRE, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				0.8F + world.rand.nextFloat() / 10F);
		if (world.isRemote) {
			float[] rgb = new float[3];
			rgb[0] = -1;
			rgb[1] = -1;
			rgb[2] = -1;
			ArcaneUtils.spawnDirectionalHorizontalBlade(world, this, null, 3, getSize() * 2, 0,
					ParticleBuilder.Type.MAGIC_FIRE, ArcaneUtils.getMiddleOfEntity(this), new Vec3d(world.rand.nextGaussian() / 60, world.rand.nextGaussian() / 60,world.rand.nextGaussian() / 60),
					rgb, getSize() * 6, (int) (getSize() * 20));
		}
		this.isDead = true;
	}

	@Override
	public void setDead() {
		Dissipate();
		super.setDead();
		if (isDead && !world.isRemote)
			Thread.dumpStack();
	}

	@Override
	protected void onEntityHit(EntityLivingBase entityHit) {
		super.onEntityHit(entityHit);
		entityHit.setFire(burnDuration);
	}
}
