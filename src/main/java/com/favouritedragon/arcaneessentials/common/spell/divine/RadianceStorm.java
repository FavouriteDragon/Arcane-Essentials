package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class RadianceStorm extends Spell implements IArcaneSpell {
	private static final String KNOCKBACK_MULT = "knockback_mult";
	private static final String LIGHT_BEAMS = "light_beams";
	private static final String EXPLOSION_DAMAGE = "explosion_damage";

	private static final float CENTRE_RADIUS_FRACTION = 0.5f;

	public RadianceStorm() {
		super(ArcaneEssentials.MODID, "radiance_storm", EnumAction.BOW, false);
		addProperties(DAMAGE, EFFECT_RADIUS, BLAST_RADIUS, BURN_DURATION, KNOCKBACK_MULT, LIGHT_BEAMS,
				EXPLOSION_DAMAGE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return doCasting(world, caster, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return doCasting(world, caster, modifiers);
	}

	private boolean doCasting(World world, EntityLivingBase caster, SpellModifiers modifiers){

		if(world.canBlockSeeSky(new BlockPos(caster))){

			double maxRadius = getProperty(EFFECT_RADIUS).doubleValue();

			for(int i = 0; i < getProperty(LIGHT_BEAMS).intValue(); i++){

				double radius = maxRadius * CENTRE_RADIUS_FRACTION + world.rand.nextDouble() * maxRadius
						* (1 - CENTRE_RADIUS_FRACTION) * modifiers.get(WizardryItems.blast_upgrade);
				float beamRadius = getProperty(BLAST_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade);
				float angle = world.rand.nextFloat() * (float)Math.PI * 2;

				double x = caster.posX + radius * MathHelper.cos(angle);
				double z = caster.posZ + radius * MathHelper.sin(angle);
				Integer y = WizardryUtilities.getNearestFloor(world, new BlockPos(x, caster.posY, z), (int)maxRadius);

				if(y != null){

					spawnRadiantBeam(world, caster, new Vec3d(x, y + 30, z), new Vec3d(x, y, z), beamRadius,
							getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY),
							new Vec3d(0.5, 0.75F, 0.5).scale(getProperty(KNOCKBACK_MULT).floatValue()),
							getProperty(BURN_DURATION).intValue() * (int) modifiers.get(WizardryItems.duration_upgrade));


				}
				handleSphericalExplosion(world, caster, caster.getPositionVector(), (float) maxRadius * 0.5F, getProperty(EXPLOSION_DAMAGE).floatValue() *
						modifiers.get(SpellModifiers.POTENCY), new Vec3d(4, 8, 4), getProperty(BURN_DURATION).intValue() *
						(int) modifiers.get(WizardryItems.duration_upgrade));
			}

			return true;
		}

		return false;
	}

	private void spawnRadiantBeam(World world, EntityLivingBase caster, Vec3d startPos, Vec3d endPos, float radius, float damage, Vec3d knockBack, int fireTime) {
		if (!world.isRemote) {
			ArcaneUtils.handlePiercingBeamCollision(world, caster, startPos, endPos, radius, null, true, MagicDamage.DamageType.RADIANT,
					damage, knockBack, true, fireTime, radius, 0, RayTracer.ignoreEntityFilter(caster));
		}
		if (world.isRemote) {
			ArcaneUtils.spawnSpinningHelix(world, 300, 30, radius - 0.1F, ParticleBuilder.Type.SPARKLE, endPos,
					1, Vec3d.ZERO, 20, 1.0F, 1.0F, 0.3F, 1);
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).pos(startPos).target(endPos).scale(radius * 6).clr(1.0F, 1.0F, 0.3F)
					.fade(1F, 1F, 1F).time(10).spawn(world);
		}
		world.playSound(endPos.x, endPos.y, endPos.z, WizardrySounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND, SoundCategory.PLAYERS, 1.5F, 1F, true);
		world.playSound(endPos.x, endPos.y, endPos.z, WizardrySounds.ENTITY_HAMMER_EXPLODE, SoundCategory.PLAYERS, 1.5F, 1F, true);
	}

	private void handleSphericalExplosion(World world, EntityLivingBase caster, Vec3d position, float radius, float damage, Vec3d knockBackScale, int fireTime) {
		if (!world.isRemote) {
			AxisAlignedBB hitBox = new AxisAlignedBB(position.x - radius, position.y - radius, position.z - radius, position.x + radius, position.y + radius, position.z + radius);
			List<Entity> hit = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
			if (!hit.isEmpty()) {
				for (Entity e : hit) {
					if (AllyDesignationSystem.isValidTarget(caster, e)) {
						if (e != caster) {
							if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.RADIANT, e)) {
								double dx = e.posX - caster.posX;
								double dy = e.posY - caster.posY;
								double dz = e.posZ - caster.posZ;
								// Normalises the velocity.
								double vectorLength = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
								dx /= vectorLength;
								dy /= vectorLength;
								dz /= vectorLength;

								e.motionX = knockBackScale.x * dx;
								e.motionY = knockBackScale.y * dy + 0.1;
								e.motionZ = knockBackScale.z * dz;
								e.setFire(fireTime);
								e.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.RADIANT), damage);
								ArcaneUtils.applyPlayerKnockback(e);
							}
						}
					}
				}
			}
		}
		if (world.isRemote){
			ParticleBuilder.create(ParticleBuilder.Type.SPHERE).pos(position).scale(radius).clr(1.0F, 1.0F, 0.3F)
					.spawn(world);
		}
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}
}
