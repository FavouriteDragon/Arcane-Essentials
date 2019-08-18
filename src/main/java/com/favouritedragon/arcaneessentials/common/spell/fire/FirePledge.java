package com.favouritedragon.arcaneessentials.common.spell.fire;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.entity.EntityFlamePillarSpawner;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FirePledge extends Spell {

	public FirePledge() {
		super(ArcaneEssentials.MODID, "fire_pledge", EnumAction.BOW, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (world.getBlockState(caster.getPosition().offset(EnumFacing.DOWN)).getBlock() != Blocks.AIR) {
			Vec3d look = caster.getLookVec();
			EntityFlamePillarSpawner spawner = new EntityFlamePillarSpawner(world, caster.posX + look.x * 0.25, caster.posY, caster.posZ + look.z * 0.25, caster,
					80 + 2 * (int) modifiers.get(WizardryItems.duration_upgrade), 5F * modifiers.get(WizardryItems.blast_upgrade));
			look.scale(2);
			spawner.motionX = look.x;
			spawner.motionY = 0;
			spawner.motionZ = look.z;
			world.spawnEntity(spawner);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (world.getBlockState(caster.getPosition().offset(EnumFacing.DOWN)).getBlock() != Blocks.AIR) {
			world.spawnEntity(new EntityFlamePillarSpawner(world, caster.posX, caster.posY, caster.posZ, caster,
					40 + 2 * (int) modifiers.get(WizardryItems.duration_upgrade), 1F * modifiers.get(WizardryItems.blast_upgrade)));
			return true;
		}
		return false;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
