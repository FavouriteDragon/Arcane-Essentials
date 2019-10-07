package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.common.potion.ArcanePotions;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class FrostForm extends ArcaneSpell {

	public FrostForm() {
		super("frost_form", EnumAction.BOW, false);
		addProperties(DIRECT_EFFECT_DURATION, DAMAGE, EFFECT_RADIUS, EFFECT_STRENGTH, EFFECT_DURATION);
		soundValues(2.0F, 0.7F, 0.15F);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.BUFF).entity(caster).time(10).clr(205, 254, 255).spawn(world);
		}
		int duration = getProperty(DIRECT_EFFECT_DURATION).intValue();
		caster.addPotionEffect(new PotionEffect(ArcanePotions.frostForm, duration, 0, false, false));
		caster.playSound(WizardrySounds.MISC_FREEZE, 1.0F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10);
		caster.swingArm(hand);
		playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}
}
