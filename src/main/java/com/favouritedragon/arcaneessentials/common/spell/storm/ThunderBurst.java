package com.favouritedragon.arcaneessentials.common.spell.storm;

import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ThunderBurst extends ArcaneSpell {

	public ThunderBurst() {
		super("thunder_burst", EnumAction.BOW, false);
	}
	//TODO: Config for particles
	@Override
	public boolean cast(World world, EntityPlayer entityPlayer, EnumHand enumHand, int i, SpellModifiers spellModifiers) {
		if (!world.isRemote) {
			entityPlayer.swingArm(enumHand);
			WizardryUtilities.playSoundAtPlayer(entityPlayer, WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, 1.0f, 1.0f);
			WizardryUtilities.playSoundAtPlayer(entityPlayer, WizardrySounds.ENTITY_HAMMER_EXPLODE, 2.0f, 1.0f);
			EntityThunderBurst burst = new EntityThunderBurst(world);
			burst.setOwner(entityPlayer);
			burst.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
			burst.setCaster(entityPlayer);
			burst.lifetime = 30;
			world.spawnEntity(burst);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (!world.isRemote) {
			caster.swingArm(hand);
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, SoundCategory.HOSTILE, 1.0f, 1.0f, false);
			world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_HAMMER_EXPLODE, SoundCategory.HOSTILE, 2.0f, 1.0f, true);
			EntityThunderBurst burst = new EntityThunderBurst(world);
			burst.setOwner(caster);
			burst.setPosition(caster.posX, caster.posY, caster.posZ);
			burst.setCaster(caster);
			burst.lifetime = 30;
			world.spawnEntity(burst);
			return true;

		}
		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}


}
