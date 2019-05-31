package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RadiantBeam extends Spell {

	public RadiantBeam() {
		super(Tier.ADVANCED, 40, Element.HEALING, "radiant_beam", SpellType.ATTACK, 80, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

		Vec3d look = caster.getLookVec();
		float damage = 4.0f + 1 * modifiers.get(SpellModifiers.DAMAGE);
		float range = 60 + 2 * modifiers.get(WizardryItems.range_upgrade);

		if (!world.isRemote) {
			Vec3d startPos = new Vec3d(caster.posX, WizardryUtilities.getPlayerEyesPos(caster) - 0.4F, caster.posZ);
			Vec3d endPos = caster.getLookVec().scale(range).add(startPos);
			Vec3d knockBack = new Vec3d(6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade));
			ArcaneUtils.handlePiercingBeamCollision(world, caster, startPos, endPos, 0.5F, null, true,
					MagicDamage.DamageType.RADIANT, damage, knockBack, true, 10, 0.5F, 0);
		}

		if (world.isRemote) {
			for (int i = 0; i < 80; i++) {
				boolean b = world.rand.nextBoolean();
				double x1 = caster.posX + look.x * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				double y1 = WizardryUtilities.getPlayerEyesPos(caster) - 0.4f + look.y * i / 2
						+ world.rand.nextFloat() / 5 - 0.1f;
				double z1 = caster.posZ + look.z * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				Wizardry.proxy.spawnParticle(WizardryParticleType.SPARKLE, world, x1, y1, z1,
						b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80,
						world.rand.nextDouble() / 40,
						b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80, 30, 1.0f, 1.0f, 0.3f);
				Wizardry.proxy.spawnParticle(WizardryParticleType.SPARKLE, world, x1, y1, z1,
						b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 60,
						world.rand.nextDouble() / 40,
						b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 60, 30, 1.0f, 1.0f, 0.3f);

			}
			ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), 180, range, 0.5, WizardryParticleType.SPARKLE, new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.4F, caster.posZ),
					new Vec3d(world.rand.nextDouble() / 80, world.rand.nextDouble() / 40, world.rand.nextDouble() / 80), 30, 1.0F, 1.0F, 0.3F);

		}

		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_HEAL, 1.5F, 1);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_SHOCKWAVE, 0.5F, 1.0f);

		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target,
						SpellModifiers modifiers) {

		Vec3d look = caster.getLookVec();
		float damage = 4.0f + 1 * modifiers.get(SpellModifiers.DAMAGE);
		float range = 60 + 2 * modifiers.get(WizardryItems.range_upgrade);

		if (!world.isRemote) {
			Vec3d startPos = new Vec3d(caster.posX, caster.getEntityBoundingBox().minY + caster.getEyeHeight() - 0.4F, caster.posZ);
			Vec3d endPos = caster.getLookVec().scale(range).add(startPos);
			Vec3d knockBack = new Vec3d(6 * modifiers.get(WizardryItems.blast_upgrade),  6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade));
			ArcaneUtils.handlePiercingBeamCollision(world, caster, startPos, endPos, 0.5F, null, true,
					MagicDamage.DamageType.RADIANT, damage, knockBack, true, 10, 0.25F, 0);
		}


		if (world.isRemote) {
			for (int i = 0; i < 60; i++) {
				double x1 = caster.posX + look.x * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				double y1 = caster.posY + caster.getEyeHeight() - 0.4f + look.y * i / 2 + world.rand.nextFloat() / 5
						- 0.1f;
				double z1 = caster.posZ + look.z * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
				Wizardry.proxy.spawnParticle(WizardryParticleType.SPARKLE_ROTATING, world, x1, y1, z1,
						world.rand.nextDouble() / 80,
						world.rand.nextDouble() / 40,
						world.rand.nextDouble() / 80, 30, 1.0f, 1.0f, 0.3f);
				Wizardry.proxy.spawnParticle(WizardryParticleType.SPARKLE, world, x1, y1, z1,
						world.rand.nextDouble() / 80,
						world.rand.nextDouble() / 40,
						world.rand.nextDouble() / 80, 30, 1.0f, 1.0f, 0.3f);
			}
			ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), 180, range, 0.5, WizardryParticleType.SPARKLE, new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.4F, caster.posZ),
					new Vec3d(world.rand.nextDouble() / 80, world.rand.nextDouble() / 40, world.rand.nextDouble() / 80), 30, 1.0F, 1.0F, 0.3F);

		}

		caster.playSound(WizardrySounds.SPELL_HEAL, 1.5F, 1);
		caster.playSound(WizardrySounds.SPELL_SHOCKWAVE, 0.5F, 1.0f);


		return true;
	}


	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
