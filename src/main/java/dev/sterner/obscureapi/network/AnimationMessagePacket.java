package dev.sterner.obscureapi.network;

@EventBusSubscriber(
		bus = Bus.MOD
)
public class AnimationMessagePacket {
	public int entity;
	public NbtCompound animations;

	public AnimationMessagePacket(NbtCompound animations, int entity) {
		this.entity = entity;
		this.animations = animations;
	}

	public AnimationMessagePacket(FriendlyByteBuf buffer) {
		this.animations = buffer.m_130260_();
		this.entity = buffer.readInt();
	}

	public static void buffer(AnimationMessagePacket message, FriendlyByteBuf buffer) {
		buffer.m_130079_(message.animations);
		buffer.writeInt(message.entity);
	}

	public static void handler(AnimationMessagePacket message, Supplier<NetworkEvent.Context> context) {
		AtomicBoolean success = new AtomicBoolean(false);
		((NetworkEvent.Context)context.get()).enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
				return () -> {
					success.set(network.readPacket(message.entity, message.animations));
				};
			});
		});
		((NetworkEvent.Context)context.get()).setPacketHandled(true);
		success.get();
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ObscureAPI.addNetworkMessage(AnimationMessagePacket.class, AnimationMessagePacket::buffer, AnimationMessagePacket::new, AnimationMessagePacket::handler);
	}
}
