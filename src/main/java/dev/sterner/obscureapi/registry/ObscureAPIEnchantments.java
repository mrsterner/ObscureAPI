package dev.sterner.obscureapi.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.sterner.obscureapi.ObscureAPI.MODID;

public interface ObscureAPIEnchantments {
	Map<Identifier, Enchantment> ENCHANTMENTS = new LinkedHashMap<>();



	static <T extends Enchantment> T register(String id, T enchantment) {
		ENCHANTMENTS.put(new Identifier(MODID, id), enchantment);
		return enchantment;
	}

	static void init() {
		ENCHANTMENTS.forEach((id, enchantment) -> Registry.register(Registries.ENCHANTMENT, id, enchantment));
	}
}
