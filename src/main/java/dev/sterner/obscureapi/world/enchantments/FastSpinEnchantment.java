package dev.sterner.obscureapi.world.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FastSpinEnchantment extends Enchantment {
	public FastSpinEnchantment(EquipmentSlot... slots) {
		super(Rarity.COMMON, ObscureAPI.DYNAMIC_PROJECTILE_FAST_SPIN, slots);
	}

	public int getMaxLevel() {
		return 5;
	}

	public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack) {
		return super.canApplyAtEnchantingTable(stack);
	}

	public boolean isTreasure() {
		return false;
	}
}
