package com.favouritedragon.arcaneessentials.common.util;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.constants.Element;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;


public class ArcaneEnums {
	public static final Element WATER = EnumHelper.addEnum(Element.class, "WATER", new Class[]{Style.class, String.class, String.class},
			new Style().setColor(TextFormatting.BLUE), "water", ArcaneEssentials.MODID);
	public static final Element AIR = EnumHelper.addEnum(Element.class, "AIR", new Class[]{Style.class, String.class, String.class},
			new Style().setColor(TextFormatting.WHITE), "air", ArcaneEssentials.MODID);
}
