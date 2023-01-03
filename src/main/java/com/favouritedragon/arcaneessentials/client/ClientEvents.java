package com.favouritedragon.arcaneessentials.client;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.favouritedragon.arcaneessentials.common.item.ArcaneItems.*;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ArcaneEssentials.MODID)
public class ClientEvents {

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        registerItemModel(masterLightningSword);
        registerItemModel(masterIceSword);
        registerItemModel(masterHealingSword);
        registerItemModel(masterNatureSword);
        registerItemModel(masterNecromancySword);
        registerItemModel(masterFireSword);
        registerItemModel(masterSorcerySword);
    }

    /**
     * Registers an item model, using the item's registry name as the model name (this convention makes it easier to
     * keep track of everything). Variant defaults to "normal". Registers the model for all metadata values.
     */
    private static void registerItemModel(Item item) {
        // Changing the last parameter from null to "inventory" fixed the item/block model weirdness. No idea why!
        ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        // Assigns the model for all metadata values
        ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
