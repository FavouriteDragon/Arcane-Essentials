package com.favouritedragon.arcaneessentials.common.spell.divine;

import com.favouritedragon.arcaneessentials.common.entity.EntityMagicConstruct;
import com.favouritedragon.arcaneessentials.common.entity.EntitySaintessSun;
import com.favouritedragon.arcaneessentials.common.entity.data.MagicConstructBehaviour;
import com.favouritedragon.arcaneessentials.common.spell.ArcaneSpell;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
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

import javax.annotation.Nullable;
import java.util.List;

public class SaintessSun extends ArcaneSpell {

    public SaintessSun() {
        super("saintess_sun", EnumAction.BOW, false);
        addProperties(EFFECT_RADIUS, BURN_DURATION, DAMAGE, EFFECT_DURATION);
    }

    @Nullable
    public static void shootBeam(EntityLivingBase caster, EntitySaintessSun source, Entity target) {

        World world = source.world;

        if (caster != null && world.isRemote) {
            ParticleBuilder.create(ParticleBuilder.Type.BEAM).time(12 + ArcaneUtils.getRandomNumberInRange(0, 4))
                    .clr(1.0F, 1.0F, 0.3F).entity(caster).pos(ArcaneUtils.getMiddleOfEntity(source)
                    .subtract(caster.getPositionVector())).target(target.getPositionVector().add(0, target.getEyeHeight() / 2, 0))
                    .scale(source.getSize()).spawn(source.world);
            world.playSound(target.posX, target.posY, target.posZ, WizardrySounds.ENTITY_HAMMER_EXPLODE,
                    WizardrySounds.SPELLS, 1.5F + world.rand.nextFloat() / 4, 0.75F + world.rand.nextFloat() / 4, true);
            world.playSound(target.posX, target.posY, target.posZ, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT,
                    WizardrySounds.SPELLS, 1.5F + world.rand.nextFloat() / 4, 0.875F + world.rand.nextFloat() / 4, true);

        }
        if (caster != null && !world.isRemote) {
            DamageSource damageSource = MagicDamage.causeIndirectMagicDamage(source, caster, MagicDamage.DamageType.RADIANT);
            float damage = source.getDamage();
            if (!target.getIsInvulnerable() && !MagicDamage.isEntityImmune(MagicDamage.DamageType.RADIANT, target)
                    && target.attackEntityFrom(damageSource, damage / 4)) {
                target.setFire(source.getFireTime());

                Vec3d entityPos = target.getPositionVector();
                Vec3d sourcePos = source.getPositionVector();
                Vec3d vel = sourcePos.subtract(entityPos).scale(0.005);

                target.addVelocity(vel.x, vel.y + 0.15, vel.z);

            } else if (!target.attackEntityFrom(damageSource, damage) && target instanceof EntityDragon) {
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

    public static class SaintessSunBehaviour extends MagicConstructBehaviour {

        @Override
        public MagicConstructBehaviour onUpdate(EntityMagicConstruct entity) {
            if (entity.getCaster() != null && entity instanceof EntitySaintessSun) {
                EntityLivingBase caster = entity.getCaster();
                World world = entity.world;

                float height = entity.getSize() * 1.5F;

                Vec3d targetPos = ArcaneUtils.getEntityPos(caster).add(0, height, 0);
                Vec3d entityPos = entity.getPositionVector();
                Vec3d targetVelocity = targetPos.subtract(entityPos).scale(0.1);
                entity.setVelocity(targetVelocity.x, targetVelocity.y, targetVelocity.z);

                List<Entity> targets = world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox()
                        .grow(height * 1.25F));
                if (!targets.isEmpty()) {
                    for (Entity target : targets) {
                        if (entity.isValidTarget(target) && target != entity && target != caster) {
                            if (target instanceof EntityLivingBase && ((EntityLivingBase) target).hurtTime == 0)
                                shootBeam(caster, (EntitySaintessSun) entity, target);
                        }
                    }
                }

                if (entity.ticksExisted < 2)
                    world.playSound(entity.posX, entity.posY, entity.posZ, WizardrySounds.ENTITY_RADIANT_TOTEM_AMBIENT,
                            WizardrySounds.SPELLS, 3.0F, 1.5F, false);


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
