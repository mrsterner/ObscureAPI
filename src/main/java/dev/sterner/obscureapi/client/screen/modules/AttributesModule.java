package dev.sterner.obscureapi.client.screen.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.obscureapi.client.screen.BookScreen;
import dev.sterner.obscureapi.client.screen.widgets.ObscureTooltip;
import dev.sterner.obscureapi.client.screen.widgets.ObscureWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.List;

@ClientOnly
public class AttributesModule implements BookModule {
	public final List<ObscureWidget> WIDGETS = new ArrayList();

	public AttributesModule() {
	}

	public void tick(BookScreen screen) {
	}

	public void init(BookScreen screen) {
		this.WIDGETS.clear();
		this.WIDGETS.add(this.entry(screen, -69, -12, "magicDamage", "magic_damage"));
		this.WIDGETS.add(this.entry(screen, -69, 5, "criticalHit", "critical_hit"));
		this.WIDGETS.add(this.entry(screen, -69, 22, "criticalDamage", "critical_damage"));
		this.WIDGETS.add(this.entry(screen, -69, 39, "accuracy", "accuracy"));
		this.WIDGETS.add(this.entry(screen, -69, 56, "penetration", "penetration"));
		this.WIDGETS.add(this.entry(screen, 68, -75, "magicResistance", "magic_resistance"));
		this.WIDGETS.add(this.entry(screen, 68, -58, "dodge", "dodge"));
		this.WIDGETS.add(this.entry(screen, 68, -41, "parry", "parry"));
		this.WIDGETS.add(this.entry(screen, 68, -24, "resilience", "resilience"));
		this.WIDGETS.add(this.entry(screen, 68, -7, "regeneration", "regeneration"));
		this.WIDGETS.add(this.entry(screen, 68, 10, "healingPower", "healing_power"));
	}

	private ObscureWidget entry(final BookScreen screen, int x, int y, final String name, final String tooltip) {
		return new ObscureWidget(true, screen, x, y, 111, 17, (button) -> {
		}, new ObscureTooltip() {
			public List<Text> getTooltip(ObscureWidget widget, Screen screen) {
				return TextUtils.buildTooltip(new ArrayList(), 30, TextUtils.translation("obscure_api.book.module_attributes." + tooltip), "§c", "§7 ");
			}
		}) {
			@Override
			public void renderButton(@NotNull MatrixStack pose, int mouseX, int mouseY, float partialTicks) {
				screen.getClient().textRenderer.draw(pose, TextUtils.translatableText("attribute.obscure_api." + name), (float)(this.x + 20), (float)(this.y + 4), 0);
				if (this.hovered) {
					ClientUtils.start();
					RenderSystem._setShaderTexture(0, new Identifier("obscure_api:textures/gui/obscure_book/attribute_selection.png"));
					DrawableHelper.drawTexture(pose, this.x, this.y, 0.0F, 0.0F, 16, 16, 16, 16);
					ClientUtils.end();
				}

			}
		};
	}

	public void render(BookScreen screen, MatrixStack matrixStack, int mouseX, int mouseY) {
		ClientUtils.start();
		RenderSystem._setShaderTexture(0, new Identifier("obscure_api:textures/gui/obscure_book/module_attributes.png"));
		DrawableHelper.drawTexture(matrixStack, -145, -96, 0.0F, 0.0F, 290, 192, 290, 192);
		ClientUtils.end();
		screen.getClient().textRenderer.draw(matrixStack, TextUtils.translatableText("obscure_api.book.module_attributes.name"), -123.0F, -82.0F, 0);
		matrixStack.push();
		matrixStack.translate(-120.0, -70.0, 0.0);
		matrixStack.scale(ClientUtils.scale(), ClientUtils.scale(), 1.0F);
		ClientUtils.drawText(TextUtils.buildTooltip(new ArrayList(), 30, TextUtils.translation("obscure_api.book.module_attributes.content"), "§0"), screen.getClient().textRenderer, matrixStack);
		matrixStack.pop();
	}

	public Identifier getIcon() {
		return new Identifier("obscure_api", "textures/gui/obscure_book/icon_attributes.png");
	}

	public String getModuleName() {
		return "attributes";
	}

	public List<ObscureWidget> getWidgets() {
		return this.WIDGETS;
	}
}
