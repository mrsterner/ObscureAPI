package dev.sterner.obscureapi.client;

@EventBusSubscriber(
		bus = Bus.MOD,
		value = {Dist.CLIENT}
)
public class ModModels {
	public ModModels() {
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelVFX.LAYER, ModelVFX::createBodyLayer);
	}
}
