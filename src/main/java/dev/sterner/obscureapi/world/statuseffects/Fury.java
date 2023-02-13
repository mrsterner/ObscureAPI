package dev.sterner.obscureapi.world.statuseffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import org.jetbrains.annotations.NotNull;

public class Fury extends StatusEffect {
	public Fury() {
		super(StatusEffectType.BENEFICIAL, -52378);
	}

	@Override
	public @NotNull String getTranslationKey() {
		return "effect.obscure_api.fury";
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
