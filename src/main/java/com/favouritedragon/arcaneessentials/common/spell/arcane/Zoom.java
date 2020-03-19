package com.favouritedragon.arcaneessentials.common.spell.arcane;

import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.data.IVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class Zoom extends ArcaneSpell {


	private static final IVariable<Integer> CHARGE_TIME = new IVariable.Variable<Integer>(Persistence.DIMENSION_CHANGE).withTicker(Zoom::update);

	public Zoom() {
		super("zoom", EnumAction.NONE, false);
		addProperties(DURATION);
		this.soundValues(1.0f, 1, 0);
	}

	private static int update(EntityPlayer player, Integer chargeTime) {

		if (chargeTime != null && chargeTime != -1) {
			player.motionX = player.motionY = player.motionZ = 0;
			if (chargeTime == 0) {
				//Ignore what intellij says, not using this will crash mc.
				if (player.getBedLocation() != null && (player.world.canSeeSky(player.getPosition()) && player.dimension == 0))
					for (int i = 0; i < 10; i++) {
						BlockPos pos = player.getBedLocation(player.getSpawnDimension());
						pos = pos.up(i);
						player.world.updateBlockTick(player.getBedLocation(), player.world.getBlockState(player.getBedLocation(player.getSpawnDimension())).getBlock(), 0, 1);
						if (ArcaneUtils.attemptGroundedTeleport(player, pos.getX(), pos.getY(), pos.getZ())) {
							if (player.world.isRemote)
								ParticleBuilder.create(ParticleBuilder.Type.BEAM).time(20).entity(player)
										.clr(0, 222, 255).target(player.getPositionVector().add(0, 30, 0)).scale(10).spawn(player.world);
							player.world.updateBlockTick(player.getBedLocation(), player.world.getBlockState(player.getBedLocation(player.getSpawnDimension())).getBlock(), 0, 1);
							WizardData.get(player).setVariable(CHARGE_TIME, null);
							chargeTime = -1;
							return chargeTime;
						}
					}
				else {
					player.changeDimension(player.getSpawnDimension(), (world, entity, yaw) -> {
						//This doesn't always work ;-;
						for (int i = 0; i < 10; i++) {
							BlockPos pos = player.getBedLocation(player.getSpawnDimension());
							pos = pos.up(i);
							player.world.updateBlockTick(player.getBedLocation(), player.world.getBlockState(player.getBedLocation(player.getSpawnDimension())).getBlock(), 0, 1);
							if (ArcaneUtils.attemptGroundedTeleport(player, pos.getX(), pos.getY(), pos.getZ())) {
								if (player.world.isRemote)
									ParticleBuilder.create(ParticleBuilder.Type.BEAM).time(20).entity(player)
											.clr(0, 222, 255).target(player.getPositionVector().add(0, 30, 0)).scale(10).spawn(player.world);
								player.world.updateBlockTick(player.getBedLocation(), player.world.getBlockState(player.getBedLocation(player.getSpawnDimension())).getBlock(), 0, 1);

							}
						}
					});
					//Make sure the player goes to their bed!
					for (int i = 0; i < 10; i++) {
						BlockPos pos = player.getBedLocation(player.getSpawnDimension());
						pos = pos.up(i);
						player.world.updateBlockTick(player.getBedLocation(), player.world.getBlockState(player.getBedLocation(player.getSpawnDimension())).getBlock(), 0, 1);
						if (ArcaneUtils.attemptGroundedTeleport(player, pos.getX(), pos.getY(), pos.getZ())) {
							if (player.world.isRemote)
								ParticleBuilder.create(ParticleBuilder.Type.BEAM).time(20).entity(player)
										.clr(0, 222, 255).target(player.getPositionVector().add(0, 30, 0)).scale(10).spawn(player.world);
							player.world.updateBlockTick(player.getBedLocation(), player.world.getBlockState(player.getBedLocation(player.getSpawnDimension())).getBlock(), 0, 1);

						}
					}
					WizardData.get(player).setVariable(CHARGE_TIME, null);
					chargeTime = -1;
					return chargeTime;
				}
				//player.world.updateBlockTick(player.getBedLocation(), player.world.getBlockState(player.getBedLocation(player.getSpawnDimension())).getBlock(), 0, 1);
				WizardData.get(player).setVariable(CHARGE_TIME, null);
				chargeTime = -1;
				return chargeTime;
			} else if (chargeTime > 0) chargeTime--;
			return chargeTime;
		} else return -1;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		WizardData.get(caster).setVariable(CHARGE_TIME, (int) (getProperty(DURATION).floatValue()
				- modifiers.get(WizardryItems.duration_upgrade)));
		if (world.isRemote)
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).time((getProperty(DURATION).intValue() - 1 - (int) modifiers.get(WizardryItems.duration_upgrade))).entity(caster)
					.clr(0, 222, 255).target(caster.getPositionVector().add(0, 30, 0)).scale(20).spawn(world);

		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean canBeCastByNPCs() {
		return false;
	}
}
