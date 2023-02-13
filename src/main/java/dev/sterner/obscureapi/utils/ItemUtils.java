package dev.sterner.obscureapi.utils;

import dev.sterner.obscureapi.client.TooltipBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class ItemUtils {
	public ItemUtils() {
	}

	public static void addLore(String itemID) {
		Identifier registry = new Identifier(itemID);
		TooltipBuilder.Lore.add(registry.getNamespace(), itemID, registry.getPath());
	}

	public static void addLore(String itemID, String translationKey) {
		TooltipBuilder.Lore.add((new Identifier(itemID)).getNamespace(), itemID, translationKey);
	}

	public static void addLore(String modID, String itemID, String translationKey) {
		TooltipBuilder.Lore.add(modID, itemID, translationKey);
	}

	public static void addKnowledge(String itemID) {
		Identifier registry = new Identifier(itemID);
		TooltipBuilder.Knowledge.add(registry.getNamespace(), itemID, registry.getPath());
	}

	public static void addKnowledge(String itemID, String translationKey) {
		TooltipBuilder.Knowledge.add((new Identifier(itemID)).getNamespace(), itemID, translationKey);
	}

	public static void addKnowledge(String modID, String itemID, String translationKey) {
		TooltipBuilder.Knowledge.add(modID, itemID, translationKey);
	}

	@SafeVarargs
	public static int getArmorPieces(LivingEntity entity, Class<? extends Item>... armors) {
		boolean HEAD = false;
		boolean CHEST = false;
		boolean LEGS = false;
		boolean FEET = false;
		Class[] var6 = armors;
		int var7 = armors.length;

		int var8;
		Class item;
		for(var8 = 0; var8 < var7; ++var8) {
			item = var6[var8];
			if (hasItemInSlot(entity, EquipmentSlot.HEAD, item)) {
				HEAD = true;
			}
		}

		var6 = armors;
		var7 = armors.length;

		for(var8 = 0; var8 < var7; ++var8) {
			item = var6[var8];
			if (hasItemInSlot(entity, EquipmentSlot.CHEST, item)) {
				CHEST = true;
			}
		}

		var6 = armors;
		var7 = armors.length;

		for(var8 = 0; var8 < var7; ++var8) {
			item = var6[var8];
			if (hasItemInSlot(entity, EquipmentSlot.LEGS, item)) {
				LEGS = true;
			}
		}

		var6 = armors;
		var7 = armors.length;

		for(var8 = 0; var8 < var7; ++var8) {
			item = var6[var8];
			if (hasItemInSlot(entity, EquipmentSlot.FEET, item)) {
				FEET = true;
			}
		}

		return (HEAD ? 1 : 0) + (CHEST ? 1 : 0) + (LEGS ? 1 : 0) + (FEET ? 1 : 0);
	}

	public static boolean hasItemInSlot(LivingEntity entity, EquipmentSlot slot, Class<? extends Item> item) {
		return item.isAssignableFrom(entity.getEquippedStack(slot).getItem().getClass());
	}

	public static @Nullable NbtCompound getData(ItemStack stack) {
		return stack.getNbt() == null ? null : stack.getNbt().getCompound("ObscureData");
	}

	public static NbtCompound getOrCreateData(ItemStack stack) {
		if (!stack.getOrCreateNbt().contains("ObscureData")) {
			stack.getOrCreateNbt().put("ObscureData", new NbtCompound());
		}

		return stack.getOrCreateNbt().getCompound("ObscureData");
	}

	public static @Nullable NbtCompound getPerks(ItemStack stack) {
		NbtCompound data = getData(stack);
		return data == null ? null : data.getCompound("Perks");
	}

	public static NbtCompound getOrCreatePerks(ItemStack stack) {
		if (!getOrCreateData(stack).contains("Perks")) {
			getOrCreateData(stack).put("Perks", new NbtCompound());
		}

		return getOrCreateData(stack).getCompound("Perks");
	}

	public static boolean hasPerks(ItemStack stack) {
		NbtCompound perks = getPerks(stack);
		return perks != null && !perks.isEmpty();
	}

	public static boolean hasPerk(ItemStack stack, Identifier perk) {
		NbtCompound perks = getPerks(stack);
		return perks != null && perks.contains(perk.toString());
	}

	public static boolean hasPerk(ItemStack stack, String perk) {
		NbtCompound perks = getPerks(stack);
		return perks != null && perks.contains(perk);
	}

	public static void addPerk(ItemStack stack, Identifier perk, int level) {
		getOrCreatePerks(stack).putInt(perk.toString(), level);
	}

	public static void removePerk(ItemStack stack, Identifier perk) {
		NbtCompound perks = getPerks(stack);
		if (perks != null && perks.contains(perk.toString())) {
			perks.remove(perk.toString());
		}

	}

	public static void removePerk(ItemStack stack, String perk) {
		NbtCompound perks = getPerks(stack);
		if (perks != null && perks.contains(perk)) {
			perks.remove(perk);
		}

	}
}
