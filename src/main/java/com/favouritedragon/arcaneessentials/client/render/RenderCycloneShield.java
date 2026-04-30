package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneShield;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderCycloneShield extends Render<EntityCycloneShield> {

	public RenderCycloneShield(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityCycloneShield entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityCycloneShield entity) {
		return null;
	}
}
