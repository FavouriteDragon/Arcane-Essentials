package com.favouritedragon.arcaneessentials.common.spell;

import com.favouritedragon.arcaneessentials.ArcaneEssentials;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.SpellProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell.SWORDS;

public abstract class SpellRay extends electroblob.wizardry.spell.SpellRay implements IArcaneSpell {


    public SpellRay(String name, EnumAction action, boolean isContinuous) {
        super(ArcaneEssentials.MODID, name, action, isContinuous);
    }


    public boolean isPiercing() {
        return false;
    }

    public abstract void playSound(World world, EntityLivingBase caster);

    @Override
    protected SoundEvent[] createSounds() {
        return new SoundEvent[]{ArcaneUtils.createSound("spell." + this.getRegistryName().getPath())};
    }

    @Override
    public boolean isSwordCastable() {
        return getProperty(SWORDS).floatValue() == 1;
    }

    @Override
    public boolean isWandCastable() {
        return isEnabled(SpellProperties.Context.WANDS);
    }
}
