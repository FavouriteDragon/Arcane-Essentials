package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityCycloneBolt;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.client.render.RenderUtils.drawSphere;

public class RenderCycloneBolt extends Render<EntityCycloneBolt> {

	private static final float EXPANSION_TIME = 3;

	public RenderCycloneBolt(RenderManager renderManager) {
		super(renderManager);
	}

	//Copied from the forcefiled class
	@Override
	public void doRender(@Nonnull EntityCycloneBolt entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

		GlStateManager.translate(x, y + entity.height / 1.5, z);

		float latStep = (float) Math.PI / 5;
		float longStep = (float) Math.PI / 5;

		float pulse = MathHelper.sin((entity.ticksExisted + partialTicks) / 10f);

		float r = 1, g = 0.95F + 0.05f * pulse, b = 1;

		float radius = entity.width / 3;
		float a = 0.5f;

		if (entity.ticksExisted > entity.getLifetime() - EXPANSION_TIME) {
			radius *= 1 + 0.2f * (entity.ticksExisted + partialTicks - (entity.getLifetime() - EXPANSION_TIME)) / EXPANSION_TIME;
			a *= Math.max(0, 1 - (entity.ticksExisted + partialTicks - (entity.getLifetime() - EXPANSION_TIME)) / EXPANSION_TIME);
		} else if (entity.ticksExisted < EXPANSION_TIME) {
			radius *= 1 - (EXPANSION_TIME - entity.ticksExisted - partialTicks) / EXPANSION_TIME;
			a *= 1 - (EXPANSION_TIME - entity.ticksExisted - partialTicks) / EXPANSION_TIME;
		}

		// Draw the inside first
		drawSphere(radius - 0.1f - 0.025f * pulse, latStep, longStep, true, r, g, b, a * 0.7F);
		drawSphere(radius - 0.1f - 0.025f * pulse, latStep, longStep, false, 1, 1, 1, a * 0.7F);
		drawSphere(radius, latStep, longStep, false, r, g, b, 0.7f * a);

		//Particles
		ArcaneUtils.spawnSpinningDirectionalVortex(entity.world, entity.getCaster(), Vec3d.ZERO, 15, 1, 0, 72,
				ParticleBuilder.Type.FLASH, entity.getPositionVector().add(0, entity.height / 2, 0), new Vec3d(0.4, 0.1, 0.4), new Vec3d(entity.motionX * 1.05,
						entity.motionY * 1.05, entity.motionZ * 1.05), 8, 0.85F, 0.85F, 0.85F, 1.5F);


		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();

	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityCycloneBolt entity) {
		return null;
	}
}
