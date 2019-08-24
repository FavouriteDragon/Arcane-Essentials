package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlizzardBeam extends SpellRay {

	public BlizzardBeam() {
		super(ArcaneEssentials.MODID, "blizzard_beam", false, EnumAction.BOW);
		addProperties(DAMAGE, EFFECT_RADIUS, EFFECT_DURATION, EFFECT_STRENGTH, DIRECT_EFFECT_STRENGTH);
		this.aimAssist = 0.75F;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if(MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, target)){
			if(!world.isRemote && caster instanceof EntityPlayer) ((EntityPlayer)caster).sendStatusMessage(
					new TextComponentTranslation("spell.resist", target.getName(), this.getNameForTranslationFormatted()), true);
		}
		else
			{
				if (!world.isRemote) {
					target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), getProperty(DAMAGE).floatValue()
							* modifiers.get(SpellModifiers.POTENCY));
					Vec3d knockBack = hit.subtract(origin).scale(getProperty(DIRECT_EFFECT_STRENGTH).doubleValue());
					target.addVelocity(knockBack.x, knockBack.y, knockBack.z);
					if (target instanceof EntityLivingBase) {
						((EntityLivingBase) target).addPotionEffect(new PotionEffect(WizardryPotions.frost, getProperty(EFFECT_DURATION).intValue() *
								(int) modifiers.get(SpellModifiers.POTENCY), getProperty(EFFECT_STRENGTH).intValue() * (int) getProperty(SpellModifiers.POTENCY)));
					}
				}
				else {
					for (int i = 0; i < 20; i++) {
						ParticleBuilder.create(ParticleBuilder.Type.SNOW).time(15).pos(hit).vel(world.rand.nextGaussian() * 0.05F,
								world.rand.nextDouble() * 0.5F, world.rand.nextGaussian() * 0.05F).spawn(world);
					}
				}

		}

		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (world.isRemote) {
			for (int i = 0; i < 20; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.SNOW).time(15).pos(hit).vel(world.rand.nextGaussian() * 0.05F,
						world.rand.nextDouble() * 0.5F, world.rand.nextGaussian() * 0.05F).spawn(world);
			}
		}
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance) {
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).scale(1.3F).pos(origin).target(origin.add(direction.scale(distance)))
					.time(6).clr(174, 252, 255).fade(230, 253, 254).spawn(world);
			ArcaneUtils.spawnSpinningDirectionalHelix(world, caster, direction, Vec3d.ZERO, (int) (distance + 1) * 8, distance, getProperty(EFFECT_RADIUS).floatValue(),
					ParticleBuilder.Type.SNOW, origin, new Vec3d(0.0075, 0.005, 0.0075), 12, -1, -1, -1);
		}
	}
}
