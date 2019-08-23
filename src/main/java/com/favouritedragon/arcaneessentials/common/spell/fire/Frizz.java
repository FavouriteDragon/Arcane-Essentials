package com.favouritedragon.arcaneessentials.common.spell.fire;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityFireball;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Frizz extends Spell {

	private static final String SIZE = "size";

	public Frizz() {
		super(ArcaneEssentials.MODID, "frizz", EnumAction.BOW, false);
		addProperties(DAMAGE, RANGE, BURN_DURATION, SIZE);
	}


	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote) {
			cast(world, caster, modifiers);
			return true;
		}
		return false;
	}

	private void cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
		float size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		Vec3d pos = caster.getPositionVector().add(caster.getLookVec()).add(0, caster.getEyeHeight() - 0.2, 0);
		EntityFireball fireball = new EntityFireball(world);
		fireball.setSize(size);
		fireball.setDamage(damage);
		fireball.setPosition(pos.x, pos.y, pos.z);
		fireball.setCaster(caster);
		fireball.aim(caster, getProperty(RANGE).floatValue(), 0F);
		world.spawnEntity(fireball);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (!world.isRemote) {
			cast(world, caster, modifiers);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		if (!world.isRemote) {
			float size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			Vec3i dir = direction.getDirectionVec();
			EntityFireball fireball = new EntityFireball(world);
			fireball.setSize(size);
			fireball.setDamage(damage);
			fireball.setPosition(x, y, z);
			fireball.shoot(dir.getX(), dir.getY(), dir.getZ(), getProperty(RANGE).floatValue(), 0F);
			world.spawnEntity(fireball);
			return true;
		}
		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}

	@Override
	public boolean canBeCastByDispensers() {
		return true;
	}

}
