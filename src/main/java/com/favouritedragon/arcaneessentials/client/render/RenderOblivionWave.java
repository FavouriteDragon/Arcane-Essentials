package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityOblivionWave;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderOblivionWave extends Render<EntityOblivionWave> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("avatarmod",
			"textures/entity/fireball.png");

	protected RenderOblivionWave(RenderManager renderManager) {
		super(renderManager);
	}
	@Override
	public void doRender(EntityFireball entity, double xx, double yy, double zz, float entityYaw,
						 float partialTicks) {



		float x = (float) xx, y = (float) yy, z = (float) zz;

		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

		float ticks = entity.ticksExisted + partialTicks;

		float rotation = ticks / 3f;
		float size = .8f + cos(ticks / 5f) * .05f;
		size *= Math.sqrt(entity.getSize() / 30f);


		enableBlend();

		if (entity.ticksExisted % 3 == 0) {
			World world = entity.world;
			AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
			double spawnX = boundingBox.minX + random.nextDouble() * (boundingBox.maxX - boundingBox.minX);
			double spawnY = boundingBox.minY + random.nextDouble() * (boundingBox.maxY - boundingBox.minY);
			double spawnZ = boundingBox.minZ + random.nextDouble() * (boundingBox.maxZ - boundingBox.minZ);
			world.spawnParticle(EnumParticleTypes.FLAME, spawnX, spawnY, spawnZ, 0, 0, 0);
		}

		//   if (MinecraftForgeClient.getRenderPass() == 0) {
		disableLighting();

		renderCube(x, y, z, //
				0, 8 / 256.0, 0, 8 / 256.0, //
				.5f, //
				ticks / 25F, ticks / 25f, ticks / 25F);

		int i = 15728880;
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);

		//  } else {

		pushMatrix();
		renderCube(x, y, z, //
				8 / 256.0, 16 / 256.0, 0 / 256.0, 8 / 256.0, //
				size, //
				rotation * .2f, rotation, rotation * -.4f);
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
