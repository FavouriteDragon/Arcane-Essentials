package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneUtils.applyPlayerKnockback;

public class BlizzardBeam extends SpellRay {

	public BlizzardBeam() {
		super(ArcaneEssentials.MODID, "blizzard_beam", false, EnumAction.BOW);
		addProperties(DAMAGE, EFFECT_DURATION, EFFECT_STRENGTH, DIRECT_EFFECT_STRENGTH);
	}


	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		Vec3d knockBack = hit.subtract(origin).scale(.01 * getProperty(DIRECT_EFFECT_STRENGTH).floatValue());
		float damage = getProperty(DAMAGE).floatValue() + modifiers.get(SpellModifiers.POTENCY);
		if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, target) && target instanceof EntityLivingBase && caster != null) {
			target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), damage);
			target.motionX += knockBack.x;
			target.motionY += knockBack.y;
			target.motionZ += knockBack.z;
			((EntityLivingBase) target).addPotionEffect(new PotionEffect(WizardryPotions.frost, getProperty(EFFECT_DURATION).intValue() * (int) modifiers.get(WizardryItems.duration_upgrade),
					getProperty(EFFECT_STRENGTH).intValue() * (int) modifiers.get(SpellModifiers.POTENCY)));
			applyPlayerKnockback(target);
		}
		return true;
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
		super.spawnParticleRay(world, origin, direction, caster, distance);
		Vec3d endPos = origin.add(direction.scale(distance));
		int particles = distance / getProperty(RANGE).floatValue() >= 1 ? 180 : (int) (distance / getProperty(RANGE).floatValue() * 180);
		ParticleBuilder.create(ParticleBuilder.Type.BEAM).scale(getProperty(EFFECT_RADIUS).floatValue() * 5).pos(origin).target(endPos)
				.time(15).clr(174, 252, 255).fade(230, 253, 254).collide(true).spawn(world);
		ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), particles, distance, getProperty(EFFECT_RADIUS).floatValue(),
				ParticleBuilder.Type.SNOW, origin, new Vec3d(world.rand.nextGaussian() / 80, world.rand.nextGaussian() / 40, world.rand.nextGaussian() / 80),
				12, -1, -1, -1);

	}

	@Override
	public boolean isPiercing() {
		return true;
	}

	@Override
	public void playSound(World world, EntityLivingBase caster) {
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_SHARD_SMASH, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_CHARGE_ICE, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_GIANT_ATTACK, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 10,
				1.0F + world.rand.nextFloat() / 10F, true);
	}
}
