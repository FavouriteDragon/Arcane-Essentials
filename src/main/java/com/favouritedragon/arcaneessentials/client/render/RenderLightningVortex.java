package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityLightningVortex;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.WizardryParticleType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class RenderLightningVortex extends Render<EntityLightningVortex> {

	public RenderLightningVortex(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityLightningVortex entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		ArcaneUtils.spawnSpinningVortex(entity.world, 240, 7, 0.25, 80, WizardryParticleType.SPARK,
				new Vec3d(entity.posX, entity.posY, entity.posZ), new Vec3d(0.15, 0.05, 0.15), new Vec3d(entity.motionX, entity.motionY, entity.motionZ), 2, 0, 0, 0);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityLightningVortex entity) {
		return null;
	}
}
