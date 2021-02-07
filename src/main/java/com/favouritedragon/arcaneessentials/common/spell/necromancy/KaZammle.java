package com.favouritedragon.arcaneessentials.common.spell.necromancy;

import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.LIFE_STEAL;


public class KaZammle extends SpellRay {

	public KaZammle() {
		super("kazammle", EnumAction.BOW, false);
		addProperties(DAMAGE, LIFE_STEAL, EFFECT_STRENGTH);
	}



	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		Vec3d vel;
		if (caster != null) {
			if (target instanceof EntityLivingBase || target.canBeCollidedWith() && target.canBePushed()) {
				if (!world.isRemote) {
					if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.WITHER, target) && AllyDesignationSystem.isValidTarget(caster, target)) {
						float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
						if (target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.WITHER), damage)) {
							if (origin == null || hit == null)
								vel = caster.getLookVec().scale(getProperty(EFFECT_STRENGTH).floatValue());
							else
								vel = hit.subtract(origin).scale(0.25F * getProperty(EFFECT_STRENGTH).doubleValue()).add(0, 0.125F, 0);
							caster.heal(getProperty(LIFE_STEAL).floatValue() * damage * modifiers.get(SpellModifiers.POTENCY));
							target.addVelocity(vel.x, vel.y, vel.z);
							world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_LIGHTNING_DISC_HIT, SoundCategory.PLAYERS,
									1F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10F, false);
						}

					}
				}
				if (world.isRemote) {
					ParticleBuilder.create(ParticleBuilder.Type.BUFF).entity(target).time(10).clr(33, 0, 71).spawn(world);
					ParticleBuilder.create(ParticleBuilder.Type.SCORCH).pos(target.getPositionVector().add(0, target.getEyeHeight() / 2, 0))
							.clr(33, 0, 71).time(40).scale(4).spawn(world);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		Vec3d vec = hit.add(new Vec3d(side.getDirectionVec()).scale(GeometryUtils.ANTI_Z_FIGHTING_OFFSET));
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.SCORCH).pos(vec).face(side).clr(33, 0, 71).scale(6).time(45).spawn(world);
		}
		return true;

	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance) {
		if (world.isRemote) {
			Vec3d endPos = origin.add(direction.scale(distance));
			ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING, caster).pos(origin).
					target(endPos).clr(33, 0, 71).scale(getProperty(EFFECT_RADIUS).floatValue() * 6).time(8).spawn(world);
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).scale(getProperty(EFFECT_RADIUS).floatValue() * 6).time(18)
					.clr(33, 0, 71).pos(endPos.add(0, 30, 0)).target(endPos)
					.face(EnumFacing.DOWN).spawn(world);
			for (int i = 0; i < 3; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.SPHERE).pos(endPos).time(18).
						clr(33, 0, 71).scale(getProperty(EFFECT_RADIUS).floatValue()).spawn(world);
			}
			for (int i = 0; i < 80; i++)
				ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(endPos).clr(33, 0, 71)
						.scale(getProperty(EFFECT_RADIUS).floatValue() * 2).time(18).vel(world.rand.nextGaussian() / 5, world.rand.nextGaussian() / 5,
						world.rand.nextGaussian() / 5).spawn(world);
		}
	}


	@Override
	public void playSound(World world, EntityLivingBase caster) {
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_SHADOW_WRAITH_HURT, WizardrySounds.SPELLS,
				1.0F + world.rand.nextFloat(), 0.8F + world.rand.nextFloat() / 10F, false);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_DARKNESS_ORB_HIT, WizardrySounds.SPELLS,
				1.0F + world.rand.nextFloat(), 0.8F + world.rand.nextFloat() / 10F, false);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_LIGHTNING_DISC_HIT, WizardrySounds.SPELLS,
				0.8F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10F, false);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_ENDERDRAGON_GROWL, WizardrySounds.SPELLS,
				1.5F + world.rand.nextFloat(), 0.8F + world.rand.nextFloat() / 10F, false);
	}

}
