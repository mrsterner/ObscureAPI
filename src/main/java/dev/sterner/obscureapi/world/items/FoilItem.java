package dev.sterner.obscureapi.world.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class FoilItem extends Item {
	public FoilItem(Item.Settings properties) {
		super(properties);
	}

	@ClientOnly
	public boolean hasGlint(@NotNull ItemStack stack) {
		return true;
	}
}
