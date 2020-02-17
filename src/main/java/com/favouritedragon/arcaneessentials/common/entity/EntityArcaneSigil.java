package com.favouritedragon.arcaneessentials.common.entity;

import electroblob.wizardry.util.AllyDesignationSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityArcaneSigil extends EntityMagicConstruct {

	private double range;

	public void setRange(double range) {
		this.range = range;
	}

	//Not to be confused with the arcane sigil, this functions as a teleporter+gate, rather than a simple teleportation hindrance,
	public EntityArcaneSigil(World world) {
		super(world);
		this.setSize(3, 0.5F);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		List<Entity> nearby = world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox());
		if (!nearby.isEmpty()) {
			Vec3d dir = getLookVec().scale(range);
			for (Entity teleport : nearby) {
				if (teleport != getCaster() && AllyDesignationSystem.isValidTarget(this, teleport)) {
					if (teleport.canBeCollidedWith() && !(teleport instanceof EntityXPOrb)) {
						teleport.setPositionAndUpdate(posX + dir.x, posY + dir.y, posZ + dir.z);
					}

				}
			}
		}
	}
}
