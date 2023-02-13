package dev.sterner.obscureapi.client.screen.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;


@ClientOnly
public abstract class ObscureWidget extends TexturedButtonWidget {
	public final ObscureTooltip TOOLTIP;
	public final Screen SCREEN;
	public final boolean SQUARE;
	public int CENTER_X = 0;
	public int CENTER_Y = 0;

	public void tick() {
	}

	public ObscureWidget(boolean square, Screen screen, int x, int y, int width, int height, ButtonWidget.PressAction action, ObscureTooltip tooltip) {
		super(x - width / 2, y - height / 2, width, height, 0, 0, 0, new Identifier("obscure_skills:empty"), 16, 16, action, Text.empty());
		this.TOOLTIP = tooltip;
		this.SCREEN = screen;
		this.SQUARE = square;
	}

	public void renderButton(@NotNull MatrixStack pose, int mouseX, int mouseY, float partialTicks) {
	}

	@Override
	public void render(@NotNull MatrixStack pose, int mouseX, int mouseY, float partialTicks) {
		this.hovered = this.isMouseOver((double)mouseX, (double)mouseY);
		this.CENTER_X = MinecraftClient.getInstance().currentScreen != null ? MinecraftClient.getInstance().currentScreen.width / 2 : 0;
		this.CENTER_Y = MinecraftClient.getInstance().currentScreen != null ? MinecraftClient.getInstance().currentScreen.height / 2 : 0;
		this.renderButton(pose, mouseX, mouseY, partialTicks);
	}

	public void drawTooltip(Screen screen, MatrixStack pose, int mouseX, int mouseY) {
		if (this.hovered && this.TOOLTIP != null) {
			pose.push();
			pose.translate((double)(-this.CENTER_X), (double)(-this.CENTER_Y), 0.0);
			screen.renderTooltip(pose, this.TOOLTIP.getTooltip(this, screen), mouseX, mouseY);
			pose.pop();
		}

	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (!this.SQUARE) {
			int cx = this.x + this.CENTER_X + this.width / 2;
			int cy = this.y + this.CENTER_Y + this.height / 2;
			int offset = ((int)mouseX - cx < 0 ? ((int)mouseX - cx) * -1 : (int)mouseX - cx) + ((int)mouseY - cy < 0 ? ((int)mouseY - cy) * -1 : (int)mouseY - cy);
			return offset < this.width / 2;
		} else {
			return mouseX >= (double)this.x + (double)this.CENTER_X && mouseY >= (double)this.y + (double)this.CENTER_Y && mouseX < (double)(this.x + this.width + this.CENTER_X) && mouseY < (double)(this.y + this.height + this.CENTER_Y);
		}
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return this.active && this.visible && this.hovered;
	}
}
