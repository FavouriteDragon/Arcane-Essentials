package com.favouritedragon.arcaneessentials.common.spell.fire;

import com.favouritedragon.arcaneessentials.common.entity.EntityFireball;
import com.favouritedragon.arcaneessentials.common.entity.EntityMagicBolt;
import com.favouritedragon.arcaneessentials.common.entity.data.MagicBoltBehaviour;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import static com.favouritedragon.arcaneessentials.common.util.SpellUtils.SIZE;

public class KaFrizzle extends ArcaneSpell {

    public KaFrizzle() {
        super("kafrizzle", EnumAction.BOW, false);
        addProperties(DAMAGE, RANGE, BURN_DURATION, SIZE);
        soundValues(2.0F, 0.9F, 0.15F);
    }


    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
        caster.swingArm(hand);
        playSound(world, caster, ticksInUse, -1, modifiers);
        return cast(world, caster, modifiers);
    }

    private boolean cast(World world, EntityLivingBase caster, SpellModifiers modifiers) {
        float size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        int burnDuration = getProperty(BURN_DURATION).intValue() * (int) modifiers.get(SpellModifiers.POTENCY);
        world.playSound(caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_GHAST_SHOOT, WizardrySounds.SPELLS,
                1.0F + world.rand.nextFloat(), 1.0F + world.rand.nextFloat(), false);
        EntityFireball fireball = new EntityFireball(world);
        fireball.setCaster(caster);
        fireball.setSize(size);
        fireball.setLifetime(70);
        fireball.setDamage(damage);
        fireball.setKnockbackStrength((int) size * 2);
        fireball.setBurnDuration(burnDuration);
        fireball.aim(caster, getProperty(RANGE).floatValue() / 50, 0F);
        fireball.setBehaviour(new KaFrizzleBehaviour());
        if (!world.isRemote)
            return world.spawnEntity(fireball);
        return false;
    }

    @Override
    public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
        caster.swingArm(hand);
        playSound(world, caster, ticksInUse, -1, modifiers);
        return cast(world, caster, modifiers);
    }

    @Override
    public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
        float size = getProperty(SIZE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
        Vec3i dir = direction.getDirectionVec();
        EntityFireball fireball = new EntityFireball(world);
        fireball.setSize(size);
        fireball.setDamage(damage);
        fireball.setPosition(x, y, z);
        fireball.shoot(dir.getX(), dir.getY(), dir.getZ(), getProperty(RANGE).floatValue(), 0F);
        fireball.setBehaviour(new KaFrizzleBehaviour());
        playSound(world, x, y, z, ticksInUse, -1, modifiers);
        if (!world.isRemote)
            return world.spawnEntity(fireball);

        return false;
    }

    public static class KaFrizzleBehaviour extends MagicBoltBehaviour {

        @Override
        public MagicBoltBehaviour onUpdate(EntityMagicBolt entity) {
            //Actual code is relegated to the entity class, due to how the entity is coded.
            //TODO: Properly rewrite the entity code
            return this;
        }

        @Override
        public void fromBytes(PacketBuffer buf) {

        }

        @Override
        public void toBytes(PacketBuffer buf) {

        }

        @Override
        public void load(NBTTagCompound nbt) {

        }

        @Override
        public void save(NBTTagCompound nbt) {

        }
    }
}
