package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import electroblob.wizardry.Wizardry;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderThunderBurst extends Render<EntityThunderBurst> {
	private final ResourceLocation[] textures = new ResourceLocation[8];
	private float scale = 1.0f;

	public RenderThunderBurst(RenderManager renderManager, float scale){
		super(renderManager);
		for(int i = 0; i < textures.length; i++){
			textures[i] = new ResourceLocation(Wizardry.MODID, "textures/entity/lightning_pulse_" + i + ".png");
		}
		this.scale = scale;
	}

	@Override
	public void doRender(EntityThunderBurst entity, double par2, double par4, double par6, float par8, float par9){

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		float yOffset = 0;

		GlStateManager.translate((float)par2, (float)par4 + yOffset, (float)par6);

		int textureNumber = entity.ticksExisted < 9 ? entity.ticksExisted : 8;

		this.bindTexture(textures[textureNumber]);
		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.5F;

		GlStateManager.rotate(-90, 1, 0, 0);

		GlStateManager.scale(scale, scale, scale);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos((double)(0.0F - f7), (double)(0.0F - f8), 0.01).tex(0, 1).endVertex();
		buffer.pos((double)(f6 - f7), (double)(0.0F - f8), 0.01).tex(1, 1).endVertex();
		buffer.pos((double)(f6 - f7), (double)(1.0F - f8), 0.01).tex(1, 0).endVertex();
		buffer.pos((double)(0.0F - f7), (double)(1.0F - f8), 0.01).tex(0, 0).endVertex();

		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}
	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityThunderBurst entity) {
		return null;
	}
}
