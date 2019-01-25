package com.favouritedragon.arcaneessentials.client;

import com.favouritedragon.arcaneessentials.client.render.RenderLightningVortex;
import com.favouritedragon.arcaneessentials.client.render.RenderThunderBurst;
import com.favouritedragon.arcaneessentials.common.EntityLightningVortex;
import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import com.favouritedragon.arcaneessentials.proxy.IProxy;
import electroblob.wizardry.client.renderer.RenderBlank;
import net.minecraft.client.renderer.entity.RenderLightningBolt;
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
		RenderingRegistry.registerEntityRenderingHandler(EntityThunderBurst.class, RenderThunderBurst::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningVortex.class, RenderLightningVortex::new);
}


}
