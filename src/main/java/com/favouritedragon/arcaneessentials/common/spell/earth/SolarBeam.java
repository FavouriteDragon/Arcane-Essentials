package com.favouritedragon.arcaneessentials.common.spell.earth;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntitySolarBeam;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SolarBeam extends Spell {

	public SolarBeam() {
		super(Tier.ADVANCED, 40, Element.EARTH, "solar_beam", SpellType.ATTACK, 140, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		Vec3d look = caster.getLookVec();
		look = look.scale(0.5).add(caster.getPositionVector());
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_EARTHQUAKE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_SHOCKWAVE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		if (!world.isRemote) {
			double damageMult = 1.0 * modifiers.get(WizardryItems.blast_upgrade);
			float range = 20 + 5 * modifiers.get(WizardryItems.range_upgrade);
			double size = 2.0 + 1.0 * modifiers.get(WizardryItems.blast_upgrade);
			EntitySolarBeam beam = new EntitySolarBeam(world, look.x, look.y + caster.getEyeHeight(), look.z, caster, 80, (float) damageMult);
			beam.setRadius((float) size / 2);
			beam.setRange(range);
			beam.rotationPitch = caster.rotationPitch;
			beam.rotationYaw = caster.rotationYaw;
			beam.motionX = beam.motionY = beam.motionZ = 0;
			beam.width = 0.1F;
			beam.height = 0.1F;
			world.spawnEntity(beam);
			caster.swingArm(hand);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		Vec3d look = caster.getLookVec();
		look = look.scale(0.5).add(caster.getPositionVector());
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_EARTHQUAKE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.SPELL_SHOCKWAVE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		if (!world.isRemote) {
			double damageMult = 1.0 * modifiers.get(WizardryItems.blast_upgrade);
			float range = 20 + 5 * modifiers.get(WizardryItems.range_upgrade);
			double size = 2.0 + 1.0 * modifiers.get(WizardryItems.blast_upgrade);
			EntitySolarBeam beam = new EntitySolarBeam(world, look.x, look.y, look.z, caster, 80, (float) damageMult);
			beam.setRadius((float) size / 2);
			beam.setRange(range);
			beam.rotationPitch = caster.rotationPitch;
			beam.rotationYaw = caster.rotationYaw;
			beam.motionX = beam.motionY = beam.motionZ = 0;
			beam.width = 0.1F;
			beam.height = 0.1F;
			world.spawnEntity(beam);
			caster.swingArm(hand);
			return true;
		}
		return super.cast(world, caster, hand, ticksInUse, target, modifiers);
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}


}
