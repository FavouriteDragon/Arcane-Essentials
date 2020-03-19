package com.favouritedragon.arcaneessentials.common.item;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.registry.WizardrySounds;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class ArcaneMaterials {

	public static final ItemArmor.ArmorMaterial MANA_ARMOUR = EnumHelper.addArmorMaterial(ArcaneEssentials.MODID + ":mana_armour", ArcaneEssentials.MODID
			+ ":mana", 300, new int[]{400, 600, 500, 300}, 20, WizardrySounds.ITEM_WAND_LEVELUP, 3);
}
