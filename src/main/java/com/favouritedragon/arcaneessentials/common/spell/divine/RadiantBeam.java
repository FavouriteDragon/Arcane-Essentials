package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
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
		super(ArcaneEssentials.MODID, "radiant_beam", EnumAction.BOW, false);
		addProperties(DAMAGE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

		Vec3d look = caster.getLookVec();
		float damage = getProperty(DAMAGE).floatValue() + modifiers.get(WizardryItems.blast_upgrade);

		float range = 60 + 2 * modifiers.get(WizardryItems.range_upgrade);
		double eyepos = caster.getEntityBoundingBox().minY + caster.getEyeHeight() - 0.4F;
		Vec3d startPos = new Vec3d(caster.posX, eyepos, caster.posZ);
		Vec3d endPos = caster.getLookVec().scale(range).add(startPos);

		if (!world.isRemote) {
			Vec3d knockBack = new Vec3d(6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade));
			ArcaneUtils.handlePiercingBeamCollision(world, caster, startPos, endPos, 0.5F, null, true,
					MagicDamage.DamageType.RADIANT, damage, knockBack, true, 10, 0.5F, 0);
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
					new Vec3d(world.rand.nextDouble() / 80, world.rand.nextDouble() / 40, world.rand.nextDouble() / 80), 15, 1.0F, 1.0F, 0.3F);
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).pos(startPos).target(endPos).clr(1.0F, 1.0F, 0.3F).fade(1.0F,
					1.0F, 1.0F).scale(4F).time(4).spawn(world);
		}

		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND, 1.5F, 1);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT, 0.5F, 1.0f);

		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target,
						SpellModifiers modifiers) {

		Vec3d look = caster.getLookVec();
		float damage = getProperty(DAMAGE).floatValue() + modifiers.get(WizardryItems.blast_upgrade);

		float range = 60 + 2 * modifiers.get(WizardryItems.range_upgrade);
		double eyepos = caster.getEntityBoundingBox().minY + caster.getEyeHeight() - 0.4F;

		if (!world.isRemote) {
			Vec3d startPos = new Vec3d(caster.posX, eyepos, caster.posZ);
			Vec3d endPos = caster.getLookVec().scale(range).add(startPos);
			Vec3d knockBack = new Vec3d(6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade), 6 * modifiers.get(WizardryItems.blast_upgrade));
			ArcaneUtils.handlePiercingBeamCollision(world, caster, startPos, endPos, 0.5F, null, true,
					MagicDamage.DamageType.RADIANT, damage, knockBack, true, 10, 0.5F, 0);
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
						b ? world.rand.nextDouble() / 80 : -world.rand.nextDouble() / 80).time(30).clr(1.0F, 1.0F, 0.3F).spawn(world);

			}
			ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), 180, range, 0.5, ParticleBuilder.Type.SPARKLE, new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.4F, caster.posZ),
					new Vec3d(world.rand.nextDouble() / 80, world.rand.nextDouble() / 40, world.rand.nextDouble() / 80), 30, 1.0F, 1.0F, 0.3F);

		}


		caster.playSound(WizardrySounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND, 1.5F, 1);
		caster.playSound(WizardrySounds.ENTITY_FORCEFIELD_DEFLECT, 0.5F, 1.0f);


		return true;
	}


	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
