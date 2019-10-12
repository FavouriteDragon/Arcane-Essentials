package com.favouritedragon.arcaneessentials.common.spell.fire;

import com.favouritedragon.arcaneessentials.common.entity.EntityFlameSlash;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.SIZE;

public class FlameCleave extends ArcaneSpell {

	public FlameCleave() {
		super("flame_cleave", EnumAction.NONE, false);
		addProperties(DAMAGE, RANGE, BURN_DURATION, SIZE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return cast(world, caster, modifiers);
	}

	private boolean cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
		float size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		int burnDuration = getProperty(BURN_DURATION).intValue() * (int) modifiers.get(SpellModifiers.POTENCY);
		world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS,
				1.0F + world.rand.nextFloat(), 1.0F + world.rand.nextFloat(), false);
		EntityFlameSlash slash = new EntityFlameSlash(world);
		slash.setCaster(caster);
		slash.setSize(size);
		slash.setLifetime(40);
		slash.setKnockbackStrength(getProperty(EFFECT_STRENGTH).floatValue());
		slash.setDamage(damage);
		slash.setKnockbackStrength((int) size * 2);
		slash.setBurnDuration(burnDuration);
		slash.aim(caster, getProperty(RANGE).floatValue() / 50, 0F);
		if (!world.isRemote) {
			return world.spawnEntity(slash);
		} else return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return false;
	}

	@Override
	public boolean castRightClick() {
		return false;
	}

	@Override
	public boolean isWandCastable() {
		return false;
	}
}
