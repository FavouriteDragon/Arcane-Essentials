package com.favouritedragon.arcaneessentials.common.potion;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@GameRegistry.ObjectHolder(ArcaneEssentials.MODID)
public class ArcanePotions {

	public static final Potion infernoForm = new PotionInfernoForm(false, 0xFc191);

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

		registerPotion(registry, "inferno_form", infernoForm); // Colour was 0x38ddec (was arbitrary anyway)


		/*registerPotion(registry, "fireskin", new PotionMagicEffectParticles(false, 0,
				new ResourceLocation(Wizardry.MODID, "textures/gui/potion_icon_fireskin.png")) {
			@Override
			public void spawnCustomParticle(World world, double x, double y, double z) {
				world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0, 0, 0);
			}

			@Override
			public void performEffect(EntityLivingBase entitylivingbase, int strength) {
				entitylivingbase.extinguish(); // Stops melee mobs that are on fire from setting the player on fire,
				// without allowing the player to actually stand in fire or swim in lava without taking damage.
			}
		}.setBeneficial()); // 0xff2f02**/



	}
}
