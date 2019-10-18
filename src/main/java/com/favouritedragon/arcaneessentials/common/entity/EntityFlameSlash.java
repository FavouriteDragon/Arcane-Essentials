package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundCategory;
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
			if (ticksExisted >= 1 || ticksExisted % 20 == 0) {
				for (int angle = 0; angle < 360; angle += 6) {
					double radians = Math.toRadians(angle);
					double x = Math.cos(radians) * .5;
					double z = Math.sin(radians) * .5;
					ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).scale(getSize() * 0.75F).time((int) (getSize() * 20)).pos(ArcaneUtils.getMiddleOfEntity(this).add(x, 0, z))
							.spin(getSize() / 2, -0.075).vel(motionX * 1.05, motionY * 1.05, motionZ * 1.05).spawn(world);
				}
			}
		}
	}

	private void Dissipate() {
		world.playSound(null, posX, posY, posZ, WizardrySounds.ENTITY_FIREBOMB_FIRE, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				0.8F + world.rand.nextFloat() / 10F);
		if (world.isRemote) {
			for (int angle = 0; angle < 360; angle += 6) {
				double radians = Math.toRadians(angle);
				double x = Math.cos(radians) * .5;
				double z = Math.sin(radians) * .5;
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).scale(getSize()).time((int) (getSize() * 4)).pos(ArcaneUtils.getMiddleOfEntity(this).add(x, 0, z))
						.spin(getSize(), -0.25).vel(world.rand.nextGaussian() / 10, world.rand.nextGaussian() / 10, world.rand.nextGaussian() / 10).spawn(world);

			}
		}
		this.isDead = true;
	}

	@Override
	public void setDead() {
		Dissipate();
		super.setDead();
	}

	@Override
	protected void onEntityHit(EntityLivingBase entityHit) {
		super.onEntityHit(entityHit);
		entityHit.setFire(burnDuration);
	}
}
