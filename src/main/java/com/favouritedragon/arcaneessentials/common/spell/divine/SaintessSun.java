package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.common.entity.EntityMagicConstruct;
import com.favouritedragon.arcaneessentials.common.entity.EntitySaintessSun;
import com.favouritedragon.arcaneessentials.common.entity.data.Behaviour;
import com.favouritedragon.arcaneessentials.common.entity.data.MagicConstructBehaviour;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class SaintessSun extends ArcaneSpell {

    public SaintessSun(String name, EnumAction action, boolean isContinuous) {
        super("saintess_sun", EnumAction.BOW, true);
    }

    private static void shootBeam(EntityLivingBase caster, EntitySaintessSun source, Entity target) {
        SaintessSun spell = (SaintessSun) Spell.get("saintess_sun");
        World world = caster.world;

        if (world.isRemote)
            ParticleBuilder.create(ParticleBuilder.Type.BEAM).time(10 + ArcaneUtils.getRandomNumberInRange(0, 4))
                    .clr(1.0F, 1.0F, 0.3F).entity(source).spawn(world);
        else {
            DamageSource damageSource = MagicDamage.causeIndirectMagicDamage(source, caster, MagicDamage.DamageType.RADIANT);
            float damage = source.getDamage();
            if (target.attackEntityFrom(damageSource, damage)) {
                target.setFire(source.getFireTime());

                Vec3d entityPos = target.getPositionVector();
                Vec3d sourcePos = source.getPositionVector();
                Vec3d vel = sourcePos.subtract(entityPos).scale(0.005);
            }
            if (!target.attackEntityFrom(damageSource, damage) && target instanceof EntityDragon) {
                if (((EntityDragon) target).attackEntityFromPart(((EntityDragon) target).dragonPartBody, damageSource, damage)) {
                    target.setFire(source.getFireTime());
                }
            }

        }
    }

    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    public static class SaintessSunBehaviour extends MagicConstructBehaviour {

        @Override
        public MagicConstructBehaviour onUpdate(EntityMagicConstruct entity) {
            if (entity.getCaster() != null) {
                EntityLivingBase caster = entity.getCaster();
                World world = entity.world;

                float height = entity.getSize() * 4;

                Vec3d targetPos = ArcaneUtils.getEntityPos(entity).add(0, height, 0);
                Vec3d entityPos = entity.getPositionVector();
                Vec3d targetVelocity = targetPos.subtract(entityPos).scale(0.0025);
                entity.setVelocity(targetVelocity.x, targetVelocity.y, targetVelocity.z);

                List<Entity> targets = world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox().grow(height));
                if (!targets.isEmpty()) {
                    for (Entity target : targets) {
                        if (entity.isValidTarget(target) && target != entity && target != caster) {

                        }
                    }
                }
            }
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
