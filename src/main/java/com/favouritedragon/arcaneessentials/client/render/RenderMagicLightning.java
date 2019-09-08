package com.favouritedragon.arcaneessentials.client.render;

import com.favouritedragon.arcaneessentials.common.entity.EntityMagicLightning;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static electroblob.wizardry.util.WizardryUtilities.ANTI_Z_FIGHTING_OFFSET;

public class RenderMagicLightning extends Render<EntityMagicLightning> {

	public RenderMagicLightning(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull EntityMagicLightning entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING, entity).pos(entity.getPositionVector().add(0, 120, 0)).target(entity.getPositionVector())
				.scale(4).spawn(entity.world);
		Vec3d pos = entity.getPositionVector().add(new Vec3d(EnumFacing.UP.getDirectionVec()).scale(ANTI_Z_FIGHTING_OFFSET));
		ParticleBuilder.create(ParticleBuilder.Type.SCORCH, entity).pos(pos).scale(2).spawn(entity.world);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityMagicLightning entity) {
		return null;
	}
}
