package dev.sterner.obscureapi.world.statuseffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import org.jetbrains.annotations.NotNull;

public class Knowledge extends StatusEffect {
	public Knowledge() {
		super(StatusEffectType.BENEFICIAL, -13408513);
	}

	@Override
	public @NotNull String getTranslationKey() {
		return "effect.obscure_api.knowledge";
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
