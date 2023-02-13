package dev.sterner.obscureapi.registry;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.sterner.obscureapi.ObscureAPI.MODID;

public interface ObscureAPIItems {
	Map<Item, Identifier> ITEMS = new LinkedHashMap<>();


	static <T extends Item> T register(String name, T item) {
		ITEMS.put(item, new Identifier(MODID, name));
		return item;
	}

	static void init(){
		ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
	}
}
