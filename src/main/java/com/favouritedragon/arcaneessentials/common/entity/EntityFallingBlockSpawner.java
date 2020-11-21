package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.entity.data.Behaviour;
import com.favouritedragon.arcaneessentials.common.entity.data.MagicConstructBehaviour;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.MagicDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static com.favouritedragon.arcaneessentials.common.util.DamageSources.EARTH;

public class EntityFallingBlockSpawner extends EntityMagicSpawner {

    //TODO: Rewrite projectile code
    public EntityFallingBlockSpawner(World world) {
        super(world);
    }

    public EntityFallingBlockSpawner(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float damageMultiplier) {
        super(world, x, y, z, caster, lifetime, damageMultiplier);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //Sizing causes hella weird positioning shenanigans, so it's better not to mess with it, which is why render size is used.
        List<Entity> nearby = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow((getRenderSize() - 1) / 2));
        nearby.remove(getCaster());
        if (!nearby.isEmpty()) {
            for (Entity hit : nearby) {
                if (hit != this && AllyDesignationSystem.isValidTarget(getCaster(), hit) && hit.canBePushed() && hit.canBeCollidedWith()) {
                    if (!world.isRemote) {
                        hit.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster(), EARTH), damageMultiplier);
                        hit.addVelocity(motionX / 4 * getRenderSize(), 0.1 * getRenderSize(), motionZ / 4 * getRenderSize());
                    }
                }
            }
        }
        setRenderSize(getRenderSize() * 1.005F);
        if (ticksExisted % 2 == 0) {
            playSound(world.getBlockState(getPosition().down()).getBlock().getSoundType().getBreakSound(), 0.8F + world.rand.nextFloat() / 10F, 0.8F + world.rand.nextFloat() / 10F);
        }
    }

    @Override
    protected int getFrequency() {
        return 2;
    }

    @Override
    protected boolean spawnEntity() {
        if (!world.isRemote) {
            BlockPos pos = new BlockPos(posX, posY, posZ);

            if (!BlockUtils.isBlockUnbreakable(world, pos.down()) && !world.isAirBlock(pos.down()) && world.isBlockNormalCube(pos.down(), false)
                    // Checks that the block above is not solid, since this causes the falling sand to vanish.
                    && !world.isBlockNormalCube(pos, false)) {

                // Falling blocks do the setting block to air themselves.
                EntityFallingBlock fallingblock = new EntityFallingBlock(world, posX, posY - 1, posZ,
                        world.getBlockState(new BlockPos(posX, posY - 1, posZ)));
                fallingblock.motionY = 0.3;
                return world.spawnEntity(fallingblock);
            }
        }
        return false;
    }

    @Override
    public void playSound() {
        playSound(SoundEvents.BLOCK_SAND_FALL, 1.0F + world.rand.nextFloat() / 10, 0.8F + world.rand.nextFloat() / 10F);
    }

    public static class FallingBlockBehaviour extends MagicConstructBehaviour {

        @Override
        public Behaviour onUpdate(EntityMagicConstruct entity) {
            if (entity instanceof EntityFloatingBlock) {
                entity.motionY -= 0.0240;
                if (entity.collided) {
                    entity.setDead();

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
