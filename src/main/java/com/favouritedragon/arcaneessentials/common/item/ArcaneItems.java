package com.favouritedragon.arcaneessentials.common.item;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.item.armour.ItemManaArmour;
import com.favouritedragon.arcaneessentials.common.item.weapon.ItemMagicSword;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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

	//Swords
	public static final Item masterLightningSword = new ItemMagicSword(Item.ToolMaterial.DIAMOND, Tier.MASTER, Element.LIGHTNING);
	public static final Item masterSorcerySword = new ItemMagicSword(Item.ToolMaterial.DIAMOND, Tier.MASTER, Element.SORCERY);
	public static final Item masterNatureSword = new ItemMagicSword(Item.ToolMaterial.DIAMOND, Tier.MASTER, Element.EARTH);
	public static final Item masterFireSword = new ItemMagicSword(Item.ToolMaterial.DIAMOND, Tier.MASTER, Element.FIRE);
	public static final Item masterIceSword = new ItemMagicSword(Item.ToolMaterial.DIAMOND, Tier.MASTER, Element.ICE);
	public static final Item masterHealingSword = new ItemMagicSword(Item.ToolMaterial.DIAMOND, Tier.MASTER, Element.HEALING);
	public static final Item masterNecromancySword = new ItemMagicSword(Item.ToolMaterial.DIAMOND, Tier.MASTER, Element.NECROMANCY);

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> registry = event.getRegistry();
		registerItem(registry, "master_lightning_sword", masterLightningSword);
		registerItem(registry, "master_sorcery_sword", masterSorcerySword);
		registerItem(registry, "master_nature_sword", masterNatureSword);
		registerItem(registry, "master_fire_sword", masterFireSword);
		registerItem(registry, "master_ice_sword", masterIceSword);
		registerItem(registry, "master_healing_sword", masterHealingSword);
		registerItem(registry, "master_necromancy_sword", masterNecromancySword);
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
	private static void registerItem(IForgeRegistry<Item> registry, String name, Item item){

		item.setRegistryName(ArcaneEssentials.MODID, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);


		if(item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed){
			((WizardryTabs.CreativeTabListed)item.getCreativeTab()).order.add(item);
		}
	}

}
