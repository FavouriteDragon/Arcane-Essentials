package com.favouritedragon.arcaneessentials.proxy;

import com.favouritedragon.arcaneessentials.client.render.RenderThunderBurst;
import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import electroblob.wizardry.client.renderer.RenderLightningPulse;
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
		RenderingRegistry.registerEntityRenderingHandler(EntityThunderBurst.class, (manager) -> {
			return new RenderThunderBurst(manager, 8.0F); });
	}


}
