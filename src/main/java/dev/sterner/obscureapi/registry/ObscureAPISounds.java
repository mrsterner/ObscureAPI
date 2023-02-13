package dev.sterner.obscureapi.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.sterner.obscureapi.ObscureAPI.MODID;

public interface ObscureAPISounds {
	Map<Identifier, SoundEvent> SOUND_EVENTS = new LinkedHashMap<>();


	static SoundEvent register(String name) {
		Identifier id = new Identifier(MODID, name);
		SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(id);
		SOUND_EVENTS.put(id, soundEvent);
		return soundEvent;
	}


	static void init() {
		SOUND_EVENTS.forEach((id, sound) -> Registry.register(Registries.SOUND_EVENT, id, sound));
	}
}
