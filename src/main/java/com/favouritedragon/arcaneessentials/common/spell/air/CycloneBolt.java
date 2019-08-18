package com.favouritedragon.arcaneessentials.common.spell.air;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneBolt;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.SpellProperties;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneEnums.AIR;

public class CycloneBolt extends Spell {

	public static final String SPEED = "speed";
	public CycloneBolt() {
		//super(Tier.APPRENTICE, 10, Element.EARTH, "cyclone_bolt", SpellType.ATTACK, 20, EnumAction.BOW, false, ArcaneEssentials.MODID);
		super(ArcaneEssentials.MODID, "cyclone_bolt", EnumAction.BOW, false);
		addProperties(DAMAGE, SPEED);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote) {
			caster.swingArm(hand);
			float speed = 0.5F * modifiers.get(WizardryItems.range_upgrade) + getProperty(SPEED).floatValue();
			int knockBackStrength = 3 + (int) modifiers.get(WizardryItems.blast_upgrade);
			float damageMultiplier = 1 * modifiers.get(WizardryItems.blast_upgrade);
			world.spawnEntity(new EntityCycloneBolt(world, caster, speed, damageMultiplier, knockBackStrength));
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_FIREWORK_LAUNCH, 2.0F, 0.2F + world.rand.nextFloat());
			return true;
		}
		return false;
	}
}
