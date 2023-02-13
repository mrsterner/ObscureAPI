package dev.sterner.obscureapi.api.triggers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TriggerableItem {
    boolean criticalHit() default false;

    boolean hit() default false;

    boolean kill() default false;

    boolean hurt() default false;

    boolean death() default false;
}
