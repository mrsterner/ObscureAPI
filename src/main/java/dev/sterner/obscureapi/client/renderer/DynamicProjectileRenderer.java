package dev.sterner.obscureapi.client.renderer;

import dev.sterner.obscureapi.api.DynamicProjectile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public abstract class DynamicProjectileRenderer<T extends DynamicProjectile> extends EntityRenderer<T> {
	private final EntityModel<DynamicProjectile> MODEL;

	public DynamicProjectileRenderer(EntityRendererFactory.Context context, EntityModel<DynamicProjectile> model) {
		super(context);
		this.MODEL = model;
	}

	public abstract Identifier getGlowingTextureLocation(T var1);

	@Override
	public void render(@NotNull T entity, float headYaw, float limbSwing, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int packetLight) {
		matrices.push();
		Identifier texture = this.getTexture(entity);
		Identifier glowingTexture = this.getGlowingTextureLocation(entity);
		matrices.scale(-1.0F, -1.0F, 1.0F);
		matrices.translate(0.0, -1.5010000467300415, 0.0);
		matrices.multiply(Axis.Y_POSITIVE.rotation(90.0F));
		this.MODEL.setAngles(entity, limbSwing, 0.0F, MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), MinecraftClient.getInstance().isPaused() ? (float)entity.age : (float)(entity.age - 1), (float)entity.age), 0.0F, 0.0F);
		this.MODEL.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture)), packetLight, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		if (glowingTexture != null) {
			this.MODEL.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(glowingTexture)), packetLight, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		}
		super.render(entity, headYaw, limbSwing, matrices, vertexConsumers, packetLight);
		matrices.pop();
	}
}
