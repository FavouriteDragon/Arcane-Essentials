package com.favouritedragon.arcaneessentials.common.potion;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

//@GameRegistry.ObjectHolder(ArcaneEssentials.MODID)
public class ArcanePotions {

	public static final Potion infernoForm = new PotionInfernoForm(false, 0xFc191);
	public static final Potion frostForm = new PotionFrostForm(false, 0xCDFEFF);

	private ArcanePotions() {
	} // No instances!

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() {
		return null;
	}

	/**
	 * Sets both the registry and unlocalised names of the given potion, then registers it with the given registry. Use
	 * this instead of {@link net.minecraft.potion.Potion#setRegistryName(String)} and {@link net.minecraft.potion.Potion#setPotionName(String)} during
	 * construction, for convenience and consistency.
	 *
	 * @param registry The registry to register the given potion to.
	 * @param name     The name of the potion, without the mod ID or the .name stuff. The registry name will be
	 *                 {@code arcane_essentials:[name]}. The unlocalised name will be {@code potion.arcane_essentials:[name].name}.
	 * @param potion   The potion to register.
	 */
	public static void registerPotion(IForgeRegistry<Potion> registry, String name, Potion potion) {
		potion.setRegistryName(ArcaneEssentials.MODID, name);
		// For some reason, Potion#getName() doesn't prepend "potion." itself, so it has to be done here.
		potion.setPotionName("potion." + potion.getRegistryName().toString());
		registry.register(potion);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Potion> event) {

		IForgeRegistry<Potion> registry = event.getRegistry();

		// Interestingly, setting the colour to black stops the particles from rendering.

		registerPotion(registry, "inferno_form", infernoForm);
		registerPotion(registry, "frost_form", frostForm);

	}
}
