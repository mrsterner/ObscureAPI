package dev.sterner.obscureapi.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.obscureapi.client.screen.modules.BookModule;
import dev.sterner.obscureapi.client.screen.modules.HomeModule;
import dev.sterner.obscureapi.client.screen.modules.InboxModule;
import dev.sterner.obscureapi.client.screen.widgets.ModuleWidget;
import dev.sterner.obscureapi.client.screen.widgets.ObscureWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.HashMap;
import java.util.Iterator;

@ClientOnly
public class BookScreen extends Screen {
	public static final Identifier BOOK_RESOURCES = new Identifier("obscure_api", "textures/gui/obscure_book/book.png");
	private static final HashMap<String, BookModule> MODULES = new HashMap();
	private static final HomeModule HOME = new HomeModule();
	private static final InboxModule INBOX = new InboxModule();
	private float xScale;
	private float xScaleLerp;
	private float yScale;
	private float yScaleLerp;
	private BookModule MODULE;

	public static void registerModule(BookModule module) {
		MODULES.put(module.getModuleName(), module);
	}

	public BookScreen() {
		this("attributes");
	}

	public BookScreen(String module) {
		super(Text.empty());
		this.xScale = 0.0F;
		this.xScaleLerp = 0.0F;
		this.yScale = 0.0F;
		this.yScaleLerp = 0.0F;
		this.MODULE = (BookModule)MODULES.getOrDefault(module, (BookModule)MODULES.values().stream().toList().get(0));
	}

	@Override
	public void tick() {
		this.xScaleLerp = this.xScale;
		this.yScaleLerp = this.yScale;
		this.xScale = Math.min(1.0F, this.xScale + Math.max(0.1F, (1.0F - this.xScale) * 0.25F));
		this.yScale = Math.min(1.0F, this.yScale + Math.max(0.1F, (1.0F - this.yScale) * 0.3F));
		this.MODULE.tick(this);
	}

	@Override
	protected void init() {
		int i = 0;
		Iterator var2 = MODULES.values().iterator();

		while(var2.hasNext()) {
			BookModule module = (BookModule)var2.next();
			this.addDrawableChild(new ModuleWidget(this, module, false, -116 + 25 * i++, 101));
		}

		this.addDrawableChild(new ModuleWidget(this, INBOX, true, 91, 101));
		this.addDrawableChild(new ModuleWidget(this, HOME, true, 116, 101));
		this.MODULE.init(this);
		this.MODULE.getWidgets().forEach((x$0) -> {
			ObscureWidget var10000 = (ObscureWidget)this.addDrawableChild(x$0);
		});
		this.animate();
	}

	@Override
	public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTick) {
		int centerX = MinecraftClient.getInstance().currentScreen != null ? MinecraftClient.getInstance().currentScreen.width / 2 : 0;
		int centerY = MinecraftClient.getInstance().currentScreen != null ? MinecraftClient.getInstance().currentScreen.height / 2 : 0;
		this.renderBackground(matrixStack);
		matrixStack.push();
		matrixStack.translate((double)centerX, (double)centerY, 0.0);
		matrixStack.scale(MathHelper.lerp(this.getClient().getTickDelta(), this.xScaleLerp, this.xScale), MathHelper.lerp(this.getClient().getTickDelta(), this.yScaleLerp, this.yScale), 1.0F);
		ClientUtils.start();
		RenderSystem._setShaderTexture(0, BOOK_RESOURCES);
		drawTexture(matrixStack, -145, -96, 0.0F, 0.0F, 290, 192, 512, 256);
		ClientUtils.end();
		matrixStack.scale(1.0F, 1.0F, 1.0F);
		this.MODULE.render(this, matrixStack, mouseX, mouseY);
		super.render(matrixStack, mouseX, mouseY, partialTick);
		this.drawables.forEach((widget) -> {
			if (widget instanceof ObscureWidget obscureWidget) {
				obscureWidget.drawTooltip(this, matrixStack, mouseX, mouseY);
			}

		});
		matrixStack.pop();
	}

	public void reload() {
		this.clear(this.MODULE);
		this.MODULE.init(this);
		this.MODULE.getWidgets().forEach((x$0) -> {
			ObscureWidget var10000 = (ObscureWidget)this.addDrawableChild(x$0);
		});
	}

	public void openModule(BookModule module) {
		if (this.MODULE != module) {
			this.clear(this.MODULE);
			this.MODULE = module;
			this.MODULE.init(this);
			this.MODULE.getWidgets().forEach((x$0) -> {
				ObscureWidget var10000 = (ObscureWidget)this.addDrawableChild(x$0);
			});
			this.animate();
		}
	}

	private void clear(BookModule module) {
		module.getWidgets().forEach((x$0) -> {
			this.remove(x$0);
		});
		module.getWidgets().clear();
	}

	private void animate() {
		this.xScale = 0.7F;
		this.yScale = 0.7F;
		this.getClient().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

	@Override
	public boolean keyPressed(int key, int mouseX, int mouseY) {
		if (key == 69) {
			this.closeScreen();
			return true;
		} else {
			return super.keyPressed(key, mouseX, mouseY);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
