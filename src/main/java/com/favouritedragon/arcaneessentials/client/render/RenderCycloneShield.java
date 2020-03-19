package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneShield;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
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
		ArcaneUtils.spawnSpinningVortex(entity.world, 60, entity.getRadius() + 1.75, entity.getRadius(), 60, ParticleBuilder.Type.FLASH, entity.getPositionVector(),
				new Vec3d(0.005, 0, 0.005), Vec3d.ZERO, 12, 0.85F, 0.85F, 0.85F, entity.getRadius() * 0.75F);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityCycloneShield entity) {
		return null;
	}
}
