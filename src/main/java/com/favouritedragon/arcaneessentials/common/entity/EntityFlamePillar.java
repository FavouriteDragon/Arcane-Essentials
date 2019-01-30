package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFlamePillar extends EntityMagicConstruct {

	public EntityFlamePillar(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
		super(world, x, y, z, caster, lifetime, damageMultiplier);
		setSize(3, 10);
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
		if (!world.isRemote) {
			world.playSound(posX, posY, posZ, WizardrySounds.SPELL_LOOP_FIRE, SoundCategory.HOSTILE, 1 + world.rand.nextFloat()/10, 0.5F + world.rand.nextFloat()/10, false);
		}
		assert getCaster() != null;
		ArcaneUtils.handlePiercingBeamCollision(world, getCaster(), getCaster(), getPositionVector(), getPositionVector().add(0, 10, 0), 1.5F,
				this, false, MagicDamage.DamageType.FIRE, 0.5F * damageMultiplier, new Vec3d(0.05, 0.3, 0.05), true, 20, 1.5F);
	}
}
