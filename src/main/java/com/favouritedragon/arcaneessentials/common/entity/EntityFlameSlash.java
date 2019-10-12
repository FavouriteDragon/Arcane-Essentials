package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//Entity for FlameCleave.
public class EntityFlameSlash extends EntityMagicBolt {

	/**
	 * Creates a new projectile in the given world.
	 *
	 * @param world The world the entity is in.
	 */
	public EntityFlameSlash(World world) {
		super(world);
	}

	private float damage;
	private int lifetime;

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	@Override
	public double getDamage() {
		return damage;
	}

	@Override
	public int getLifetime() {
		return lifetime;
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
			//We don't want to override the default flame particle colouring.
			float[] rgb = new float[3];
			rgb[0] = -1;
			rgb[1] = -1;
			rgb[2] = -1;
			ArcaneUtils.spawnDirectionalHorizontalBlade(world, this, null, 1, getSize() * 2, ticksExisted, getSize() / 2,
					ParticleBuilder.Type.MAGIC_FIRE, getPositionVector(), Vec3d.ZERO, rgb, getSize(), (int) (getSize() * 20));
		}
	}
}
