package com.favouritedragon.arcaneessentials.common.util;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import electroblob.wizardry.util.IElementalDamage;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ArcaneEssentials.MODID)
public class DamageSources extends MagicDamage implements IElementalDamage {
	//TODO: Implement a new damage source

	public DamageSources(String name, Entity caster, DamageType type, boolean isRetaliatory) {
		super(name, caster, type, isRetaliatory);
	}


}
