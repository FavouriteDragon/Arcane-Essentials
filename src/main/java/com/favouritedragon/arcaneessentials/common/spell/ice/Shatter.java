package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import electroblob.wizardry.registry.WizardryBlocks;
import electroblob.wizardry.tileentity.TileEntityStatue;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

import static electroblob.wizardry.util.SpellModifiers.POTENCY;

public class Shatter extends SpellRay {

	//Shatters frozen entities
	public Shatter() {
		super(ArcaneEssentials.MODID, "shatter", false, EnumAction.BOW);
		addProperties(DAMAGE, EFFECT_STRENGTH);
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
		if (world.getTileEntity(pos) instanceof TileEntityStatue && world.getBlockState(pos).getBlock() == WizardryBlocks.ice_statue) {
			if (((TileEntityStatue) Objects.requireNonNull(world.getTileEntity(pos))).isIce) {
				world.getBlockState(pos).getBlock().breakBlock(world, pos, world.getBlockState(pos));
				if (((TileEntityStatue) world.getTileEntity(pos)).creature != null) {
					EntityLivingBase target = ((TileEntityStatue) world.getTileEntity(pos)).creature;
					target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), getProperty(DAMAGE).floatValue()
							* getProperty(POTENCY).floatValue());
					if (world.isRemote)
						ParticleBuilder.create(ParticleBuilder.Type.ICE).time(30).collide(true).pos(hit).target(target).scale(getProperty(EFFECT_STRENGTH).floatValue() * 2).spawn(world);

				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		BlockPos pos = new BlockPos(origin.add(direction).x, origin.add(direction).y, origin.add(direction).z);
		if (world.getTileEntity(pos) instanceof TileEntityStatue && world.getBlockState(pos).getBlock() == WizardryBlocks.ice_statue) {
			if (((TileEntityStatue) Objects.requireNonNull(world.getTileEntity(pos))).isIce) {
				world.getBlockState(pos).getBlock().breakBlock(world, pos, world.getBlockState(pos));
				if (((TileEntityStatue) world.getTileEntity(pos)).creature != null) {
					EntityLivingBase target = ((TileEntityStatue) world.getTileEntity(pos)).creature;
					if (target.getHealth() <= 4) {
						if (world.isRemote)
							ParticleBuilder.create(ParticleBuilder.Type.ICE).time(26).collide(true).pos(origin.add(direction)).target(target).scale(getProperty(EFFECT_STRENGTH).floatValue() * 2).spawn(world);
					}
					target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), getProperty(DAMAGE).floatValue()
							* getProperty(POTENCY).floatValue());
				}
				return true;
			}
		}
		return false;
	}
}
