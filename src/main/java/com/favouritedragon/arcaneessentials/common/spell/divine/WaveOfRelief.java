package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

public class WaveOfRelief extends Spell {

	public WaveOfRelief() {
		super(ArcaneEssentials.MODID, "wave_of_relief", EnumAction.BOW, false);
		addProperties(EFFECT_RADIUS, HEALTH);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float healAmount = modifiers.get(SpellModifiers.POTENCY) * getProperty(HEALTH).floatValue();
		float radius = modifiers.get(WizardryItems.range_upgrade) * getProperty(EFFECT_RADIUS).floatValue();

		List<EntityLivingBase> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, caster.getEntityBoundingBox().grow(radius));
		if (!nearby.isEmpty()) {
				for (EntityLivingBase ally : nearby) {
					if (ally instanceof IEntityOwnable && AllyDesignationSystem.isOwnerAlly(caster, (IEntityOwnable) ally) ||
					AllyDesignationSystem.isAllied(caster, ally)) {
						if (!world.isRemote) {
							Collection<PotionEffect> potions = ally.getActivePotionEffects();
							for (PotionEffect effect : potions) {
								if (effect.getPotion().isBadEffect()) {
									ally.removeActivePotionEffect(effect.getPotion());
								}
							}
							ally.heal(healAmount);
						}
						else {
							ParticleBuilder.spawnHealParticles(world, ally);
					}
				}
			}
		}
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.SPHERE).clr(1.0F, 0.3F, 0.3F).entity(caster).pos(caster.getPositionVector()).time(30).scale(radius / 15).spawn(world);
		}
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		float healAmount = modifiers.get(SpellModifiers.POTENCY) * getProperty(HEALTH).floatValue();
		float radius = modifiers.get(WizardryItems.range_upgrade) * getProperty(EFFECT_RADIUS).floatValue();

		List<EntityLivingBase> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, caster.getEntityBoundingBox().grow(radius));
		if (!nearby.isEmpty()) {
			for (EntityLivingBase ally : nearby) {
				if (ally.getTeam() != null && ally.getTeam() == caster.getTeam()) {
					if (!world.isRemote) {
						Collection<PotionEffect> potions = ally.getActivePotionEffects();
						for (PotionEffect effect : potions) {
							if (effect.getPotion().isBadEffect()) {
								ally.removeActivePotionEffect(effect.getPotion());
							}
						}
						ally.heal(healAmount);
					}
					else {
						ParticleBuilder.spawnHealParticles(world, ally);
					}
				}
			}
		}
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.SPHERE).clr(1.0F, 0.3F, 0.3F).entity(caster).pos(caster.getPositionVector()).time(30).scale(radius / 15).spawn(world);
		}
		return true;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
