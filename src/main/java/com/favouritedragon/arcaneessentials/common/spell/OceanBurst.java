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

		}
		if (world.isRemote) {
			final double dtheta = 360.0 / (2 * Math.PI * range) - 1;
			for (double theta = 0; theta < 360; theta += dtheta) {
				double rtheta = Math.toRadians(theta);
				Vec3d vec = new Vec3d(Math.cos(rtheta), 0, Math.sin(rtheta));
				Wizardry.proxy.spawnParticle(WizardryParticleType.MAGIC_BUBBLE, world, vec.x + caster.posX, vec.y + caster.posY + caster.getEyeHeight(),
						vec.z + caster.posZ, world.rand.nextDouble()/10, world.rand.nextDouble()/10, world.rand.nextDouble()/10, 10);

			}
			return true;

		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		double range = 4 + 2 * modifiers.get(WizardryItems.range_upgrade);
		if (!world.isRemote) {

		}
		if (world.isRemote) {
			final double dtheta = 360.0 / (2 * Math.PI * range) - 1;
			for (double theta = 0; theta < 360; theta += dtheta) {
				double rtheta = Math.toRadians(theta);
				Vec3d vec = new Vec3d(Math.cos(rtheta), 0, Math.sin(rtheta));
				Wizardry.proxy.spawnParticle(WizardryParticleType.SPARKLE, world, vec.x + caster.posX, vec.y + caster.posY + caster.getEyeHeight(),
						vec.z + caster.posZ, world.rand.nextDouble()/10, world.rand.nextDouble()/10, world.rand.nextDouble()/10, 10);

			}
			return true;

		}
		return false;
	}
}
