package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityWhirlpool;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class RenderWhirlpool extends Render<EntityWhirlpool> {

	public RenderWhirlpool(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityWhirlpool entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		ArcaneUtils.spawnSpinningVortex(entity.world, 180, 2, 0.25, 60, ParticleBuilder.Type.MAGIC_BUBBLE,
				new Vec3d(entity.posX, entity.posY, entity.posZ), new Vec3d(0.15, 0.05, 0.15), Vec3d.ZERO, 2, 0, 0, 0);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityWhirlpool entity) {
		return null;
	}
}
