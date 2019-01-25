package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.EntityLightningVortex;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundEvent;

public class LightningVortex extends Spell {

	public LightningVortex() {
		super(Tier.ADVANCED, 200, Element.LIGHTNING, "lightning_vortex", SpellType.ATTACK, 150, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = 6 + 1 * modifiers.get(WizardryItems.range_upgrade);
		float damage = 2 + 1 * modifiers.get(WizardryItems.blast_upgrade);
		RayTraceResult result = WizardryUtilities.rayTrace(range, world, caster, true);
		if (result != null) {
			world.spawnEntity(new EntityLightningVortex(world, result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(),
					caster, 100 + 10 * (int) modifiers.get(WizardryItems.duration_upgrade), damage));
			WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_LIGHTNING, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_LIGHTNING_THUNDER, 1F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_FORCE, 2.0F,
					world.rand.nextFloat() * 0.2F + 1.0F);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return super.cast(world, caster, hand, ticksInUse, target, modifiers);
	}
}
