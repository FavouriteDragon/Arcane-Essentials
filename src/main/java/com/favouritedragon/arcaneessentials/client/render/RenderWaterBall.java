package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityWaterBall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.client.render.RenderUtils.drawQuad;
import static net.minecraft.util.math.MathHelper.cos;
import static net.minecraft.util.math.MathHelper.sin;

public class RenderWaterBall extends Render<EntityWaterBall> {
	private static final ResourceLocation water = new ResourceLocation("minecraft",
			"textures/blocks/water_still.png");

	public RenderWaterBall(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityWaterBall ball, double x, double y, double z, float entityYaw,
						 float partialTicks) {

		float ticks = ball.ticksExisted + partialTicks;
		float colorEnhancement = 1.2f;

		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(water);
		GlStateManager.enableBlend();
		GlStateManager.color(colorEnhancement, colorEnhancement, colorEnhancement, 0.6f);

		Matrix4f mat = new Matrix4f();
		mat.translate((float) x - 0.5f, (float) y + 0.5F, (float) z - 0.5f);

		//TIL that putting the rotation code here makes it orbit around you. Very cool, but not the intended effect xD
		//Using mul.rotate instead of GlStateManager does the same thing to the entity, BUT it just spins the water bubble around a corner

		//TODO: Add rotations, size scaling, and wobbling (only wobble when degrees per second is 0)

		// (0 Left)/(1 Right), (0 Bottom)/(1 Top), (0 Front)/(1 Back)
		//Vector4f mid = new Vector4f((float) x, (float) y + .5f, (float) z, 1);


		//mat.rotate(ticks / 25f * bubble.getDegreesPerSecond(), 1, 0, 0);
		//mat.rotate(ticks / 25f * bubble.getDegreesPerSecond(), 0, 1, 0);
		//mat.rotate(ticks / 25f * bubble.getDegreesPerSecond(), 0, 0, 1);


		// @formatter:off
		Vector4f
				lbf = new Vector4f(0, 0, 0, 1).mul(mat),
				rbf = new Vector4f(1, 0, 0, 1).mul(mat),
				ltf = new Vector4f(0, 1, 0, 1).mul(mat).mul(ball.getSize()),
				rtf = new Vector4f(1, 1, 0, 1).mul(mat).mul(ball.getSize()),
				lbb = new Vector4f(0, 0, 1, 1).mul(mat).mul(ball.getSize()),
				rbb = new Vector4f(1, 0, 1, 1).mul(mat).mul(ball.getSize()),
				ltb = new Vector4f(0, 1, 1, 1).mul(mat).mul(ball.getSize()),
				rtb = new Vector4f(1, 1, 1, 1).mul(mat).mul(ball.getSize());


		float t1 = ticks * (float) Math.PI / 10f;
		float t2 = t1 + (float) Math.PI / 2f;
		float amt = .05f;
		//lbf.rotate()

		lbf.add(cos(t1) * amt, sin(t2) * amt, cos(t2) * amt, 0);
		rbf.add(sin(t1) * amt, cos(t2) * amt, sin(t2) * amt, 0);
		lbb.add(sin(t2) * amt, cos(t2) * amt, cos(t2) * amt, 0);
		rbb.add(cos(t2) * amt, cos(t1) * amt, cos(t1) * amt, 0);

		ltf.add(cos(t2) * amt, cos(t1) * amt, sin(t1) * amt, 0);
		rtf.add(sin(t2) * amt, sin(t1) * amt, cos(t1) * amt, 0);
		ltb.add(sin(t1) * amt, sin(t2) * amt, cos(t1) * amt, 0);
		rtb.add(cos(t1) * amt, cos(t2) * amt, sin(t1) * amt, 0);

		// @formatter:on



		float existed = ticks / 4f;
		int anim = ((int) existed % 16);
		float v1 = anim / 16f, v2 = v1 + 1f / 16;

		drawQuad(2, ltb, lbb, lbf, ltf, 0, v1, 1, v2); // -x
		drawQuad(2, rtb, rbb, rbf, rtf, 0, v1, 1, v2); // +x
		drawQuad(2, rbb, rbf, lbf, lbb, 0, v1, 1, v2); // -y
		drawQuad(2, rtb, rtf, ltf, ltb, 0, v1, 1, v2); // +y
		drawQuad(2, rtf, rbf, lbf, ltf, 0, v1, 1, v2); // -z
		drawQuad(2, rtb, rbb, lbb, ltb, 0, v1, 1, v2); // +z

		//float rotation = ticks * 20F;
		//GlStateManager.rotate(rotation * 0.2F, 1, 0, 0);
		//GlStateManager.rotate(rotation, 0, 1, 0);
		//GlStateManager.rotate(rotation * 0.2F, 0, 0, 1);

		//GlStateManager.scale(ball.getSize(), ball.getSize(), ball.getSize());


		//drawSphere(ball.getSize());

		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableBlend();

	}

	private static void drawSphere(float radius, float latStep, float longStep, boolean inside, float r, float g, float b, float a, @Nullable ResourceLocation texture){

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

		boolean goingUp = inside;

		buffer.pos(0, goingUp ? -radius : radius, 0).color(r, g, b, a).endVertex(); // Start at the north pole

		for(float longitude = -(float)Math.PI; longitude <= (float)Math.PI; longitude += longStep){

			// Leave the poles out since they only have a single point per stack instead of two
			for(float theta = (float)Math.PI/2 - latStep; theta >= -(float)Math.PI/2 + latStep; theta -= latStep){

				float latitude = goingUp ? -theta : theta;

				float hRadius = radius * MathHelper.cos(latitude);
				float vy = radius * MathHelper.sin(latitude);
				float vx = hRadius * MathHelper.sin(longitude);
				float vz = hRadius * MathHelper.cos(longitude);

				buffer.pos(vx, vy, vz).color(r, g, b, a).endVertex();

				vx = hRadius * MathHelper.sin(longitude + longStep);
				vz = hRadius * MathHelper.cos(longitude + longStep);

				if (texture != null) {
					Minecraft.getMinecraft().renderEngine.bindTexture(texture);
				}
				buffer.pos(vx, vy, vz).color(r, g, b, a).endVertex();
			}

			// The next pole
			buffer.pos(0, goingUp ? radius : -radius, 0).color(r, g, b, a).endVertex();

			goingUp = !goingUp;
		}

		tessellator.draw();
	}


	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityWaterBall entity) {
		return null;
	}
}
