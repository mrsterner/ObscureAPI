package dev.sterner.obscureapi.api.tooltips;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Tooltip {
	Type type() default Tooltip.Type.EXPAND_TOP;

	int wight() default 34;

	public static enum Type {
		EXPAND_TOP,
		EXPAND_BOTTOM,
		TOP,
		BOTTOM,
		ICONS_START,
		ICONS_END;

		private Type() {
		}
	}
}
