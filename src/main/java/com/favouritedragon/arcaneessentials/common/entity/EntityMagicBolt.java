package com.favouritedragon.arcaneessentials.common.entity;

import com.favouritedragon.arcaneessentials.common.entity.data.MagicBoltBehaviour;
import com.favouritedragon.arcaneessentials.common.util.ArcaneUtils;
import electroblob.wizardry.entity.projectile.EntityMagicProjectile;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.RayTracer;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

//Copy and pasted from eb's class, since IntelliJ is fudged up.

public abstract class EntityMagicBolt extends EntityMagicProjectile {

    public static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityMagicBolt.class,
            DataSerializers.FLOAT);
    public static final DataParameter<MagicBoltBehaviour> SYNC_BEHAVIOUR = EntityDataManager.createKey(EntityMagicBolt.class,
            MagicBoltBehaviour.DATA_SERIALIZER);

    public static final double LAUNCH_Y_OFFSET = 0.2;
    public static final int SEEKING_TIME = 15;
    /**
     * Seems to be some sort of timer for animating an arrow.
     */
    public int arrowShake;
    /**
     * The damage multiplier for the projectile.
     */
    public float damageMultiplier = 1.0f;
    //How much to expand the hitbox by when looking for blocks
    public float blockBoxX, blockBoxY, blockBoxZ;
    int ticksInGround;
    int ticksInAir;
    private int blockX = -1;
    private int blockY = -1;
    private int blockZ = -1;
    /**
     * The block the arrow is stuck in
     */
    private IBlockState stuckInBlock;
    /**
     * The metadata of the block the arrow is stuck in
     */
    private int inData;
    private boolean inGround;
    /**
     * The owner of this arrow.
     */
    private WeakReference<EntityLivingBase> caster;
    /**
     * The UUID of the caster. Note that this is only for loading purposes; during normal updates the actual entity
     * instance is stored (so that getEntityByUUID is not called constantly), so this will not always be synced (this is
     * why it is private).
     */
    private UUID casterUUID;
    /**
     * The amount of knockback an arrow applies when it hits a mob.
     */
    private double knockbackStrength;

    /**
     * Creates a new projectile in the given world.
     */
    public EntityMagicBolt(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.noClip = false;
        this.blockBoxX = this.blockBoxY = this.blockBoxZ = 0.125F;
    }

    public float getSize() {
        return dataManager.get(SYNC_SIZE);
    }

    public void setSize(float size) {
        dataManager.set(SYNC_SIZE, size);
    }

    public MagicBoltBehaviour getBehaviour() {
        return dataManager.get(SYNC_BEHAVIOUR);
    }

    public void setBehaviour(MagicBoltBehaviour behaviour) {
        dataManager.set(SYNC_BEHAVIOUR, behaviour);
    }

    // Initialiser methods

    /**
     * Sets the shooter of the projectile to the given caster, positions the projectile at the given caster's eyes and
     * aims it in the direction they are looking with the given speed.
     */
    public void aim(EntityLivingBase caster, float speed, float error) {

        this.setCaster(caster);

        this.setLocationAndAngles(caster.posX, caster.getEntityBoundingBox().minY + (double) caster.getEyeHeight() - LAUNCH_Y_OFFSET,
                caster.posZ, caster.rotationYaw, caster.rotationPitch);

        //this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        //this.posY -= 0.10000000149011612D;
        //this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);

        Vec3d pos = caster.getLookVec().scale(0.75 + width / 2);

        this.setPosition(this.posX + pos.x, this.posY + pos.y, this.posZ + pos.z);

        // yOffset was set to 0 here, but that has been replaced by getYOffset(), which returns 0 in Entity anyway.
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
                * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
                * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);

        this.shoot(this.motionX, this.motionY, this.motionZ, speed * 1.5F, error);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    /**
     * Sets the shooter of the projectile to the given caster, positions the projectile at the given caster's eyes and
     * aims it at the given target with the given speed. The trajectory will be altered slightly by a random amount
     * determined by the aimingError parameter. For reference, skeletons set this to 10 on easy, 6 on normal and 2 on hard
     * difficulty.
     */
    @Override
    public void aim(EntityLivingBase caster, Entity target, float speed, float aimingError) {

        this.setCaster(caster);

        this.posY = caster.getEntityBoundingBox().minY + (double) caster.getEyeHeight() - LAUNCH_Y_OFFSET;
        double dx = target.posX - caster.posX;
        double dy = this.doGravity() ? target.getEntityBoundingBox().minY + (double) (target.height / 3.0f) - this.posY
                : target.getEntityBoundingBox().minY + (double) (target.height / 2.0f) - this.posY;
        double dz = target.posZ - caster.posZ;
        double horizontalDistance = MathHelper.sqrt(dx * dx + dz * dz);

        if (horizontalDistance >= 1.0E-7D) {
            float yaw = (float) (Math.atan2(dz, dx) * 180.0d / Math.PI) - 90.0f;
            float pitch = (float) (-(Math.atan2(dy, horizontalDistance) * 180.0d / Math.PI));
            double dxNormalised = dx / horizontalDistance;
            double dzNormalised = dz / horizontalDistance;
            this.setLocationAndAngles(caster.posX + dxNormalised, this.posY, caster.posZ + dzNormalised, yaw, pitch);
            // yOffset was set to 0 here, but that has been replaced by getYOffset(), which returns 0 in Entity anyway.

            // Depends on the horizontal distance between the two entities and accounts for bullet drop,
            // but of course if gravity is ignored this should be 0 since there is no bullet drop.
            float bulletDropCompensation = this.doGravity() ? (float) horizontalDistance * 0.2f : 0;
            this.shoot(dx, dy + (double) bulletDropCompensation, dz, speed, aimingError);
        }
    }

    // Property getters (to be overridden by subclasses)

    /**
     * Subclasses must override this to set their own base damage.
     */
    public abstract double getDamage();

    /**
     * Returns the maximum flight time in ticks before this projectile disappears, or -1 if it can continue
     * indefinitely until it hits something. This should be constant.
     */
    public abstract int getLifetime();

    /**
     * Override this to specify the damage type dealt. Defaults to {@link MagicDamage.DamageType#MAGIC}.
     */
    public MagicDamage.DamageType getDamageType() {
        return MagicDamage.DamageType.MAGIC;
    }

    /**
     * Override this to enable gravity.
     */
    protected boolean doGravity() {
        return false;
    }

    /**
     * Override this to disable deceleration (generally speaking, this isn't noticeable unless gravity is turned off).
     * Returns true by default.
     */
    public boolean doDeceleration() {
        return true;
    }

    /**
     * Override this to allow the projectile to pass through mobs intact (the onEntityHit method will still be called
     * and damage will still be applied). Returns false by default.
     */
    public boolean doOverpenetration() {
        return false;
    }

    /**
     * Returns the seeking strength of this projectile, or the maximum distance from a target the projectile can be
     * heading for that will make it curve towards that target. By default, this is 2 if the caster is wearing a ring
     * of attraction, otherwise it is 0.
     */
    public float getSeekingStrength() {
        return getCaster() instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) getCaster(),
                WizardryItems.ring_seeking) ? 2 : 0;
    }

    // Setters and getters

    /**
     * Sets the amount of knockback the projectile applies when it hits a mob.
     */
    public void setKnockbackStrength(float knockback) {
        this.knockbackStrength = knockback;
    }

    /**
     * Returns the EntityLivingBase that created this construct, or null if it no longer exists. Cases where the entity
     * may no longer exist are: entity died or was deleted, mob despawned, player logged out, entity teleported to
     * another dimension, or this construct simply had no caster in the first place.
     */
    public EntityLivingBase getCaster() {
        return caster == null ? null : caster.get();
    }

    public void setCaster(EntityLivingBase entity) {
        caster = new WeakReference<>(entity);
    }

    // Methods triggered during the update cycle

    /**
     * Called each tick when the projectile is in a block. Defaults to setDead(), but can be overridden to change the
     * behaviour.
     */
    protected void tickInGround() {
        this.setDead();
    }

    /**
     * Called each tick when the projectile is in the air. Override to add particles and such like.
     */
    protected void tickInAir() {
    }

    /**
     * Called when the projectile hits an entity. Override to add potion effects and the like.
     * Used to determine whether the projectile hit an entity
     */
    protected void onEntityHit(EntityLivingBase entityHit) {
        if (canCollideWithEntity(entityHit)) {
            if (!doOverpenetration())
                setDead();
        }
    }

    /**
     * Called when the projectile hits a block. Override to add sound effects and the like.
     * Can also be used for whether to set the entity dead upon hitting a block/whether it hit a block.
     *
     * @param hit A vector representing the exact coordinates of the hit; use this to centre particle effects, for
     *            example.
     */
    protected void onBlockHit(RayTraceResult hit) {
        if (hit.typeOfHit == RayTraceResult.Type.BLOCK && canCollideWithSolid(world.getBlockState(hit.getBlockPos())) ||
                world.collidesWithAnyBlock(getEntityBoundingBox().grow(blockBoxX, blockBoxY, blockBoxZ))) {
            onGround = true;
            setDead();
        }
    }

    /**
     * Similar to onBlockHit, but this just checks whether to do anything, rather than performing a function.
     *
     * @param hit The raytrace used to hit something
     * @return Whether the entity can collide/has collided
     */
    public boolean canCollideWithSolid(IBlockState hit) {
        return hit.isFullBlock() && hit.getBlock() != Blocks.AIR || hit.isFullCube() && hit.getBlock() != Blocks.AIR;

    }

    /**
     * Same as onCollideWithSolid, except for entities.
     *
     * @param entityHit The entity hit.
     * @return Whether the entity hit can be affected.
     */
    public boolean canCollideWithEntity(EntityLivingBase entityHit) {
        return AllyDesignationSystem.isValidTarget(this, entityHit) && entityHit != getCaster() && entityHit.canBeCollidedWith() && entityHit.canBePushed();
    }

    @Override
    public void onUpdate() {

        //super.onUpdate();

        setBehaviour((MagicBoltBehaviour) getBehaviour().onUpdate(this));
        setSize(getSize(), getSize());
        // Projectile disappears after its lifetime (if it has one) has elapsed
        if (getLifetime() > 0 && this.ticksExisted > getLifetime()) {
            this.setDead();
        }

        if (this.getCaster() == null && this.casterUUID != null) {
            Entity entity = EntityUtils.getEntityByUUID(world, casterUUID);
            if (entity instanceof EntityLivingBase) {
                this.caster = new WeakReference<>((EntityLivingBase) entity);
            }
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D
                    / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D
                    / Math.PI);
        }

        BlockPos blockpos = new BlockPos(this.blockX, this.blockY, this.blockZ);
        IBlockState iblockstate = this.world.getBlockState(blockpos);

        if (iblockstate.getMaterial() != Material.AIR) {
            AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

            if (axisalignedbb != Block.NULL_AABB
                    && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        // When the arrow is in the ground
        if (this.inGround) {
            ++this.ticksInGround;
            this.tickInGround();
        }
        // When the arrow is in the air
        else {

            this.tickInAir();

            this.ticksInGround = 0;
            ++this.ticksInAir;

            // Does a ray trace to determine whether the projectile will hit a block in the next tick

            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d = new Vec3d(this.posX + this.motionX / 1.625, this.posY + this.motionY / 1.625, this.posZ + this.motionZ / 1.625);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
            vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (raytraceresult != null) {
                vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y,
                        raytraceresult.hitVec.z);
            }

            // Uses bounding boxes to determine whether the projectile will hit an entity in the next tick, and if so
            // overwrites the block hit with an entity

            Entity entity = null;
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()
                    .expand(this.motionX / 1.75, this.motionY / 1.75, this.motionZ / 1.75).grow(0.5));
            double d0 = 0.0D;
            int i;
            float f1;

            for (i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.getCaster() || this.ticksInAir >= 5)) {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().grow(f1, f1,
                            f1);
                    RayTraceResult RayTraceResult1 = axisalignedbb1.calculateIntercept(vec3d1, vec3d);

                    if (RayTraceResult1 != null) {
                        double d1 = vec3d1.distanceTo(RayTraceResult1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }

            // Players that are considered invulnerable to the caster allow the projectile to pass straight through
            // them.
            if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

                if (entityplayer.capabilities.disableDamage || this.getCaster() instanceof EntityPlayer
                        && !((EntityPlayer) this.getCaster()).canAttackPlayer(entityplayer)) {
                    raytraceresult = null;
                }
            }

            List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox());

            // If the arrow hits something
            if (raytraceresult != null) {
                // If the arrow hits an entity
                if (raytraceresult.entityHit != null && raytraceresult.entityHit != getCaster()) {
                    if (!hit.isEmpty() && hit.contains(raytraceresult.entityHit)) {
                        DamageSource damagesource;

                        if (this.getCaster() == null) {
                            damagesource = DamageSource.causeThrownDamage(this, this);
                        } else {
                            damagesource = MagicDamage.causeIndirectMagicDamage(this, this.getCaster(), this.getDamageType()).setProjectile();
                        }

                        if (raytraceresult.entityHit.attackEntityFrom(damagesource,
                                (float) (this.getDamage() * this.damageMultiplier))) {
                            if (raytraceresult.entityHit instanceof EntityLivingBase) {
                                EntityLivingBase entityHit = (EntityLivingBase) raytraceresult.entityHit;

                                this.onEntityHit(entityHit);

                                if (this.knockbackStrength > 0) {
                                    float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                    if (f4 > 0.0F) {
                                        raytraceresult.entityHit.addVelocity(
                                                this.motionX * this.knockbackStrength * 0.6000000238418579D
                                                        / (double) f4,
                                                0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D
                                                        / (double) f4);
                                    }
                                }

                                // Thorns enchantment
                                if (this.getCaster() != null) {
                                    EnchantmentHelper.applyThornEnchantments(entityHit, this.getCaster());
                                    EnchantmentHelper.applyArthropodEnchantments(this.getCaster(), entityHit);
                                }

                                if (this.getCaster() != null && raytraceresult.entityHit != this.getCaster()
                                        && raytraceresult.entityHit instanceof EntityPlayer
                                        && this.getCaster() instanceof EntityPlayerMP) {
                                    ((EntityPlayerMP) this.getCaster()).connection
                                            .sendPacket(new SPacketChangeGameState(6, 0.0F));
                                }
                            }

                            if (!(raytraceresult.entityHit instanceof EntityEnderman) && !this.doOverpenetration()) {
                                this.setDead();
                            }
                        } else {
                            if (!this.doOverpenetration()) this.setDead();

                            // Was the 'rebound' that happened when entities were immune to damage
                            /* this.motionX *= -0.10000000149011612D; this.motionY *= -0.10000000149011612D; this.motionZ *=
                             * -0.10000000149011612D; this.rotationYaw += 180.0F; this.prevRotationYaw += 180.0F;
                             * this.ticksInAir = 0; */
                        }
                    }
                }
            }
            if (!hit.isEmpty()) {
                if (raytraceresult != null)
                    hit.remove(raytraceresult.entityHit);
                for (Entity e : hit) {
                    if (AllyDesignationSystem.isValidTarget(this, e) && e != getCaster()) {
                        DamageSource damagesource;

                        if (this.getCaster() == null) {
                            damagesource = DamageSource.causeThrownDamage(this, this);
                        } else {
                            damagesource = MagicDamage.causeIndirectMagicDamage(this, this.getCaster(), this.getDamageType()).setProjectile();
                        }

                        if (e.attackEntityFrom(damagesource,
                                (float) (this.getDamage() * this.damageMultiplier))) {
                            if (e instanceof EntityLivingBase) {

                                this.onEntityHit((EntityLivingBase) e);

                                if (this.knockbackStrength > 0) {
                                    float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                    if (f4 > 0.0F) {
                                        e.addVelocity(
                                                this.motionX * this.knockbackStrength * 0.6000000238418579D
                                                        / (double) f4,
                                                0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D
                                                        / (double) f4);
                                    }
                                }

                                // Thorns enchantment
                                if (this.getCaster() != null) {
                                    EnchantmentHelper.applyThornEnchantments((EntityLivingBase) e, this.getCaster());
                                    EnchantmentHelper.applyArthropodEnchantments(this.getCaster(), e);
                                }

                                if (this.getCaster() != null && e != this.getCaster()
                                        && e instanceof EntityPlayer
                                        && this.getCaster() instanceof EntityPlayerMP) {
                                    ((EntityPlayerMP) this.getCaster()).connection
                                            .sendPacket(new SPacketChangeGameState(6, 0.0F));
                                }
                            }

                            if (!(e instanceof EntityEnderman) && !this.doOverpenetration()) {
                                this.setDead();
                            }
                        } else {
                            if (!this.doOverpenetration()) this.setDead();
                        }
                    }
                }
            }
            // If the arrow hits a block
            else {
                if (raytraceresult != null) {
                    this.blockX = raytraceresult.getBlockPos().getX();
                    this.blockY = raytraceresult.getBlockPos().getY();
                    this.blockZ = raytraceresult.getBlockPos().getZ();
                    this.stuckInBlock = this.world.getBlockState(raytraceresult.getBlockPos());
                    this.motionX = (float) (raytraceresult.hitVec.x - this.posX);
                    this.motionY = (float) (raytraceresult.hitVec.y - this.posY);
                    this.motionZ = (float) (raytraceresult.hitVec.z - this.posZ);
                    // f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ *
                    // this.motionZ);
                    // this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
                    // this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
                    // this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
                    // this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;

                    if (this.collided && (world.getBlockState(getPosition()).getBlock() != Blocks.AIR &&
                            world.getBlockState(getPosition()).isFullCube()))
                        this.onBlockHit(raytraceresult);

                    if (this.stuckInBlock.getMaterial() != Material.AIR) {
                        this.stuckInBlock.getBlock().onEntityCollision(this.world, raytraceresult.getBlockPos(),
                                this.stuckInBlock, this);
                    }
                }
            }
        }

        // Seeking
        if (getSeekingStrength() > 0) {

            Vec3d velocity = new Vec3d(motionX, motionY, motionZ);

            RayTraceResult hit = RayTracer.rayTrace(world, this.getPositionVector(),
                    this.getPositionVector().add(velocity.scale(SEEKING_TIME)), getSeekingStrength(), false,
                    true, false, EntityLivingBase.class, RayTracer.ignoreEntityFilter(null));

            if (hit != null && hit.entityHit != null) {

                if (AllyDesignationSystem.isValidTarget(getCaster(), hit.entityHit)) {

                    Vec3d direction = new Vec3d(hit.entityHit.posX, hit.entityHit.posY + hit.entityHit.height / 2,
                            hit.entityHit.posZ).subtract(this.getPositionVector()).normalize().scale(velocity.length());

                    motionX = motionX + 2 * (direction.x - motionX) / SEEKING_TIME;
                    motionY = motionY + 2 * (direction.y - motionY) / SEEKING_TIME;
                    motionZ = motionZ + 2 * (direction.z - motionZ) / SEEKING_TIME;
                }
            }
        }

        //Failsafes in case the default raytrace stuff fails
        if (!noClip) {
            for (int j = 0; j < getSize() * 10; j++) {
                AxisAlignedBB boundingBox = getEntityBoundingBox();
                double x = boundingBox.minX + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxX - boundingBox.minX);
                double y = boundingBox.minY + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxY - boundingBox.minY);
                double z = boundingBox.minZ + ArcaneUtils.getRandomNumberInRange(1, 10) / 10F * (boundingBox.maxZ - boundingBox.minZ);
                BlockPos pos = new BlockPos(x, y, z);
                if (canCollideWithSolid(world.getBlockState(pos)) && world.collidesWithAnyBlock(getEntityBoundingBox().grow(blockBoxX, blockBoxY, blockBoxZ))) {
                    onGround = true;
                    setDead();
                }
            }
        }


        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        // f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        // for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI);
        // this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        // {
        // ;
        // }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        float f3 = 0.99F;

        if (this.isInWater()) {
            for (int l = 0; l < 4; ++l) {
                float f4 = 0.25F;
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) f4,
                        this.posY - this.motionY * (double) f4, this.posZ - this.motionZ * (double) f4, this.motionX,
                        this.motionY, this.motionZ);
            }

            f3 = 0.8F;
        }

        if (this.isWet()) {
            this.extinguish();
        }

        if (this.doDeceleration()) {
            this.motionX *= f3;
            this.motionY *= f3;
            this.motionZ *= f3;
        }

        if (this.doGravity()) {
            this.motionY -= 0.05;
        }
        this.setPosition(this.posX, this.posY, this.posZ);
        this.doBlockCollisions();
    }

    @Override
    public void shoot(double x, double y, double z, float speed, float randomness) {
        float f2 = MathHelper.sqrt(x * x + y * y + z * z);
        x /= f2;
        y /= f2;
        z /= f2;
        x += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) randomness;
        y += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) randomness;
        z += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) randomness;
        x *= speed;
        y *= speed;
        z *= speed;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f3 = MathHelper.sqrt(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    // There was an override for setPositionAndRotationDirect here, but it was exactly the same as the superclass
    // method (in Entity), so it was removed since it was redundant.

    /**
     * Sets the velocity to the args. Args: x, y, z. THIS IS CLIENT SIDE ONLY! DO NOT USE IN COMMON OR SERVER CODE!
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
        this.motionX = p_70016_1_;
        this.motionY = p_70016_3_;
        this.motionZ = p_70016_5_;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70016_3_, f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void setDead() {
        super.setDead();
        //There was some weird shenanigans going on with projectiles instantly dying
        if (this.isDead && !world.isRemote) {
            //Thread.dumpStack();
        }
    }

    // Data reading and writing

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        tag.setShort("xTile", (short) this.blockX);
        tag.setShort("yTile", (short) this.blockY);
        tag.setShort("zTile", (short) this.blockZ);
        tag.setShort("life", (short) this.ticksInGround);
        if (this.stuckInBlock != null) {
            ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.stuckInBlock.getBlock());
            tag.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        }
        tag.setByte("inData", (byte) this.inData);
        tag.setByte("shake", (byte) this.arrowShake);
        tag.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        tag.setFloat("damageMultiplier", this.damageMultiplier);
        if (this.getCaster() != null) {
            tag.setUniqueId("casterUUID", this.getCaster().getUniqueID());
        }
        //tag.setInteger("Behaviour", getBehaviour().getId());
        //	getBehaviour().save(NBTUtils.nestedCompound(tag, "BehaviorData"));

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.blockX = tag.getShort("xTile");
        this.blockY = tag.getShort("yTile");
        this.blockZ = tag.getShort("zTile");
        this.ticksInGround = tag.getShort("life");
        // Commented out for now because there's some funny stuff going on with blockstates and id.
        // this.stuckInBlock = Block.getBlockById(tag.getByte("inTile") & 255);
        this.inData = tag.getByte("inData") & 255;
        this.arrowShake = tag.getByte("shake") & 255;
        this.inGround = tag.getByte("inGround") == 1;
        this.damageMultiplier = tag.getFloat("damageMultiplier");
        casterUUID = tag.getUniqueId("casterUUID");
        //setBehaviour((MagicBoltBehaviour) Behaviour.lookup(tag.getInteger("Behaviour"), this));
        //getBehaviour().load(tag.getCompoundTag("BehaviourData"));

    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        if (this.getCaster() != null) buffer.writeInt(this.getCaster().getEntityId());
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        if (buffer.isReadable()) this.caster = new WeakReference<>(
                (EntityLivingBase) this.world.getEntityByID(buffer.readInt()));
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @SideOnly(Side.CLIENT)


    // Miscellaneous overrides

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(SYNC_SIZE, 1.0F);
        dataManager.register(SYNC_BEHAVIOUR, new MagicBoltBehaviour.Idle());
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit instanceof EntityLivingBase && result.entityHit != getCaster()) {
            onEntityHit((EntityLivingBase) result.entityHit);
        }
    }

}