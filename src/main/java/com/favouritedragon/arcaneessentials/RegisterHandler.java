package com.favouritedragon.arcaneessentials;

import com.favouritedragon.arcaneessentials.common.entity.*;
import com.favouritedragon.arcaneessentials.common.spell.air.CycloneBolt;
import com.favouritedragon.arcaneessentials.common.spell.air.CycloneShield;
import com.favouritedragon.arcaneessentials.common.spell.arcane.Zoom;
import com.favouritedragon.arcaneessentials.common.spell.divine.RadianceStorm;
import com.favouritedragon.arcaneessentials.common.spell.divine.RadiantBeam;
import com.favouritedragon.arcaneessentials.common.spell.divine.SaintessSun;
import com.favouritedragon.arcaneessentials.common.spell.divine.WaveOfRelief;
import com.favouritedragon.arcaneessentials.common.spell.earth.KaQuake;
import com.favouritedragon.arcaneessentials.common.spell.earth.Quake;
import com.favouritedragon.arcaneessentials.common.spell.earth.Shake;
import com.favouritedragon.arcaneessentials.common.spell.earth.SolarBeam;
import com.favouritedragon.arcaneessentials.common.spell.fire.*;
import com.favouritedragon.arcaneessentials.common.spell.ice.*;
import com.favouritedragon.arcaneessentials.common.spell.necromancy.*;
import com.favouritedragon.arcaneessentials.common.spell.sorcery.DragonRoar;
import com.favouritedragon.arcaneessentials.common.spell.sorcery.FlashStep;
import com.favouritedragon.arcaneessentials.common.spell.sorcery.UnrelentingForce;
import com.favouritedragon.arcaneessentials.common.spell.storm.LightningVortex;
import com.favouritedragon.arcaneessentials.common.spell.storm.StormBlink;
import com.favouritedragon.arcaneessentials.common.spell.storm.ThunderBurst;
import com.favouritedragon.arcaneessentials.common.spell.storm.ThunderingChain;
import com.favouritedragon.arcaneessentials.common.spell.water.*;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellBuff;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nonnull;

//@GameRegistry.ObjectHolder(ArcaneEssentials.MODID)
@Mod.EventBusSubscriber(modid = ArcaneEssentials.MODID)
public class RegisterHandler {
    private static final int LIVING_UPDATE_INTERVAL = 3;
    private static final int PROJECTILE_UPDATE_INTERVAL = 10;
    //Used for when spell properties are needed outside of the spell class
    public static Spell inferno_form = null;
    public static Spell frost_form = null;

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

        //Base classes
        registerEntity(EntityMagicConstruct.class, "MagicConstruct", ++id, 128, LIVING_UPDATE_INTERVAL, false);
        registerEntity(EntityMagicBolt.class, "MagicBolt", ++id, 128, PROJECTILE_UPDATE_INTERVAL, true);
        registerEntity(EntityMagicSpawner.class, "MagicSpawner", ++id, 128, LIVING_UPDATE_INTERVAL, true);

        //Constructs
        registerEntity(EntityThunderBurst.class, "ThunderBurst", ++id, 128, LIVING_UPDATE_INTERVAL, false);
        registerEntity(EntityLightningVortex.class, "LightningVortex", ++id, 128, PROJECTILE_UPDATE_INTERVAL, true);
        registerEntity(EntityFlamePillar.class, "FlamePillar", ++id, 128, LIVING_UPDATE_INTERVAL, false);
        registerEntity(EntityWhirlpool.class, "Whirlpool", ++id, 128, LIVING_UPDATE_INTERVAL, false);
        registerEntity(EntityCycloneShield.class, "CyloneShield", ++id, 128, LIVING_UPDATE_INTERVAL, false);
        registerEntity(EntitySolarBeam.class, "SolarBeam", ++id, 128, LIVING_UPDATE_INTERVAL, false);
        registerEntity(EntityMagicLightning.class, "MagicLightning", ++id, 128, LIVING_UPDATE_INTERVAL, false);
        registerEntity(EntitySaintessSun.class, "SaintessSun", ++id, 128, LIVING_UPDATE_INTERVAL, true);

        //Spawners
        registerEntity(EntityFlamePillarSpawner.class, "FlamePillarSpawner", ++id, 128, PROJECTILE_UPDATE_INTERVAL, true);
        registerEntity(EntityLightningSpawner.class, "LightningSpawner", ++id, 128, LIVING_UPDATE_INTERVAL, true);
        registerEntity(EntityFallingBlockSpawner.class, "BlockSpawner", ++id, 128, LIVING_UPDATE_INTERVAL, true);

        //Projectiles
        registerEntity(EntityCycloneBolt.class, "CycloneBolt", ++id, 128, PROJECTILE_UPDATE_INTERVAL, true);
        registerEntity(EntityFireball.class, "Fireball", ++id, 128, PROJECTILE_UPDATE_INTERVAL, true);
        registerEntity(EntityFlameSlash.class, "FlameSlash", ++id, 128, PROJECTILE_UPDATE_INTERVAL, true);
        registerEntity(EntityFloatingBlock.class, "FloatingBlock", ++id, 128, PROJECTILE_UPDATE_INTERVAL, true);
        registerEntity(EntityWaterBall.class, "Waterball", ++id, 128, PROJECTILE_UPDATE_INTERVAL, true);

    }

    //TIL Why you have static final spells.

    public static void registerLoot() {

    }

    public static void registerAll() {
        registerLoot();
        registerEntities();
        registerItems();

    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Spell> event) {
        event.getRegistry().register(new AbsoluteZero());
        event.getRegistry().register(new BlizzardBeam());
        event.getRegistry().register(new CycloneBolt());
        event.getRegistry().register(new CycloneShield());
        event.getRegistry().register(new DragonRoar());
        event.getRegistry().register(new FirePledge());
        event.getRegistry().register(new FlameCleave());
        event.getRegistry().register(new FlashStep());
        event.getRegistry().register(new Frizz());
        event.getRegistry().register(new Frizzle());
        event.getRegistry().register(new FrostFan());
        event.getRegistry().register(new FrostBlast());
        //event.getRegistry().register(new FrostFan());
        event.getRegistry().register(frost_form = new FrostForm());
        event.getRegistry().register(new IcicleStorm());
        event.getRegistry().register(inferno_form = new InfernoForm());
        event.getRegistry().register(new InfernoPillar());
        event.getRegistry().register(new KaFrizz());
        event.getRegistry().register(new KaFrizzle());
        event.getRegistry().register(new KaQuake());
        event.getRegistry().register(new KaSploosh());
        event.getRegistry().register(new KaSplooshle());
        event.getRegistry().register(new KaThwack());
        event.getRegistry().register(new KaZam());
        event.getRegistry().register(new KaZammle());
        event.getRegistry().register(new LightningVortex());
        event.getRegistry().register(new OceanBurst());
        event.getRegistry().register(new SaintessSun());
        event.getRegistry().register(new SpellBuff(ArcaneEssentials.MODID,"oomph", 1F, 90 / 255F, 0,
                () -> MobEffects.STRENGTH).soundValues(1, 0.8f, 0.2f));
        event.getRegistry().register(new Quake());
        event.getRegistry().register(new RadianceStorm());
        event.getRegistry().register(new RadiantBeam());
        event.getRegistry().register(new Shake());
        event.getRegistry().register(new Shatter());
        event.getRegistry().register(new SolarBeam());
        event.getRegistry().register(new Splash());
        event.getRegistry().register(new Sploosh());
        event.getRegistry().register(new StormBlink());
        event.getRegistry().register(new ThunderBurst());
        event.getRegistry().register(new ThunderingChain());
        event.getRegistry().register(new UnrelentingForce());
        event.getRegistry().register(new WaveOfRelief());
        event.getRegistry().register(new Whirlpool());
        event.getRegistry().register(new Zam());
        event.getRegistry().register(new Zammle());
        event.getRegistry().register(new Zoom());

    }


    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static <T> T placeholder() {
        return null;
    }
}
