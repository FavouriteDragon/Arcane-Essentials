package com.favouritedragon.arcaneessentials.common.util;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ArcaneEssentials.MODID)
public class RadiantDamageModifier {

	@SubscribeEvent
	public static void radianceHurtEvent(LivingHurtEvent event) {

	}
}
