package com.favouritedragon.arcaneessentials.common.spell.earth;

import com.favouritedragon.arcaneessentials.common.entity.EntityFallingBlockSpawner;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Quake extends ArcaneSpell {

	public Quake() {
		super("quake", EnumAction.BOW, false);
		addProperties(DAMAGE, DURATION, RANGE, BLAST_RADIUS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (world.getBlockState(caster.getPosition().offset(EnumFacing.DOWN)).isFullBlock()) {
			float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			int lifetime = getProperty(DURATION).intValue() * 20 * (int) modifiers.get(WizardryItems.duration_upgrade);
			caster.swingArm(hand);
			for (int i = 0; i < 3; i++) {
				//Starts 15 degrees to the left, then iterates right
				Vec3d look = ArcaneUtils.toRectangular(Math.toRadians(caster.rotationYaw - 15 + i * 15), 0);
				EntityFallingBlockSpawner spawner = new EntityFallingBlockSpawner(world, caster.posX + look.x * 0.25, caster.getEntityBoundingBox().minY, caster.posZ + look.z * 0.25, caster,
						lifetime, damage);
				look.scale(getProperty(RANGE).doubleValue());
				spawner.setOwner(caster);
				spawner.motionX = look.x;
				spawner.motionY = 0;
				spawner.motionZ = look.z;
				spawner.setSize(getProperty(BLAST_RADIUS).floatValue());
				if (!world.isRemote)
					world.spawnEntity(spawner);
			}
			return true;
		}
		return false;
	}


	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (world.getBlockState(caster.getPosition().offset(EnumFacing.DOWN)).isFullBlock()) {
			float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			int lifetime = getProperty(DURATION).intValue() * 20 * (int) modifiers.get(WizardryItems.duration_upgrade);
			caster.swingArm(hand);
			for (int i = 0; i < 3; i++) {
				//Starts 30 degrees to the left, then iterates right
				Vec3d look = ArcaneUtils.toRectangular(Math.toRadians(caster.rotationYaw - 30 + i * 30), 0);
				EntityFallingBlockSpawner spawner = new EntityFallingBlockSpawner(world, caster.posX + look.x * 0.25, caster.getEntityBoundingBox().minY, caster.posZ + look.z * 0.25, caster,
						lifetime, damage);
				look.scale(getProperty(RANGE).doubleValue());
				spawner.setOwner(caster);
				spawner.motionX = look.x;
				spawner.motionY = 0;
				spawner.motionZ = look.z;
				spawner.setRenderSize(getProperty(BLAST_RADIUS).floatValue());
				world.spawnEntity(spawner);
			}
			return true;
		}
		return false;
	}

}
