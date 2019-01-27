package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.util.WizardryParticleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFlamePillar extends EntityMagicConstruct {

	public EntityFlamePillar(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
	}

	@Override
	protected void entityInit() {

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
			ArcaneUtils.spawnSpinningHelix(world,360, 10, 3,
					WizardryParticleType.MAGIC_FIRE, getPositionVector(), new Vec3d(0.2, 0.05, 0.2), new Vec3d(0, 0, 0), 10, 1, 0,0);
	}
}
