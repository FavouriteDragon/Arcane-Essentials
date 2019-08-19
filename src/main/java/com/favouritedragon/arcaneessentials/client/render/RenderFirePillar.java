package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityFlamePillar;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.ParticleBuilder;
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
		ArcaneUtils.spawnSpinningHelix(entity.world, entity.getParticleAmount(), entity.getVortexHeight(), entity.getRadius(), EnumParticleTypes.FLAME, entity.getPositionVector(),
				new Vec3d(0.03, 0.075, 0.03), Vec3d.ZERO);
		ArcaneUtils.spawnSpinningHelix(entity.world, entity.getParticleAmount() * 2/3, entity.getVortexHeight(), entity.getRadius(), ParticleBuilder.Type.MAGIC_FIRE, entity.getPositionVector(),
				1, Vec3d.ZERO, 20, 1.0F, 1.0F, 1.0F);
		if (entity.ticksExisted <= 1 || entity.ticksExisted % 6 == 0) {
			ParticleBuilder.create(ParticleBuilder.Type.BEAM).pos(entity.getPositionVector()).target(entity.getPositionVector()
					.add(0, entity.getVortexHeight(), 0)).clr(226, 68, 24).fade(250, 250, 0).scale(entity.getRadius() * 4)
					.time(6).spin(0, 1).spawn(entity.world);
		}
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityFlamePillar entity) {
		return null;
	}
}
