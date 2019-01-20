package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.entity.EntityArc;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryParticleType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class RadiantBeam extends Spell {

	public RadiantBeam() {
		super(Tier.ADVANCED, 40, Element.HEALING, "radiant_beam", SpellType.ATTACK, 80, EnumAction.BOW, false, ArcaneEssentials.MODID);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		RayTraceResult rayTrace = WizardryUtilities.rayTrace(60 + 2 * modifiers.get(WizardryItems.range_upgrade), world, caster, true);
		if (rayTrace != null) {
			if (!world.isRemote) {
				Entity hit = rayTrace.entityHit;
				if (hit != null) {
					EntityArc arc = new EntityArc(world);
					arc.setEndpointCoords(caster.posX, caster.posY + 1.2, caster.posZ, hit.posX, hit.posY + hit.height / 2, hit.posZ);
					arc.lifetime = 1;
					world.spawnEntity(arc);
					if (MagicDamage.isEntityImmune(MagicDamage.DamageType.RADIANT, hit)) {
						caster.sendMessage(new TextComponentTranslation("spell.resist", hit.getName(),
								this.getNameForTranslationFormatted()));
					} else {
						WizardryUtilities.attackEntityWithoutKnockback(hit,
								MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.RADIANT),
								3.0f * modifiers.get(SpellModifiers.DAMAGE));
					}
				} else {
					BlockPos pos = rayTrace.getBlockPos();
					EntityArc arc = new EntityArc(world);
					arc.setEndpointCoords(caster.posX, caster.posY + 1.2, caster.posZ, pos.getX(), pos.getY(), pos.getZ());
					arc.lifetime = 1;
					world.spawnEntity(arc);
				}
			}
			if (world.isRemote) {
				Entity hit = rayTrace.entityHit;
				if (hit != null) {
					double dist = caster.getDistance(hit);
					for (double i = 0; i < 1; i += dist) {
						Vec3d startPos = new Vec3d(caster.posX, caster.posY + 1.2, caster.posZ);
						Vec3d endPos = new Vec3d(hit.posX, hit.posY + hit.getEyeHeight(), hit.posZ);
						Vec3d distance = endPos.subtract(startPos);
						distance = new Vec3d(distance.x * i, distance.y * i, distance.z * i);
						Wizardry.proxy.spawnParticle(WizardryParticleType.SPARKLE, world, distance.x + startPos.x, distance.y + startPos.y,
								distance.z + startPos.z, world.rand.nextDouble(), world.rand.nextDouble(), world.rand.nextDouble(),
								20 + world.rand.nextInt(12), 1.0f, 1.0f, 0.3f);

					}
				}
			}


			return true;
		}
		return false;
	}
}
