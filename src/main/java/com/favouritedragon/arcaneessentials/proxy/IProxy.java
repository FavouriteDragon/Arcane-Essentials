package com.favouritedragon.arcaneessentials.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {

	public void preInit(FMLPreInitializationEvent event);

	public void Init(FMLInitializationEvent event);

	public void postInit(FMLInitializationEvent event);

}
