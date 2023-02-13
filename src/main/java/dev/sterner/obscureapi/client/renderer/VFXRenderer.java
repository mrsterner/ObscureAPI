package dev.sterner.obscureapi.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.obscureapi.api.VFX;
import dev.sterner.obscureapi.client.model.ModelVFX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class VFXRenderer extends EntityRenderer<VFX> {
	private final ModelVFX<VFX> MODEL;

	public VFXRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.MODEL = new ModelVFX(context.getPart(ModelVFX.LAYER));
	}

	@Override
	public @NotNull Identifier getTexture(@NotNull VFX entity) {
		return entity.getTexture();
	}

	public void render(@NotNull VFX vfx, float headYaw, float limbSwing, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		float partialTicks = MinecraftClient.getInstance().getTickDelta();
		matrixStack.push();
		float scale = vfx.getScale(partialTicks);
		matrixStack.scale(scale, scale, scale);
		matrixStack.translate(0.0, 1.5, 0.0);
		matrixStack.multiply(Axis.Y_POSITIVE.rotation(180.0F - headYaw));
		Identifier identifier = this.getTexture(vfx);
		ModelVFX<VFX> model = this.MODEL;
		matrixStack.scale(-1.0F, -1.0F, 1.0F);
		matrixStack.multiply(Axis.Y_POSITIVE.rotation(90.0F));
		model.setAngles(vfx, limbSwing, 0.0F, MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), (float)(vfx.age - 1), (float)vfx.age), 0.0F, 0.0F);
		model.setupTransformations(vfx.getXOff(partialTicks), vfx.getYOff(partialTicks), vfx.getXRotMain(), vfx.getYRotMain(), vfx.getXRot(partialTicks), vfx.getYRot(partialTicks), vfx.getZRot(partialTicks));
		VertexConsumer vertexconsumer = vertexConsumerProvider.getBuffer(this.getRenderType(vfx, identifier));
		model.render(matrixStack, vertexconsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, vfx.getAlpha(partialTicks));
		super.render(vfx, headYaw, limbSwing, matrixStack, vertexConsumerProvider, i);
		matrixStack.pop();
	}

	private RenderLayer getRenderType(VFX vfx, Identifier resourceLocation) {
		return switch (vfx.getRenderType()) {
			case 1 -> RenderLayer.getEyes(resourceLocation);
			case 2 -> RenderLayer.getEndGateway();
			case 3 -> RenderLayer.getEndPortal();
			default -> RenderLayer.getEntityTranslucentEmissive(resourceLocation, false);
		};
	}
}
