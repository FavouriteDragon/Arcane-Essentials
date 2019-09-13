package com.favouritedragon.arcaneessentials.common.spell.air;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneBolt;
import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.LIFETIME;
import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.SPEED;


public class CycloneBolt extends Spell implements IArcaneSpell {

	public CycloneBolt() {
		super(ArcaneEssentials.MODID, "cyclone_bolt", EnumAction.BOW, false);
		addProperties(DAMAGE, SPEED, LIFETIME);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return cast(world, caster, hand, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return cast(world, caster, hand, modifiers);
	}

	private boolean cast(World world, EntityLivingBase caster, EnumHand hand, SpellModifiers modifiers) {
		if (!world.isRemote) {
			caster.swingArm(hand);
			float speed = 0.5F * modifiers.get(WizardryItems.range_upgrade) + getProperty(SPEED).floatValue();
			int knockBackStrength = getProperty(SPEED).intValue() / 3 + (int) modifiers.get(WizardryItems.blast_upgrade);
			float damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
			EntityCycloneBolt bolt = new EntityCycloneBolt(world);
			bolt.setCaster(caster);
			bolt.aim(caster, speed / 25, 0);
			bolt.setLifetime(getProperty(LIFETIME).intValue() * 20);
			bolt.setDamage(getProperty(DAMAGE).floatValue() * damageMultiplier);
			bolt.setKnockbackStrength(knockBackStrength);
			world.spawnEntity(bolt);
			caster.playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 1.5F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10);
			return true;
		}
		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}

	@Override
	public boolean isSwordCastable() {
		return true;
	}
}
