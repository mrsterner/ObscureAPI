package dev.sterner.obscureapi.world.statuseffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import org.jetbrains.annotations.NotNull;

public class Rush extends StatusEffect {
	public Rush() {
		super(StatusEffectType.BENEFICIAL, -6736897);
	}

	public @NotNull String getTranslationKey() {
		return "effect.obscure_api.rush";
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
