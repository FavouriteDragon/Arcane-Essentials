package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.util.WizardryParticleType;
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

				x = entity.ticksExisted * 1 * Math.cos(rphi) * Math.sin(rtheta);
				y = entity.ticksExisted * 1 * Math.sin(rphi) * Math.sin(rtheta);
				z = entity.ticksExisted * 1 * Math.cos(rtheta);

				Wizardry.proxy.spawnParticle(WizardryParticleType.SPARK, entity.world, x + entity.posX, y + entity.posY, z + entity.posZ, 0, 0, 0, 10);

			}
		}

	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityThunderBurst entity) {
		return null;
	}
}

