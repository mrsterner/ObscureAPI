package dev.sterner.obscureapi.registry;

import dev.sterner.obscureapi.api.VFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.sterner.obscureapi.ObscureAPI.MODID;

public interface ObscureAPIEntityTypes {
	Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

	EntityType<VFX> VFX = register("etheric_nitrate", EntityType.Builder.create(VFX::new, SpawnGroup.MISC)
			.setDimensions(1.0F, 1.0F)
			.maxTrackingRange(64).makeFireImmune()
			.build(null));

	static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		ENTITY_TYPES.put(new Identifier(MODID, id), type);
		return type;
	}

	static void init() {
		ENTITY_TYPES.forEach((id, entityType) -> Registry.register(Registries.ENTITY_TYPE, id, entityType));
	}
}
