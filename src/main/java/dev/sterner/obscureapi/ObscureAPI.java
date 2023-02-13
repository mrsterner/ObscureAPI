package dev.sterner.obscureapi;

import dev.sterner.obscureapi.api.classes.ObscureClass;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ObscureAPI implements ModInitializer {
	public static final String MODID = "obscureapi";
	public static final Logger LOGGER = LoggerFactory.getLogger("Obscure API");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
	}

	public static class Classes {
		private static final HashMap<Identifier, ObscureClass> CLASSES = new HashMap();
		public static final ObscureClass BLANK = new ObscureClass("obscure_api", "blank");

		public Classes() {
		}

		public static ObscureClass register(ObscureClass obscureClass) {
			CLASSES.put(obscureClass.getRegistry(), obscureClass);
			return obscureClass;
		}

		public static boolean isPresent(Identifier key) {
			return CLASSES.containsKey(key);
		}

		public static ObscureClass get(Identifier key) {
			return (ObscureClass)CLASSES.getOrDefault(key, BLANK);
		}
	}
}
