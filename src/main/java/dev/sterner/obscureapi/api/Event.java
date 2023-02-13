package dev.sterner.obscureapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class Event {
	private final Data DATA;
	private int TICKS;

	private Event(Data data) {
		this.DATA = data;
	}

	@Nullable
	public static Event create(Data data) {
		if (data.SIDE.isServer() && data.LEVEL.f_46443_) {
			return null;
		} else if (data.SIDE.isClient() && !data.WORLD.isClient) {
			return null;
		} else {
			Event event = new Event(data);
			MinecraftForge.EVENT_BUS.register(event);
			return event;
		}
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == Phase.END) {
			++this.TICKS;
			if (this.TICKS >= this.DATA.LIFETIME) {
				MinecraftForge.EVENT_BUS.unregister(this);
			}

			this.DATA.action.accept(this);
		}
	}

	public int getTicks() {
		return this.TICKS;
	}

	public Data getData() {
		return this.DATA;
	}

	public static class Data {
		private final World WORLD;
		private final int LIFETIME;
		private final LogicalSide SIDE;
		@Nullable
		private PlayerEntity PLAYER = null;
		@Nullable
		private LivingEntity LIVING_ENTITY = null;
		@Nullable
		private Entity ENTITY = null;
		@Nullable
		private BlockPos BLOCK_POS = null;
		@Nullable
		private Vec3d VEC = null;
		private Consumer<Event> action = (event) -> {
		};

		private Data(World world, int lifetime, LogicalSide side) {
			this.WORLD = world;
			this.LIFETIME = lifetime;
			this.SIDE = side;
		}

		public static Data build(World world, int lifetime, LogicalSide side) {
			return new Data(world, lifetime, side);
		}

		public Data action(Consumer<Event> action) {
			this.action = action;
			return this;
		}

		public Data withPlayer(PlayerEntity player) {
			this.PLAYER = player;
			return this;
		}

		public Data withLivingEntity(LivingEntity livingEntity) {
			this.LIVING_ENTITY = livingEntity;
			return this;
		}

		public Data withEntity(Entity entity) {
			this.ENTITY = entity;
			return this;
		}

		public Data withBlockPos(BlockPos blockPos) {
			this.BLOCK_POS = blockPos;
			return this;
		}

		public Data withVec(Vec3d vec) {
			this.VEC = vec;
			return this;
		}

		@Nullable
		public PlayerEntity getPlayer() {
			return this.PLAYER;
		}

		@Nullable
		public LivingEntity getLivingEntity() {
			return this.LIVING_ENTITY;
		}

		@Nullable
		public Entity getEntity() {
			return this.ENTITY;
		}

		@Nullable
		public BlockPos getBlockPos() {
			return this.BLOCK_POS;
		}

		@Nullable
		public Vec3d getVec() {
			return this.VEC;
		}
	}
}
