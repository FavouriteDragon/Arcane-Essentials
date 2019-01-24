package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.ArcaneUtils;
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
		double range = 3 + 2 * modifiers.get(WizardryItems.range_upgrade);
		if (!world.isRemote) {
			return true;
		}
		//Spawn particles
		Vec3d look = caster.getLookVec();
		double random = world.rand.nextDouble() / 10;
		ArcaneUtils.spawnDirectionalVortex(world, caster, look, 240, range, 240 / 1.5, WizardryParticleType.MAGIC_BUBBLE, caster.posX, caster.posY,
				caster.posZ, random, random, random, 10);
		/*	for (int angle = 0; angle < 180; angle++) {
				double radius = angle / 120F;
				double x = radius * Math.cos(angle);
				double y = angle / (180 / range);
				double z = radius * Math.sin(angle);
				Vec3d pos = new Vec3d(x, y, z);
				pos = ArcaneUtils.rotateAroundAxisX(pos, caster.rotationPitch + 90);
				pos = ArcaneUtils.rotateAroundAxisY(pos, caster.rotationYaw);
				Wizardry.proxy.spawnParticle(WizardryParticleType.MAGIC_BUBBLE, world, pos.x + caster.posX + look.x, pos.y + caster.posY + caster.getEyeHeight() + look.y,
						pos.z + caster.posZ + look.z, 0, 0, 0, 10);
			}**/

		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		double range = 4 + 2 * modifiers.get(WizardryItems.range_upgrade);
		if (!world.isRemote) {
			return true;
		}
		//Spawn particles
		for (int angle = 0; angle < 180; angle++) {
			double x = Math.cos(angle);
			double y = 180F / angle;
			double z = Math.sin(angle);
		}


		return false;
	}
}
