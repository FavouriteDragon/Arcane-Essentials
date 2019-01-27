package com.favouritedragon.arcaneessentials.common.util;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.util.IElementalDamage;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ArcaneEssentials.MODID)
public class RadiantDamageModifier {

	@SubscribeEvent
	public static void radianceHurtEvent(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase entity = event.getEntityLiving();
			if (event.getSource() instanceof IElementalDamage) {
				IElementalDamage source = (IElementalDamage) event.getSource();
				if (source.getType() == MagicDamage.DamageType.RADIANT) {
					if (entity.isEntityUndead()) {
						event.setAmount(event.getAmount() * 1.5F);

					}
				}

			}
		}
	}
}
