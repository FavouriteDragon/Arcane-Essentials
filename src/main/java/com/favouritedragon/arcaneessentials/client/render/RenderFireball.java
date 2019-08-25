package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityFireball;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.client.render.RenderUtils.drawSphere;

public class RenderFireball extends Render<EntityFireball> {
	private static final float EXPANSION_TIME = 3;
	private static final ResourceLocation TEXTURE = new ResourceLocation(Wizardry.MODID, "textures/entity/fireball.png");

	public RenderFireball(RenderManager renderManager) {
		super(renderManager);
	}

	//Copied from the forcefield class
	@Override
	public void doRender(@Nonnull EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

		GlStateManager.translate(x, y + entity.getSize() / 1.75, z);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);


		float latStep = (float) Math.PI / 20;
		float longStep = (float) Math.PI / 20;


		float r = 245 / 255F, g = 0.05f, b = 0;
		float r1 = 1F, g1 = 175 / 255F, b1 = 51 / 255F;
		float r2 = 252 / 255F, g2 = 1F, b2 = 51 / 255F;

		float radius = entity.getSize() / 2;
		float a = 0.5f;

		if (entity.ticksExisted > entity.getLifetime() - EXPANSION_TIME) {
			radius *= 1 + 0.2f * (entity.ticksExisted + partialTicks - (entity.getLifetime() - EXPANSION_TIME)) / EXPANSION_TIME;
			a *= Math.max(0, 1 - (entity.ticksExisted + partialTicks - (entity.getLifetime() - EXPANSION_TIME)) / EXPANSION_TIME);
		} else if (entity.ticksExisted < EXPANSION_TIME) {
			radius *= 1 - (EXPANSION_TIME - entity.ticksExisted - partialTicks) / EXPANSION_TIME;
			a *= 1 - (EXPANSION_TIME - entity.ticksExisted - partialTicks) / EXPANSION_TIME;
		}


		drawSphere(radius, latStep, longStep, false, r, g, b, a * 0.85F);
		drawSphere(radius - 0.05f, latStep, longStep, true, r1, g1, b1, a * 1F);
		drawSphere(radius - 0.1F, latStep, longStep, true, r2, g2, b2, 2f * a);

		//Particles

		for (int i = 0; i < 6; i++) {
			if (entity.world.isRemote) {
				AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
				double spawnX = boundingBox.minX + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxX - boundingBox.minX);
				double spawnY = boundingBox.minY + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxY - boundingBox.minY);
				double spawnZ = boundingBox.minZ + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxZ - boundingBox.minZ);
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).vel(entity.motionX, entity.motionY, entity.motionZ).pos(spawnX, spawnY, spawnZ).collide(true)
						.time(5).scale(entity.getSize() * 2 + entity.world.rand.nextFloat() / 2).spawn(entity.world);
			}
		}


		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();

	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityFireball entity) {
		return TEXTURE;
	}
}
