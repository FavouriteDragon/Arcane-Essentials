package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityFallingBlockSpawner;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderBlockSpawner extends Render<EntityFallingBlockSpawner> {

	public RenderBlockSpawner(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull EntityFallingBlockSpawner entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		entity.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, entity.posX, entity.posY, entity.posZ, entity.world.rand.nextGaussian() * 0.5,
				entity.world.rand.nextDouble() / 5, entity.world.rand.nextGaussian() * 0.4,
				Block.getStateId(entity.world.getBlockState(entity.getPosition().down())));
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityFallingBlockSpawner entity) {
		return null;
	}
}
