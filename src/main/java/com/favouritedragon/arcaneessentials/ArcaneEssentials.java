package com.favouritedragon.arcaneessentials;

import com.favouritedragon.arcaneessentials.common.spell.ThunderBurst;
import com.favouritedragon.arcaneessentials.proxy.IProxy;
import electroblob.wizardry.spell.Spell;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ArcaneEssentials.MODID, name = ArcaneEssentials.NAME, version = ArcaneEssentials.VERSION, dependencies="required-after:ebwizardry")
public class ArcaneEssentials
{
    public static final String MODID = "arcane_essentials";
    public static final String NAME = "Arcane Essentials";
    public static final String VERSION = "1.0";
    public static final String MC_VERSION = "1.12.2";

    public static final String CLIENT = "com.favouritedragon.arcaneessentials.proxy.ClientProxy";
	public static final String SERVER = "com.favouritedragon.arcaneessentials.proxy.ServerProxy";

    private static Logger logger;

    @Mod.Instance(ArcaneEssentials.MODID)
    public static ArcaneEssentials instance;

	@SidedProxy(clientSide = ArcaneEssentials.CLIENT, serverSide = ArcaneEssentials.SERVER)
	public static IProxy proxy;


	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        proxy.registerRender();
        RegisterHandler.registerAll();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.Init(event);
    }

    @EventHandler
	public void postInit(FMLInitializationEvent event) {
		proxy.postInit(event);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event){
		event.getRegistry().register(new ThunderBurst());
	}
}
