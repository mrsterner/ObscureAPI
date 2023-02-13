package dev.sterner.obscureapi.network;


import dev.sterner.obscureapi.ObscureAPI;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class ReloadMessage {
	public static final Identifier ID = new Identifier(ObscureAPI.MODID, "reload_packet");

	public static void send(PlayerEntity player, int messageType) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(messageType);
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int messageType = buf.readInt();
		client.execute(() -> {
			ObscuriaCollection::reload;
		});
	}
}
