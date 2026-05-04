package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityWaterWall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.client.render.RenderUtils.drawQuad;
import static net.minecraft.util.math.MathHelper.cos;
import static net.minecraft.util.math.MathHelper.sin;

/**
 * Renders EntityWaterWall as an oriented flat water slab using the same water_still.png
 * animated texture and vertex-wobble technique as RenderWaterBall.
 *
 * The wall's width axis is determined by entityYaw (perpendicular to the caster's facing).
 * Dimensions: width = size, height = size * 1.25, depth = 0.3 (thin slab).
 */
public class RenderWaterWall extends Render<EntityWaterWall> {

    private static final ResourceLocation WATER_TEXTURE = new ResourceLocation("minecraft",
            "textures/blocks/water_still.png");

    public RenderWaterWall(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@Nonnull EntityWaterWall wall, double x, double y, double z,
                         float entityYaw, float partialTicks) {

        float ticks = wall.ticksExisted + partialTicks;
        float size = wall.getSize();

        // Wall geometry: width = size * 1.5 (half = size * 0.75 each side),
        //                height = size * 0.8,  depth = 0.3 (thin)
        float w = size * 0.75f;
        float h = size * 0.8f;
        float d = 0.6f;

        Minecraft.getMinecraft().renderEngine.bindTexture(WATER_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.color(1.1f, 1.1f, 1.4f, 0.65f); // slight blue tint

        // Build transform: translate to entity render position, then rotate by yaw around Y.
        // JOML order: mat = T * R → vec.mul(mat) = T * R * vec = rotate-then-translate.
        // This orients the wall perpendicular to the caster's facing direction.
        Matrix4f mat = new Matrix4f();
        mat.translate((float) x, (float) y, (float) z);
        mat.rotate(-(entityYaw * (float) Math.PI / 180f), 0, 1, 0);

        // 8 corners of the slab in local wall-space (centered on entity position horizontally,
        // bottom at entity Y):
        // @formatter:off
        Vector4f
                lbf = new Vector4f(-w,  0,  -d, 1).mul(mat),   // left  bottom front
                rbf = new Vector4f( w,  0,  -d, 1).mul(mat),   // right bottom front
                ltf = new Vector4f(-w,  h,  -d, 1).mul(mat),   // left  top    front
                rtf = new Vector4f( w,  h,  -d, 1).mul(mat),   // right top    front
                lbb = new Vector4f(-w,  0,   d, 1).mul(mat),   // left  bottom back
                rbb = new Vector4f( w,  0,   d, 1).mul(mat),   // right bottom back
                ltb = new Vector4f(-w,  h,   d, 1).mul(mat),   // left  top    back
                rtb = new Vector4f( w,  h,   d, 1).mul(mat);   // right top    back
        // @formatter:on

        // Vertex wobble — same sin/cos technique as RenderWaterBall, but only in the wall plane
        // (X and Y offsets; no depth wobble so the slab stays coherent)
        float t1 = ticks * (float) Math.PI / 10f;
        float t2 = t1 + (float) Math.PI / 2f;
        float amt = 0.04f;

        lbf.add(cos(t1) * amt, sin(t2) * amt * 0.5f, 0, 0);
        rbf.add(sin(t1) * amt, cos(t2) * amt * 0.5f, 0, 0);
        ltf.add(cos(t2) * amt, sin(t1) * amt * 0.5f, 0, 0);
        rtf.add(sin(t2) * amt, cos(t1) * amt * 0.5f, 0, 0);
        lbb.add(sin(t2) * amt, cos(t1) * amt * 0.5f, 0, 0);
        rbb.add(cos(t2) * amt, sin(t1) * amt * 0.5f, 0, 0);
        ltb.add(sin(t1) * amt, cos(t2) * amt * 0.5f, 0, 0);
        rtb.add(cos(t1) * amt, sin(t2) * amt * 0.5f, 0, 0);

        // Animated UV — step through the 16-frame vertical strip in water_still.png
        float existed = ticks / 4f;
        int anim = ((int) existed % 16);
        float v1 = anim / 16f, v2 = v1 + 1f / 16f;

        // Draw all 6 faces of the slab (double-sided via drawQuad mode 2)
        drawQuad(2, ltb, lbb, lbf, ltf, 0, v1, 1, v2); // left  face (-X local)
        drawQuad(2, rtb, rbb, rbf, rtf, 0, v1, 1, v2); // right face (+X local)
        drawQuad(2, rbb, rbf, lbf, lbb, 0, v1, 1, v2); // bottom face
        drawQuad(2, rtb, rtf, ltf, ltb, 0, v1, 1, v2); // top face
        drawQuad(2, rtf, rbf, lbf, ltf, 0, v1, 1, v2); // front face (-Z local)
        drawQuad(2, rtb, rbb, lbb, ltb, 0, v1, 1, v2); // back  face (+Z local)

        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.disableBlend();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityWaterWall entity) {
        return WATER_TEXTURE;
    }
}
