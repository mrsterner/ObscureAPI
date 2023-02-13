package dev.sterner.obscureapi.registry;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.sterner.obscureapi.ObscureAPI.MODID;

public interface ObscureAPIAttributes {
	Map<Identifier, EntityAttribute> ATTRIBUTES = new LinkedHashMap<>();


	static <T extends EntityAttribute> EntityAttribute register(String id, EntityAttribute attribute) {
		ATTRIBUTES.put(new Identifier(MODID, id), attribute);
		return attribute;
	}
	static void init() {
		ATTRIBUTES.forEach((id, attribute) -> Registry.register(Registries.ENTITY_ATTRIBUTE, id, attribute));
	}
}
