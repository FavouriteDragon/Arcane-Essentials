package com.favouritedragon.arcaneessentials.common.spell.air;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneBolt;
import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneSpellProperties.LIFETIME;
import static com.favouritedragon.arcaneessentials.common.util.ArcaneSpellProperties.SPEED;

public class CycloneBolt extends Spell implements IArcaneSpell {

	public CycloneBolt() {
		super(ArcaneEssentials.MODID, "cyclone_bolt", EnumAction.BOW, false);
		addProperties(DAMAGE, SPEED, LIFETIME);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
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
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_FIREWORK_LAUNCH, 2.0F, 0.2F + world.rand.nextFloat());
			return true;
		}
		return false;
	}

	@Override
	public boolean isSwordCastable() {
		return true;
	}
}
