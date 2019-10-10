package com.favouritedragon.arcaneessentials.common.spell.ice;

import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryBlocks;
import electroblob.wizardry.tileentity.TileEntityStatue;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static electroblob.wizardry.util.SpellModifiers.POTENCY;

public class Shatter extends ArcaneSpell {

	//Shatters frozen entities
	public Shatter() {
		super("shatter", EnumAction.BOW, false);
		addProperties(DAMAGE, EFFECT_STRENGTH, EFFECT_RADIUS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		double range = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(POTENCY);
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(POTENCY);
		float particleSize = getProperty(EFFECT_STRENGTH).floatValue();


		for (int i = 0; i <= range; i++) {
			for (int angle = 0; angle < 360; angle++) {
				double radians = Math.toRadians(angle);
				double x = i * Math.cos(radians);
				double z = i * Math.sin(radians);
				double y = caster.getEntityBoundingBox().minY - range / 2;
				for (int j = 0; j <= range; j++) {
					y += j;
					BlockPos pos = new BlockPos(x + caster.posX, y, z + caster.posZ);
					if (world.getBlockState(pos).getBlock() == WizardryBlocks.ice_statue) {
						if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityStatue) {
							TileEntityStatue statue = (TileEntityStatue) world.getTileEntity(pos);
							if (statue != null && statue.creature != null) {
								EntityLivingBase hit = statue.creature;
								int height = hit.height < 1 ? 1 : (int) hit.height;
								for (int h = 0; h < height; h++) {
									Block block = world.getBlockState(pos.up(h)).getBlock();
									block.breakBlock(world, pos, world.getBlockState(pos.up(h)));
									statue.setLifetime(0);
									if (world.getTileEntity(pos.up(h)) != null && world.getTileEntity(pos.up(h)) instanceof TileEntityStatue)
										((TileEntityStatue) world.getTileEntity(pos.up(h))).setLifetime(0);
								}
								for (int p = 0; p < 8; p++) {
									if (world.isRemote)
										ParticleBuilder.create(ParticleBuilder.Type.ICE, world.rand, hit.posX, hit.getEyeHeight() + hit.getEntityBoundingBox().minY,
												hit.posZ, hit.getEntityBoundingBox().maxX - hit.getEntityBoundingBox().minX, true).time(20)
												.scale(particleSize).spawn(world);
								}
								if (AllyDesignationSystem.isValidTarget(caster, hit)) {
									if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, hit)) {
										if (!world.isRemote)
											hit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), damage);
									}
								}
							}
						}
					}
				}
			}
		}
		caster.swingArm(hand);

		return true;
	}
}
