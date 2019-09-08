package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityLightningSpawner;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderLightningSpawner extends Render<EntityLightningSpawner> {

	public RenderLightningSpawner(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull EntityLightningSpawner entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		ParticleBuilder.create(ParticleBuilder.Type.SPARK).entity(entity).scale(2.0F).spawn(entity.world);
		entity.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX, entity.posY, entity.posZ, 0, 0, 0);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityLightningSpawner entity) {
		return null;
	}
}
