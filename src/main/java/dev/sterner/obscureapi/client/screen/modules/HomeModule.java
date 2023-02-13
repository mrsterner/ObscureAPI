package dev.sterner.obscureapi.client.screen.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.obscureapi.client.screen.BookScreen;
import dev.sterner.obscureapi.client.screen.widgets.ObscureTooltip;
import dev.sterner.obscureapi.client.screen.widgets.ObscureWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ClientOnly
public class HomeModule implements BookModule {
	private static final Identifier RESOURCES = new Identifier("obscure_api", "textures/gui/obscure_book/module_home.png");
	public final List<ObscureWidget> WIDGETS = new ArrayList();

	public HomeModule() {
	}

	public void tick(BookScreen screen) {
		this.WIDGETS.forEach(ObscureWidget::tick);
	}

	public void init(BookScreen screen) {
		MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master((SoundEvent)ObscureAPISounds.OBSCURE_BOOK_MIRACLE.get(), 1.0F, 1.0F));
		this.WIDGETS.clear();
		this.WIDGETS.add(this.social(screen, -103, 26, 30, "discord", "https://discord.gg/jSHHJSUWdY"));
		this.WIDGETS.add(this.social(screen, -86, 43, 0, "curseforge", "https://www.curseforge.com/members/obscuriaofficial/projects"));
		this.WIDGETS.add(this.social(screen, -69, 26, 0, "patreon", "https://www.patreon.com/Obscuria"));
		this.WIDGETS.add(this.social(screen, -52, 43, 90, "github", "https://github.com/ObscuriaLithium"));
		this.WIDGETS.add(this.social(screen, -35, 26, 60, "planet", "https://www.planetminecraft.com/member/obscuria"));
		int offsetX = 0;
		int offsetY = 0;
		boolean offset = false;
		Iterator var5 = ObscuriaCollection.getMods().iterator();

		while(var5.hasNext()) {
			ObscuriaCollection.Mod mod = (ObscuriaCollection.Mod)var5.next();
			this.WIDGETS.add(this.mod(mod, screen, (offset ? 39 : 27) + 24 * offsetX, -57 + 12 * offsetY));
			++offsetX;
			if (offsetX >= 4) {
				offsetX = 0;
				++offsetY;
				offset = !offset;
			}
		}

	}

	private ObscureWidget mod(final ObscuriaCollection.Mod mod, BookScreen screen, int x, int y) {
		return new ObscureWidget(false, screen, x, y, 22, 22, (button) -> {
			Util.m_137581_().m_137646_(TextUtils.getString(mod.getInfo(), "link"));
		}, new ObscureTooltip() {
			public List<Text> getTooltip(ObscureWidget widget, Screen screen) {
				return TextUtils.buildTooltip(new ArrayList(), 30, TextUtils.translation("obscure_api.book.mod_template").replace("{NAME}", ModList.get().getModContainerById(mod.getID()).isPresent() ? ((ModContainer)ModList.get().getModContainerById(mod.getID()).get()).getModInfo().getDisplayName() : "Mod ID not present").replace("{DESCRIPTION}", TextUtils.format(mod.getInfo(), "description")).replace("{ID}", mod.getID()).replace("{VERSION}", ModList.get().getModFileById(mod.getID()).versionString()), "§f", "§7 ");
			}
		}) {
			public void renderButton(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
				ClientUtils.start();
				RenderSystem._setShaderTexture(0, new Identifier(mod.getID(), "textures/gui/obscure_book/icon.png"));
				DrawableHelper.drawTexture(matrixStack, this.x, this.y, 0.0F, this.hovered ? 22.0F : 0.0F, 22, 22, 22, 44);
				ClientUtils.end();
			}
		};
	}

	private ObscureWidget social(BookScreen screen, int x, int y, final int offset, final String name, String link) {
		return new ObscureWidget(false, screen, x, y, 30, 30, (button) -> {
			Util.m_137581_().m_137646_(link);
		}, new ObscureTooltip() {
			public List<Text> getTooltip(ObscureWidget widget, Screen screen) {
				return TextUtils.buildTooltip(new ArrayList(), 30, TextUtils.translation("obscure_api.book.module_home." + name), "§6", "§7 ");
			}
		}) {
			int animation = 1;

			public void tick() {
				++this.animation;
				if (this.animation == 31) {
					this.animation = 1;
				}

			}

			public void renderButton(@NotNull MatrixStack pose, int mouseX, int mouseY, float partialTicks) {
				ClientUtils.start();
				if (name.equals("patreon")) {
					int var10003 = this.animation;
					RenderSystem._setShaderTexture(0, new Identifier("obscure_api:textures/gui/patreon/" + var10003 + ".png"));
					GuiText.drawTexture(pose, this.x, this.y, (float)offset, this.hovered ? 30.0F : 0.0F, 30, 30, 30, 60);
				} else {
					RenderSystem._setShaderTexture(0, HomeModule.RESOURCES);
					GuiText.drawTexture(pose, this.x, this.y, (float)offset, this.hovered ? 222.0F : 192.0F, 30, 30, 512, 256);
				}

				ClientUtils.end();
			}
		};
	}

	public void render(BookScreen screen, MatrixStack matrixStack, int mouseX, int mouseY) {
		ClientUtils.start();
		RenderSystem._setShaderTexture(0, RESOURCES);
		DrawableHelper.drawTexture(matrixStack, -145, -96, 0.0F, 0.0F, 290, 192, 512, 256);
		ClientUtils.end();
		screen.getClient().textRenderer.m_92883_(matrixStack, TextUtils.format(ObscuriaCollection.getMainFile(), "title"), -123.0F, -82.0F, 0);
		matrixStack.pop();
		matrixStack.translate(-120.0, -70.0, 0.0);
		matrixStack.scale(ClientUtils.scale(), ClientUtils.scale(), 1.0F);
		ClientUtils.drawText(TextUtils.buildTooltip(new ArrayList(), 30, TextUtils.format(ObscuriaCollection.getMainFile(), "content"), "§0"), screen.getClient().textRenderer, matrixStack);
		matrixStack.pop();
		screen.getClient().textRenderer.m_92883_(matrixStack, TextUtils.translation("obscure_api.book.module_home.mods").replace("{1}", "" + ObscuriaCollection.getMods().size()).replace("{2}", "" + ObscuriaCollection.getTotalMods()), 15.0F, -82.0F, 0);
		matrixStack.push();
		matrixStack.translate(18.0, 45.0, 0.0);
		matrixStack.scale(ClientUtils.scale(), ClientUtils.scale(), 1.0F);
		ClientUtils.drawText(TextUtils.buildTooltip(new ArrayList(), 30, TextUtils.translation("obscure_api.book.module_home.mod_list"), "§0"), screen.getClient().textRenderer, matrixStack);
		matrixStack.pop();
	}

	public Identifier getIcon() {
		return new Identifier("obscure_api", "textures/gui/obscure_book/icon_home.png");
	}

	public String getModuleName() {
		return "home";
	}

	public List<ObscureWidget> getWidgets() {
		return this.WIDGETS;
	}
}
