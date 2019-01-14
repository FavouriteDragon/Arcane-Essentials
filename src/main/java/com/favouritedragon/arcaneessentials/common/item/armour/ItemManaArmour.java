package com.favouritedragon.arcaneessentials.common.item.armour;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemManaArmour extends ItemArmor {

	public ItemManaArmour(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String unlocalizedName, String registryName) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		setUnlocalizedName(ArcaneEssentials.MODID + "." + unlocalizedName);
		setCreativeTab(WizardryTabs.WIZARDRY);
		setRegistryName(registryName);

	}
}
