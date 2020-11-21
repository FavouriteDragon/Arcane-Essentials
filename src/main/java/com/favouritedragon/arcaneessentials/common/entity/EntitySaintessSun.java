package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.world.World;

public class EntitySaintessSun extends EntityMagicConstruct {

    private float damage;
    private int fireTime;
    private float knockback;

    public EntitySaintessSun(World world) {
        super(world);
        this.noClip = true;
        this.ignoreFrustumCheck = true;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setFireTime(int fireTime) {
        this.fireTime = fireTime;
    }

    public int getFireTime() {
        return this.fireTime;
    }

    public void setKnockback(float knockback) {
        this.knockback = knockback;
    }

    public float getKnockback() {
        return this.knockback;
    }


    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            for (double i = 0; i < width; i += 0.15) {
                ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(getPositionVector()).clr(1.0F, 1.0F, 0.3F)
                        .time(6 + ArcaneUtils.getRandomNumberInRange(2, 4)).scale(getSize() / 2).vel(world.rand.nextGaussian() / 10,
                        world.rand.nextGaussian() / 10, world.rand.nextGaussian() / 10)
                        .spawn(world);
            }
            ParticleBuilder.create(ParticleBuilder.Type.SPHERE).clr(1.0F, 1.0F, 0.3F)
                    .entity(this).pos(getPositionVector()).time(20 + ArcaneUtils.getRandomNumberInRange(0, 2))
                    .scale(getSize()).spawn(world);
        }
    }
}
