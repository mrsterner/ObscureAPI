package dev.sterner.obscureapi.world.items;

import dev.sterner.obscureapi.registry.ObscureAPIStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class VialOfKnowledgeItem extends Item {
	public VialOfKnowledgeItem() {
		super((new Item.Settings()).maxCount(4).rarity(Rarity.UNCOMMON).food((new FoodComponent.Builder()).hunger(0).saturationModifier(0.2F).alwaysEdible().build()));
	}

	@Override
	public @NotNull UseAction getUseAction(@NotNull ItemStack itemstack) {
		return UseAction.DRINK;
	}

	@Override
	public int getMaxUseTime(@NotNull ItemStack itemstack) {
		return 20;
	}

	@Override
	@ClientOnly
	public boolean hasGlint(@NotNull ItemStack itemstack) {
		return true;
	}

	@Override
	public @NotNull ItemStack finishUsing(@NotNull ItemStack itemstack, @NotNull World world, @NotNull LivingEntity entity) {
		ItemStack retval = super.finishUsing(itemstack, world, entity);
		entity.addStatusEffect(new StatusEffectInstance(ObscureAPIStatusEffects.KNOWLEDGE, 2400, 0));
		return retval;
	}
}
