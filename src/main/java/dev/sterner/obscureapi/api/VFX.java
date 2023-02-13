package dev.sterner.obscureapi.api;

import dev.sterner.obscureapi.registry.ObscureAPIEntityTypes;
import dev.sterner.obscureapi.utils.EventUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class VFX extends Entity {

	private static final String BLANK_TEXTURE_LOC = "obscure_api";
	private static final String BLANK_TEXTURE_NAME = "blank";
	private static final UUID BLANK_OWNER = UUID.fromString("0-0-0-0-0");
	private static final TrackedData<Integer> LIFETIME;
	private static final TrackedData<Float> FORWARD;
	private static final TrackedData<Float> SIDE;
	private static final TrackedData<Float> UP;
	private static final TrackedData<Float> X_OFF;
	private static final TrackedData<Float> Y_OFF;
	private static final TrackedData<Float> X_OFF_MOD;
	private static final TrackedData<Float> Y_OFF_MOD;
	private static final TrackedData<Float> X_OFF_MOD_A;
	private static final TrackedData<Float> Y_OFF_MOD_A;
	private static final TrackedData<Float> X_ROT_MAIN;
	private static final TrackedData<Float> Y_ROT_MAIN;
	private static final TrackedData<Float> X_ROT;
	private static final TrackedData<Float> Y_ROT;
	private static final TrackedData<Float> Z_ROT;
	private static final TrackedData<Float> X_MOD;
	private static final TrackedData<Float> Y_MOD;
	private static final TrackedData<Float> Z_MOD;
	private static final TrackedData<Float> X_MOD_A;
	private static final TrackedData<Float> Y_MOD_A;
	private static final TrackedData<Float> Z_MOD_A;
	private static final TrackedData<Float> SCALE;
	private static final TrackedData<Float> SCALE_MOD;
	private static final TrackedData<Float> SCALE_MOD_A;
	private static final TrackedData<Float> ALPHA;
	private static final TrackedData<Float> ALPHA_MOD;
	private static final TrackedData<Float> ALPHA_MOD_A;
	private static final TrackedData<Optional<UUID>> OWNER_UUID;
	private static final TrackedData<Integer> OWNER_ID;
	private static final TrackedData<Boolean> BOUND_POS;
	private static final TrackedData<Boolean> BOUND_ROT;
	private static final TrackedData<Boolean> ANIMATED;
	private static final TrackedData<Integer> FRAMES;
	private static final TrackedData<Integer> FRAME_TIME;
	private static final TrackedData<String> TEXTURE_LOC;
	private static final TrackedData<String> TEXTURE_NAME;
	private static final TrackedData<Integer> RENDER_TYPE;
	private static final TrackedData<Integer> AGE;
	private Consumer<VFX> action;
	private boolean created;
	private float xOff;
	private float xOffLerp;
	private float yOff;
	private float yOffLerp;
	private float xRot;
	private float xRotLerp;
	private float yRot;
	private float yRotLerp;
	private float zRot;
	private float zRotLerp;
	private float scale;
	private float scaleLerp;
	private float alpha;
	private float alphaLerp;
	private int frame;
	private int frameTime;

	public VFX(EntityType<?> variant, World world) {
		super(variant, world);
		this.action = (vfx) -> {
		};
		this.created = false;
		this.frame = 1;
		this.frameTime = 0;
	}

	private VFX(Builder builder, World world) {
		super(ObscureAPIEntityTypes.VFX, world);
		this.action = (vfx) -> {
		};
		this.created = false;
		this.frame = 1;
		this.frameTime = 0;
		if (builder.owner != null) {
			this.getDataTracker().set(OWNER_UUID, Optional.of(builder.owner.getUuid()));
		}

		this.getDataTracker().set(FORWARD, builder.forward);
		this.getDataTracker().set(SIDE, builder.side);
		this.getDataTracker().set(UP, builder.up);
		this.getDataTracker().set(X_OFF, builder.xOff);
		this.getDataTracker().set(X_OFF_MOD, builder.xOffMod);
		this.getDataTracker().set(X_OFF_MOD_A, builder.xOffModA);
		this.getDataTracker().set(Y_OFF, builder.yOff);
		this.getDataTracker().set(Y_OFF_MOD, builder.yOffMod);
		this.getDataTracker().set(Y_OFF_MOD_A, builder.yOffModA);
		this.getDataTracker().set(X_ROT_MAIN, builder.xRotMain);
		this.getDataTracker().set(X_ROT, builder.xRot);
		this.getDataTracker().set(X_MOD, builder.xMod);
		this.getDataTracker().set(X_MOD_A, builder.xModA);
		this.getDataTracker().set(Y_ROT_MAIN, builder.yRotMain);
		this.getDataTracker().set(Y_ROT, builder.yRot);
		this.getDataTracker().set(Y_MOD, builder.yMod);
		this.getDataTracker().set(Y_MOD_A, builder.yModA);
		this.getDataTracker().set(Z_ROT, builder.zRot);
		this.getDataTracker().set(Z_MOD, builder.zMod);
		this.getDataTracker().set(Z_MOD_A, builder.zModA);
		this.getDataTracker().set(LIFETIME, builder.lifetime);
		this.getDataTracker().set(SCALE, builder.scale);
		this.getDataTracker().set(SCALE_MOD, builder.scaleMod);
		this.getDataTracker().set(SCALE_MOD_A, builder.scaleModA);
		this.getDataTracker().set(ALPHA, builder.alpha);
		this.getDataTracker().set(ALPHA_MOD, builder.alphaMod);
		this.getDataTracker().set(ALPHA_MOD_A, builder.alphaModA);
		this.getDataTracker().set(BOUND_POS, builder.boundPos);
		this.getDataTracker().set(BOUND_ROT, builder.boundRot);
		this.getDataTracker().set(ANIMATED, builder.animated);
		this.getDataTracker().set(FRAMES, builder.frames);
		this.getDataTracker().set(FRAME_TIME, builder.frameTime);
		this.getDataTracker().set(TEXTURE_LOC, builder.textureLoc);
		this.getDataTracker().set(TEXTURE_NAME, builder.textureName);
		this.getDataTracker().set(RENDER_TYPE, builder.renderType);
		this.action = builder.action;
		this.refreshPositionAfterTeleport(new Vec3d((double)builder.x, (double)builder.y, (double)builder.z));
		world.spawnEntity(this);
		if (builder.sound != null) {
			this.world.playSoundFromEntity((PlayerEntity)null, this, builder.sound, SoundCategory.MASTER, builder.volume, builder.pitch);
		}
	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(LIFETIME, 20);
		this.getDataTracker().startTracking(FORWARD, 0.0F);
		this.getDataTracker().startTracking(SIDE, 0.0F);
		this.getDataTracker().startTracking(UP, 0.0F);
		this.getDataTracker().startTracking(X_OFF, 0.0F);
		this.getDataTracker().startTracking(X_OFF_MOD, 0.0F);
		this.getDataTracker().startTracking(X_OFF_MOD_A, 0.0F);
		this.getDataTracker().startTracking(Y_OFF, 0.0F);
		this.getDataTracker().startTracking(Y_OFF_MOD, 0.0F);
		this.getDataTracker().startTracking(Y_OFF_MOD_A, 0.0F);
		this.getDataTracker().startTracking(X_ROT_MAIN, 0.0F);
		this.getDataTracker().startTracking(X_ROT, 0.0F);
		this.getDataTracker().startTracking(X_MOD, 0.0F);
		this.getDataTracker().startTracking(X_MOD_A, 0.0F);
		this.getDataTracker().startTracking(Y_ROT_MAIN, 0.0F);
		this.getDataTracker().startTracking(Y_ROT, 0.0F);
		this.getDataTracker().startTracking(Y_MOD, 0.0F);
		this.getDataTracker().startTracking(Y_MOD_A, 0.0F);
		this.getDataTracker().startTracking(Z_ROT, 0.0F);
		this.getDataTracker().startTracking(Z_MOD, 0.0F);
		this.getDataTracker().startTracking(Z_MOD_A, 0.0F);
		this.getDataTracker().startTracking(SCALE, 1.0F);
		this.getDataTracker().startTracking(SCALE_MOD, 0.0F);
		this.getDataTracker().startTracking(SCALE_MOD_A, 0.0F);
		this.getDataTracker().startTracking(ALPHA, 1.0F);
		this.getDataTracker().startTracking(ALPHA_MOD, 0.0F);
		this.getDataTracker().startTracking(ALPHA_MOD_A, 0.0F);
		this.getDataTracker().startTracking(OWNER_UUID, Optional.of(BLANK_OWNER));
		this.getDataTracker().startTracking(OWNER_ID, 0);
		this.getDataTracker().startTracking(BOUND_POS, false);
		this.getDataTracker().startTracking(BOUND_ROT, false);
		this.getDataTracker().startTracking(AGE, 0);
		this.getDataTracker().startTracking(ANIMATED, false);
		this.getDataTracker().startTracking(FRAMES, 1);
		this.getDataTracker().startTracking(FRAME_TIME, 20);
		this.getDataTracker().startTracking(TEXTURE_LOC, "obscure_api");
		this.getDataTracker().startTracking(TEXTURE_NAME, "blank");
		this.getDataTracker().startTracking(RENDER_TYPE, 0);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		if (nbt.contains("VFX")) {
			NbtCompound tag = nbt.getCompound("VFX");
			this.getDataTracker().set(LIFETIME, tag.getInt("lifetime"));
			this.getDataTracker().set(FORWARD, tag.getFloat("forward"));
			this.getDataTracker().set(SIDE, tag.getFloat("side"));
			this.getDataTracker().set(UP, tag.getFloat("up"));
			this.getDataTracker().set(X_OFF, tag.getFloat("xOff"));
			this.getDataTracker().set(X_OFF_MOD, tag.getFloat("xOffMod"));
			this.getDataTracker().set(X_OFF_MOD_A, tag.getFloat("xOffModA"));
			this.getDataTracker().set(Y_OFF, tag.getFloat("yOff"));
			this.getDataTracker().set(Y_OFF_MOD, tag.getFloat("yOffMod"));
			this.getDataTracker().set(Y_OFF_MOD_A, tag.getFloat("yOffModA"));
			this.getDataTracker().set(X_ROT_MAIN, tag.getFloat("xRotMain"));
			this.getDataTracker().set(X_ROT, tag.getFloat("xRot"));
			this.getDataTracker().set(X_MOD, tag.getFloat("xMod"));
			this.getDataTracker().set(X_MOD_A, tag.getFloat("xModA"));
			this.getDataTracker().set(Y_ROT_MAIN, tag.getFloat("yRotMain"));
			this.getDataTracker().set(Y_ROT, tag.getFloat("yRot"));
			this.getDataTracker().set(Y_MOD, tag.getFloat("yMod"));
			this.getDataTracker().set(Y_MOD_A, tag.getFloat("yModA"));
			this.getDataTracker().set(Z_ROT, tag.getFloat("zRot"));
			this.getDataTracker().set(Z_MOD, tag.getFloat("zMod"));
			this.getDataTracker().set(Z_MOD_A, tag.getFloat("zModA"));
			this.getDataTracker().set(SCALE, tag.getFloat("scale"));
			this.getDataTracker().set(SCALE_MOD, tag.getFloat("scaleMod"));
			this.getDataTracker().set(SCALE_MOD_A, tag.getFloat("scaleModA"));
			this.getDataTracker().set(ALPHA, tag.getFloat("alpha"));
			this.getDataTracker().set(ALPHA_MOD, tag.getFloat("alphaMod"));
			this.getDataTracker().set(ALPHA_MOD_A, tag.getFloat("alphaModA"));
			this.getDataTracker().set(OWNER_UUID, Optional.of(tag.getUuid("ownerUUID")));
			this.getDataTracker().set(OWNER_ID, tag.getInt("ownerID"));
			this.getDataTracker().set(BOUND_POS, tag.getBoolean("boundPos"));
			this.getDataTracker().set(BOUND_ROT, tag.getBoolean("boundRot"));
			this.getDataTracker().set(AGE, tag.getInt("age"));
			this.getDataTracker().set(ANIMATED, tag.getBoolean("animated"));
			this.getDataTracker().set(FRAMES, tag.getInt("frames"));
			this.getDataTracker().set(FRAME_TIME, tag.getInt("frameTime"));
			this.getDataTracker().set(TEXTURE_LOC, tag.getString("textureLoc"));
			this.getDataTracker().set(TEXTURE_NAME, tag.getString("textureName"));
			this.getDataTracker().set(RENDER_TYPE, tag.getInt("renderType"));
		}
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {

	}

	@Override
	public void tick() {
		this.xOffLerp = this.xOff;
		this.yOffLerp = this.yOff;
		this.xRotLerp = this.xRot;
		this.yRotLerp = this.yRot;
		this.zRotLerp = this.zRot;
		this.scaleLerp = this.scale;
		this.alphaLerp = this.alpha;
		this.xOff = (Float)this.getDataTracker().get(X_OFF);
		this.yOff = (Float)this.getDataTracker().get(Y_OFF);
		this.xRot = (Float)this.getDataTracker().get(X_ROT);
		this.yRot = (Float)this.getDataTracker().get(Y_ROT);
		this.zRot = (Float)this.getDataTracker().get(Z_ROT);
		this.scale = (Float)this.getDataTracker().get(SCALE);
		this.alpha = (Float)this.getDataTracker().get(ALPHA);
		if (!this.created) {
			this.xOffLerp = this.xOff;
			this.yOffLerp = this.yOff;
			this.xRotLerp = this.xRot;
			this.yRotLerp = this.yRot;
			this.zRotLerp = this.zRot;
			this.scaleLerp = this.scale;
			this.alphaLerp = this.alpha;
			this.created = true;
			this.tick();
		} else {
			this.action.accept(this);
		}

		if (!this.world.isClient) {
			this.getDataTracker().set(AGE, (Integer)this.getDataTracker().get(AGE) + 1);
			if ((Integer)this.getDataTracker().get(AGE) >= (Integer)this.getDataTracker().get(LIFETIME)) {
				this.discard();
			}

			this.getDataTracker().set(X_OFF, (Float)this.getDataTracker().get(X_OFF) + (Float)this.getDataTracker().get(X_OFF_MOD));
			this.getDataTracker().set(Y_OFF, (Float)this.getDataTracker().get(Y_OFF) + (Float)this.getDataTracker().get(Y_OFF_MOD));
			this.getDataTracker().set(X_ROT, (Float)this.getDataTracker().get(X_ROT) + (Float)this.getDataTracker().get(X_MOD));
			this.getDataTracker().set(Y_ROT, (Float)this.getDataTracker().get(Y_ROT) + (Float)this.getDataTracker().get(Y_MOD));
			this.getDataTracker().set(Z_ROT, (Float)this.getDataTracker().get(Z_ROT) + (Float)this.getDataTracker().get(Z_MOD));
			this.getDataTracker().set(SCALE, (Float)this.getDataTracker().get(SCALE) + (Float)this.getDataTracker().get(SCALE_MOD));
			this.getDataTracker().set(ALPHA, (Float)this.getDataTracker().get(ALPHA) + (Float)this.getDataTracker().get(ALPHA_MOD));
			this.getDataTracker().set(X_OFF_MOD, (Float)this.getDataTracker().get(X_OFF_MOD) + (Float)this.getDataTracker().get(X_OFF_MOD_A));
			this.getDataTracker().set(Y_OFF_MOD, (Float)this.getDataTracker().get(Y_OFF_MOD) + (Float)this.getDataTracker().get(Y_OFF_MOD_A));
			this.getDataTracker().set(X_MOD, (Float)this.getDataTracker().get(X_MOD) + (Float)this.getDataTracker().get(X_MOD_A));
			this.getDataTracker().set(Y_MOD, (Float)this.getDataTracker().get(Y_MOD) + (Float)this.getDataTracker().get(Y_MOD_A));
			this.getDataTracker().set(Z_MOD, (Float)this.getDataTracker().get(Z_MOD) + (Float)this.getDataTracker().get(Z_MOD_A));
			this.getDataTracker().set(SCALE_MOD, (Float)this.getDataTracker().get(SCALE_MOD) + (Float)this.getDataTracker().get(SCALE_MOD_A));
			this.getDataTracker().set(ALPHA_MOD, (Float)this.getDataTracker().get(ALPHA_MOD) + (Float)this.getDataTracker().get(ALPHA_MOD_A));
			World var2 = this.world;
			if (var2 instanceof ServerWorld) {
				ServerWorld server = (ServerWorld)var2;
				UUID ownerUUID = (UUID)((Optional)this.getDataTracker().get(OWNER_UUID)).orElse(BLANK_OWNER);
				Entity owner = server.getEntity(ownerUUID);
				if (owner != null) {
					this.getDataTracker().set(OWNER_ID, owner.getId());
				}
			}

			Entity owner = this.getOwner();
			if ((Boolean)this.getDataTracker().get(BOUND_POS) && owner != null) {
				this.refreshPositionAfterTeleport(this.getRelativePos(owner));
			}
		} else if ((Boolean)this.getDataTracker().get(ANIMATED)) {
			++this.frameTime;
			if (this.frameTime >= (Integer)this.getDataTracker().get(FRAME_TIME)) {
				this.frameTime = 0;
				++this.frame;
				if (this.frame > (Integer)this.getDataTracker().get(FRAMES)) {
					this.frame = 1;
				}
			}
		}
		super.tick();
	}

	public int getAge() {
		return (Integer)this.getDataTracker().get(AGE);
	}

	public int getLifeTime() {
		return (Integer)this.getDataTracker().get(LIFETIME);
	}

	@Nullable
	public Entity getOwner() {
		return this.world.getEntityById((Integer)this.getDataTracker().get(OWNER_ID));
	}

	public Vec3d getRelativePos(Entity entity) {
		Vec3d pos = entity.getPos();
		return new Vec3d(pos.x + Math.cos(Math.toRadians((double)(entity.getHeadYaw() + 90.0F + (Float)this.getDataTracker().get(SIDE)))) * (double)(Float)this.getDataTracker().get(FORWARD), pos.y + (double)(Float)this.getDataTracker().get(UP), pos.z + Math.sin(Math.toRadians((double)(entity.getHeadYaw() + 90.0F + (Float)this.getDataTracker().get(SIDE)))) * (double)(Float)this.getDataTracker().get(FORWARD));
	}

	public Identifier getTexture() {
		String var10002;
		if ((Boolean)this.getDataTracker().get(ANIMATED)) {
			var10002 = (String)this.getDataTracker().get(TEXTURE_LOC);
			String var1 = (String)this.getDataTracker().get(TEXTURE_NAME);
			return new Identifier(var10002, "textures/vfx/" + var1 + "_" + this.frame + ".png");
		} else {
			var10002 = (String)this.getDataTracker().get(TEXTURE_LOC);
			DataTracker var10003 = this.getDataTracker();
			return new Identifier(var10002, "textures/vfx/" + (String)var10003.get(TEXTURE_NAME) + ".png");
		}
	}

	public float getXOff(float partialTicks) {
		return MathHelper.lerp(partialTicks, this.xOffLerp, this.xOff);
	}

	public float getYOff(float partialTicks) {
		return MathHelper.lerp(partialTicks, this.yOffLerp, this.yOff);
	}

	public float getXRotMain() {
		return (Float)this.getDataTracker().get(X_ROT_MAIN);
	}

	public float getYRotMain() {
		return (Float)this.getDataTracker().get(Y_ROT_MAIN);
	}

	public float getXRot(float partialTicks) {
		return MathHelper.lerp(partialTicks, this.xRotLerp, this.xRot);
	}

	public float getYRot(float partialTicks) {
		return MathHelper.lerp(partialTicks, this.yRotLerp, this.yRot);
	}

	public float getZRot(float partialTicks) {
		return MathHelper.lerp(partialTicks, this.zRotLerp, this.zRot);
	}

	public float getScale(float partialTicks) {
		return Math.max(0.0F, MathHelper.lerp(partialTicks, this.scaleLerp, this.scale));
	}

	public float getAlpha(float partialTicks) {
		return Math.min(1.0F, Math.max(0.0F, MathHelper.lerp(partialTicks, this.alphaLerp, this.alpha)));
	}

	public int getRenderType() {
		return (Integer)this.getDataTracker().get(RENDER_TYPE);
	}

	@NotNull
	public Box getVisibilityBoundingBox() {
		return super.getVisibilityBoundingBox().expand(10.0);
	}

	static {
		LIFETIME = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.INTEGER);
		FORWARD = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		SIDE = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		UP = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		X_OFF = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Y_OFF = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		X_OFF_MOD = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Y_OFF_MOD = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		X_OFF_MOD_A = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Y_OFF_MOD_A = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		X_ROT_MAIN = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Y_ROT_MAIN = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		X_ROT = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Y_ROT = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Z_ROT = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		X_MOD = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Y_MOD = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Z_MOD = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		X_MOD_A = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Y_MOD_A = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		Z_MOD_A = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		SCALE = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		SCALE_MOD = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		SCALE_MOD_A = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		ALPHA = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		ALPHA_MOD = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		ALPHA_MOD_A = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.FLOAT);
		OWNER_UUID = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
		OWNER_ID = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.INTEGER);
		BOUND_POS = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.BOOLEAN);
		BOUND_ROT = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.BOOLEAN);
		ANIMATED = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.BOOLEAN);
		FRAMES = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.INTEGER);
		FRAME_TIME = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.INTEGER);
		TEXTURE_LOC = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.STRING);
		TEXTURE_NAME = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.STRING);
		RENDER_TYPE = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.INTEGER);
		AGE = DataTracker.registerData(VFX.class, TrackedDataHandlerRegistry.INTEGER);
	}

	public static class Builder {
		private final int lifetime;
		private float x;
		private float y;
		private float z;
		private float forward;
		private float side;
		private float up;
		private float xRotMain;
		private float yRotMain;
		private float xRot;
		private float yRot;
		private float zRot;
		private float xOff;
		private float xOffMod;
		private float xOffModA;
		private float yOff;
		private float yOffMod;
		private float yOffModA;
		private float xMod;
		private float yMod;
		private float zMod;
		private float xModA;
		private float yModA;
		private float zModA;
		private float scaleMod;
		private float scaleModA;
		private float alphaMod;
		private float alphaModA;
		private float scale;
		private float alpha = 1.0F;
		private Entity owner = null;
		private boolean boundPos;
		private boolean boundRot;
		private boolean animated = false;
		private int frames = 1;
		private int frameTime = 20;
		private String textureLoc = "obscure_api";
		private String textureName = "blank";
		private int renderType;
		private Consumer<VFX> action = (vfx) -> {
		};
		private SoundEvent sound = null;
		private float volume;
		private float pitch = 1.0F;

		private Builder(int lifetime) {
			this.lifetime = lifetime;
		}

		public static Builder create(int lifetime) {
			return new Builder(lifetime);
		}

		public Builder pos(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}

		public Builder pos(Vec3d pos) {
			this.x = (float)pos.x;
			this.y = (float)pos.y;
			this.z = (float)pos.z;
			return this;
		}

		public Builder pos(double x, double y, double z) {
			this.x = (float)x;
			this.y = (float)y;
			this.z = (float)z;
			return this;
		}

		public Builder relativePos(LivingEntity entity, float forward, float left, float up) {
			Vec3d pos = EventUtils.getRelativePos(entity, forward, left, up);
			this.x = (float)pos.x;
			this.y = (float)pos.y;
			this.z = (float)pos.z;
			return this;
		}

		public Builder moveForward(float offset, float mod, float modA) {
			this.xOff = offset * -1.0F;
			this.xOffMod = mod * -1.0F;
			this.xOffModA = modA * -1.0F;
			return this;
		}

		public Builder moveUp(float offset, float mod, float modA) {
			this.yOff = offset * -1.0F;
			this.yOffMod = mod * -1.0F;
			this.yOffModA = modA * -1.0F;
			return this;
		}

		public Builder xRot(float rot, float mod, float modA) {
			this.xRot += rot;
			this.xMod = mod;
			this.xModA = modA;
			return this;
		}

		public Builder yRot(float rot, float mod, float modA) {
			this.yRot += rot;
			this.yMod = mod;
			this.yModA = modA;
			return this;
		}

		public Builder zRot(float rot, float mod, float modA) {
			this.zRot += rot;
			this.zMod = mod;
			this.zModA = modA;
			return this;
		}

		public Builder mainRot(float xRot, float yRot) {
			if (xRot != 0.0F) {
				this.xRotMain = xRot;
			}

			if (yRot != 0.0F) {
				this.yRotMain = yRot;
			}

			return this;
		}

		public Builder relativeRot(LivingEntity entity, boolean x, boolean y) {
			if (x) {
				this.xRotMain = entity.getPitch(1.0F);
			}

			if (y) {
				this.yRotMain = entity.getYaw(1.0F) - 90.0F;
			}

			return this;
		}

		public Builder scale(float scale, float mod, float modA) {
			this.scale = scale;
			this.scaleMod = mod;
			this.scaleModA = modA;
			return this;
		}

		public Builder alpha(float alpha, float mod, float modA) {
			this.alpha = alpha;
			this.alphaMod = mod;
			this.alphaModA = modA;
			return this;
		}

		public Builder owner(Entity owner) {
			this.owner = owner;
			return this;
		}

		public Builder boundPos(boolean flag, float relativeFront, float relativeSide, float relativeUp) {
			this.boundPos = flag;
			this.forward = relativeFront;
			this.side = relativeSide;
			this.up = relativeUp;
			return this;
		}

		public Builder boundRot(boolean flag) {
			this.boundRot = flag;
			return this;
		}

		public Builder texture(String modID, String name) {
			this.textureLoc = modID;
			this.textureName = name;
			return this;
		}

		public Builder animatedTexture(String modID, String name, int frames, int frameTime) {
			this.animated = true;
			this.textureLoc = modID;
			this.textureName = name;
			this.frames = frames;
			this.frameTime = frameTime;
			return this;
		}

		public Builder render(RenderType type) {
			this.renderType = type.getValue();
			return this;
		}

		public Builder sound(SoundEvent sound, float volume, float pitch) {
			this.sound = sound;
			this.volume = volume;
			this.pitch = pitch;
			return this;
		}

		public Builder action(Consumer<VFX> action) {
			this.action = action;
			return this;
		}

		public void build(World world) {
			if (!world.isClient
) {
				new VFX(this, world);
			}

		}

		public static enum RenderType {
			TRANSLUCENT_EMISSIVE(0),
			EYES(1),
			END_GATEWAY(2),
			END_PORTAL(3);

			private final int VALUE;

			private RenderType(int value) {
				this.VALUE = value;
			}

			public int getValue() {
				return this.VALUE;
			}
		}
	}
}
