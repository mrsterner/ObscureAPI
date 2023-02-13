package dev.sterner.obscureapi.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class ModelVFX<T extends Entity> extends EntityModel<T> {
	public static final EntityModelLayer LAYER = new EntityModelLayer(new Identifier("modid", "modelfx"), "main");
	private final ModelPart mainLayer;
	private final ModelPart rotLayer;

	public ModelVFX(ModelPart root) {
		this.mainLayer = root.getChild("mainLayer");
		this.rotLayer = this.mainLayer.getChild("rotLayer");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();
		ModelPartData mainLayer = root.addChild("mainLayer", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		mainLayer.addChild("rotLayer", ModelPartBuilder.create().uv(-32, 0).cuboid(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.05F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}



	public void setupTransformations(float xOff, float yOff, float xRotMain, float yRotMain, float xRot, float yRot, float zRot) {
		this.mainLayer.pitch = (float)Math.toRadians((double)xRotMain);
		this.mainLayer.yaw = (float)Math.toRadians((double)yRotMain);
		this.rotLayer.pitch = (float)Math.toRadians((double)xRot);
		this.rotLayer.yaw = (float)Math.toRadians((double)yRot);
		this.rotLayer.roll = (float)Math.toRadians((double)zRot);
		this.rotLayer.pivotZ = xOff;
		this.rotLayer.pivotY = yOff;
	}


	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		mainLayer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
