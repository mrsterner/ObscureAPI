package dev.sterner.obscureapi.world.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ObscureBookItem extends FoilItem {
	public ObscureBookItem() {
		super((new Item.Settings()).rarity(ObscureRarity.MYTHIC).tab(CreativeModeTab.f_40756_).maxCount(1));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.getWorld().isClient()) {
			ObscureAPIClient.openBook();
			return TypedActionResult.success(user.getStackInHand(hand));
		} else {
			return TypedActionResult.success(user.getStackInHand(hand));
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if (entity.getWorld().isClient()) {
			ObscureAPIClient.checkInbox(2);
		}
	}
}
