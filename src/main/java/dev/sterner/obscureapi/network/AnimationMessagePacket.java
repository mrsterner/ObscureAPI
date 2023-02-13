package dev.sterner.obscureapi.network;

import dev.sterner.obscureapi.ObscureAPI;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationMessagePacket {
	public static final Identifier ID = new Identifier(ObscureAPI.MODID, "animation_message_packet");


	public static void send(PlayerEntity player, NbtCompound animations, int entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeNbt(animations);
		buf.writeInt(entity);

		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		AtomicBoolean success = new AtomicBoolean(false);
		NbtCompound animations = buf.readNbt();
		int entity = buf.readInt();
		client.execute(() -> {
			success.set(network.readPacket(entity, animations));
		});
		success.get();
	}
}
