package com.favouritedragon.arcaneessentials.common.item;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.item.armour.ItemManaArmour;
import javafx.scene.paint.Material;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ArcaneEssentials.MODID)
public class ArcaneItems {

	public static final Item manaHelm = new ItemManaArmour(ArcaneMaterials.MANA_ARMOUR, 1, EntityEquipmentSlot.HEAD, "mana_helm", "mana_helm");
	public static final Item manaChestplate = new ItemManaArmour(ArcaneMaterials.MANA_ARMOUR, 1, EntityEquipmentSlot.CHEST, "mana_chestplate", "mana_chestplate");

}
