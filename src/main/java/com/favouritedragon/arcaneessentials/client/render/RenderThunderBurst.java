package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderThunderBurst extends Render<EntityThunderBurst> {
	public RenderThunderBurst(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityThunderBurst entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		for (double theta = 0; theta <= 180; theta += 1) {
			double dphi = 20 / Math.sin(Math.toRadians(theta));

			for (double phi = 0; phi < 360; phi += dphi) {
				double rphi = Math.toRadians(phi);
				double rtheta = Math.toRadians(theta);

				x = entity.ticksExisted * 0.4 * Math.cos(rphi) * Math.sin(rtheta);
				y = entity.ticksExisted * 0.4 * Math.sin(rphi) * Math.sin(rtheta);
				z = entity.ticksExisted * 0.4 * Math.cos(rtheta);

				ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING).pos(entity.getPositionVector()).spin(entity.ticksExisted * 0.4, 0.8)
						.vel(x, y, z).spawn(entity.world);

			}
		}

	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityThunderBurst entity) {
		return null;
	}
}

