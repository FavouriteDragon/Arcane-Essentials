package com.favouritedragon.arcaneessentials.common.spell.earth;

import com.favouritedragon.arcaneessentials.common.entity.EntitySolarBeam;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SolarBeam extends ArcaneSpell {

	public SolarBeam() {
		super("solar_beam", EnumAction.BOW, false);
		addProperties(DAMAGE, RANGE, EFFECT_RADIUS, EFFECT_DURATION);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		Vec3d look = caster.getLookVec();
		look = look.scale(0.5).add(caster.getPositionVector());
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		if (!world.isRemote) {
			float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
			double size = getProperty(EFFECT_RADIUS).doubleValue() * modifiers.get(SpellModifiers.POTENCY);
			EntitySolarBeam beam = new EntitySolarBeam(world);
			beam.setPosition(look.x, look.y + caster.getEyeHeight(), look.z);
			beam.setOwner(caster);
			beam.setDamage(damage);
			beam.lifetime = getProperty(EFFECT_DURATION).intValue() * 20 * (int) modifiers.get(WizardryItems.duration_upgrade);
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
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.HOSTILE, 0.5F + world.rand.nextFloat() / 20, 2.0F + world.rand.nextFloat() / 10, true);
		if (!world.isRemote) {
			float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
			double size = getProperty(EFFECT_RADIUS).doubleValue() * modifiers.get(SpellModifiers.POTENCY);
			EntitySolarBeam beam = new EntitySolarBeam(world);
			beam.setPosition(look.x, look.y + caster.getEyeHeight(), look.z);
			beam.setOwner(caster);
			beam.setDamage(damage);
			beam.lifetime = getProperty(EFFECT_DURATION).intValue() * 20 * (int) modifiers.get(WizardryItems.duration_upgrade);
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
	public boolean canBeCastByNPCs() {
		return true;
	}


}
