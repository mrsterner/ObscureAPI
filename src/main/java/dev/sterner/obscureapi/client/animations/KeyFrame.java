package dev.sterner.obscureapi.client.animations;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.List;

@ClientOnly
public class KeyFrame {
	public final int TICK_START;
	public final int TICK_END;
	public final float SPEED_IN;
	public final float SPEED_OUT;
	public final List<AnimatedPart> PARTS;

	public KeyFrame(int startTick, int endTick, float speedIn, float speedOut, AnimatedPart... list) {
		this.TICK_START = startTick;
		this.TICK_END = endTick;
		this.SPEED_IN = speedIn;
		this.SPEED_OUT = speedOut;
		this.PARTS = List.of(list);
	}
}
