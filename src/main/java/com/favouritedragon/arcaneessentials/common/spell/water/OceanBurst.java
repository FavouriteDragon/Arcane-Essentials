package com.favouritedragon.arcaneessentials.common.spell.water;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.SPLASH;

public class OceanBurst extends Spell {

	public OceanBurst() {
		super(ArcaneEssentials.MODID, "ocean_burst", EnumAction.BOW, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = 3 + 2 * modifiers.get(WizardryItems.range_upgrade);
		Vec3d look = caster.getLookVec();
		double mult = modifiers.get(WizardryItems.range_upgrade) > 0 ? 0.6 + 0.2 * modifiers.get(WizardryItems.range_upgrade) : 0.6;
		double eyepos = caster.getEyeHeight() + caster.getEntityBoundingBox().minY;
		if (world.isRemote) {
			//Spawn particles
				for(int i = 0; i < 40; i++) {
					double x1 = caster.posX + look.x + world.rand.nextFloat() / 10 - 0.05f;
					double y1 = eyepos - 0.4F + world.rand.nextFloat() / 10 - 0.05f;
					double z1 = caster.posZ + look.z + world.rand.nextFloat() / 10 - 0.05f;

					//Using the random function each time ensures a different number for every value, making the ability "feel" better.
					ParticleBuilder.create(ParticleBuilder.Type.MAGIC_BUBBLE).pos(x1, y1, z1).vel(look.x * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 25
									+ ArcaneUtils.getRandomNumberInRange(-10, 10) / 20F,
							look.y * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 25
									+ ArcaneUtils.getRandomNumberInRange(-10, 10) / 20F,
							look.z * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 25
									+ ArcaneUtils.getRandomNumberInRange(-10, 10) / 20F).time(8)
							.face(caster.rotationYaw, caster.rotationPitch).spawn(world);
				}
		}
		if (!world.isRemote) {
			Vec3d startPos = new Vec3d(caster.posX, eyepos, caster.posZ);
			Vec3d endPos = startPos.add(caster.getLookVec().scale(mult * range));
			ArcaneUtils.vortexEntityCollision(world, caster, null, startPos, endPos, 0.74F, 3 + 2 * modifiers.get(WizardryItems.blast_upgrade),
					look.scale(1 + 1 * modifiers.get(WizardryItems.blast_upgrade)), SPLASH, true);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_GENERIC_SWIM, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_ICE_WRAITH_AMBIENT, 0.8F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_FORCE_ORB_HIT, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);

			return true;
		}

		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		double range = 3 + 2 * modifiers.get(WizardryItems.range_upgrade);
		Vec3d look = caster.getLookVec();
		double mult = modifiers.get(WizardryItems.range_upgrade) > 0 ? 0.6 + 0.2 * modifiers.get(WizardryItems.range_upgrade) : 0.6;
		if (world.isRemote) {
			//Spawn particles
			for(int i = 0; i < 40; i++) {
				double x1 = caster.posX + look.x + world.rand.nextFloat() / 10 - 0.05f;
				double y1 = caster.getEyeHeight() + caster.getEntityBoundingBox().minY - 0.4f + world.rand.nextFloat() / 10 - 0.05f;
				double z1 = caster.posZ + look.z + world.rand.nextFloat() / 10 - 0.05f;

				//Using the random function each time ensures a different number for every value, making the ability "feel" better.
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_BUBBLE).pos(x1, y1, z1).vel(look.x * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 25
								+ ArcaneUtils.getRandomNumberInRange(-10, 10) / 20F,
						look.y * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 25
								+ ArcaneUtils.getRandomNumberInRange(-10, 10) / 20F,
						look.z * mult * ArcaneUtils.getRandomNumberInRange(1, 100) / 25
								+ ArcaneUtils.getRandomNumberInRange(-10, 10) / 20F).time(8)
						.face(caster.rotationYaw, caster.rotationPitch).spawn(world);
			}
		}
		if (!world.isRemote) {
			Vec3d startPos = new Vec3d(caster.posX, caster.getEyeHeight() + caster.getEntityBoundingBox().minY, caster.posZ);
			Vec3d endPos = startPos.add(caster.getLookVec().scale(mult * range));
			ArcaneUtils.vortexEntityCollision(world, caster, null, startPos, endPos, 0.74F, 4 + 2 * modifiers.get(WizardryItems.blast_upgrade),
					look.scale(2 + 1 * modifiers.get(WizardryItems.blast_upgrade)), SPLASH, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_ICE_LANCE_SMASH, SoundCategory.HOSTILE, 0.8F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FORCE_ARROW_HIT, SoundCategory.HOSTILE, 1.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			return true;
		}

		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
