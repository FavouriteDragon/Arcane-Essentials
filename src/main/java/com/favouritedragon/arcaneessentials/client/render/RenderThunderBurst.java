package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderThunderBurst extends Render<EntityThunderBurst> {
	public RenderThunderBurst(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull EntityThunderBurst entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		assert entity.getCaster() != null;
		if (entity.ticksExisted <= 1) {
			for (double theta = 0; theta <= 180; theta += 1) {
				double dphi = 40 / Math.sin(Math.toRadians(theta));

				for (double phi = 0; phi < 360; phi += dphi) {
					double rphi = Math.toRadians(phi);
					double rtheta = Math.toRadians(theta);

					x = entity.ticksExisted  * Math.cos(rphi) * Math.sin(rtheta);
					y = entity.ticksExisted  * Math.sin(rphi) * Math.sin(rtheta);
					z = entity.ticksExisted  * Math.cos(rtheta);

					double px, py, pz;

					for(int i = 0; i < 4; i++) {
						px = x + entity.world.rand.nextDouble() - 0.5;
						py = y + entity.world.rand.nextDouble() - 0.5;
						pz = z + entity.world.rand.nextDouble() - 0.5;
						ParticleBuilder.create(ParticleBuilder.Type.SPARK).pos(px + entity.posX, py + entity.getEntityBoundingBox().minY,
								pz + entity.posZ).vel(entity.world.rand.nextDouble() * x, entity.world.rand.nextDouble() * y,
										entity.world.rand.nextDouble() * z).time(10).spawn(entity.world);
					}
				}
			}

		}
		RenderUtils.drawSphere(entity.ticksExisted * 0.7F, (float) Math.PI / 20, (float) Math.PI / 20, false, 138 / 255F, 1F, 1F,
				0.55F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();

	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityThunderBurst entity) {
		return null;
	}
}

