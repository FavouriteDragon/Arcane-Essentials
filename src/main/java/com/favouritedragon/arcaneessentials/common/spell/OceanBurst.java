package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryParticleType;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class OceanBurst extends Spell {

	public OceanBurst() {
		super(Tier.APPRENTICE, 40, Element.EARTH, "ocean_burst", SpellType.ATTACK, 80, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = 4 + 2 * modifiers.get(WizardryItems.range_upgrade);
		if (!world.isRemote) {
			return true;
		}
		//Spawn par ticles
		Vec3d look = caster.getLookVec();
		double radius = 0.01;
		for (int angle = 0; angle < 180; angle++) {
			double x = radius * Math.cos(angle);
			double y = 180F/angle;
			double z = radius * Math.sin(angle);
			radius = 90F / angle;
			Wizardry.proxy.spawnParticle(WizardryParticleType.MAGIC_BUBBLE, world, x + caster.posX + look.x, y + caster.posY + 1 + look.y,
					z + caster.posZ + look.z, 0, 0, 0, 10);
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		double range = 4 + 2 * modifiers.get(WizardryItems.range_upgrade);
		if (!world.isRemote) {
			return true;
		}
		//Spawn par ticles
		for (int angle = 0; angle < 180; angle++) {
			double x = Math.cos(angle);
			double y = 180F/angle;
			double z = Math.sin(angle);
		}


		return false;
	}
}
