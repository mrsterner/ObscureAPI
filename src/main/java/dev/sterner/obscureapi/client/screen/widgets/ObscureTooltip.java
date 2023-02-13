package dev.sterner.obscureapi.client.screen.widgets;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.List;

@ClientOnly
public abstract class ObscureTooltip {
	public ObscureTooltip() {
	}

	public List<Text> getTooltip(ObscureWidget widget, Screen screen) {
		return new ArrayList<>();
	}
}
