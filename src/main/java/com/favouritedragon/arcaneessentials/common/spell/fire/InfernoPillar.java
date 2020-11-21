package com.favouritedragon.arcaneessentials.common.spell.fire;

import com.favouritedragon.arcaneessentials.common.entity.EntityFlamePillar;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class InfernoPillar extends ArcaneSpell {

    public InfernoPillar() {
        super("inferno_pillar", EnumAction.BOW, false);
        addProperties(DAMAGE, EFFECT_RADIUS, EFFECT_DURATION, RANGE);
        soundValues(1.5F, 0.8F, 0.15F);
    }

    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
        float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        int lifetime = getProperty(EFFECT_DURATION).intValue() * 20 * (int) modifiers.get(WizardryItems.duration_upgrade);
        float radius = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        float height = getProperty(RANGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        world.spawnEntity(new EntityFlamePillar(world, caster.posX, caster.posY, caster.posZ, caster, lifetime,
                damage, radius, height, 120));
        playSound(world, caster, ticksInUse, -1, modifiers);
        EntityUtils.playSoundAtPlayer(caster, WizardrySounds.ENTITY_FIRE_RING_AMBIENT, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10);
        EntityUtils.playSoundAtPlayer(caster, WizardrySounds.ENTITY_FIRE_SIGIL_TRIGGER, 1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10);
        caster.swingArm(hand);

        return true;
    }

    @Override
    public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
        float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        int lifetime = getProperty(EFFECT_DURATION).intValue() * 20 * (int) modifiers.get(WizardryItems.duration_upgrade);
        float radius = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        float height = getProperty(RANGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        world.spawnEntity(new EntityFlamePillar(world, caster.posX, caster.posY, caster.posZ, caster, lifetime,
                damage, radius, height, 120));
        playSound(world, caster, ticksInUse, -1, modifiers);
        world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FIRE_RING_AMBIENT, SoundCategory.NEUTRAL,
                1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
        world.playSound(caster.posX, caster.posY, caster.posZ, WizardrySounds.ENTITY_FIRE_SIGIL_TRIGGER, SoundCategory.NEUTRAL,
                1 + world.rand.nextFloat() / 10, 0.5F + world.rand.nextFloat() / 10, true);
        caster.swingArm(hand);

        return true;
    }

    @Override
    public boolean isShieldCastable() {
        return true;
    }
}
