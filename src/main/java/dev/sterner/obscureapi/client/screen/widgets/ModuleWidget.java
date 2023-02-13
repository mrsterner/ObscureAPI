package dev.sterner.obscureapi.client.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.obscureapi.client.screen.BookScreen;
import dev.sterner.obscureapi.client.screen.modules.BookModule;
import dev.sterner.obscureapi.client.screen.modules.HomeModule;
import dev.sterner.obscureapi.client.screen.modules.InboxModule;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ClientOnly
public class ModuleWidget extends ObscureWidget {
	public final BookModule MODULE;
	private final boolean COLOR;

	public ModuleWidget(Screen screen, BookModule module, boolean color, int x, int y) {
		super(true, screen, x, y, 24, 38, (button) -> {
			if (button instanceof ModuleWidget widget) {
				Screen patt1287$temp = widget.SCREEN;
				if (patt1287$temp instanceof BookScreen bookScreen) {
					if (isLoaded(widget)) {
						bookScreen.openModule(widget.MODULE);
					}
				}
			}

		}, new ObscureTooltip() {
			public List<Text> getTooltip(ObscureWidget widget, Screen screen) {
				if (widget instanceof ModuleWidget moduleWidget) {
					ArrayList var10000;
					String var10002;
					if (moduleWidget.MODULE instanceof InboxModule && ObscuriaCollection.isLoaded()) {
						AtomicInteger newMessages = new AtomicInteger();
						ObscuriaCollection.getMessages().forEach((message) -> {
							newMessages.set(newMessages.get() + (message.opened ? 0 : 1));
						});
						var10000 = new ArrayList();
						var10002 = TextUtils.translation("obscure_api.book.module_inbox.name");
						return TextUtils.buildTooltip(var10000, 30, var10002 + (newMessages.get() > 0 ? " > " + TextUtils.translation("obscure_api.book.module_inbox.unread_messages").replace("#", "" + newMessages.get()) : ""), "§f", "§2 ");
					} else {
						var10000 = new ArrayList();
						var10002 = TextUtils.translation("obscure_api.book.module_" + moduleWidget.MODULE.getModuleName() + ".name");
						return TextUtils.buildTooltip(var10000, 30, var10002 + (ModuleWidget.isLoaded(moduleWidget) ? "" : " > " + TextUtils.translation("obscure_api.book.loading_error")), "§f", "§c ");
					}
				} else {
					return super.getTooltip(widget, screen);
				}
			}
		});
		this.MODULE = module;
		this.COLOR = color;
	}

	@Override
	public void renderButton(@NotNull MatrixStack pose, int mouseX, int mouseY, float partialTick) {
		ClientUtils.start();
		RenderSystem._setShaderTexture(0, BookScreen.BOOK_RESOURCES);
		drawTexture(pose, this.x, this.y, (float)((this.COLOR ? 48 : 0) + (this.hovered ? 24 : 0)), 192.0F, 24, 38, 512, 256);
		RenderSystem._setShaderTexture(0, this.MODULE.getIcon());
		drawTexture(pose, this.x + 2, this.y + (this.hovered ? 7 : 2), 0.0F, 0.0F, 20, 20, 20, 20);
		ClientUtils.end();
	}

	private static boolean isLoaded(ModuleWidget widget) {
		return !(widget.MODULE instanceof HomeModule) && !(widget.MODULE instanceof InboxModule) || ObscuriaCollection.isLoaded();
	}
}
