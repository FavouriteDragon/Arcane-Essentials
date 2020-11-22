package com.favouritedragon.arcaneessentials.common.potion;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.RegisterHandler;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.block.BlockStatue;
import electroblob.wizardry.entity.projectile.EntityMagicProjectile;
import electroblob.wizardry.potion.ISyncedPotion;
import electroblob.wizardry.potion.PotionMagicEffect;
import electroblob.wizardry.registry.WizardryBlocks;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.IElementalDamage;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.SPLASH;

@Mod.EventBusSubscriber(modid = ArcaneEssentials.MODID)
public class PotionFrostForm extends PotionMagicEffect implements ISyncedPotion {

	PotionFrostForm(boolean isBadEffect, int liquidColour) {
		super(isBadEffect, liquidColour, new ResourceLocation(ArcaneEssentials.MODID, "textures/gui/potion_icon_frost_form.png"));
		// This needs to be here because registerPotionAttributeModifier doesn't like it if the potion has no name yet.
		this.setPotionName("potion." + ArcaneEssentials.MODID + ":frost_form");
		setBeneficial();
	}

	@SubscribeEvent
	public static void handlePotion(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() != null) {
			EntityLivingBase entity = event.getEntityLiving();
			if (entity.isPotionActive(ArcanePotions.frostForm)) {
				entity.playSound(WizardrySounds.ENTITY_ICE_WRAITH_AMBIENT, 0.5F, 1.0F);
				entity.setInvisible(entity.isPotionActive(ArcanePotions.frostForm));
				if (entity.world.isRemote) {
					assert entity.getActivePotionEffect(ArcanePotions.frostForm) != null;
					if (Objects.requireNonNull(entity.getActivePotionEffect(ArcanePotions.frostForm)).getIsPotionDurationMax()
							|| entity.ticksExisted % 2 == 0) {
						ArcaneUtils.spawnSpinningHelix(entity.world, 120, 7F, 1F * RegisterHandler.frost_form.getProperty(Spell.EFFECT_RADIUS).floatValue(), ParticleBuilder.Type.SNOW,
								entity.getPositionVector(), - 0.025, new Vec3d(entity.motionX, entity.motionY, entity.motionZ),
								20 + ArcaneUtils.getRandomNumberInRange(1, 10), -1, -1, -1, 0.8f + entity.world.rand.nextFloat() / 2);

					}
					if (ArcaneUtils.getRandomNumberInRange(1, 20) <= 10) {
						ParticleBuilder.create(ParticleBuilder.Type.BUFF).entity(entity).time(20).
								clr(205, 254, 255).spawn(entity.world);
					}
				} else {
					float radius = RegisterHandler.frost_form.getProperty(Spell.EFFECT_RADIUS).floatValue();
					List<Entity> nearby = entity.world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox()
							.grow(radius, radius / 3, radius));
					if (!nearby.isEmpty()) {
						nearby.remove(entity);
						for (Entity hit : nearby) {
							if (AllyDesignationSystem.isValidTarget(entity, hit)) {
								if (hit.canBeCollidedWith() && hit.canBePushed() && hit != entity) {
									if (hit instanceof EntityArrow || (hit instanceof EntityThrowable && !(hit instanceof EntityMagicProjectile))) {
										hit.addVelocity(hit.motionX * -1.01, 0, hit.motionZ * -1.01);
									} else {
										if (entity.ticksExisted % 6 == 0) {
											if (hit instanceof EntityLivingBase) {
												PotionEffect effect = ((EntityLivingBase) hit).getActivePotionEffect(WizardryPotions.frost);
												int effectLevel = effect == null ? 0 : effect.getAmplifier();
												((EntityLivingBase) hit).addPotionEffect(new PotionEffect(WizardryPotions.frost,  RegisterHandler.frost_form.getProperty(Spell.EFFECT_DURATION).intValue() / 8,
														effectLevel + 1));
												if (effectLevel == 7 - RegisterHandler.frost_form.getProperty(Spell.EFFECT_STRENGTH).intValue()) {
													((BlockStatue) WizardryBlocks.ice_statue).convertToStatue((EntityLiving) hit, entity, RegisterHandler.frost_form.getProperty(Spell.EFFECT_DURATION).intValue());
												}

											}
											hit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(entity, MagicDamage.DamageType.FROST),
													RegisterHandler.frost_form.getProperty(Spell.DAMAGE).floatValue());
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}


	@SubscribeEvent
	public static void onHurtEvent(LivingHurtEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
			if (attacker.isPotionActive(ArcanePotions.frostForm)) {
				if (event.getSource() instanceof IElementalDamage) {
					if (((IElementalDamage) event.getSource()).getType() == MagicDamage.DamageType.FROST) {
						event.setAmount(event.getAmount() * 1.75F);
					}
				}
			}
		}
		if (event.getEntityLiving().isPotionActive(ArcanePotions.frostForm)) {
			if (event.getSource() == DamageSource.LAVA || event.getSource().isFireDamage() || (event.getSource() instanceof IElementalDamage && ((IElementalDamage) event.getSource()).getType() == MagicDamage.DamageType.FIRE)) {
				event.setAmount(event.getAmount() * 1.5F);
			} else if (event.getSource() instanceof IElementalDamage && (((IElementalDamage) event.getSource()).getType() == SPLASH ||
					((IElementalDamage) event.getSource()).getType() == MagicDamage.DamageType.FROST)) {
				event.setAmount(event.getAmount() * 0.05F);
			}
			else if (event.getSource() == DamageSource.LIGHTNING_BOLT || event.getSource() instanceof IElementalDamage && ((IElementalDamage) event.getSource()).getType() == MagicDamage.DamageType.SHOCK) {
				event.setAmount(event.getAmount() * 1.25F);
			}
			else
				event.setAmount(event.getAmount() * 0.6F);
		}
	}



	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public void performEffect(@Nonnull EntityLivingBase entityLivingBaseIn, int amplifier) {
		super.performEffect(entityLivingBaseIn, amplifier);
		entityLivingBaseIn.extinguish();
	}


}
