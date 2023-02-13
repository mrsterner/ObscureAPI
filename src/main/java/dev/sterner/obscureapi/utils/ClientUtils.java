package dev.sterner.obscureapi.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class ClientUtils {
	public ClientUtils() {
	}

	public static void drawWindow(MatrixStack pose, WindowType type, int width, int height) {
		drawWindow(pose, type.getTexture(), width, height);
	}

	public static void drawWindow(MatrixStack pose, Identifier texture, int width, int height) {
		start();
		RenderSystem._setShaderTexture(0, texture);
		DrawableHelper.drawTexture(pose, 0, 0, 0.0F, 0.0F, 16, 16, 32, 32);
		DrawableHelper.drawTexture(pose, 16 + Math.max(width - 32, 0), 0, 16.0F, 0.0F, 16, 16, 32, 32);
		DrawableHelper.drawTexture(pose, 0, 16 + Math.max(height - 32, 0), 0.0F, 16.0F, 16, 16, 32, 32);
		DrawableHelper.drawTexture(pose, 16 + Math.max(width - 32, 0), 16 + Math.max(height - 32, 0), 16.0F, 16.0F, 16, 16, 32, 32);
		if (width > 32) {
			pose.push();
			pose.translate(16.0, 0.0, 0.0);
			pose.scale((float)(width - 32), 1.0F, 1.0F);
			DrawableHelper.drawTexture(pose, 0, 0, 16.0F, 0.0F, 1, 16, 32, 32);
			pose.pop();
			pose.push();
			pose.translate(16.0, (double)(16 + Math.max(height - 32, 0)), 0.0);
			pose.scale((float)(width - 32), 1.0F, 1.0F);
			DrawableHelper.drawTexture(pose, 0, 0, 16.0F, 16.0F, 1, 16, 32, 32);
			pose.pop();
		}

		if (height > 32) {
			pose.push();
			pose.translate(0.0, 16.0, 0.0);
			pose.scale(1.0F, (float)(height - 32), 1.0F);
			DrawableHelper.drawTexture(pose, 0, 0, 0.0F, 16.0F, 16, 1, 32, 32);
			pose.pop();
			pose.push();
			pose.translate((double)(16 + Math.max(width - 32, 0)), 16.0, 0.0);
			pose.scale(1.0F, (float)(height - 32), 1.0F);
			DrawableHelper.drawTexture(pose, 0, 0, 16.0F, 16.0F, 16, 1, 32, 32);
			pose.pop();
		}

		if (width > 32 && height > 32) {
			pose.push();
			pose.translate(16.0, 16.0, 0.0);
			pose.scale((float)(width - 32), (float)(height - 32), 1.0F);
			DrawableHelper.drawTexture(pose, 0, 0, 16.0F, 16.0F, 1, 1, 32, 32);
			pose.pop();
		}

		end();
	}

	public static void drawStyledWindow(MatrixStack pose, int width, int height) {
		start();
		RenderSystem._setShaderTexture(0, new Identifier("obscure_api:textures/gui/background.png"));
		DrawableHelper.drawTexture(pose, 1, 1, (float)(320 - Math.round((float)(width - 2) / 2.0F)), (float)(180 - Math.round((float)(height - 2) / 2.0F)), width - 2, height - 2, 640, 360);
		end();
		drawWindow(pose, new Identifier("obscure_api:textures/gui/background_border_0.png"), width + 2, height + 2);
		drawWindow(pose, new Identifier("obscure_api:textures/gui/background_border_1.png"), width, height);
	}

	public static void drawText(List<Text> lines, TextRenderer font, MatrixStack pose) {
		int i = 0;

		for(Iterator var4 = lines.iterator(); var4.hasNext(); ++i) {
			Text line = (Text)var4.next();
			Objects.requireNonNull(font);
			font.draw(pose, line, 0.0F, (float)(i * 9), 0);
		}

	}

	public static float scale() {
		return switch (MinecraftClient.getInstance().options.getGuiScale().get()) {
			case 2 -> 0.5F;
			case 3 -> 0.666666F;
			case 4 -> 0.75F;
			default -> 1.0F;
		};
	}

	public static void start(float r, float g, float b, float a) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(r, g, b, a);
	}

	public static void start() {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	}

	public static void end() {
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static enum WindowType {
		COMMON(0),
		FLAT(1),
		HEADER(2);

		private final int type;

		private WindowType(int type) {
			this.type = type;
		}

		public Identifier getTexture() {
			return new Identifier("obscure_api:textures/gui/window_" + this.type + ".png");
		}
	}
}
