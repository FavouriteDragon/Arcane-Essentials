package com.favouritedragon.arcaneessentials.common.spell.sorcery;

import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MagicMirror extends Spell {


	/**
	 * This constructor should be called from any subclasses, either feeding in the constants directly or through their
	 * own constructor from wherever the spell is registered.
	 *
	 * @param modID        The mod id of the mod that added this spell. This allows wizardry to use the correct file path for
	 *                     the spell icon, and also more generally to distinguish between original and addon spells.
	 * @param name         The <i>registry name</i> of the spell, excluding the mod id. This will also be the name of the icon
	 *                     file. The spell's unlocalised name will be a resource location with the format [modid]:[name].
	 * @param action       The vanilla usage action to be displayed when casting this spell (see {@link}EnumAction)
	 * @param isContinuous Whether this spell is continuous, meaning you cast it for a length of time by holding the
	 */
	public MagicMirror(String modID, String name, EnumAction action, boolean isContinuous) {
		super(modID, name, action, isContinuous);
	}

	@Override
	public boolean cast(World world, EntityPlayer entityPlayer, EnumHand enumHand, int i, SpellModifiers spellModifiers) {
		return false;
	}
}
