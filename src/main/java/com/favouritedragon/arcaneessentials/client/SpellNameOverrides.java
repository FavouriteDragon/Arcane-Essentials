package com.favouritedragon.arcaneessentials.client;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.config.ArcaneConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.Locale;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class SpellNameOverrides {

    private static final Map<String, String> LEGACY_NAMES = new LinkedHashMap<>();
    private static final Map<String, String> ORIGINAL_NAMES = new HashMap<>();
    private static boolean initialized = false;

    static {
        LEGACY_NAMES.put("spell.arcane_essentials:frizz", "Frizz");
        LEGACY_NAMES.put("spell.arcane_essentials:frizzle", "Frizzle");
        LEGACY_NAMES.put("spell.arcane_essentials:kafrizz", "KaFrizz");
        LEGACY_NAMES.put("spell.arcane_essentials:kafrizzle", "KaFrizzle");
        LEGACY_NAMES.put("spell.arcane_essentials:kaquake", "KaQuake");
        LEGACY_NAMES.put("spell.arcane_essentials:kaquakele", "KaQuakele");
        LEGACY_NAMES.put("spell.arcane_essentials:kasploosh", "KaSploosh");
        LEGACY_NAMES.put("spell.arcane_essentials:kasplooshle", "KaSplooshle");
        LEGACY_NAMES.put("spell.arcane_essentials:kathwack", "KaThwack");
        LEGACY_NAMES.put("spell.arcane_essentials:kazam", "KaZam");
        LEGACY_NAMES.put("spell.arcane_essentials:kazammle", "KaZammle");
        LEGACY_NAMES.put("spell.arcane_essentials:oomph", "Oomph");
        LEGACY_NAMES.put("spell.arcane_essentials:quake", "Quake");
        LEGACY_NAMES.put("spell.arcane_essentials:shake", "Shake");
        LEGACY_NAMES.put("spell.arcane_essentials:splash", "Splash");
        LEGACY_NAMES.put("spell.arcane_essentials:sploosh", "Sploosh");
        LEGACY_NAMES.put("spell.arcane_essentials:zam", "Zam");
        LEGACY_NAMES.put("spell.arcane_essentials:zammle", "Zammle");
        LEGACY_NAMES.put("spell.arcane_essentials:zoom", "Zoom");
    }

    private SpellNameOverrides() {
    }

    public static void init() {
        if (initialized) return;
        initialized = true;

        if (Minecraft.getMinecraft().getResourceManager() instanceof IReloadableResourceManager) {
            IReloadableResourceManager manager = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
            manager.registerReloadListener((IResourceManagerReloadListener) resourceManager -> applyNameOverrides());
        }

        applyNameOverrides();
    }

    @SuppressWarnings("unchecked")
    public static void applyNameOverrides() {
        try {
            Locale locale = ReflectionHelper.getPrivateValue(I18n.class, null, "i18nLocale", "field_74839_a");
            if (locale == null) return;

            Map<String, String> properties = ReflectionHelper.getPrivateValue(Locale.class, locale, "properties", "field_135032_a");
            if (properties == null) return;

            for (Map.Entry<String, String> entry : LEGACY_NAMES.entrySet()) {
                String key = entry.getKey();
                if (!ORIGINAL_NAMES.containsKey(key) && properties.containsKey(key)) {
                    ORIGINAL_NAMES.put(key, properties.get(key));
                }

                if (ArcaneConfig.CLIENT.useLegacySpellNames) {
                    properties.put(key, entry.getValue());
                } else if (ORIGINAL_NAMES.containsKey(key)) {
                    properties.put(key, ORIGINAL_NAMES.get(key));
                }
            }
        } catch (Exception e) {
            ArcaneEssentials.logger.error("Failed applying spell name overrides", e);
        }
    }
}