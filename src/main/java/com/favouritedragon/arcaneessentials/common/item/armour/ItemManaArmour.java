package com.favouritedragon.arcaneessentials.common.item.armour;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.item.ArcaneMaterials.MANA_ARMOUR;

public class ItemManaArmour extends ItemArmor {

	public ItemManaArmour(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String unlocalizedName, String registryName) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		//setUnlocalizedName(ArcaneEssentials.MODID + "." + unlocalizedName);
		setTranslationKey(ArcaneEssentials.MODID + "." + unlocalizedName);
		setCreativeTab(WizardryTabs.WIZARDRY);
		setRegistryName(registryName);

	}

	@Override
	public ArmorMaterial getArmorMaterial() {
		return MANA_ARMOUR;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		super.onArmorTick(world, player, itemStack);
		ItemStack stack = player.getHeldItemMainhand();
		if (stack.getItem() instanceof ItemWand) {
			ItemWand wand = (ItemWand) stack.getItem();
			if (wand.getDamage(stack) >= 1) {
				if (player.ticksExisted % 100 == 0) {
					wand.setDamage(stack, stack.getItemDamage() - 1);
				}
			}
		}
		ItemStack stack2 = player.getHeldItemOffhand();
		if (stack2.getItem() instanceof ItemWand) {
			ItemWand wand = (ItemWand) stack2.getItem();
			if (wand.getDamage(stack2) >= 1) {
				if (player.ticksExisted % 100 == 0) {
					wand.setDamage(stack2, stack2.getItemDamage() - 1);
				}
			}
		}

	}
}
