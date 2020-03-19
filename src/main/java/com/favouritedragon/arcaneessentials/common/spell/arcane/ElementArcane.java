package com.favouritedragon.arcaneessentials.common.spell.arcane;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.api.WizardryEnumHelper;
import electroblob.wizardry.constants.Element;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class ElementArcane {
	public static final Element ARCANE = WizardryEnumHelper.addElement("arcane", new Style().setColor(TextFormatting.LIGHT_PURPLE), "arcane", ArcaneEssentials.MODID);
}
