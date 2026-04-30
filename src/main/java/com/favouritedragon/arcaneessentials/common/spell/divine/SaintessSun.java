package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.common.entity.EntitySaintessSun;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class SaintessSun extends ArcaneSpell {

    public SaintessSun() {
        super("saintess_sun", EnumAction.BOW, false);
        addProperties(EFFECT_RADIUS, BURN_DURATION, DAMAGE, EFFECT_DURATION);
    }

    private boolean cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
        float size, damage;
        int lifetime, fireTime;

        size = getProperty(EFFECT_RADIUS).floatValue();
        damage = getProperty(DAMAGE).floatValue();
        lifetime = getProperty(EFFECT_DURATION).intValue();
        fireTime = getProperty(BURN_DURATION).intValue();

        size *= modifiers.get(WizardryItems.blast_upgrade);
        damage *= modifiers.get(WizardryItems.blast_upgrade);
        lifetime *= modifiers.get(WizardryItems.range_upgrade);


        EntitySaintessSun sun = new EntitySaintessSun(world);
        sun.setPosition(caster.posX, caster.posY + size * 4, caster.posZ);
        sun.setSize(size);
        sun.setDamage(damage);
        sun.setFireTime(fireTime);
        sun.setLifetime(lifetime);
        sun.setCaster(caster);
        if (!world.isRemote)
            world.spawnEntity(sun);
        world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_STORMCLOUD_THUNDER,
                WizardrySounds.SPELLS, 2.0F, 0.675F + world.rand.nextFloat() / 4, false);
        world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT,
                WizardrySounds.SPELLS, 2.0F, 0.875F + world.rand.nextFloat() / 4, false);

        return true;
    }

    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
        caster.swingArm(hand);
        return cast(world, caster, modifiers);
    }
}
