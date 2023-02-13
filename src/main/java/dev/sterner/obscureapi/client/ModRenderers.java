package dev.sterner.obscureapi.client;

@EventBusSubscriber(
		bus = Bus.MOD,
		value = {Dist.CLIENT}
)
public class ModRenderers {
	public ModRenderers() {
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer((EntityType)ObscureAPIEntities.VFX.get(), VFXRenderer::new);
	}
}
