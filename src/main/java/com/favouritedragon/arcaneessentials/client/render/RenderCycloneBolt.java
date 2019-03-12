package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneBolt;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class RenderCycloneBolt extends Render<EntityCycloneBolt> {

	public RenderCycloneBolt(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityCycloneBolt entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		ArcaneUtils.spawnSpinningDirectionalVortex(entity.world, entity.getShootingEntity(), Vec3d.ZERO, 24, 1, 0, 72,
				EnumParticleTypes.EXPLOSION_NORMAL, entity.getPositionVector(), new Vec3d(0.4, 0.1, 0.4), new Vec3d(entity.motionX, entity.motionY, entity.motionZ));
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityCycloneBolt entity) {
		return null;
	}
}
