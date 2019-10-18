package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.entity.projectile.EntityMagicProjectile;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.List;

public class DragonRoar extends ArcaneSpell {

	public DragonRoar() {
		super("dragon_roar", EnumAction.BOW, false);
		addProperties(EFFECT_DURATION, EFFECT_STRENGTH, EFFECT_RADIUS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		caster.swingArm(hand);
		return cast(world, caster, modifiers);
	}

	private boolean cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
		double radius = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		int duration = (int) (getProperty(EFFECT_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade));
		int amplifier = (int) (getProperty(EFFECT_STRENGTH).floatValue() * modifiers.get(SpellModifiers.POTENCY));
		if (world.isRemote) {
			for (int angle = 0; angle < 360; angle++) {
				double radians = Math.toRadians(angle);
				double x = Math.cos(radians);
				double z = Math.sin(radians);
				//On the client, the posY is the eye position.
				ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(x + caster.posX, caster.posY + 0.5, z + caster.posZ).vel(x * radius / 10,
						0, z * radius / 10).time(6).clr(155, 6, 185).spawn(world);
			}
			ParticleBuilder.create(ParticleBuilder.Type.SPHERE).entity(caster).clr(155, 6, 185).time(6).scale((float) radius).spawn(world);
		}
		List<Entity> nearby = ArcaneUtils.getEntitiesWithinRadius(radius, caster.posX, caster.posY, caster.posZ, world);
		nearby.remove(caster);
		for (Entity target : nearby) {
			if (AllyDesignationSystem.isValidTarget(caster, target) && target != caster && target.canBeCollidedWith()) {
				if (target instanceof EntityLivingBase) {
					((EntityLivingBase) target).addPotionEffect(new PotionEffect(WizardryPotions.paralysis, duration, amplifier));
				}
				if (!world.isRemote) {
					if (target instanceof EntityArrow) {
						target.addVelocity(target.motionX * -1.2, target.motionY * -1.2, target.motionZ * -1.2);
					} else if (target instanceof EntityThrowable && !(target instanceof EntityMagicProjectile)) {
						target.addVelocity(target.motionX * -3, target.motionY * -3, target.motionZ * -3);
					}
				}
			}
		}
		caster.playSound(SoundEvents.ENTITY_ENDERDRAGON_GROWL, 1.5F + world.rand.nextFloat() / 10, 0.75F + world.rand.nextFloat() / 10);
		return true;
	}
}
