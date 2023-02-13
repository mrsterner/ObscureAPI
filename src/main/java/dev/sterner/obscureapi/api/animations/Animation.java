package dev.sterner.obscureapi.api.animations;

public class Animation {
	public String name;
	public int ticks;

	Animation(String name, int ticks) {
		this.name = name;
		this.ticks = ticks;
	}

	public void tick(AnimationProvider provider) {
		--this.ticks;
		if (this.ticks <= 0) {
			provider.getAnimations().remove(this);
		}

	}
}
