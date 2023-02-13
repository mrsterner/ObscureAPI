package dev.sterner.obscureapi.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DynamicProjectileItem {
	boolean mirror() default false;

	boolean fastSpin() default false;

	boolean distance() default false;
}
