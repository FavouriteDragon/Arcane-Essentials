package com.favouritedragon.arcaneessentials.common.spell.necromancy;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import electroblob.wizardry.registry.WizardrySounds;
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

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.LIFE_STEAL;

public class Zam extends SpellRay {

	public Zam() {
		super(ArcaneEssentials.MODID, "zam", false, EnumAction.BOW);
		addProperties(DAMAGE, LIFE_STEAL, EFFECT_STRENGTH);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			if (target instanceof EntityLivingBase || target.canBeCollidedWith() && target.canBePushed()) {
				if (!world.isRemote) {
					if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.WITHER, target)) {
						float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
						if (target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.WITHER), damage)) {
							caster.heal(getProperty(LIFE_STEAL).floatValue() * damage * modifiers.get(SpellModifiers.POTENCY));
							Vec3d vel = hit.subtract(origin).scale(0.25 * getProperty(EFFECT_STRENGTH).doubleValue()).add(0, 0.125F, 0);
							target.addVelocity(vel.x, vel.y, vel.z);
							world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_LIGHTNING_DISC_HIT, WizardrySounds.SPELLS,
									0.8F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10F, false);
						}

					}
				}
				if (world.isRemote) {
					ParticleBuilder.create(ParticleBuilder.Type.BUFF).entity(target).time(10).clr(33, 0, 71).spawn(world);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance) {
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING).entity(caster).pos(origin.subtract(caster.getPositionVector())).
					length(distance).clr(33, 0, 71).scale(getProperty(EFFECT_RADIUS).floatValue() * 2).time(4).spawn(world);
			for (int i = 0; i < 15; i++)
				ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(origin.add(direction.scale(distance))).clr(33, 0, 71)
						.scale(getProperty(EFFECT_RADIUS).floatValue() * 2).time(8).vel(world.rand.nextGaussian() / 20, world.rand.nextGaussian() / 20,
						world.rand.nextGaussian() / 20).spawn(world);
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
	}
}
