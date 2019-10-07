package com.favouritedragon.arcaneessentials.common.spell.water;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityWaterBall;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.SIZE;

public class KaSploosh extends ArcaneSpell {

	public KaSploosh() {
		super("kasploosh", EnumAction.BOW, false);
		addProperties(DAMAGE, EFFECT_STRENGTH, RANGE, SIZE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return cast(world, caster, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return cast(world, caster, modifiers);
	}

	private boolean cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
		caster.playSound(SoundEvents.ENTITY_GENERIC_SWIM, 0.8F + world.rand.nextFloat() / 10, 0.6F + world.rand.nextFloat() / 10);
		caster.playSound(WizardrySounds.ENTITY_MAGIC_SLIME_ATTACK, 1.0F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10);
		float speed, damage, knockback, size;
		damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		speed = getProperty(RANGE).floatValue() / 13 * modifiers.get(WizardryItems.range_upgrade);
		knockback = getProperty(EFFECT_STRENGTH).floatValue() / 2 * modifiers.get(WizardryItems.blast_upgrade);
		if (!world.isRemote) {
			EntityWaterBall ball = new EntityWaterBall(world);
			ball.setCaster(caster);
			ball.setSize(size);
			ball.setSpawnWhirlPool(true);
			ball.setDamage(damage);
			ball.setKnockbackStrength((int) knockback);
			ball.aim(caster, speed, 0F);
			return world.spawnEntity(ball);
		}
		return false;
	}


	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
