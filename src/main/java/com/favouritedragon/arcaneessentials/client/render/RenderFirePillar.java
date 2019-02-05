package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityFlamePillar;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.WizardryParticleType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class RenderFirePillar extends Render<EntityFlamePillar> {

	public RenderFirePillar(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityFlamePillar entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		ArcaneUtils.spawnSpinningHelix(entity.world, 120 * entity.getRadius(), 15, entity.getRadius(), EnumParticleTypes.FLAME, entity.getPositionVector(),
				new Vec3d(0.04, 0.1, 0.04), Vec3d.ZERO);
		ArcaneUtils.spawnSpinningHelix(entity.world, 80 * entity.getRadius(), 15, entity.getRadius(), WizardryParticleType.MAGIC_FIRE, entity.getPositionVector(),
				new Vec3d(0.04, 0.1, 0.04), Vec3d.ZERO, 10, 1.0F, 1.0F, 1.0F);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityFlamePillar entity) {
		return null;
	}
}
