package dev.sterner.obscureapi.client.screen.modules;

import dev.sterner.obscureapi.client.screen.BookScreen;
import dev.sterner.obscureapi.client.screen.widgets.ObscureWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.List;

@ClientOnly
public interface BookModule {
	void tick(BookScreen var1);

	void init(BookScreen var1);

	void render(BookScreen var1, MatrixStack matrixStack, int var3, int var4);

	Identifier getIcon();

	String getModuleName();

	List<ObscureWidget> getWidgets();
}
