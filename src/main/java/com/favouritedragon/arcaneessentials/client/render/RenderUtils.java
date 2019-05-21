package com.favouritedragon.arcaneessentials.client.render;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;


public class RenderUtils {

	// @formatter:on

	private void renderCube(float x, float y, float z, double u1, double u2, double v1, double v2, float size,
							float rotateX, float rotateY, float rotateZ) {
		Matrix4f mat = new Matrix4f();
		mat.translate(x, y + .4f, z);

		mat.rotate(rotateX, 1, 0, 0);
		mat.rotate(rotateY, 0, 1, 0);
		mat.rotate(rotateZ, 0, 0, 1);

		// @formatter:off
		// Can't use .mul(size) here because it would mul the w component
		Vector4f
				lbf = new Vector4f(-.5f * size, -.5f * size, -.5f * size, 1).mul(mat),
				rbf = new Vector4f(0.5f * size, -.5f * size, -.5f * size, 1).mul(mat),
				ltf = new Vector4f(-.5f * size, 0.5f * size, -.5f * size, 1).mul(mat),
				rtf = new Vector4f(0.5f * size, 0.5f * size, -.5f * size, 1).mul(mat),
				lbb = new Vector4f(-.5f * size, -.5f * size, 0.5f * size, 1).mul(mat),
				rbb = new Vector4f(0.5f * size, -.5f * size, 0.5f * size, 1).mul(mat),
				ltb = new Vector4f(-.5f * size, 0.5f * size, 0.5f * size, 1).mul(mat),
				rtb = new Vector4f(0.5f * size, 0.5f * size, 0.5f * size, 1).mul(mat);

		// @formatter:on

		drawQuad(2, ltb, lbb, lbf, ltf, u1, v1, u2, v2); // -x
		drawQuad(2, rtb, rbb, rbf, rtf, u1, v1, u2, v2); // +x
		drawQuad(2, rbb, rbf, lbf, lbb, u1, v1, u2, v2); // -y
		drawQuad(2, rtb, rtf, ltf, ltb, u1, v1, u2, v2); // +y
		drawQuad(2, rtf, rbf, lbf, ltf, u1, v1, u2, v2); // -z
		drawQuad(2, rtb, rbb, lbb, ltb, u1, v1, u2, v2); // +z
	}
}
