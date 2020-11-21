package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.common.entity.EntityMagicConstruct;
import com.favouritedragon.arcaneessentials.common.entity.EntitySaintessSun;
import com.favouritedragon.arcaneessentials.common.entity.data.MagicConstructBehaviour;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import com.favouritedragon.arcaneessentials.common.spell.necromancy.AvadaKedavra;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
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

    public SaintessSun() {
        super("saintess_sun", EnumAction.BOW, false);
        addProperties(EFFECT_RADIUS, BURN_DURATION, DAMAGE, EFFECT_DURATION);
    }

    private static void shootBeam(EntityLivingBase caster, EntitySaintessSun source, Entity target) {
        World world = caster.world;

        if (world.isRemote)
            ParticleBuilder.create(ParticleBuilder.Type.BEAM).time(10 + ArcaneUtils.getRandomNumberInRange(0, 4))
                    .clr(1.0F, 1.0F, 0.3F).entity(source).target(target.getPositionVector().add(0, target.getEyeHeight(), 0))
                    .spawn(world);
        else {
            DamageSource damageSource = MagicDamage.causeIndirectMagicDamage(source, caster, MagicDamage.DamageType.RADIANT);
            float damage = source.getDamage();
            if (target.attackEntityFrom(damageSource, damage)) {
                target.setFire(source.getFireTime());

                Vec3d entityPos = target.getPositionVector();
                Vec3d sourcePos = source.getPositionVector();
                Vec3d vel = sourcePos.subtract(entityPos).scale(0.005);

                target.addVelocity(vel.x, vel.y + 0.15, vel.z);

            }
            if (!target.attackEntityFrom(damageSource, damage) && target instanceof EntityDragon) {
                if (((EntityDragon) target).attackEntityFromPart(((EntityDragon) target).dragonPartBody, damageSource, damage)) {
                    target.setFire(source.getFireTime());
                }
            }

        }
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
        sun.setBehaviour(new SaintessSunBehaviour());
        sun.setCaster(caster);
        if (!world.isRemote)
           return world.spawnEntity(sun);
        return false;
    }

    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
        caster.swingArm(hand);
        return cast(world, caster, modifiers);
    }

    public static class SaintessSunBehaviour extends MagicConstructBehaviour {

        @Override
        public MagicConstructBehaviour onUpdate(EntityMagicConstruct entity) {
            if (entity.getCaster() != null && entity instanceof EntitySaintessSun) {
                EntityLivingBase caster = entity.getCaster();
                World world = entity.world;

                float height = entity.getSize() * 2;

                Vec3d targetPos = ArcaneUtils.getEntityPos(caster).add(0, height, 0);
                Vec3d entityPos = entity.getPositionVector();
                Vec3d targetVelocity = targetPos.subtract(entityPos).scale(0.1);
                entity.setVelocity(targetVelocity.x, targetVelocity.y, targetVelocity.z);

                List<Entity> targets = world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox().grow(height));
                if (!targets.isEmpty()) {
                    for (Entity target : targets) {
                        if (entity.isValidTarget(target) && target != entity && target != caster) {
                            shootBeam(caster, (EntitySaintessSun) entity, target);
                        }
                    }
                }

                if (world.isRemote) {
                    System.out.println("Hm");
                    for (double i = 0; i < entity.width; i += 0.15) {
                        ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(entityPos).clr(1.0F, 1.0F, 0.3F)
                                .time(12 + ArcaneUtils.getRandomNumberInRange(2, 4)).scale(entity.getSize()).vel(world.rand.nextGaussian() / 20,
                                world.rand.nextGaussian() / 20, world.rand.nextGaussian() / 20)
                                .spawn(world);
                    }
                    ParticleBuilder.create(ParticleBuilder.Type.SPHERE).clr(1.0F, 1.0F, 0.3F)
                            .entity(entity).pos(entityPos).time(20 + ArcaneUtils.getRandomNumberInRange(0, 2))
                            .scale(entity.getSize()).spawn(world);
                }

            }
            entity.move(MoverType.SELF, entity.motionX, entity.motionY, entity.motionZ);
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
