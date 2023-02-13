package dev.sterner.obscureapi.mixin;

import dev.sterner.obscureapi.world.items.ObscureRarity;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Rarity.class)
public abstract class RarityMixin {

	@Final
	@Mutable
	@Shadow(aliases = "field_8905")
	private static Rarity[] VALUES;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void obscureapi$addRarityEnum(CallbackInfo ci){
		ArrayList<Rarity> rarities = new ArrayList<>(List.of(VALUES));
		rarities.add(rarities.size() + 1, ObscureRarity.MYTHIC);
		rarities.add(rarities.size() + 2, ObscureRarity.LEGENDARY);
		VALUES = rarities.toArray(Rarity[]::new);
	}
}
