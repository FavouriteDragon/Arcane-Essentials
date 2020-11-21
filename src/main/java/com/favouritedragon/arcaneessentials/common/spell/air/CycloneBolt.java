package com.favouritedragon.arcaneessentials.common.spell.air;

import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneBolt;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.LIFETIME;
import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.SPEED;


public class CycloneBolt extends ArcaneSpell {

	public CycloneBolt() {
		super("cyclone_bolt", EnumAction.BOW, false);
		addProperties(DAMAGE, SPEED, LIFETIME);
		//soundValues(2F, 1.0F, 0.15F);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
	//	playSound(world, caster, ticksInUse, -1, modifiers);
		return cast(world, caster, hand, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		//playSound(world, caster, ticksInUse, -1, modifiers);
		return cast(world, caster, hand, modifiers);
	}

	private boolean cast(World world, EntityLivingBase caster, EnumHand hand, SpellModifiers modifiers) {
		caster.swingArm(hand);
		float speed = 0.5F * modifiers.get(WizardryItems.range_upgrade) + getProperty(SPEED).floatValue();
		float knockBackStrength = getProperty(SPEED).floatValue() / 6F * modifiers.get(WizardryItems.blast_upgrade);
		float damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
		EntityCycloneBolt bolt = new EntityCycloneBolt(world);
		bolt.setCaster(caster);
		bolt.aim(caster, speed / 20, 0);
		bolt.setLifetime(getProperty(LIFETIME).intValue() * 20);
		bolt.setDamage(getProperty(DAMAGE).floatValue() * damageMultiplier);
		bolt.setKnockbackStrength(knockBackStrength);
		caster.playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 1.5F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10);
		if (!world.isRemote) {
			return world.spawnEntity(bolt);
		}
		return false;
	}

}
