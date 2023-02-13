package dev.sterner.obscureapi.registry;

import dev.sterner.obscureapi.world.statuseffects.Fury;
import dev.sterner.obscureapi.world.statuseffects.Knowledge;
import dev.sterner.obscureapi.world.statuseffects.Rush;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.sterner.obscureapi.ObscureAPI.MODID;

public interface ObscureAPIStatusEffects {
	Map<Identifier, StatusEffect> STATUS_EFFECTS = new LinkedHashMap<>();

	StatusEffect KNOWLEDGE = register("knowledge", new Knowledge());
	StatusEffect FURY = register("fury", new Fury());
	StatusEffect RUSH = register("rush", new Rush());

	static <T extends StatusEffect> StatusEffect register(String id, T effect) {
		STATUS_EFFECTS.put(new Identifier(MODID, id), effect);
		return effect;
	}

	static void init() {
		STATUS_EFFECTS.forEach((id, effect) -> Registry.register(Registries.STATUS_EFFECT, id, effect));
	}
}
