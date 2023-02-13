package dev.sterner.obscureapi.world.statuseffects;

public class Knowledge extends StatusEffect {
	public Knowledge() {
		super(StatusEffectCategory.BENEFICIAL, -13408513);
	}

	public @NotNull String m_19481_() {
		return "effect.obscure_api.knowledge";
	}

	public boolean m_6584_(int duration, int amplifier) {
		return true;
	}
}
