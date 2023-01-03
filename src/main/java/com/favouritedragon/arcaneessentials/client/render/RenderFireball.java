package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityFireball;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.client.render.RenderUtils.drawSphere;

public class RenderFireball extends Render<EntityFireball> {
    private static final float EXPANSION_TIME = 3;

    public RenderFireball(RenderManager renderManager) {
        super(renderManager);
    }

    //Copied from the forcefield class
    @Override
    public void doRender(@Nonnull EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();

        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y + entity.getSize() / 1.75, z);

//
//        float latStep = (float) Math.PI / 20;
//        float longStep = (float) Math.PI / 20;
//
//
//        float r = 245 / 255F, g = 0.05f, b = 0;
//        float r1 = 1F, g1 = 175 / 255F, b1 = 51 / 255F;
//        float r2 = 252 / 255F, g2 = 1F, b2 = 51 / 255F;
//
//        float radius = entity.getSize() / 2;
//        float a = 0.5f;
//
//        if (entity.ticksExisted > entity.getLifetime() - EXPANSION_TIME) {
//            radius *= 1 + 0.2f * (entity.ticksExisted + partialTicks - (entity.getLifetime() - EXPANSION_TIME)) / EXPANSION_TIME;
//            a *= Math.max(0, 1 - (entity.ticksExisted + partialTicks - (entity.getLifetime() - EXPANSION_TIME)) / EXPANSION_TIME);
//        } else if (entity.ticksExisted < EXPANSION_TIME) {
//            radius *= 1 - (EXPANSION_TIME - entity.ticksExisted - partialTicks) / EXPANSION_TIME;
//            a *= 1 - (EXPANSION_TIME - entity.ticksExisted - partialTicks) / EXPANSION_TIME;
//        }
//
//        //Colour shifting;
//        float range = 0.15F;
//        float rInitial = 245 / 255F;
//        float gInitial = 0.05f;
//        float bInitial = 0;
//        float aInitial = 0.5F;
//        float red = rInitial, green = gInitial, blue = bInitial, alpha = aInitial;
//        for (int i = 0; i < 4; i++) {
//            float rMin = rInitial - range;
//            float gMin = 0;
//            float bMin = 0;
//            float aMin = aInitial - range;
//            float rMax = rInitial + range;
//            float gMax = bInitial + range;
//            float bMax = gInitial + range;
//            float aMax = aInitial + range;
//            switch (i) {
//                case 0:
//                    float amountR = ArcaneUtils.getRandomNumberInRange(0,
//                            (int) (100 / rMax)) / 100F * 0.075F;
//                    red = entity.world.rand.nextBoolean() ? rInitial + amountR : rInitial - amountR;
//                    red = MathHelper.clamp(red, rMin, rMax);
//                    break;
//
//                case 1:
//                    float amountG = ArcaneUtils.getRandomNumberInRange(0,
//                            (int) (100 / gMax)) / 100F * 0.075F;
//                    green = entity.world.rand.nextBoolean() ? gInitial + amountG : gInitial - amountG;
//                    green = MathHelper.clamp(green, gMin, gMax);
//                    break;
//
//                case 2:
//                    float amountB = ArcaneUtils.getRandomNumberInRange(0,
//                            (int) (100 / bMax)) / 100F * 0.075F;
//                    blue = entity.world.rand.nextBoolean() ? bInitial + amountB : bInitial - amountB;
//                    blue = MathHelper.clamp(blue, bMin, bMax);
//                    break;
//
//                case 3:
//                    float amountA = ArcaneUtils.getRandomNumberInRange(0,
//                            (int) (100 / aMax)) / 100F * 0.025F;
//                    alpha = entity.world.rand.nextBoolean() ? aInitial + amountA : aInitial - amountA;
//                    alpha = MathHelper.clamp(alpha, aMin, aMax);
//                    break;
//            }
//            drawSphere(radius, latStep, longStep, false, red, green, blue, alpha);
//        }
//        //drawSphere(radius - 0.05f, latStep, longStep, true, r1, g1, b1, a * 1F);
//        //drawSphere(radius / 2, latStep, longStep, false, r2, g2, b2, 1.4f * a);
//        //GlStateManager.rotate(entity.ticksExisted * 20, 0, 0, 1);


        GlStateManager.enableLighting();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();

    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityFireball entity) {
        return null;
    }
}
