package com.favouritedragon.arcaneessentials.common.entity;

import net.minecraft.world.World;

public class EntitySaintessSun extends EntityMagicConstruct {

    private float damage;
    private int fireTime;
    private float knockback;

    public EntitySaintessSun(World world) {
        super(world);
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
}
