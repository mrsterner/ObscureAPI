package dev.sterner.obscureapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

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
		} else if (data.SIDE.isClient() && !data.LEVEL.f_46443_) {
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
		private final Level LEVEL;
		private final int LIFETIME;
		private final LogicalSide SIDE;
		@Nullable
		private Player PLAYER = null;
		@Nullable
		private LivingEntity LIVING_ENTITY = null;
		@Nullable
		private Entity ENTITY = null;
		@Nullable
		private BlockPos BLOCK_POS = null;
		@Nullable
		private Vec3 VEC = null;
		private Consumer<Event> action = (event) -> {
		};

		private Data(Level level, int lifetime, LogicalSide side) {
			this.LEVEL = level;
			this.LIFETIME = lifetime;
			this.SIDE = side;
		}

		public static Data build(Level level, int lifetime, LogicalSide side) {
			return new Data(level, lifetime, side);
		}

		public Data action(Consumer<Event> action) {
			this.action = action;
			return this;
		}

		public Data withPlayer(Player player) {
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

		public Data withVec(Vec3 vec) {
			this.VEC = vec;
			return this;
		}

		@Nullable
		public Player getPlayer() {
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
		public Vec3 getVec() {
			return this.VEC;
		}
	}
}
