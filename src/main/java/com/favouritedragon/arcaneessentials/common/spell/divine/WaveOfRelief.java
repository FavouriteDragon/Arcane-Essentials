package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

public class WaveOfRelief extends ArcaneSpell {

	public WaveOfRelief() {
		super("wave_of_relief", EnumAction.BOW, false);
		addProperties(EFFECT_RADIUS, HEALTH);
	}

	//TODO: Sounds

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return cast(world, caster, hand, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return cast(world, caster, hand, modifiers);
	}

	private boolean cast(World world, EntityLivingBase caster, EnumHand hand, SpellModifiers modifiers) {
		float healAmount = modifiers.get(SpellModifiers.POTENCY) * getProperty(HEALTH).floatValue();
		float radius = modifiers.get(WizardryItems.range_upgrade) * getProperty(EFFECT_RADIUS).floatValue();

		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.SPHERE).clr(1.0F, 1.0F, 0.3F).pos(caster.getPositionVector()).time(7).scale(radius / 2).spawn(world);
		}

		List<EntityLivingBase> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, caster.getEntityBoundingBox().grow(radius));
		if (!nearby.isEmpty()) {
			for (EntityLivingBase ally : nearby) {
				if (ally != null && (ally.getTeam() != null && ally.getTeam() == caster.getTeam() || caster == ally)) {
					List<PotionEffect> potions = ally.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getPotion().isBadEffect()).collect(Collectors.toList());
					if (!potions.isEmpty()) {
						for (PotionEffect effect : potions) {
							if (effect.getPotion().isBadEffect()) {
								ally.removePotionEffect(effect.getPotion());
							}
						}
					}
					ally.heal(healAmount);
					if (ally instanceof EntityPlayer) {
						((EntityPlayer) ally).getFoodStats().setFoodLevel(((EntityPlayer) ally).getFoodStats().getFoodLevel() + 4);
					}
					if (world.isRemote){
						ParticleBuilder.spawnHealParticles(world, ally);
					}
				}
			}
		}

		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_HEAL_AURA_AMBIENT, SoundCategory.PLAYERS, 1.5F, 0.9F + world.rand.nextFloat() / 10, false);
		world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT, SoundCategory.PLAYERS, 0.675F + world.rand.nextFloat() / 10, 0.875F + world.rand.nextFloat() / 10, false);

		caster.swingArm(hand);
		return true;
	}



}
