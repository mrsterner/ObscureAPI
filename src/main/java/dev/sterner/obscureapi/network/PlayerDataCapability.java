package dev.sterner.obscureapi.network;

@EventBusSubscriber(
		bus = Bus.MOD
)
public class PlayerDataCapability {
	public static final Capability<PlayerData> OBS_DATA = CapabilityManager.get(new CapabilityToken<PlayerData>() {
	});

	public PlayerDataCapability() {
	}

	public static NbtCompound get(Player player) {
		return ((PlayerData)player.getCapability(OBS_DATA, (Direction)null).orElse(new PlayerData())).tag;
	}

	public static NbtCompound get(Player player, String name) {
		NbtCompound tag = get(player);
		return tag.contains(name) ? (NbtCompound)tag.m_128423_(name) : new NbtCompound();
	}

	public static void put(Player player, NbtCompound tag) {
		player.getCapability(OBS_DATA, (Direction)null).ifPresent((capability) -> {
			capability.tag = tag;
			capability.syncPlayerData(player);
		});
	}

	public static void put(Player player, String name, NbtCompound data) {
		NbtCompound tag = get(player);
		if (tag.contains(name)) {
			tag.remove(name);
		}

		tag.put(name, data);
		put(player, tag);
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		ObscureAPI.addNetworkMessage(PlayerDataMessage.class, PlayerDataMessage::buffer, PlayerDataMessage::new, PlayerDataMessage::handler);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event) {
		event.register(PlayerData.class);
	}

	public static class PlayerData {
		public NbtCompound tag = new NbtCompound();

		public PlayerData() {
		}

		public void syncPlayerData(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer) {
				ObscureAPI.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> {
					return serverPlayer;
				}), new PlayerDataMessage(this));
			}

		}
	}

	public static class PlayerDataMessage {
		public PlayerData data;

		public PlayerDataMessage(FriendlyByteBuf buffer) {
			this.data = new PlayerData();
			this.data.tag = buffer.m_130260_();
		}

		public PlayerDataMessage(PlayerData data) {
			this.data = data;
		}

		public static void buffer(PlayerDataMessage message, FriendlyByteBuf buffer) {
			buffer.m_130079_(message.data.tag);
		}

		public static void handler(PlayerDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = (NetworkEvent.Context)contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer()) {
					assert Minecraft.m_91087_().f_91074_ != null;

					PlayerData dataTag = (PlayerData)Minecraft.m_91087_().f_91074_.getCapability(PlayerDataCapability.OBS_DATA, (Direction)null).orElse(new PlayerData());
					dataTag.tag = message.data.tag;
				}

			});
			context.setPacketHandled(true);
		}
	}

	@EventBusSubscriber
	private static class PlayerDataProvider implements ICapabilitySerializable<Tag> {
		private final PlayerData playerData = new PlayerData();
		private final LazyOptional<PlayerData> instance = LazyOptional.of(() -> {
			return this.playerData;
		});

		private PlayerDataProvider() {
		}

		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer)) {
				event.addCapability(new Identifier("obscuria", "data"), new PlayerDataProvider());
			}

		}

		public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
			return cap == PlayerDataCapability.OBS_DATA ? this.instance.cast() : LazyOptional.empty();
		}

		public Tag serializeNBT() {
			return this.playerData.tag;
		}

		public void deserializeNBT(Tag nbt) {
			this.playerData.tag = (NbtCompound)nbt;
		}
	}

	@EventBusSubscriber
	public static class EventBusHandlers {
		public EventBusHandlers() {
		}

		@SubscribeEvent
		public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.getEntity().f_19853_.m_5776_()) {
				((PlayerData)event.getEntity().getCapability(PlayerDataCapability.OBS_DATA, (Direction)null).orElse(new PlayerData())).syncPlayerData(event.getEntity());
			}
		}

		@SubscribeEvent
		public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
			if (!event.getEntity().f_19853_.m_5776_()) {
				((PlayerData)event.getEntity().getCapability(PlayerDataCapability.OBS_DATA, (Direction)null).orElse(new PlayerData())).syncPlayerData(event.getEntity());
			}
		}

		@SubscribeEvent
		public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getEntity().f_19853_.m_5776_()) {
				((PlayerData)event.getEntity().getCapability(PlayerDataCapability.OBS_DATA, (Direction)null).orElse(new PlayerData())).syncPlayerData(event.getEntity());
			}
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			event.getOriginal().revive();
			PlayerData original = (PlayerData)event.getOriginal().getCapability(PlayerDataCapability.OBS_DATA, (Direction)null).orElse(new PlayerData());
			PlayerData clone = (PlayerData)event.getEntity().getCapability(PlayerDataCapability.OBS_DATA, (Direction)null).orElse(new PlayerData());
			clone.tag = original.tag;
		}
	}
}
