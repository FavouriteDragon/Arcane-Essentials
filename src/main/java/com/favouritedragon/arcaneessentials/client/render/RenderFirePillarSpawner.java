package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityFlamePillarSpawner;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RenderFirePillarSpawner extends Render<EntityFlamePillarSpawner> {

	public RenderFirePillarSpawner(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityFlamePillarSpawner entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		World world = entity.world;
		boolean pX = world.rand.nextBoolean();
		boolean pZ = world.rand.nextBoolean();
		double xV = world.rand.nextDouble() / 100;
		double zV = world.rand.nextDouble() / 100;
		world.spawnParticle(EnumParticleTypes.FLAME, entity.posX, entity.posY, entity.posZ, pX ? xV : - xV, world.rand.nextDouble() / 100, pZ ? zV : -zV);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityFlamePillarSpawner entity) {
		return null;
	}
}
