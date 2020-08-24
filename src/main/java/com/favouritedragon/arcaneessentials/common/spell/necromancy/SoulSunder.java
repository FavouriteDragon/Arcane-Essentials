package com.favouritedragon.arcaneessentials.common.spell.necromancy;

import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class SoulSunder extends ArcaneSpell {

    public SoulSunder() {
        super("soul_sunder", EnumAction.NONE, false);
        npcSelector = npcSelector.negate();
    }

    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

        return false;
    }

    @Override
    public boolean castRightClick() {
        return false;
    }


}
