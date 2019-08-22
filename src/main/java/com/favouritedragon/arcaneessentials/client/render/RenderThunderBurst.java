package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityThunderBurst;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class RenderThunderBurst extends Render<EntityThunderBurst> {
	public RenderThunderBurst(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityThunderBurst entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		Vec3d prevpos = entity.getPositionVector();
		assert entity.getCaster() != null;
		if (entity.ticksExisted <= 1) {
			for (double theta = 0; theta <= 180; theta += 1) {
				double dphi = 40 / Math.sin(Math.toRadians(theta));

				for (double phi = 0; phi < 360; phi += dphi) {
					double rphi = Math.toRadians(phi);
					double rtheta = Math.toRadians(theta);

					x = entity.ticksExisted  * Math.cos(rphi) * Math.sin(rtheta);
					y = entity.ticksExisted  * Math.sin(rphi) * Math.sin(rtheta);
					z = entity.ticksExisted  * Math.cos(rtheta);

					double px, py, pz;

					for(int i = 0; i < 4; i++) {
						px = x + entity.world.rand.nextDouble() - 0.5;
						py = y + entity.world.rand.nextDouble() - 0.5;
						pz = z + entity.world.rand.nextDouble() - 0.5;
						ParticleBuilder.create(ParticleBuilder.Type.SPARK).pos(px + entity.posX, py + entity.getEntityBoundingBox().minY,
								pz + entity.posZ).vel(entity.world.rand.nextDouble() * x, entity.world.rand.nextDouble() * y,
										entity.world.rand.nextDouble() * z).time(10).spawn(entity.world);
					}
				}
			}

		}
		ParticleBuilder.create(ParticleBuilder.Type.SPHERE).time(10)
				.pos(entity.getPositionVector().add(0, entity.getCaster().height / 2, 0)).clr(138, 255, 255)
				.scale(entity.ticksExisted * 0.9F).spawn(entity.world);


	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityThunderBurst entity) {
		return null;
	}
}

