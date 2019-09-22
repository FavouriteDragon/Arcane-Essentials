package com.favouritedragon.arcaneessentials.common.spell.storm;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityLightningVortex;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.SPEED;


public class LightningVortex extends Spell {

	public LightningVortex() {
		super(ArcaneEssentials.MODID, "lightning_vortex", EnumAction.BOW, false);
		addProperties(DAMAGE, RANGE, SPEED);
	}

	//TODO: Fix lightning vortex rendering
	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = getProperty(RANGE).doubleValue() + 1 * modifiers.get(WizardryItems.range_upgrade);
		float damage = getProperty(DAMAGE).floatValue() + 1 * modifiers.get(WizardryItems.blast_upgrade);
		Vec3d vel = caster.getLookVec().scale(range / 20 * getProperty(SPEED).floatValue());
		RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, range,true);
		if (result != null) {
			Vec3d pos = result.hitVec;
			WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_LIGHTNING_THUNDER, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_LIGHTNING_IMPACT, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			return world.spawnEntity(new EntityLightningVortex(world, pos.x, pos.y, pos.z, vel,
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
		} else {
			Vec3d pos = caster.getLookVec().scale(1.5).add(caster.getPositionVector());
			WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_LIGHTNING_THUNDER, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_LIGHTNING_IMPACT, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			return world.spawnEntity(new EntityLightningVortex(world, pos.x, pos.y, pos.z, vel,
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
		}
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		double range = getProperty(RANGE).doubleValue() + 1 * modifiers.get(WizardryItems.range_upgrade);
		float damage = getProperty(DAMAGE).floatValue() + 1 * modifiers.get(WizardryItems.blast_upgrade);
		Vec3d vel = caster.getLookVec().scale(range / 20 * getProperty(SPEED).floatValue());
		RayTraceResult result = RayTracer.standardBlockRayTrace(world, caster, range,true);
		if (result != null) {
			Vec3d pos = result.hitVec;
			world.spawnEntity(new EntityLightningVortex(world, pos.x, pos.y, pos.z, vel,
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_HAMMER_THROW, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			return true;
		} else {
			Vec3d pos = caster.getLookVec().scale(1.5).add(caster.getPositionVector());
			world.spawnEntity(new EntityLightningVortex(world, pos.x, pos.y, pos.z, vel,
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_HAMMER_THROW, SoundCategory.HOSTILE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F, true);
			return true;
		}
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}

