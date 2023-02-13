package dev.sterner.obscureapi;

import dev.sterner.obscureapi.api.DynamicSound;
import dev.sterner.obscureapi.client.screen.BookScreen;
import dev.sterner.obscureapi.utils.EventUtils;
import dev.sterner.obscureapi.utils.TextUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ObscureAPIClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		ItemUtils.addLore("obscure_api:vial_of_knowledge");
		ItemUtils.addLore("obscure_api:obscure_book");
		ObscuriaCollection.sync();

		event.registerLayerDefinition(Model.LAYER_LOCATION, PatreonLayer.Model::createBodyLayer);
	}

	public static void addLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach((skin) -> {
			LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = event.getSkin(skin);
			if (renderer != null) {
				renderer.m_115326_(new PatreonLayer(renderer, event.getEntityModels()));
			}

		});
	}

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.player != null) {
			ObscureAPIClient.checkInbox(1);
		}

	}

	public static void openBook() {
		MinecraftClient.getInstance().setScreen(new BookScreen());
	}

	public static void playLocalDynamicSound(Entity entity, SoundEvent sound, SoundCategory source, float volume, float pitch) {
		MinecraftClient.getInstance().getSoundManager().play(new DynamicSound(entity, sound, source, volume, pitch));
	}

	public static void playLocalDynamicSound(Entity entity, SoundEvent sound, SoundCategory source, float volume, float pitch, Predicate<Entity> condition, boolean loop) {
		MinecraftClient.getInstance().getSoundManager().play(new DynamicSound(entity, sound, source, volume, pitch, condition, loop));
	}

	public static void checkInbox(int type) {
		if ((Integer)Client.messageNotification.get() == type) {
			if (ObscuriaCollection.checkInbox && MinecraftClient.getInstance().player != null) {
				ObscuriaCollection.checkInbox = false;
				AtomicInteger newMessages = new AtomicInteger();
				ObscuriaCollection.getMessages().forEach((message) -> {
					newMessages.set(newMessages.get() + (message.opened ? 0 : 1));
				});
				if (newMessages.get() > 0) {
					ClientPlayerEntity player = MinecraftClient.getInstance().player;
					String var10001 = TextUtils.translation("icon.info");
					EventUtils.sendMessage(player, var10001 + TextUtils.translation("obscure_book.module_inbox.unread_messages_event").replace("#", "" + newMessages.get()));
				}
			}

		}
	}
}
