package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntitySolarBeam;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;

import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderSolarBeam extends Render<EntitySolarBeam> {

	public RenderSolarBeam(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull EntitySolarBeam entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if (entity.ticksExisted == 2 || entity.ticksExisted % 20 == 0) {
			ArcaneUtils.spawnSpinningDirectionalHelix(entity.world, entity, Vec3d.ZERO, Vec3d.ZERO, 270, entity.getRange(),
					entity.getRadius() * 0.75f, ParticleBuilder.Type.LEAF, entity.getPositionVector(),
					new Vec3d(0.05, 0.0075, 0.05), 20, -1, -1, -1);
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).entity(entity).length(entity.getRange()).scale(entity.getRadius() * 3)
					.clr(0.1F, 1.0F, 0.4F).time(20).spawn(entity.world);
		}
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntitySolarBeam entity) {
		return null;
	}
}
