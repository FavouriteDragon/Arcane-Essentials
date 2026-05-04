package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.SoundEvents;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * A stationary water-wall construct. Entities (other than the caster) that pass through it have
 * their horizontal and upward velocity reduced to ~5% each tick, making passage very slow.
 * noClip is true (inherited from EntityMagicConstruct), so nothing is physically blocked.
 */
public class EntityWaterWall extends EntityMagicConstruct {

    public EntityWaterWall(World world) {
        super(world);
        this.lifetime = 200;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    // Wall slab dimensions — must match RenderWaterWall exactly.
    private static final float HALF_WIDTH = 0.75f; // multiplied by size
    private static final float HALF_HEIGHT = 0.8f; // multiplied by size
    private static final float HALF_DEPTH  = 0.6f; // fixed (renderer d value)

    /**
     * Returns true if the given entity's feet position is inside the oriented wall slab.
     * Uses dot-product projection onto the wall's local width and depth axes.
     */
    private boolean isInsideWall(EntityLivingBase entity) {
        float size = getSize();
        float halfW = size * HALF_WIDTH;
        float halfH = size * HALF_HEIGHT;
        float yawRad = (float) (rotationYaw * Math.PI / 180.0);
        // Width axis (along the wall face): (cos, 0, sin)
        // Depth axis (through the face):   (-sin, 0, cos)
        float widthAxisX =  MathHelper.cos(yawRad);
        float widthAxisZ =  MathHelper.sin(yawRad);
        float depthAxisX = -MathHelper.sin(yawRad);
        float depthAxisZ =  MathHelper.cos(yawRad);

        double dx = entity.posX - posX;
        double dy = entity.posY - posY;
        double dz = entity.posZ - posZ;

        double projWidth = dx * widthAxisX + dz * widthAxisZ;
        double projDepth = dx * depthAxisX + dz * depthAxisZ;

        return Math.abs(projWidth) <= halfW
            && Math.abs(projDepth) <= HALF_DEPTH
            && dy >= 0 && dy <= halfH;
    }

    @Override
    public void onUpdate() {
        this.motionX = this.motionY = this.motionZ = 0;
        double savedX = posX, savedY = posY, savedZ = posZ;
        super.onUpdate();
        // Restore position — super may drift us via setSize/move
        this.setPosition(savedX, savedY, savedZ);
        this.motionX = this.motionY = this.motionZ = 0;

        float yawRad = (float) (rotationYaw * Math.PI / 180.0);

        if (!world.isRemote) {
            // Fetch candidates from a conservative AABB, then filter with the precise OBB test
            float size = getSize();
            float search = size * HALF_WIDTH + HALF_DEPTH + 1f;
            AxisAlignedBB searchBox = new AxisAlignedBB(
                    posX - search, posY, posZ - search,
                    posX + search, posY + size * HALF_HEIGHT, posZ + search);
            List<EntityLivingBase> candidates = world.getEntitiesWithinAABB(
                    EntityLivingBase.class, searchBox);
            for (EntityLivingBase entity : candidates) {
                if (!isInsideWall(entity)) continue;
                // Extinguish fire on everyone inside the wall, including caster and allies
                if (entity.isBurning()) {
                    entity.extinguish();
                }
                if (entity != getCaster() && isValidTarget(entity)) {
                    // Cancel velocity through the wall (depth axis) almost entirely,
                    // leave along-wall velocity mostly intact so sliding along it feels natural
                    float depthX = -MathHelper.sin(yawRad);
                    float depthZ =  MathHelper.cos(yawRad);
                    // Project motion onto depth axis
                    double depthDot = entity.motionX * depthX + entity.motionZ * depthZ;
                    // Strip out the depth component almost entirely (retain 1%)
                    entity.motionX -= depthDot * depthX * 0.99;
                    entity.motionZ -= depthDot * depthZ * 0.99;
                    // Also dampen along-wall motion heavily
                    entity.motionX *= 0.1;
                    entity.motionZ *= 0.1;
                    if (entity.motionY > 0.05) {
                        entity.motionY *= 0.01;
                    }
                    // Slowness III for 1 second (20 ticks), refreshed every tick inside the wall
                    entity.addPotionEffect(new net.minecraft.potion.PotionEffect(
                            net.minecraft.init.MobEffects.SLOWNESS, 20, 2, false, false));
                    // Damage water-sensitive mobs once per second
                    if ((entity instanceof EntityEnderman || entity instanceof EntityBlaze)
                            && ticksExisted % 20 == 0) {
                        entity.attackEntityFrom(DamageSource.DROWN, 2.0f);
                    }
                }
            }
            // Extinguish fire blocks within the wall volume (once per second)
            if (ticksExisted % 20 == 0) for (BlockPos bp : BlockPos.getAllInBox(
                    (int) Math.floor(posX - search), (int) Math.floor(posY),                      (int) Math.floor(posZ - search),
                    (int) Math.ceil(posX + search),  (int) Math.ceil(posY + size * HALF_HEIGHT), (int) Math.ceil(posZ + search))) {
                if (world.getBlockState(bp).getBlock() == Blocks.FIRE) {
                    world.setBlockToAir(bp);
                }
            }
            if (ticksExisted % 20 == 0) {
                world.playSound(null, posX, posY, posZ,
                        SoundEvents.ENTITY_PLAYER_SWIM, SoundCategory.AMBIENT,
                        1.2f, 0.8f + world.rand.nextFloat() * 0.4f);
            }
        } else {
            // Particles spread along the wall face
            float widthX = MathHelper.cos(yawRad);
            float widthZ = MathHelper.sin(yawRad);
            float size = getSize();
            // Depth axis — perpendicular to wall face
            float depthX = -MathHelper.sin(yawRad);
            float depthZ =  MathHelper.cos(yawRad);

            // Spawn particles on both faces every tick
            for (int face = -1; face <= 1; face += 2) {
                float faceOffX = depthX * HALF_DEPTH * face;
                float faceOffZ = depthZ * HALF_DEPTH * face;

                // Drops falling down the face surface
                double t = (world.rand.nextDouble() - 0.5) * size * 1.5;
                world.spawnParticle(EnumParticleTypes.WATER_DROP,
                        posX + widthX * t + faceOffX,
                        posY + world.rand.nextDouble() * size * HALF_HEIGHT,
                        posZ + widthZ * t + faceOffZ,
                        0, -0.05, 0);

                // Drops spraying outward from the face, arcing downward
                for (int i = 0; i < 2; i++) {
                    t = (world.rand.nextDouble() - 0.5) * size * 1.5;
                    double speed = 0.04 + world.rand.nextDouble() * 0.06;
                    world.spawnParticle(EnumParticleTypes.WATER_DROP,
                            posX + widthX * t + faceOffX,
                            posY + world.rand.nextDouble() * size * HALF_HEIGHT,
                            posZ + widthZ * t + faceOffZ,
                            depthX * speed * face,
                            -0.03 - world.rand.nextDouble() * 0.05,
                            depthZ * speed * face);
                }

                // Splash on the face
                if (ticksExisted % 2 == 0) {
                    t = (world.rand.nextDouble() - 0.5) * size * 1.5;
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH,
                            posX + widthX * t + faceOffX,
                            posY + world.rand.nextDouble() * size * HALF_HEIGHT,
                            posZ + widthZ * t + faceOffZ,
                            world.rand.nextGaussian() * 0.03,
                            0.02 + world.rand.nextDouble() * 0.03,
                            world.rand.nextGaussian() * 0.03);
                }

                // Rising bubbles on the face
                if (ticksExisted % 3 == 0) {
                    t = (world.rand.nextDouble() - 0.5) * size * 1.5;
                    ParticleBuilder.create(ParticleBuilder.Type.MAGIC_BUBBLE)
                            .pos(posX + widthX * t + faceOffX,
                                    posY + world.rand.nextDouble() * size * HALF_HEIGHT,
                                    posZ + widthZ * t + faceOffZ)
                            .vel(0, 0.025 + world.rand.nextDouble() * 0.02, 0)
                            .time(15 + (int) (size * 2))
                            .scale(0.15f + size * 0.06f)
                            .spawn(world);
                }
            }
        }
    }
}
