package dev.sterner.obscureapi.world.items;

import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

public class ObscureRarity {
	public static final Rarity MYTHIC;
	public static final Rarity LEGENDARY;

	public ObscureRarity() {
	}

	static {
		MYTHIC = Rarity.create("mythic", Formatting.RED);
		LEGENDARY = Rarity.create("legendary", Formatting.GOLD);
	}
}
