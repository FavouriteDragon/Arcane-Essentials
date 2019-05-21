package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityOblivionWave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import static java.lang.Math.cos;
import static net.minecraft.client.renderer.GlStateManager.*;

public class RenderOblivionWave extends Render<EntityOblivionWave> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("arcane_essentials",
			"textures/entity/oblivion_wave.png");

	protected RenderOblivionWave(RenderManager renderManager) {
		super(renderManager);
	}
	@Override
	public void doRender(EntityOblivionWave entity, double xx, double yy, double zz, float entityYaw,
						 float partialTicks) {



		float x = (float) xx, y = (float) yy, z = (float) zz;

		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

		float ticks = entity.ticksExisted + partialTicks;

		float size = .8f + (float) cos(ticks / 5f) * .05f;
		size *= Math.sqrt(entity.getSize() / 30f);


		enableBlend();


		//   if (MinecraftForgeClient.getRenderPass() == 0) {
		disableLighting();


		int i = 15728880;
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);

		//  } else {

		pushMatrix();
		RenderUtils.renderCube(x, y, z, //
				8 / 256.0, 16 / 256.0, 0 / 256.0, 8 / 256.0, //
				size, //
				ticks / 30F, ticks / 30F, ticks / 30F);
		popMatrix();

		//  }
		enableLighting();
		disableBlend();

	}


	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityOblivionWave entity) {
		return null;
	}
}
