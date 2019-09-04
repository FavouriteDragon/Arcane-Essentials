package com.favouritedragon.arcaneessentials.common.item;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.item.armour.ItemManaArmour;
import com.favouritedragon.arcaneessentials.common.item.weapon.ItemMagicSword;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(ArcaneEssentials.MODID)
@Mod.EventBusSubscriber(modid = ArcaneEssentials.MODID)
public class ArcaneItems {

	public static final Item manaHelm = new ItemManaArmour(ArcaneMaterials.MANA_ARMOUR, 1, EntityEquipmentSlot.HEAD, "mana_helm", "mana_helm");
	public static final Item manaChestplate = new ItemManaArmour(ArcaneMaterials.MANA_ARMOUR, 1, EntityEquipmentSlot.CHEST, "mana_chestplate", "mana_chestplate");

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> registry = event.getRegistry();
		registerItem(registry, "lightning_sword", new ItemMagicSword(Item.ToolMaterial.DIAMOND, Tier.MASTER, Element.LIGHTNING));
	}

	//Copied from eb's class

	/**
	 * Sets both the registry and unlocalised names of the given item, then registers it with the given registry. Use
	 * this instead of {@link Item#setRegistryName(String)} and {@link Item#setTranslationKey(String)} during
	 * construction, for convenience and consistency. As of wizardry 4.2, this also automatically adds it to the order
	 * list for its creative tab if that tab is a {@link WizardryTabs.CreativeTabListed}, meaning the order can be defined simply
	 * by the order in which the items are registered in this class.
	 *
	 * @param registry The registry to register the given item to.
	 * @param name The name of the item, without the mod ID or the .name stuff. The registry name will be
	 *        {@code ebwizardry:[name]}. The unlocalised name will be {@code item.ebwizardry:[name].name}.
	 * @param item The item to register.
	 */
	// It now makes sense to have the name first, since it's shorter than an entire item declaration.
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item){
		registerItem(registry, name, item, false);
	}

	/**
	 * Sets both the registry and unlocalised names of the given item, then registers it with the given registry. Use
	 * this instead of {@link Item#setRegistryName(String)} and {@link Item#setTranslationKey(String)} during
	 * construction, for convenience and consistency. As of wizardry 4.2, this also automatically adds it to the order
	 * list for its creative tab if that tab is a {@link WizardryTabs.CreativeTabListed}, meaning the order can be defined simply
	 * by the order in which the items are registered in this class.
	 *
	 * @param registry The registry to register the given item to.
	 * @param name The name of the item, without the mod ID or the .name stuff. The registry name will be
	 *        {@code ebwizardry:[name]}. The unlocalised name will be {@code item.ebwizardry:[name].name}.
	 * @param item The item to register.
	 * @param setTabIcon True to set this item as the icon for its creative tab.
	 */
	// It now makes sense to have the name first, since it's shorter than an entire item declaration.
	private static void registerItem(IForgeRegistry<Item> registry, String name, Item item, boolean setTabIcon){

		item.setRegistryName(ArcaneEssentials.MODID, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);

		if(setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted){
			((WizardryTabs.CreativeTabSorted)item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if(item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed){
			((WizardryTabs.CreativeTabListed)item.getCreativeTab()).order.add(item);
		}
	}

}
