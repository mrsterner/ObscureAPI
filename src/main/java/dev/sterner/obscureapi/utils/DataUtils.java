package dev.sterner.obscureapi.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;

public final class DataUtils {
	public DataUtils() {
	}

	public static NbtCompound get(PlayerEntity player) {
		return ((PlayerDataCapability.PlayerData)player.getCapability(PlayerDataCapability.OBS_DATA, (Direction)null).orElse(new PlayerDataCapability.PlayerData())).tag;
	}

	public static NbtCompound get(PlayerEntity player, String name) {
		return get(player).getCompound(name);
	}

	public static void put(PlayerEntity player, NbtCompound tag) {
		player.getCapability(PlayerDataCapability.OBS_DATA, (Direction)null).ifPresent((capability) -> {
			capability.tag = tag;
			capability.syncPlayerData(player);
		});
	}

	public static void put(PlayerEntity player, String name, NbtCompound data) {
		NbtCompound tag = get(player);
		if (tag.contains(name)) {
			tag.remove(name);
		}

		tag.put(name, data);
		put(player, tag);
	}
}
