package dev.sterner.obscureapi.world.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DistanceEnchantment extends Enchantment {
	public DistanceEnchantment(EquipmentSlot... slots) {
		super(Rarity.COMMON, ObscureAPI.DYNAMIC_PROJECTILE_DISTANCE, slots);
	}

	public int getMaxLevel() {
		return 3;
	}

	public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack) {
		return super.canApplyAtEnchantingTable(stack);
	}

	public boolean isTreasure() {
		return false;
	}
}
