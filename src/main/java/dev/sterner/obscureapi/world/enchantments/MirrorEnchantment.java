package dev.sterner.obscureapi.world.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MirrorEnchantment extends Enchantment {
	public MirrorEnchantment(EquipmentSlot... slots) {
		super(Rarity.RARE, ObscureAPI.DYNAMIC_PROJECTILE_MIRROR, slots);
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack) {
		return super.canApplyAtEnchantingTable(stack);
	}

	@Override
	public boolean isTreasure() {
		return false;
	}
}
