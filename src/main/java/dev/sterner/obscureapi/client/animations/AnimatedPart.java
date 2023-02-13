package dev.sterner.obscureapi.client.animations;

import net.minecraft.client.model.ModelPart;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class AnimatedPart {
	public final ModelPart PART;
	public final boolean SWING;
	public final float X;
	public final float Y;
	public final float Z;
	public final float XBase;
	public final float YBase;
	public final float ZBase;
	public final float SPEED;
	public final float OFFSET;

	private AnimatedPart(ModelPart part, boolean swing, float x, float y, float z, float xb, float yb, float zb, float speed, float offset) {
		this.PART = part;
		this.SWING = swing;
		this.X = x;
		this.Y = y;
		this.Z = z;
		this.XBase = xb;
		this.YBase = yb;
		this.ZBase = zb;
		this.SPEED = speed;
		this.OFFSET = offset;
	}

	public AnimatedPart(ModelPart part, float x, float y, float z) {
		this(part, false, x, y, z, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	}

	public AnimatedPart(ModelPart part, float x, float xb, float y, float yb, float z, float zb, float speed, float offset) {
		this(part, true, x, y, z, xb, yb, zb, speed, offset);
	}
}
