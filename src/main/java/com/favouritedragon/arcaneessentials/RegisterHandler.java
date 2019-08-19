package com.favouritedragon.arcaneessentials;

import com.favouritedragon.arcaneessentials.common.entity.*;
import com.favouritedragon.arcaneessentials.common.spell.air.CycloneBolt;
import com.favouritedragon.arcaneessentials.common.spell.air.CycloneShield;
import com.favouritedragon.arcaneessentials.common.spell.divine.RadianceStorm;
import com.favouritedragon.arcaneessentials.common.spell.divine.RadiantBeam;
import com.favouritedragon.arcaneessentials.common.spell.earth.SolarBeam;
import com.favouritedragon.arcaneessentials.common.spell.fire.FirePledge;
import com.favouritedragon.arcaneessentials.common.spell.fire.InfernoPillar;
import com.favouritedragon.arcaneessentials.common.spell.necromancy.KaThwack;
import com.favouritedragon.arcaneessentials.common.spell.storm.LightningVortex;
import com.favouritedragon.arcaneessentials.common.spell.storm.StormBlink;
import com.favouritedragon.arcaneessentials.common.spell.storm.ThunderBurst;
import com.favouritedragon.arcaneessentials.common.spell.water.OceanBurst;
import com.favouritedragon.arcaneessentials.common.spell.water.Whirlpool;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod.EventBusSubscriber(modid = ArcaneEssentials.MODID)
public class RegisterHandler {
	private static final int LIVING_UPDATE_INTERVAL = 3;
	private static final int PROJECTILE_UPDATE_INTERVAL = 10;

	/**
	 * Private helper method for registering entities; keeps things neater. For some reason, Forge 1.11.2 wants a
	 * ResourceLocation and a string name... probably because it's transitioning to the registry system.
	 */
	private static void registerEntity(Class<? extends Entity> entityClass, String name, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		ResourceLocation registryName = new ResourceLocation(ArcaneEssentials.MODID, name);
		EntityRegistry.registerModEntity(registryName, entityClass, registryName.toString(), id, ArcaneEssentials.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	/**
	 * Private helper method for registering entities with eggs; keeps things neater. For some reason, Forge 1.11.2
	 * wants a ResourceLocation and a string name... probably because it's transitioning to the registry system.
	 */
	private static void registerEntityAndEgg(Class<? extends Entity> entityClass, String name, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggColour, int spotColour) {
		ResourceLocation registryName = new ResourceLocation(ArcaneEssentials.MODID, name);
		EntityRegistry.registerModEntity(registryName, entityClass, registryName.toString(), id, ArcaneEssentials.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		EntityRegistry.registerEgg(registryName, eggColour, spotColour);
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

	}

	public static void registerItems() {

	}

	public static void registerEntities() {
		int id = 0;
		registerEntity(EntityThunderBurst.class, "Thunder Burst", id++, 128, LIVING_UPDATE_INTERVAL, false);
		registerEntity(EntityLightningVortex.class, "Lightning Vortex", id++, 128, PROJECTILE_UPDATE_INTERVAL, true);
		registerEntity(EntityFlamePillar.class, "Flame Pillar", id++, 128, LIVING_UPDATE_INTERVAL, false);
		registerEntity(EntityFlamePillarSpawner.class, "Flame Pillar Spawner", id++, 128, PROJECTILE_UPDATE_INTERVAL, true);
		registerEntity(EntityMagicSpawner.class, "Magic Spawner", id++, 128, PROJECTILE_UPDATE_INTERVAL, true);
		registerEntity(EntityWhirlpool.class, "Whirlpool", id++, 128, LIVING_UPDATE_INTERVAL, false);
		registerEntity(EntityMagicBolt.class, "Magic Bolt", id++, 128, PROJECTILE_UPDATE_INTERVAL, true);
		registerEntity(EntityCycloneBolt.class, "Cyclone Bolt", id++, 128, PROJECTILE_UPDATE_INTERVAL, true);
		registerEntity(EntityCycloneShield.class, "Cylone Shield", id++, 128, LIVING_UPDATE_INTERVAL, false);
		registerEntity(EntitySolarBeam.class, "Solar Beam", id++, 128, LIVING_UPDATE_INTERVAL, false);


	}

	public static void registerLoot() {

	}

	public static void registerAll() {
		registerLoot();
		registerEntities();
		registerItems();
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event) {
		event.getRegistry().register(new CycloneBolt());
		event.getRegistry().register(new CycloneShield());
		event.getRegistry().register(new FirePledge());
		event.getRegistry().register(new InfernoPillar());
		event.getRegistry().register(new KaThwack());
		event.getRegistry().register(new LightningVortex());
		event.getRegistry().register(new OceanBurst());
		event.getRegistry().register(new RadianceStorm());
		event.getRegistry().register(new RadiantBeam());
		event.getRegistry().register(new SolarBeam());
		event.getRegistry().register(new StormBlink());
		event.getRegistry().register(new ThunderBurst());
		event.getRegistry().register(new Whirlpool());

	}
}
