package dev.sterner.obscureapi.network;

@EventBusSubscriber(
		bus = Bus.MOD
)
public class ReloadMessage {
	int type;

	public ReloadMessage(int type) {
		this.type = type;
	}

	public ReloadMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
	}

	public static void buffer(ReloadMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
	}

	public static void handler(ReloadMessage message, Supplier<NetworkEvent.Context> context) {
		((NetworkEvent.Context)context.get()).enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
				return ObscuriaCollection::reload;
			});
		});
		((NetworkEvent.Context)context.get()).setPacketHandled(true);
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ObscureAPI.addNetworkMessage(ReloadMessage.class, ReloadMessage::buffer, ReloadMessage::new, ReloadMessage::handler);
	}
}
