package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellProperties;
import net.minecraft.item.EnumAction;
import net.minecraft.util.SoundEvent;

public abstract class ArcaneSpell extends Spell implements IArcaneSpell {
    public static final String SWORDS = "swords";
    public static final String AXES = "axes";
    public static final String SHIELDS = "shields";

    public ArcaneSpell(String name, EnumAction action, boolean isContinuous) {
        super(ArcaneEssentials.MODID, name, action, isContinuous);
        addProperties(SWORDS);
    }

    //TODO: Replace the interface with spell context stuff.
    //For now, the spell context will be specific to this mod's spells.
    @Override
    public boolean isSwordCastable() {
        //0 is false, 1 is true.
        return getProperty(SWORDS).intValue() == 1;
    }

    @Override
    public boolean isWandCastable() {
        return isEnabled(SpellProperties.Context.WANDS);
    }

    @Override
    public boolean isAxeCastable() {
        //return getProperty(AXES).equals(true);
        return false;
    }

    @Override
    public boolean isShieldCastable() {
        //return getProperty(SHIELDS).equals(true);
        return false;
    }

    @Override
    protected SoundEvent[] createSounds() {
        return new SoundEvent[]{ArcaneUtils.createSound("spell." + this.getRegistryName().getPath())};
    }
}
