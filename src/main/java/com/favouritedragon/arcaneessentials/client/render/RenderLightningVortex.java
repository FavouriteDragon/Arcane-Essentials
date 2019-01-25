package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.EntityLightningVortex;
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



	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityLightningVortex entity) {
		return null;
	}
}
