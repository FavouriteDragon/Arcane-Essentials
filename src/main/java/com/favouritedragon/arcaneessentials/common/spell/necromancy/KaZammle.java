package com.favouritedragon.arcaneessentials.common.spell.necromancy;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneSpellProperties.LIFE_STEAL;
import static electroblob.wizardry.spell.Spell.DAMAGE;
import static electroblob.wizardry.spell.Spell.EFFECT_STRENGTH;

//SpellRay hitboxes are whack, so I'm gonna use the normal spell class for now.
public class KaZammle extends Spell {

	public KaZammle() {
		super(ArcaneEssentials.MODID, "kazammle", EnumAction.BOW, false);
		addProperties(DAMAGE, LIFE_STEAL, EFFECT_STRENGTH);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

		Vec3d look = caster.getLookVec();
		float damage = getProperty(DAMAGE).floatValue() + modifiers.get(SpellModifiers.POTENCY);

		float range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		double eyepos = caster.getEntityBoundingBox().minY + caster.getEyeHeight() - 0.4F;
		Vec3d startPos = new Vec3d(caster.posX, eyepos, caster.posZ);
		Vec3d endPos = caster.getLookVec().scale(range).add(startPos);

		if (!world.isRemote) {
			Vec3d knockBack = new Vec3d(6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade));
			ArcaneUtils.handlePiercingBeamCollision(world, caster, startPos, endPos, 0.5F, null, true,
					MagicDamage.DamageType.RADIANT, damage, knockBack, true, 10, 0.5F, 0, RayTracer.ignoreEntityFilter(caster));
		}

		if (world.isRemote) {
			for (int i = 0; i < 80; i++) {
				boolean b = world.rand.nextBoolean();
				double x1 = caster.posX + look.x * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				double y1 = eyepos + look.y * i / 2
						+ world.rand.nextFloat() / 5 - 0.1f;
				double z1 = caster.posZ + look.z * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(x1, y1, z1).vel(b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80,
						world.rand.nextDouble() / 40,
						b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80).time(15).clr(1.0F, 1.0F, 0.3F).spawn(world);

			}
			ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), 180, range, 0.5, ParticleBuilder.Type.SPARKLE, new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.4F, caster.posZ),
					new Vec3d(world.rand.nextGaussian() / 80, world.rand.nextGaussian() / 40, world.rand.nextGaussian() / 80), 15, 1.0F, 1.0F, 0.3F);
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).pos(startPos).target(endPos).clr(1.0F, 1.0F, 0.3F).fade(1.0F,
					1.0F, 1.0F).scale(4F).time(4).spawn(world);
			return true;
		}

		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND, 1.5F, 1);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT, 0.5F, 1.0f);

		return true;
	}

	/*
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
		Vec3d vec = hit.add(new Vec3d(side.getDirectionVec()).scale(WizardryUtilities.ANTI_Z_FIGHTING_OFFSET));
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
			Vec3d pos = origin.subtract(caster.getPositionVector());
			ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING).entity(caster).pos(pos).
					length(distance).clr(33, 0, 71).scale(getProperty(EFFECT_RADIUS).floatValue() * 4).time(8).spawn(world);
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).entity(caster).scale(getProperty(EFFECT_RADIUS).floatValue() * 6).time(18)
					.clr(33, 0, 71).pos(pos.add(direction.scale(distance)).add(0, 30, 0)).target(origin.add(direction.scale(distance)))
					.face(EnumFacing.DOWN).spawn(world);
			for (int i = 0; i < 3; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.SPHERE).pos(origin.add(direction.scale(distance))).time(18).
						clr(33, 0, 71).scale(getProperty(EFFECT_RADIUS).floatValue()).spawn(world);
			}
			for (int i = 0; i < 80; i++)
				ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(origin.add(direction.scale(distance))).clr(33, 0, 71)
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
	}**/
}
