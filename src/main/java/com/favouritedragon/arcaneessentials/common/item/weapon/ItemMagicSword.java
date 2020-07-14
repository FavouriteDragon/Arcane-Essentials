package com.favouritedragon.arcaneessentials.common.item.weapon;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.SpellUtils;
import com.google.common.collect.Multimap;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.data.SpellGlyphData;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.item.IWorkbenchItem;
import electroblob.wizardry.packet.PacketCastSpell;
import electroblob.wizardry.packet.WizardryPacketHandler;
import electroblob.wizardry.registry.*;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

import static com.favouritedragon.arcaneessentials.common.spell.arcane.ElementArcane.ARCANE;

public class ItemMagicSword extends ItemSword implements IWorkbenchItem, ISpellCastingItem, IManaStoringItem {
	//TODO: Fix spells casting when right-clicking workbenches.

	//It works! Gonna leave it alone for now until eb adds proper context support

	/**
	 * The number of spell slots a wand has with no attunement upgrades applied.
	 */
	private static final int BASE_SPELL_SLOTS = 3;

	/**
	 * The number of ticks between each time a continuous spell is added to the player's recently-cast spells.
	 */
	private static final int CONTINUOUS_TRACKING_INTERVAL = 20;
	/**
	 * The increase in progression for casting spells of the matching element.
	 */
	private static final float ELEMENTAL_PROGRESSION_MODIFIER = 1.5f;
	/**
	 * The fraction of progression lost when all recently-cast spells are the same as the one being cast.
	 */
	private static final float MAX_PROGRESSION_REDUCTION = 0.8f;

	public Tier tier;
	public Element element;

	public ItemMagicSword(ToolMaterial material, Tier tier, Element element) {
		super(material);
		setMaxStackSize(1);
		setCreativeTab(WizardryTabs.GEAR);
		this.tier = tier;
		this.element = element;
		//Lower max charge for swords
		setMaxDamage((int) (this.tier.maxCharge * 0.75));
		WizardryRecipes.addToManaFlaskCharging(this);
	}

	/**
	 * Distributes the given cost (which should be the per-second cost of a continuous spell) over a second and
	 * returns the appropriate cost to be applied for the given tick. Currently the cost is distributed over 2
	 * intervals per second, meaning the returned value is 0 unless {@code castingTick} is a multiple of 10.
	 */
	protected static int getDistributedCost(int cost, int castingTick) {

		int partialCost;

		if (castingTick % 20 == 0) { // Whole number of seconds has elapsed
			partialCost = cost / 2 + cost % 2; // Make sure cost adds up to the correct value by adding the remainder here
		} else if (castingTick % 10 == 0) { // Something-and-a-half seconds has elapsed
			partialCost = cost / 2;
		} else { // Some other number of ticks has elapsed
			partialCost = 0; // Wands aren't damaged within half-seconds
		}

		return partialCost;
	}

	//hitEntity is only called server-side, so we'll have to use events
	@SubscribeEvent
	public static void onAttackEntityEvent(AttackEntityEvent event) {

		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand(); // Can't melee with offhand items

		if (stack.getItem() instanceof IManaStoringItem) {

			// Nobody said it had to be a wand, as long as it's got a melee upgrade it counts
			int level = WandHelper.getUpgradeLevel(stack, WizardryItems.melee_upgrade);
			int mana = ((IManaStoringItem) stack.getItem()).getMana(stack);

			if (level > 0 && mana > 0) {

				Random random = player.world.rand;

				player.world.playSound(player.posX, player.posY, player.posZ, WizardrySounds.ITEM_WAND_MELEE, SoundCategory.PLAYERS, 0.75f, 1, false);

				if (player.world.isRemote) {

					Vec3d origin = new Vec3d(player.posX, player.getEntityBoundingBox().minY + player.getEyeHeight(), player.posZ);
					Vec3d hit = origin.add(player.getLookVec().scale(player.getDistance(event.getTarget())));
					// Generate two perpendicular vectors in the plane perpendicular to the look vec
					Vec3d vec1 = player.getLookVec().rotatePitch(90);
					Vec3d vec2 = player.getLookVec().crossProduct(vec1);

					for (int i = 0; i < 15; i++) {
						ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(hit)
								.vel(vec1.scale(random.nextFloat() * 0.3f - 0.15f).add(vec2.scale(random.nextFloat() * 0.3f - 0.15f)))
								.clr(1f, 1f, 1f).fade(0.3f, 0.5f, 1)
								.time(8 + random.nextInt(4)).spawn(player.world);
					}
				}
			}
		}
	}

	@Nonnull
	@Override
	public Spell getCurrentSpell(ItemStack stack) {
		return WandHelper.getCurrentSpell(stack);
	}

	@Override
	public Spell[] getSpells(ItemStack stack) {
		return WandHelper.getSpells(stack);
	}

	@Override
	public void selectNextSpell(ItemStack stack) {
		WandHelper.selectNextSpell(stack);
	}

	@Override
	public void selectPreviousSpell(ItemStack stack) {
		WandHelper.selectPreviousSpell(stack);
	}

	@Override
	public boolean showSpellHUD(EntityPlayer player, ItemStack stack) {
		return true;
	}

	@Override
	public boolean showTooltip(ItemStack stack) {
		return true;
	}

	/**
	 * Does nothing, use {@link ItemMagicSword#setMana(ItemStack, int)} to modify wand mana.
	 */
	@Override
	public void setDamage(ItemStack stack, int damage) {
		// Overridden to do nothing to stop repair things from 'repairing' the mana in a wand
	}

	@Override
	public void setMana(ItemStack stack, int mana) {
		// Using super (which can only be done from in here) bypasses the above override
		super.setDamage(stack, getManaCapacity(stack) - mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return getManaCapacity(stack) - getDamage(stack);
	}

	@Override
	public int getManaCapacity(ItemStack stack) {
		return this.getMaxDamage(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.client.gui.FontRenderer getFontRenderer(ItemStack stack) {
		return Wizardry.proxy.getFontRenderer(stack);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return !Wizardry.settings.legacyWandLevelling && this.tier.level < Tier.MASTER.level
				&& WandHelper.getProgression(stack) >= Tier.values()[tier.ordinal() + 1].progression;
	}

	// Max damage is modifiable with upgrades.
	@Override
	public int getMaxDamage(ItemStack stack) {
		// + 0.5f corrects small float errors rounding down
		return (int) (super.getMaxDamage(stack) * (1.0f + Constants.STORAGE_INCREASE_PER_LEVEL
				* WandHelper.getUpgradeLevel(stack, WizardryItems.storage_upgrade)) + 0.5f);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		setMana(stack, 0); // Wands are empty when first crafted
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld) {

		WandHelper.decrementCooldowns(stack);

		// Decrements wand damage (increases mana) every 1.5 seconds if it has a condenser upgrade
		if (!world.isRemote && !this.isManaFull(stack) && world.getTotalWorldTime() % Constants.CONDENSER_TICK_INTERVAL == 0) {
			// If the upgrade level is 0, this does nothing anyway.
			this.rechargeMana(stack, WandHelper.getUpgradeLevel(stack, WizardryItems.condenser_upgrade));
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			int level = WandHelper.getUpgradeLevel(stack, WizardryItems.melee_upgrade);
			// This check doesn't affect the damage output, but it does stop a blank line from appearing in the tooltip.
			if (level > 0 && !this.isManaEmpty(stack)) {
				multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
						new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Melee upgrade modifier", level + (tier.level + 1) + getAttackDamage(), 0));
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Melee upgrade modifier", -2.4000000953674316D, 0));
			}
		}

		return multimap;
	}

	// A proper hook was introduced for this in Forge build 14.23.5.2805 - Hallelujah, finally!
	// The discussion about this was quite interesting, see the following:
	// https://github.com/TeamTwilight/twilightforest/blob/1.12.x/src/main/java/twilightforest/item/ItemTFScepterLifeDrain.java
	// https://github.com/MinecraftForge/MinecraftForge/pull/4834
	// Among the things mentioned were that it can be 'fixed' by doing the exact same hacks that I did, and that
	// returning a result of PASS rather than SUCCESS from onItemRightClick also solves the problem (not sure why
	// though, and again it's not a perfect solution)
	// Edit: It seems that the hacky fix in previous versions actually introduced a wand duplication bug... oops

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase wielder) {

		int mana = this.getMana(stack);

		if (mana > 0) this.consumeMana(stack, 1, wielder);

		return true;
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		return WandHelper.getUpgradeLevel(stack, WizardryItems.melee_upgrade) == 0;
	}

	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		// Ignore durability changes
		if (ItemStack.areItemsEqualIgnoreDurability(oldStack, newStack)) return true;
		return super.canContinueUsing(oldStack, newStack);
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
		// Ignore durability changes
		if (ItemStack.areItemsEqualIgnoreDurability(oldStack, newStack)) return false;
		return super.shouldCauseBlockBreakReset(oldStack, newStack);
	}

	@Override
	// Only called client-side
	// This method is always called on the item in oldStack, meaning that oldStack.getItem() == this
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

		// This method does some VERY strange things! Despite its name, it also seems to affect the updating of NBT...

		if (!oldStack.isEmpty() || !newStack.isEmpty()) {
			// We only care about the situation where we specifically want the animation NOT to play.
			if (oldStack.getItem() == newStack.getItem() && !slotChanged && oldStack.getItem() instanceof ItemSword
					&& newStack.getItem() instanceof ItemSword
					&& WandHelper.getCurrentSpell(oldStack) == WandHelper.getCurrentSpell(newStack))
				return false;
		}

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack) {
		return WandHelper.getCurrentSpell(itemstack).action;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> text, net.minecraft.client.util.ITooltipFlag advanced) {

		EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;
		if (player == null) {
			return;
		}
		// +0.5f is necessary due to the error in the way floats are calculated.
		if (element != null)
			text.add("\u00A78" + net.minecraft.client.resources.I18n.format("item." + ArcaneEssentials.MODID + ":sword.buff",
					(int) ((tier.level + 1) * Constants.POTENCY_INCREASE_PER_TIER * 100 + 0.5f) + "%",
					element.getDisplayName()));

		Spell spell = WandHelper.getCurrentSpell(stack);

		boolean discovered = true;
		if (Wizardry.settings.discoveryMode && !player.isCreative() && WizardData.get(player) != null
				&& !WizardData.get(player).hasSpellBeenDiscovered(spell)) {
			discovered = false;
		}

		text.add("\u00A77" + net.minecraft.client.resources.I18n.format("item." + ArcaneEssentials.MODID + ":sword.spell",
				discovered ? "\u00A77" + spell.getDisplayNameWithFormatting()
						: "#\u00A79" + SpellGlyphData.getGlyphName(spell, player.world)));

		if (advanced.isAdvanced()) {
			// Advanced tooltips for debugging
			text.add("\u00A79" + net.minecraft.client.resources.I18n.format("item." + ArcaneEssentials.MODID + ":sword.mana",
					this.getMana(stack), this.getManaCapacity(stack)));

			text.add("\u00A77" + net.minecraft.client.resources.I18n.format("item." + ArcaneEssentials.MODID + ":sword.progression",
					WandHelper.getProgression(stack), this.tier.level < Tier.MASTER.level ? Tier.values()[tier.ordinal() + 1].progression : 0));

//		}else{
//
//			ChargeStatus status = ChargeStatus.getChargeStatus(stack);
//			text.add(status.getFormattingCode() + status.getDisplayName());
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return (this.element == null ? "" : this.element.getFormattingCode()) + super.getItemStackDisplayName(stack);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {

		// Alternate left-click functions for spells
		EnumHand hand = EnumHand.MAIN_HAND;
		if (entityLiving instanceof EntityPlayer) {
			Spell spell = WandHelper.getCurrentSpell(stack);
			SpellModifiers modifiers = this.calculateModifiers(stack, (EntityPlayer) entityLiving, spell);

			if (canCast(stack, spell, (EntityPlayer) entityLiving, hand, 0, modifiers) && !SpellUtils.rightClickCastable(spell)) {
				// Now we can cast continuous spells with scrolls!
				if (spell.isContinuous) {
					if (!entityLiving.isHandActive()) {
						entityLiving.setActiveHand(hand);
						// Store the modifiers for use each tick
						if (WizardData.get((EntityPlayer) entityLiving) != null)
							WizardData.get((EntityPlayer) entityLiving).itemCastingModifiers = modifiers;
						// Return the player's held item so spells can change it if they wish (e.g. possession)
						return true;
					}
				} else {
					return cast(stack, spell, (EntityPlayer) entityLiving, hand, 0, modifiers);
				}
			}
		}

		return false;
	}

	// Continuous spells use the onUsingItemTick method instead of this one.
	/* An important thing to note about this method: it is only called on the server and the client of the player
	 * holding the item (I call this client-inconsistency). This means if you spawn particles here they will not show up
	 * on other players' screens. Instead, this must be done via packets. */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		// Alternate right-click function; overrides spell casting.
		if (this.selectMinionTarget(player, world)) return new ActionResult<>(EnumActionResult.SUCCESS, stack);

		Spell spell = WandHelper.getCurrentSpell(stack);
		SpellModifiers modifiers = this.calculateModifiers(stack, player, spell);

		if (canCast(stack, spell, player, hand, 0, modifiers) && SpellUtils.rightClickCastable(spell)) {
			// Now we can cast continuous spells with scrolls!
			if (spell.isContinuous) {
				if (!player.isHandActive()) {
					player.setActiveHand(hand);
					// Store the modifiers for use each tick
					if (WizardData.get(player) != null) WizardData.get(player).itemCastingModifiers = modifiers;
					// Return the player's held item so spells can change it if they wish (e.g. possession)
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
				}
			} else {
				if (cast(stack, spell, player, hand, 0, modifiers)) {
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
				}
			}
		}

		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	// For continuous spells. The count argument actually decrements by 1 each tick.
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase user, int count) {

		if (user instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) user;

			Spell spell = WandHelper.getCurrentSpell(stack);

			SpellModifiers modifiers;

			if (WizardData.get(player) != null) {
				modifiers = WizardData.get(player).itemCastingModifiers;
			} else {
				modifiers = this.calculateModifiers(stack, (EntityPlayer) user, spell); // Fallback to the old way, should never be used
			}

			int castingTick = stack.getMaxItemUseDuration() - count;

			// Continuous spells (these must check if they can be cast each tick since the mana changes)
			// Don't call canCast when castingTick == 0 because we already did it in onItemRightClick
			if (spell.isContinuous && (castingTick == 0 || canCast(stack, spell, player, player.getActiveHand(), castingTick, modifiers))) {
				cast(stack, spell, player, player.getActiveHand(), castingTick, modifiers);
			} else {
				// Stops the casting if it was interrupted, either by events or because the wand ran out of mana
				player.stopActiveHand();
			}
		}
	}

	@Override
	public boolean canCast(ItemStack stack, Spell spell, EntityPlayer caster, EnumHand hand, int castingTick, SpellModifiers modifiers) {

		// Spells can only be cast if the casting events aren't cancelled...
		if (castingTick == 0) {
			if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(SpellCastEvent.Source.OTHER, spell, caster, modifiers)))
				return false;
		} else {
			if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Tick(SpellCastEvent.Source.OTHER, spell, caster, modifiers, castingTick)))
				return false;
		}

		int cost = (int) (spell.getCost() * modifiers.get(SpellModifiers.COST));

		// As of wizardry 4.2 mana cost is only divided over two intervals each second
		if (spell.isContinuous) cost = getDistributedCost(cost, castingTick);

		//TODO: Add sword spell property so I can check whether the spell can be cast by a sword.

		// ...and the sword has enough mana to cast the spell...
		return cost <= this.getMana(stack) // This comes first because it changes over time
				// ...and the wand is the same tier as the spell or higher...
				&& spell.getTier().level <= this.tier.level
				// ...and either the spell is not in cooldown or the player is in creative mode
				&& (WandHelper.getCurrentCooldown(stack) == 0 || caster.isCreative())
				//And the spell is castable by swords
				&& SpellUtils.isSwordCastable(spell)
				//And the spell is the same element, or the generic one
				&& spell.getElement() == this.element || spell.getElement().equals(Element.MAGIC) || spell.getElement().equals(ARCANE);
	}

	@Override
	public boolean cast(ItemStack stack, Spell spell, EntityPlayer caster, EnumHand hand, int castingTick, SpellModifiers modifiers) {

		World world = caster.world;

		if (world.isRemote && !spell.isContinuous && spell.requiresPacket()) return false;

		if (spell.cast(world, caster, hand, castingTick, modifiers)) {

			if (castingTick == 0)
				MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.OTHER, spell, caster, modifiers));

			if (!world.isRemote) {

				// Continuous spells never require packets so don't rely on the requiresPacket method to specify it
				if (!spell.isContinuous && spell.requiresPacket()) {
					// Sends a packet to all players in dimension to tell them to spawn particles.
					IMessage msg = new PacketCastSpell.Message(caster.getEntityId(), hand, spell, modifiers);
					WizardryPacketHandler.net.sendToDimension(msg, world.provider.getDimension());
				}

				caster.setActiveHand(hand);

				// Mana cost
				int cost = (int) (spell.getCost() * modifiers.get(SpellModifiers.COST));
				// As of wizardry 4.2 mana cost is only divided over two intervals each second
				if (spell.isContinuous) cost = getDistributedCost(cost, castingTick);

				if (cost > 0) this.consumeMana(stack, cost, caster);

			}

			// Cooldown
			if (!spell.isContinuous && !caster.isCreative()) { // Spells only have a cooldown in survival
				WandHelper.setCurrentCooldown(stack, (int) (spell.getCooldown() * modifiers.get(WizardryItems.cooldown_upgrade)));
			}

			// Progression
			if (this.tier.level < Tier.MASTER.level && castingTick % CONTINUOUS_TRACKING_INTERVAL == 0) {

				// We don't care about cost modifiers here, otherwise players would be penalised for wearing robes!
				int progression = (int) (spell.getCost() * modifiers.get(SpellModifiers.PROGRESSION));
				WandHelper.addProgression(stack, progression);

				if (!Wizardry.settings.legacyWandLevelling) { // Don't display the message if legacy wand levelling is enabled
					// If the wand just gained enough progression to be upgraded...
					Tier nextTier = Tier.values()[tier.ordinal() + 1];
					int excess = WandHelper.getProgression(stack) - nextTier.progression;
					if (excess >= 0 && excess < progression) {
						// ...display a message above the player's hotbar
						caster.playSound(WizardrySounds.ITEM_WAND_LEVELUP, 1.25f, 1);
						if (!world.isRemote)
							caster.sendMessage(new TextComponentTranslation("item." + ArcaneEssentials.MODID + ":sword.levelup",
									this.getItemStackDisplayName(stack), nextTier.getNameForTranslationFormatted()));
					}
				}

				WizardData.get(caster).trackRecentSpell(spell);
			}

			return true;
		}

		return false;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase user, int timeLeft) {

		if (user instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) user;

			Spell spell = WandHelper.getCurrentSpell(stack);

			SpellModifiers modifiers;

			if (WizardData.get(player) != null) {
				modifiers = WizardData.get(player).itemCastingModifiers;
			} else {
				modifiers = this.calculateModifiers(stack, (EntityPlayer) user, spell); // Fallback to the old way, should never be used
			}

			int castingTick = stack.getMaxItemUseDuration() - timeLeft; // Might as well include this

			int cost = getDistributedCost((int) (spell.getCost() * modifiers.get(SpellModifiers.COST)), castingTick);

			// Still need to check there's enough mana or the spell will finish twice, since running out of mana is
			// handled separately.
			if (spell.isContinuous && spell.getTier().level <= this.tier.level && cost <= this.getMana(stack)) {

				MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Finish(SpellCastEvent.Source.OTHER, spell, player, modifiers, castingTick));
				spell.finishCasting(world, player, Double.NaN, Double.NaN, Double.NaN, null, castingTick, modifiers);

				if (!player.isCreative()) { // Spells only have a cooldown in survival
					WandHelper.setCurrentCooldown(stack, (int) (spell.getCooldown() * modifiers.get(WizardryItems.cooldown_upgrade)));
				}
			}
		}
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {

		if (player.isSneaking() && entity instanceof EntityPlayer && WizardData.get(player) != null) {
			String string = WizardData.get(player).toggleAlly((EntityPlayer) entity) ? "item." + Wizardry.MODID + ":wand.addally"
					: "item." + Wizardry.MODID + ":wand.removeally";
			if (!player.world.isRemote) player.sendMessage(new TextComponentTranslation(string, entity.getName()));
			return true;
		}

		return false;
	}

	/**
	 * Returns a SpellModifiers object with the appropriate modifiers applied for the given ItemStack and Spell.
	 */
	// This is now public because artefacts use it
	public SpellModifiers calculateModifiers(ItemStack stack, EntityPlayer player, Spell spell) {

		SpellModifiers modifiers = new SpellModifiers();

		// Now we only need to add multipliers if they are not 1.
		int level = WandHelper.getUpgradeLevel(stack, WizardryItems.range_upgrade);
		if (level > 0)
			modifiers.set(WizardryItems.range_upgrade, 1.0f + level * Constants.RANGE_INCREASE_PER_LEVEL, true);

		level = WandHelper.getUpgradeLevel(stack, WizardryItems.duration_upgrade);
		if (level > 0)
			modifiers.set(WizardryItems.duration_upgrade, 1.0f + level * Constants.DURATION_INCREASE_PER_LEVEL, false);

		level = WandHelper.getUpgradeLevel(stack, WizardryItems.blast_upgrade);
		if (level > 0)
			modifiers.set(WizardryItems.blast_upgrade, 1.0f + level * Constants.BLAST_RADIUS_INCREASE_PER_LEVEL, true);

		level = WandHelper.getUpgradeLevel(stack, WizardryItems.cooldown_upgrade);
		if (level > 0)
			modifiers.set(WizardryItems.cooldown_upgrade, 1.0f - level * Constants.COOLDOWN_REDUCTION_PER_LEVEL, true);

		float progressionModifier = 1.0f - ((float) WizardData.get(player).countRecentCasts(spell) / WizardData.MAX_RECENT_SPELLS)
				* MAX_PROGRESSION_REDUCTION;

		if (this.element == spell.getElement()) {
			modifiers.set(SpellModifiers.POTENCY, 1.0f + (this.tier.level + 1) * Constants.POTENCY_INCREASE_PER_TIER, true);
			progressionModifier *= ELEMENTAL_PROGRESSION_MODIFIER;
		}

		modifiers.set(SpellModifiers.PROGRESSION, progressionModifier, false);

		return modifiers;
	}

	// Workbench stuff

	private boolean selectMinionTarget(EntityPlayer player, World world) {

		RayTraceResult rayTrace = RayTracer.standardEntityRayTrace(world, player, 16, false);

		if (rayTrace != null && WizardryUtilities.isLiving(rayTrace.entityHit)) {

			EntityLivingBase entity = (EntityLivingBase) rayTrace.entityHit;

			// Sets the selected minion's target to the right-clicked entity
			if (player.isSneaking() && WizardData.get(player) != null && WizardData.get(player).selectedMinion != null) {

				ISummonedCreature minion = WizardData.get(player).selectedMinion.get();

				if (minion instanceof EntityLiving && minion != entity) {
					// There is now only the new AI! (which greatly improves things)
					((EntityLiving) minion).setAttackTarget(entity);
					// Deselects the selected minion
					WizardData.get(player).selectedMinion = null;
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public int getSpellSlotCount(ItemStack stack) {
		int slotModifier = this.tier.level > 0 ? this.tier.level - 1 : 0;
		return BASE_SPELL_SLOTS + WandHelper.getUpgradeLevel(stack, WizardryItems.attunement_upgrade) - 2 + slotModifier;
	}

	@Override
	public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {

		boolean changed = false;

		// Upgrades wand if necessary. Damage is copied, preserving remaining durability,
		// and also the entire NBT tag compound.
		if (upgrade.getStack().getItem() == WizardryItems.arcane_tome) {

			Tier tier = Tier.values()[upgrade.getStack().getItemDamage()];

			// Checks the wand upgrade is for the tier above the wand's tier, and that either the wand has enough
			// progression or the player is in creative mode.
			// It is guaranteed that: this == centre.getStack().getItem()
			if ((player.isCreative() || Wizardry.settings.legacyWandLevelling
					|| WandHelper.getProgression(centre.getStack()) >= tier.progression)
					&& tier.ordinal() - 1 == this.tier.ordinal()) {

				// We're not carrying over excess progression for now, but if we do want to, this is how
//				if(!Wizardry.settings.legacyWandLevelling){
//					// Easy way to carry excess progression over to the new stack
//					WandHelper.setProgression(centre.getStack(), WandHelper.getProgression(centre.getStack()) - tier.progression);
//				}

				ItemStack newWand = new ItemStack(WizardryItems.getWand(tier, this.element));
				newWand.setTagCompound(centre.getStack().getTagCompound());
				// This needs to be done after copying the tag compound so the mana capacity for the new wand
				// takes storage upgrades into account
				// Note the usage of the new wand item and not 'this' to ensure the correct capacity is used
				((IManaStoringItem) newWand.getItem()).setMana(newWand, this.getMana(centre.getStack()));

				centre.putStack(newWand);
				upgrade.decrStackSize(1);

				changed = true;
			}

		} else if (WandHelper.isWandUpgrade(upgrade.getStack().getItem())) {

			// Special upgrades
			Item specialUpgrade = upgrade.getStack().getItem();

			if (WandHelper.getTotalUpgrades(centre.getStack()) < this.tier.upgradeLimit
					&& WandHelper.getUpgradeLevel(centre.getStack(), specialUpgrade) < Constants.UPGRADE_STACK_LIMIT) {

				// Used to preserve existing mana when upgrading storage rather than creating free mana.
				int prevMana = this.getMana(centre.getStack());

				WandHelper.applyUpgrade(centre.getStack(), specialUpgrade);

				// Special behaviours for specific upgrades
				if (specialUpgrade == WizardryItems.storage_upgrade) {

					this.setMana(centre.getStack(), prevMana);

				} else if (specialUpgrade == WizardryItems.attunement_upgrade) {

					int slotModifier = this.tier.level > 0 ? this.tier.level - 1 : 0;
					int newSlotCount = BASE_SPELL_SLOTS + WandHelper.getUpgradeLevel(centre.getStack(),
							WizardryItems.attunement_upgrade) - 2 + slotModifier;

					Spell[] spells = WandHelper.getSpells(centre.getStack());
					Spell[] newSpells = new Spell[newSlotCount];

					for (int i = 0; i < newSpells.length; i++) {
						newSpells[i] = i < spells.length && spells[i] != null ? spells[i] : Spells.none;
					}

					WandHelper.setSpells(centre.getStack(), newSpells);

					int[] cooldowns = WandHelper.getCooldowns(centre.getStack());
					int[] newCooldowns = new int[newSlotCount];

					if (cooldowns.length > 0) {
						System.arraycopy(cooldowns, 0, newCooldowns, 0, cooldowns.length);
					}

					WandHelper.setCooldowns(centre.getStack(), newCooldowns);
				}

				upgrade.decrStackSize(1);
				WizardryAdvancementTriggers.special_upgrade.triggerFor(player);

				if (WandHelper.getTotalUpgrades(centre.getStack()) == Tier.MASTER.upgradeLimit) {
					WizardryAdvancementTriggers.max_out_wand.triggerFor(player);
				}

				changed = true;
			}
		}

		// Reads NBT spell metadata array to variable, edits this, then writes it back to NBT.
		// Original spells are preserved; if a slot is left empty the existing spell binding will remain.
		// Accounts for spells which cannot be applied because they are above the wand's tier; these spells
		// will not bind but the existing spell in that slot will remain and other applicable spells will
		// be bound as normal, along with any upgrades and crystals.
		Spell[] spells = WandHelper.getSpells(centre.getStack());

		if (spells.length <= 0) {
			// Base value here because if the spell array doesn't exist, the wand can't possibly have attunement upgrades
			spells = new Spell[BASE_SPELL_SLOTS];
		}

		for (int i = 0; i < spells.length; i++) {
			if (spellBooks[i].getStack() != ItemStack.EMPTY) {

				Spell spell = Spell.byMetadata(spellBooks[i].getStack().getItemDamage());
				// If the wand is powerful enough for the spell, it's not already bound to that slot and it's enabled for wands
				//TODO: Change the context to swords, add a fail message
				if (!(spell.getTier().level > this.tier.level) && spells[i] != spell && SpellUtils.isSwordCastable(spell) &&
						(spell.getElement() == this.element || spell.getElement().equals(Element.MAGIC))) {
					spells[i] = spell;
					changed = true;
				}
				else player.sendMessage(new TextComponentTranslation(I18n.format("This spell is incompatible with that sword!", TextFormatting.BOLD)));
			}
		}

		WandHelper.setSpells(centre.getStack(), spells);

		// Charges wand by appropriate amount
		if (crystals.getStack() != ItemStack.EMPTY && !this.isManaFull(centre.getStack())) {

			int chargeDepleted = this.getManaCapacity(centre.getStack()) - this.getMana(centre.getStack());

			int manaPerItem = Constants.MANA_PER_CRYSTAL;
			if (crystals.getStack().getItem() == WizardryItems.crystal_shard) manaPerItem = Constants.MANA_PER_SHARD;
			if (crystals.getStack().getItem() == WizardryItems.grand_crystal)
				manaPerItem = Constants.GRAND_CRYSTAL_MANA;

			if (crystals.getStack().getCount() * manaPerItem < chargeDepleted) {
				// If there aren't enough crystals to fully charge the wand
				this.rechargeMana(centre.getStack(), crystals.getStack().getCount() * manaPerItem);
				crystals.decrStackSize(crystals.getStack().getCount());

			} else {
				// If there are excess crystals (or just enough)
				this.setMana(centre.getStack(), this.getManaCapacity(centre.getStack()));
				crystals.decrStackSize((int) Math.ceil(((double) chargeDepleted) / manaPerItem));
			}

			changed = true;
		}

		return changed;
	}


}