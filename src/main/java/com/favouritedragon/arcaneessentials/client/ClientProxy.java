package com.favouritedragon.arcaneessentials.client;

import com.favouritedragon.arcaneessentials.client.render.*;
import com.favouritedragon.arcaneessentials.common.entity.*;
import com.favouritedragon.arcaneessentials.proxy.IProxy;
import electroblob.wizardry.client.renderer.RenderBlank;
import net.minecraft.client.renderer.entity.Render;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void Init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLInitializationEvent event) {

	}

	@Override
	public void registerRender() {
		//TODO: Move all particle spawning to entity class
		RenderingRegistry.registerEntityRenderingHandler(EntityCycloneBolt.class, RenderCycloneBolt::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCycloneShield.class, RenderCycloneShield::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockSpawner.class, RenderBlockSpawner::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFireball.class, RenderFireball::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlamePillar.class, RenderFirePillar::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlamePillarSpawner.class, RenderFirePillarSpawner::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlameSlash.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFloatingBlock.class, RenderFloatingBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningVortex.class, RenderLightningVortex::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningSpawner.class, RenderLightningSpawner::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMagicLightning.class, RenderMagicLightning::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMagicSpawner.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySolarBeam.class, RenderSolarBeam::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityThunderBurst.class, RenderThunderBurst::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterBall.class, RenderWaterBall::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWhirlpool.class, RenderBlank::new);

	}


}
