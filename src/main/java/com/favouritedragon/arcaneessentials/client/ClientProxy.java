package com.favouritedragon.arcaneessentials.client;

import com.favouritedragon.arcaneessentials.client.render.RenderFirePillar;
import com.favouritedragon.arcaneessentials.client.render.RenderFirePillarSpawner;
import com.favouritedragon.arcaneessentials.client.render.RenderThunderBurst;
import com.favouritedragon.arcaneessentials.common.entity.*;
import com.favouritedragon.arcaneessentials.proxy.IProxy;
import electroblob.wizardry.client.renderer.RenderBlank;
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
		RenderingRegistry.registerEntityRenderingHandler(EntityFlamePillar.class, RenderFirePillar::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlamePillarSpawner.class, RenderFirePillarSpawner::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningVortex.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityThunderBurst.class, RenderThunderBurst::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWhirlpool.class, RenderBlank::new);

}


}
