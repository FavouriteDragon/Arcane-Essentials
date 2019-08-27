package com.favouritedragon.arcaneessentials.common.spell.necromancy;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneSpellProperties.LIFE_STEAL;

public class KaZam extends SpellRay {

	public KaZam() {
		super(ArcaneEssentials.MODID, "kazam", false, EnumAction.BOW);
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
							Vec3d vel = hit.subtract(origin).scale(modifiers.get(WizardryItems.blast_upgrade) * getProperty(EFFECT_STRENGTH).doubleValue()).add(0, 0.125F, 0);
							target.addVelocity(vel.x, vel.y, vel.z);
							world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_LIGHTNING_DISC_HIT, WizardrySounds.SPELLS,
									1F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10F, false);
						}

					}
				}
				if (world.isRemote) {
					ParticleBuilder.create(ParticleBuilder.Type.BUFF).entity(target).time(10).clr(33, 0, 71).spawn(world);
					ParticleBuilder.create(ParticleBuilder.Type.SCORCH).pos(target.getPositionVector().add(0, target.getEyeHeight() / 2, 0))
							.clr(33, 0, 71).time(40).scale(2).spawn(world);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		Vec3d vec = hit.add(new Vec3d(side.getDirectionVec()).scale(WizardryUtilities.ANTI_Z_FIGHTING_OFFSET));
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.SCORCH).pos(vec).face(side).clr(33, 0, 71).scale(4).time(45).spawn(world);
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
					target(endPos).clr(33, 0, 71).scale(getProperty(EFFECT_RADIUS).floatValue() * 4.5F).time(8).spawn(world);
			for (int i = 0; i < 2; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.SPHERE).pos(endPos).time(18).
						clr(33, 0, 71).scale(getProperty(EFFECT_RADIUS).floatValue()).spawn(world);
			}
			for (int i = 0; i < 60; i++)
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
				0.45F + world.rand.nextFloat(), 0.8F + world.rand.nextFloat() / 10F, false);
	}
}
