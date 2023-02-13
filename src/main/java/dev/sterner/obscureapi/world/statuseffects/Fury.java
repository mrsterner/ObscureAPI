package dev.sterner.obscureapi.world.statuseffects;

public class Fury extends StatusEffect {
	public Fury() {
		super(StatusEffectCategory.BENEFICIAL, -52378);
	}

	public @NotNull String m_19481_() {
		return "effect.obscure_api.fury";
	}

	public boolean m_6584_(int duration, int amplifier) {
		return true;
	}
}
