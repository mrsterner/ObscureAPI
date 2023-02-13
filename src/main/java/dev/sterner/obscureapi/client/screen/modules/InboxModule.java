package dev.sterner.obscureapi.client.screen.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.obscureapi.client.screen.BookScreen;
import dev.sterner.obscureapi.client.screen.widgets.ObscureTooltip;
import dev.sterner.obscureapi.client.screen.widgets.ObscureWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ClientOnly
public class InboxModule implements BookModule {
	private static final Identifier RESOURCES = new Identifier("obscure_api", "textures/gui/obscure_book/module_inbox.png");
	private static ObscuriaCollection.Message OPENED;
	public final List<ObscureWidget> WIDGETS = new ArrayList();

	public InboxModule() {
	}

	public void tick(BookScreen screen) {
		this.WIDGETS.forEach(ObscureWidget::tick);
	}

	public void init(BookScreen screen) {
		this.WIDGETS.clear();
		if (OPENED == null) {
			int xPos = -69;
			int yOffset = 0;
			Iterator var4 = ObscuriaCollection.getMessages().iterator();

			while(var4.hasNext()) {
				ObscuriaCollection.Message message = (ObscuriaCollection.Message)var4.next();
				this.WIDGETS.add(this.message(message, screen, xPos, -75 + 19 * yOffset++));
				if (this.WIDGETS.size() == 8) {
					xPos = 68;
					yOffset = 0;
				}
			}

			AtomicInteger newMessages = new AtomicInteger();
			ObscuriaCollection.getMessages().forEach((messagex) -> {
				newMessages.set(newMessages.get() + (messagex.opened ? 0 : 1));
			});
			if (newMessages.get() > 0) {
				this.WIDGETS.add(new ObscureWidget(true, screen, 150, -79, 16, 16, (button) -> {
					ObscuriaCollection.getMessages().forEach((message) -> {
						ObscuriaCollection.setOpened(message, true);
					});
				}, new ObscureTooltip() {
					public List<Text> getTooltip(ObscureWidget widget, Screen screen) {
						return List.of(TextUtils.component("§fMark All as Read"));
					}
				}) {
					public void renderButton(@NotNull MatrixStack pose, int mouseX, int mouseY, float partialTicks) {
						ClientUtils.start();
						RenderSystem._setShaderTexture(0, InboxModule.RESOURCES);
						GuiText.drawTexture(pose, this.x, this.y, 240.0F, this.hovered ? 16.0F : 0.0F, 16, 16, 290, 192);
						ClientUtils.end();
					}
				});
			}
		} else {
			this.WIDGETS.add(new ObscureWidget(true, screen, -115, -79, 18, 10, (button) -> {
				OPENED = null;
				screen.reload();
			}, new ObscureTooltip() {
				public List<Text> getTooltip(ObscureWidget widget, Screen screen) {
					return List.of(TextUtils.component("§fBack"));
				}
			}) {
				public void renderButton(@NotNull MatrixStack pose, int mouseX, int mouseY, float partialTicks) {
					ClientUtils.start();
					RenderSystem._setShaderTexture(0, InboxModule.RESOURCES);
					GuiText.drawTexture(pose, this.x, this.y, 222.0F, this.hovered ? 10.0F : 0.0F, 18, 10, 290, 192);
					ClientUtils.end();
				}
			});
		}

	}

	private ObscureWidget message(final ObscuriaCollection.Message message, BookScreen screen, int x, int y) {
		return new ObscureWidget(true, screen, x, y, 111, 19, (button) -> {
			OPENED = message;
			screen.reload();
			ObscuriaCollection.setOpened(message, true);
		}, new ObscureTooltip() {
			public List<Text> getTooltip(ObscureWidget widget, Screen screen) {
				return TextUtils.buildTooltip(new ArrayList(), 30, message.TITLE + " > " + message.SUBJECT + " > > §8" + message.DATE, "§f", "§7 ");
			}
		}) {
			public void renderButton(@NotNull MatrixStack pose, int mouseX, int mouseY, float partialTicks) {
				ClientUtils.start();
				RenderSystem._setShaderTexture(0, InboxModule.RESOURCES);
				DrawableHelper.drawTexture(pose, this.x, this.y + 1, message.opened ? 111.0F : 0.0F, this.hovered ? 17.0F : 0.0F, 111, 17, 290, 192);
				pose.push();
				pose.translate((double)(this.x + 20), (double)(this.y + 5), 0.0);
				ClientUtils.drawText(List.of(TextUtils.component(message.TITLE)), MinecraftClient.getInstance().textRenderer, pose);
				pose.pop();
				ClientUtils.end();
			}
		};
	}

	public void render(BookScreen screen, MatrixStack matrixStack, int mouseX, int mouseY) {
		if (OPENED != null) {
			matrixStack.push();
			matrixStack.translate(-102.0, -82.0, 0.0);
			matrixStack.scale(ClientUtils.scale(), ClientUtils.scale(), 1.0F);
			ClientUtils.drawText(List.of(TextUtils.component("§7" + OPENED.DATE)), MinecraftClient.getInstance().textRenderer, matrixStack);
			matrixStack.pop();
			matrixStack.push();
			matrixStack.translate(-123.0, -70.0, 0.0);
			ClientUtils.drawText(List.of(TextUtils.component(OPENED.TITLE)), MinecraftClient.getInstance().textRenderer, matrixStack);
			matrixStack.pop();
			List<Text> content = new ArrayList();
			OPENED.CONTENT.forEach((line) -> {
				content.add(TextUtils.component(TextUtils.format(line)));
			});
			int PAGE_SIZE = this.getPageSize();
			if (content.size() <= PAGE_SIZE) {
				matrixStack.push();
				matrixStack.translate(-120.0, -58.0, 0.0);
				matrixStack.scale(ClientUtils.scale(), ClientUtils.scale(), 1.0F);
				ClientUtils.drawText(content, MinecraftClient.getInstance().textRenderer, matrixStack);
				matrixStack.pop();
			} else {
				List<Text> content1 = content.subList(0, PAGE_SIZE);
				List<Text> content2 = content.subList(PAGE_SIZE, content.size());
				matrixStack.push();
				matrixStack.translate(-120.0, -58.0, 0.0);
				matrixStack.scale(ClientUtils.scale(), ClientUtils.scale(), 1.0F);
				ClientUtils.drawText(content1, MinecraftClient.getInstance().textRenderer, matrixStack);
				matrixStack.pop();
				matrixStack.push();
				matrixStack.translate(18.0, -80.0, 0.0);
				matrixStack.scale(ClientUtils.scale(), ClientUtils.scale(), 1.0F);
				ClientUtils.drawText(content2, MinecraftClient.getInstance().textRenderer, matrixStack);
				matrixStack.pop();
			}

		}
	}

	private int getPageSize() {
		byte var10000;
		switch ((Integer) MinecraftClient.getInstance().options.m_231928_().m_231551_()) {
			case 1:
				var10000 = 14;
				break;
			case 2:
				var10000 = 30;
				break;
			case 3:
				var10000 = 20;
				break;
			default:
				var10000 = 18;
		}

		return var10000;
	}

	public Identifier getIcon() {
		return new Identifier("obscure_api", "textures/gui/obscure_book/icon_inbox.png");
	}

	public String getModuleName() {
		return "inbox";
	}

	public List<ObscureWidget> getWidgets() {
		return this.WIDGETS;
	}
}
