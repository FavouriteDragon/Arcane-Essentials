package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class OceanBurst extends Spell {

	public OceanBurst() {
		super(Tier.APPRENTICE, 40, Element.EARTH, "ocean_burst", SpellType.ATTACK, 80, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = 3 + 2 * modifiers.get(WizardryItems.range_upgrade);
		Vec3d look = caster.getLookVec();
		//TODO: multiple hitoxes???
		if (world.isRemote) {
			//Spawn particles
			ArcaneUtils.spawnDirectionalVortex(world, caster, look.scale(0.8), 240, range, 0.05, 240 / 1.5, WizardryParticleType.MAGIC_BUBBLE, caster.posX, caster.posY + 1.2,
					caster.posZ, 0, 0, 0, 0, 0, 0, 0);
		}
		if (!world.isRemote) {
			Vec3d startPos = look.scale(0.8).add(caster.getPositionVector());
			Vec3d endPos = ArcaneUtils.getDirectionalVortexEndPos(caster, look.scale(0.8), 240, range, 240 / 1.5, caster.posX, caster.posY + 1.2, caster.posZ);
			startPos = startPos.add(0, 1.2, 0);
			ArcaneUtils.vortexEntityCollision(world, caster, null, startPos, endPos, 0.74F, 4 + 2 * modifiers.get(WizardryItems.blast_upgrade),
					look.scale(2 + 1 * modifiers.get(WizardryItems.blast_upgrade)), MagicDamage.DamageType.BLAST, true);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_GENERIC_SWIM, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_ICE, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_FORCE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);

			return true;
		}

		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		double range = 3 + 2 * modifiers.get(WizardryItems.range_upgrade);
		Vec3d look = caster.getLookVec();
		if (world.isRemote) {
			//Spawn particles
			ArcaneUtils.spawnDirectionalVortex(world, caster, look.scale(0.8), 240, range, 0.05,240 / 1.5, WizardryParticleType.MAGIC_BUBBLE, caster.posX, caster.posY + 1.2,
					caster.posZ, 0, 0, 0, 0, 0, 0, 0);
		}
		if (!world.isRemote) {
			Vec3d startPos = look.scale(0.8).add(caster.getPositionVector());
			Vec3d endPos = ArcaneUtils.getDirectionalVortexEndPos(caster, look.scale(0.8), 240, range, 240 / 1.5, caster.posX, caster.posY + 1.2, caster.posZ);
			startPos = startPos.add(0, 1.2, 0);
			ArcaneUtils.vortexEntityCollision(world, caster, null, startPos, endPos, 0.75F, 4 + 2 * modifiers.get(WizardryItems.blast_upgrade),
					look.scale(modifiers.get(WizardryItems.blast_upgrade)), MagicDamage.DamageType.BLAST, true);

			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_ICE, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_FORCE, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			return true;
		}

		return false;
	}
}
