package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static com.favouritedragon.arcaneessentials.common.spell.divine.SaintessSun.shootBeam;

public class EntitySaintessSun extends EntityMagicConstruct {

    private float damage;
    private int fireTime;
    private float knockback;

    public EntitySaintessSun(World world) {
        super(world);
        this.noClip = true;
        this.ignoreFrustumCheck = true;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getFireTime() {
        return this.fireTime;
    }

    public void setFireTime(int fireTime) {
        this.fireTime = fireTime;
    }

    public float getKnockback() {
        return this.knockback;
    }

    public void setKnockback(float knockback) {
        this.knockback = knockback;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            for (double i = 0; i < width; i += 0.30) {
                Vec3d pos = ArcaneUtils.getMiddleOfEntity(this);
                double x = world.rand.nextDouble() / 2 * width * world.rand.nextGaussian();
                double y = world.rand.nextDouble() / 2 * height * world.rand.nextGaussian();
                double z = world.rand.nextDouble() / 2 * width * world.rand.nextGaussian();
                pos = pos.add(x, y, z);
                ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(pos).clr(1.0F, 1.0F, ArcaneUtils.getRandomNumberInRange(90, 255) / 255F)
                        .time(12 + ArcaneUtils.getRandomNumberInRange(2, 4)).scale(getSize() / 2).vel(world.rand.nextGaussian() / 20,
                        world.rand.nextGaussian() / 20, world.rand.nextGaussian() / 20)
                        .spawn(world);
                ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(pos).clr(1.0F, 1.0F, ArcaneUtils.getRandomNumberInRange(90, 255) / 255F)
                        .time(10 + ArcaneUtils.getRandomNumberInRange(2, 4)).scale(getSize()).vel(world.rand.nextGaussian() / 20,
                        world.rand.nextGaussian() / 20, world.rand.nextGaussian() / 20)
                        .spawn(world);
            }

        }

    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void resetPositionToBB() {
        super.resetPositionToBB();
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return super.shouldRenderInPass(pass) || pass == 1;
    }
}
