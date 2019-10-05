package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import electroblob.wizardry.registry.WizardryBlocks;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Shatter extends SpellRay {

	//Shatters frozen entities
	public Shatter(String name, boolean isContinuous, EnumAction action) {
		super(name, isContinuous, action);
	}

	@Override
	public void playSound(World world, EntityLivingBase caster) {

	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		//if (world.getBlockState(pos).getBlock() == WizardryBlocks.ice_statue. && world.getBlockState(pos).getBlock().)
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
