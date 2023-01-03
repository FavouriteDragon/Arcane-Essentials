package com.favouritedragon.arcaneessentials;

import com.favouritedragon.arcaneessentials.common.entity.data.Behaviour;
import com.favouritedragon.arcaneessentials.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ArcaneEssentials.MODID, name = ArcaneEssentials.NAME, version = ArcaneEssentials.VERSION, dependencies = "required-after:ebwizardry")
public class ArcaneEssentials {

    //TODO: Fix spells sound registry modid
    public static final String MODID = "arcane_essentials";
    public static final String NAME = "Arcane Essentials";
    public static final String VERSION = "1.0";
    public static final String MC_VERSION = "1.12.2";

    public static final String CLIENT = "com.favouritedragon.arcane_essentials.client.ClientProxy";
    public static final String SERVER = "com.favouritedragon.arcane_essentials.proxy.ServerProxy";

    public static Logger logger;


    @Mod.Instance(ArcaneEssentials.MODID)
    public static ArcaneEssentials instance;

    @SidedProxy(clientSide = ArcaneEssentials.CLIENT, serverSide = ArcaneEssentials.SERVER)
    public static IProxy proxy;

    //TODO: Change spell categories back once eb fixes them; properly implement sword spells once eb fixes spell context.

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        proxy.registerRender();
        RegisterHandler.registerAll();
        Behaviour.registerBehaviours();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.Init(event);
    }

    @EventHandler
    public void postInit(FMLInitializationEvent event) {
        proxy.postInit(event);
    }


}
