package com.favouritedragon.arcaneessentials;

import com.favouritedragon.arcaneessentials.common.entity.*;
import com.favouritedragon.arcaneessentials.common.spell.air.CycloneBolt;
import com.favouritedragon.arcaneessentials.common.spell.air.CycloneShield;
import com.favouritedragon.arcaneessentials.common.spell.divine.RadianceStorm;
import com.favouritedragon.arcaneessentials.common.spell.divine.RadiantBeam;
import com.favouritedragon.arcaneessentials.common.spell.earth.SolarBeam;
import com.favouritedragon.arcaneessentials.common.spell.fire.*;
import com.favouritedragon.arcaneessentials.common.spell.ice.BlizzardBeam;
import com.favouritedragon.arcaneessentials.common.spell.necromancy.*;
import com.favouritedragon.arcaneessentials.common.spell.storm.LightningVortex;
import com.favouritedragon.arcaneessentials.common.spell.storm.StormBlink;
import com.favouritedragon.arcaneessentials.common.spell.storm.ThunderBurst;
import com.favouritedragon.arcaneessentials.common.spell.water.*;
import com.google.gson.JsonParseException;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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
		registerEntity(EntityFireball.class, "Fireball", id++, 128, PROJECTILE_UPDATE_INTERVAL, true);
		registerEntity(EntityWaterBall.class, "Waterball", id++, 128, PROJECTILE_UPDATE_INTERVAL, true);

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
		event.getRegistry().register(new BlizzardBeam());
		event.getRegistry().register(new CycloneBolt());
		event.getRegistry().register(new CycloneShield());
		event.getRegistry().register(new FirePledge());
		event.getRegistry().register(new Frizz());
		event.getRegistry().register(new Frizzle());
		event.getRegistry().register(new InfernoPillar());
		event.getRegistry().register(new KaFrizz());
		event.getRegistry().register(new KaFrizzle());
		event.getRegistry().register(new KaSploosh());
		event.getRegistry().register(new KaThwack());
		event.getRegistry().register(new KaZam());
		event.getRegistry().register(new KaZammle());
		event.getRegistry().register(new LightningVortex());
		event.getRegistry().register(new OceanBurst());
		event.getRegistry().register(new RadianceStorm());
		event.getRegistry().register(new RadiantBeam());
		event.getRegistry().register(new SolarBeam());
		event.getRegistry().register(new Splash());
		event.getRegistry().register(new Sploosh());
		event.getRegistry().register(new StormBlink());
		event.getRegistry().register(new ThunderBurst());
		event.getRegistry().register(new Whirlpool());
		event.getRegistry().register(new Zam());
		event.getRegistry().register(new Zammle());

	}

	//TODO: Debugged! It appears that something in the file finding process is going wrong- it's not even being run o_0.

	//For debugging; it's how eb gets spell property files
	public static boolean loadSpellProperties(String modID){

		// Yes, I know you're not supposed to do orElse(null). But... meh.
		ModContainer mod = Loader.instance().getModList().stream().filter(m -> m.getModId().equals(modID)).findFirst().orElse(null);

		if(mod == null){
			Wizardry.logger.warn("Tried to load spell properties for mod with ID '" + modID + "', but no such mod was loaded");
			return false; // Failed!
		}

		// Spells will be removed from this list as their properties are set
		// If everything works properly, it should be empty by the end
		List<Spell> spells = Spell.getSpells(s -> s.getRegistryName().getNamespace().equals(modID));
		if(modID.equals(Wizardry.MODID)) spells.add(Spells.none); // In this particular case we do need the none spell

		for (Spell s : spells) {
			ArcaneEssentials.logger.info(s.getRegistryName());
		}
		ArcaneEssentials.logger.info("Loading spell properties for " + spells.size() + " spells");

		// This method is used by Forge to load mod recipes and advancements, so it's a fair bet it's the right one
		// In the absence of Javadoc, here's what the non-obvious parameters do:
		// - preprocessor is called once with just the root directory, allowing any global index files to be processed
		// - processor is called once for each file in the directory so processing can be done
		// - defaultUnfoundRoot is the default value to return if the root specified isn't found
		// - visitAllFiles determines whether the method short-circuits; in other words, if the processor returns false
		// at any point and visitAllFiles is false, the method returns immediately.
		boolean success = CraftingHelper.findFiles(mod, "assets/" + modID + "/spells", null,

				(root, file) -> {

			ArcaneEssentials.logger.warn("WTFFFFFFF");
					String relative = root.relativize(file).toString();
					if(!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_")) {
						ArcaneEssentials.logger.warn("This isn't good");
						return true; // True or it'll look like it failed just because it found a non-JSON file
					}

					String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
					ResourceLocation key = new ResourceLocation(modID, name);

					Spell spell = Spell.registry.getValue(key);

					ArcaneEssentials.logger.info(spell);

					// If no spell matches a particular file, log it and just ignore the file
					if(spell == null){
						Wizardry.logger.info("Spell properties file " + name + ".json does not match any registered spells; ensure the filename is spelled correctly.");
						return true;
					}

					BufferedReader reader = null;

					// We want to do this regardless of whether the JSON file got read properly, because that prints its
					// own separate warning
					ArcaneEssentials.logger.info(key.getPath());
					if(!spells.remove(spell)) Wizardry.logger.warn("What's going on?!");

					try{

						reader = Files.newBufferedReader(file);
						ArcaneEssentials.logger.info(file);

						//JsonObject json = JsonUtils.fromJson(gson, reader, JsonObject.class);
					//	SpellProperties properties = new SpellProperties(json, spell);
					//	spell.setProperties(properties);

					}catch(JsonParseException jsonparseexception){
						Wizardry.logger.error("Parsing error loading spell property file for " + key, jsonparseexception);
						return false;
					}catch(IOException ioexception){
						Wizardry.logger.error("Couldn't read spell property file for " + key, ioexception);
						return false;
					}finally{
						IOUtils.closeQuietly(reader);
					}

					return true;

				},
				true, true);

		// If a spell is missing its file, log an error
		if(!spells.isEmpty()){
			if(spells.size() <= 15){
				spells.forEach(s -> Wizardry.logger.error("Spell " + s.getRegistryName() + " is missing a properties file!"));
			}else{
				// If there are more than 15 don't bother logging them all, chances are they're all missing
				Wizardry.logger.error("Mod " + modID + " has " + spells.size() + " spells that are missing properties files!");
			}
		}

		return success;
	}
}
