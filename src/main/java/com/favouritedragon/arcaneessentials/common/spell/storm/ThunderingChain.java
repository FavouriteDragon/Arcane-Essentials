package com.favouritedragon.arcaneessentials.common.spell.storm;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityLightningSpawner;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ThunderingChain extends Spell {

	public ThunderingChain() {
		super(ArcaneEssentials.MODID, "thundering_chain", EnumAction.BOW, false);
		addProperties(DURATION, DAMAGE, EFFECT_DURATION);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		int lifetime = getProperty(DURATION).intValue() + (int) (10 * modifiers.get(WizardryItems.duration_upgrade));
		int burnTime = getProperty(EFFECT_DURATION).intValue() + (int) (2 * modifiers.get(WizardryItems.duration_upgrade));

		Vec3d look = caster.getLookVec();
		EntityLightningSpawner spawner = new EntityLightningSpawner(world);
		spawner.damageMultiplier = damage;
		spawner.setBurnTime(burnTime);
		spawner.setLifetime(lifetime);
		spawner.setOwner(caster);
		spawner.setPosition(caster.posX + look.x * 0.25, caster.getEntityBoundingBox().minY, caster.posZ + look.z * 0.25);
		look = look.scale(2);
		spawner.motionX = look.x;
		spawner.motionY = 0;
		spawner.motionZ = look.z;
		if (!world.isRemote) {
			return world.spawnEntity(spawner);
		}
		return false;

	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
