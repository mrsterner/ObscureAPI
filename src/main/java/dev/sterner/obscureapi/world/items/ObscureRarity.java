package dev.sterner.obscureapi.world.items;

import dev.sterner.obscureapi.mixin.RarityMixin;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

public class ObscureRarity {
	public static final Rarity MYTHIC =  (Rarity) (Object) new RarityMixin("mythic", Formatting.RED);
	public static final Rarity LEGENDARY =  (Rarity) (Object) new RarityMixin("legendary", Formatting.GOLD);

	public ObscureRarity() {
	}

}
