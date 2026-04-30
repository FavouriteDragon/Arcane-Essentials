package com.favouritedragon.arcaneessentials.common.config;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Config(modid = ArcaneEssentials.MODID)
public class ArcaneConfig {

    @Config.Name("client")
    public static Client CLIENT = new Client();

    public static class Client {

        @Config.Name("Use Classic Spell Names")
        @Config.Comment({
            "If true, classic spell display names are used on the client.",
                "This only changes localization text and does not affect spell IDs or compatibility."
        })
        public boolean useLegacySpellNames = false;
    }

    @Mod.EventBusSubscriber(modid = ArcaneEssentials.MODID, value = Side.CLIENT)
    public static class ClientConfigEvents {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (!ArcaneEssentials.MODID.equals(event.getModID())) return;
            ConfigManager.sync(ArcaneEssentials.MODID, Config.Type.INSTANCE);
            com.favouritedragon.arcaneessentials.client.SpellNameOverrides.applyNameOverrides();
        }
    }
}