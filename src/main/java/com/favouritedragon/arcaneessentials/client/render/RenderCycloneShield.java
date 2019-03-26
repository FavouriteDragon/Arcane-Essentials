package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneShield;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class RenderCycloneShield extends Render<EntityCycloneShield> {

	public RenderCycloneShield(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityCycloneShield entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		ArcaneUtils.spawnSpinningVortex(entity.world, 60, entity.getRadius() + 1.75, entity.getRadius(), 60, EnumParticleTypes.CLOUD, entity.getPositionVector(),
				new Vec3d(0.05, 0, 0.05), Vec3d.ZERO);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityCycloneShield entity) {
		return null;
	}
}
