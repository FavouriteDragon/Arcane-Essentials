package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class OceanBurst extends Spell {

	public OceanBurst() {
		super(Tier.APPRENTICE, 40, Element.EARTH, "ocean_burst", SpellType.ATTACK, 80, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = 3 + 2 * modifiers.get(WizardryItems.range_upgrade);
		//Maybe use a raytrace to determine knockback?
		Vec3d look = caster.getLookVec();
		//Spawn particles
		ArcaneUtils.spawnDirectionalVortex(world, caster, look.scale(0.8), 240, range, 240 / 1.5, WizardryParticleType.MAGIC_BUBBLE, caster.posX, caster.posY + 1.2,
				caster.posZ, 0, 0, 0, 8);

		if (!world.isRemote) {
			ArcaneUtils.vortexEntityRaytrace(world, caster, null, 0.8 + range, 1.5F, 4 + 2 * modifiers.get(WizardryItems.blast_upgrade),
					look.scale(modifiers.get(WizardryItems.blast_upgrade)), MagicDamage.DamageType.BLAST, true);
			AxisAlignedBB hitBox = new AxisAlignedBB(caster.posX, caster.posY + 1.2, caster.posZ, caster.posX + look.x, caster.posY + 1.2 + look.y, caster.posZ
			+ look.z);
			List<EntityLivingBase> hit = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
			if (!hit.isEmpty()) {
				for (EntityLivingBase e : hit) {
					if (e != caster)  {
						e.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.MAGIC),4 + 2 * modifiers.get(WizardryItems.blast_upgrade));
						Vec3d knockback = caster.getLookVec().scale(range/4);
						e.motionX += knockback.x;
						e.motionY += knockback.y + 0.2;
						e.motionZ += knockback.z;
						ArcaneUtils.applyPlayerKnockback(e);
					}
				}
			}
			return true;
		}
		WizardryUtilities.playSoundAtPlayer(caster, SoundEvents.ENTITY_GENERIC_SWIM, 1.0F,
				world.rand.nextFloat() * 0.2F + 1.0F);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_ICE, 0.5F,
				world.rand.nextFloat() * 0.2F + 1.0F);
		WizardryUtilities.playSoundAtPlayer(caster, WizardrySounds.SPELL_FORCE, 1.0F,
				world.rand.nextFloat() * 0.2F + 1.0F);
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		double range = 3 + 2 * modifiers.get(WizardryItems.range_upgrade);
		if (!world.isRemote) {
			return true;
		}
		//Spawn particles
		Vec3d look = caster.getLookVec();
		ArcaneUtils.spawnDirectionalVortex(world, caster, look.scale(0.8), 240, range, 240 / 1.5, WizardryParticleType.MAGIC_BUBBLE, caster.posX, caster.posY + 1.2,
				caster.posZ, 0, 0, 0, 8);
		return false;
	}
}
