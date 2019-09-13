package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import com.favouritedragon.arcaneessentials.common.spell.SpellRay;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.common.util.ArcaneUtils.applyPlayerKnockback;

public class RadiantBeam extends SpellRay implements IArcaneSpell {

	public RadiantBeam() {
		super(ArcaneEssentials.MODID, "radiant_beam", false, EnumAction.BOW);
		addProperties(DAMAGE, EFFECT_STRENGTH, BURN_DURATION);
	}


	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		Vec3d knockBack = hit.subtract(origin).scale(.01 * getProperty(EFFECT_STRENGTH).floatValue());
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.RADIANT, target) && target instanceof EntityLivingBase && caster != null) {
			target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.RADIANT), damage);
			target.motionX += knockBack.x;
			target.motionY += knockBack.y;
			target.motionZ += knockBack.z;
			target.setFire(getProperty(BURN_DURATION).intValue() * (int) modifiers.get(WizardryItems.duration_upgrade));
			applyPlayerKnockback(target);
		}
		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance) {
		int amount = distance / getProperty(RANGE).floatValue() >= 1 ? 80 : (int) (distance / getProperty(RANGE).floatValue() * 80);
		for (int i = 0; i < amount; i++) {
			double x1 = caster.posX + direction.x * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
			double y1 = origin.y + direction.y * i / 2
					+ world.rand.nextFloat() / 5 - 0.1f;
			double z1 = caster.posZ + direction.z * i / 2 + world.rand.nextFloat() / 5 - 0.1f;
			ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(x1, y1, z1).vel(world.rand.nextGaussian() / 120,
					world.rand.nextGaussian() / 80, world.rand.nextGaussian() / 120).time(15).clr(1.0F, 1.0F, 0.3F).spawn(world);

		}
		ArcaneUtils.spawnDirectionalHelix(world, caster, caster.getLookVec(), (int) (amount * 2.25), distance, 0.5, ParticleBuilder.Type.SPARKLE, new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.4F, caster.posZ),
				new Vec3d(world.rand.nextGaussian() / 120, world.rand.nextGaussian() / 80, world.rand.nextGaussian() / 120), 15, 1.0F, 1.0F, 0.3F);
		//Due to some weird positioning shenanigans, the entity's position gets added twice, you need to subtract it once.
		ParticleBuilder.create(ParticleBuilder.Type.BEAM).entity(caster).pos(origin.subtract(caster.getPositionVector())).length(distance).clr(1.0F, 1.0F, 0.3F).fade(1.0F,
				1.0F, 1.0F).scale(4F).time(4).spawn(world);
	}

	@Override
	public boolean canBeCastByNPCs() {
		return true;
	}

	@Override
	public boolean isSwordCastable() {
		return true;
	}

	@Override
	public void playSound(World world, EntityLivingBase caster) {
		caster.playSound(WizardrySounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND, 1.0F + world.rand.nextFloat() / 10, 1);
		caster.playSound(WizardrySounds.ENTITY_FORCEFIELD_DEFLECT, 1.5F + world.rand.nextFloat() / 10, 0.9F + world.rand.nextFloat() / 10);
	}

	@Override
	public boolean isPiercing() {
		return true;
	}
}
