package dev.sterner.obscureapi.utils;


import dev.sterner.obscureapi.api.triggers.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2d;
import org.joml.Vector2f;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class EventUtils {
	public EventUtils() {
	}

	public static void playLocalDynamicSound(Entity entity, SoundEvent sound, SoundCategory source, float volume, float pitch) {
		if (entity.world.isClient) {
			ObscureAPIClient.playLocalDynamicSound(entity, sound, source, volume, pitch);
		}

	}

	public static void playLocalDynamicSound(Entity entity, SoundEvent sound, SoundCategory source, float volume, float pitch, Predicate<Entity> condition, boolean loop) {
		if (entity.world.isClient) {
			ObscureAPIClient.playLocalDynamicSound(entity, sound, source, volume, pitch, condition, loop);
		}

	}

	public static void sendMessage(PlayerEntity player, String text) {
		if (player != null) {
			player.sendMessage(TextUtils.component(text), false);
		}

	}

	public static void sendSystemMessage(PlayerEntity player, String text) {
		if (player != null) {
			player.sendSystemMessage(TextUtils.component(text));
		}

	}

	public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, float forward, float left, float up, float area) {
		return getRelativeEntities(entity, targets, forward, left, up, area, false, false);
	}

	public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, Vec3d pos, float area) {
		return getRelativeEntities(entity, targets, pos, area, false, false);
	}

	public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, float forward, float left, float up, float area, boolean visibleOnly) {
		return getRelativeEntities(entity, targets, forward, left, up, area, visibleOnly, false);
	}

	public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, Vec3d pos, float area, boolean visibleOnly) {
		return getRelativeEntities(entity, targets, pos, area, visibleOnly, false);
	}

	public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, float forward, float left, float up, float area, boolean visibleOnly, boolean circle) {
		return getRelativeEntities(entity, targets, getRelativePos(entity, forward, left, up), area, visibleOnly, circle);
	}

	public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, Vec3d pos, float area, boolean visibleOnly, boolean circle) {
		List<? extends LivingEntity> list = entity.world.getNonSpectatingEntities(targets, (new Box(pos, pos)).expand((double)area));
		List<LivingEntity> entities = new ArrayList();
		list.forEach((entry) -> {
			if ((!visibleOnly || entity.canSee(entry)) && (!circle || pos.distanceTo(entry.getPos()) <= (double)area)) {
				entities.add(entry);
			}

		});
		return entities;
	}

	public static Vec3d getRelativePos(LivingEntity entity, float forward, float left, float up) {
		Vector2f vec2 = new Vector2f(entity.getPitch(1.0F), entity.getYaw(1.0F));
		Vec3d vec3 = entity.getCameraPosVec(1.0F);
		float f = MathHelper.cos((vec2.y + 90.0F) * 0.017453292F);
		float f1 = MathHelper.sin((vec2.y + 90.0F) * 0.017453292F);
		float f2 = MathHelper.cos(-vec2.x * 0.017453292F);
		float f3 = MathHelper.sin(-vec2.x * 0.017453292F);
		float f4 = MathHelper.cos((-vec2.x + 90.0F) * 0.017453292F);
		float f5 = MathHelper.sin((-vec2.x + 90.0F) * 0.017453292F);
		Vec3d vec31 = new Vec3d((f * f2), f3, (f1 * f2));
		Vec3d vec32 = new Vec3d((f * f4), f5, (f1 * f4));
		Vec3d vec33 = vec31.crossProduct(vec32).multiply(-1.0);
		double d0 = vec31.x * (double)forward + vec32.x * (double)up + vec33.x * (double)left;
		double d1 = vec31.y * (double)forward + vec32.y * (double)up + vec33.y * (double)left;
		double d2 = vec31.z * (double)forward + vec32.z * (double)up + vec33.z * (double)left;
		return new Vec3d(vec3.x + d0, vec3.y + d1, vec3.z + d2);
	}

	public static class Triggers {
		public Triggers() {
		}

		public static void criticalHit(LivingEntity source, LivingEntity target, ItemStack weapon, float damage) {
			if (weapon.getItem().getClass().isAnnotationPresent(TriggerableItem.class) && (weapon.getItem().getClass().getAnnotation(TriggerableItem.class)).criticalHit()) {
				Method[] var4 = weapon.getItem().getClass().getDeclaredMethods();
				for (Method method : var4) {
					if (method.isAnnotationPresent(TriggerCriticalHit.class)) {
						try {
							method.invoke(weapon.getItem(), source, target, weapon, damage);
						} catch (IllegalAccessException | InvocationTargetException var9) {
							throw new RuntimeException(var9);
						}
					}
				}
			}

		}

		public static void hit(LivingEntity source, LivingEntity target, ItemStack weapon, float damage) {
			if (weapon.getItem().getClass().isAnnotationPresent(TriggerableItem.class) && ((TriggerableItem)weapon.getItem().getClass().getAnnotation(TriggerableItem.class)).hit()) {
				Method[] var4 = weapon.getItem().getClass().getDeclaredMethods();
				for (Method method : var4) {
					if (method.isAnnotationPresent(TriggerHit.class)) {
						try {
							method.invoke(weapon.getItem(), source, target, weapon, damage);
						} catch (IllegalAccessException | InvocationTargetException var9) {
							throw new RuntimeException(var9);
						}
					}
				}
			}

		}

		public static void hurt(LivingEntity source, LivingEntity target, ItemStack weapon, float damage) {
			if (weapon.getItem().getClass().isAnnotationPresent(TriggerableItem.class) && (weapon.getItem().getClass().getAnnotation(TriggerableItem.class)).hurt()) {
				Method[] var4 = weapon.getItem().getClass().getDeclaredMethods();
				for (Method method : var4) {
					if (method.isAnnotationPresent(TriggerHurt.class)) {
						try {
							method.invoke(weapon.getItem(), source, target, weapon, damage);
						} catch (IllegalAccessException | InvocationTargetException var9) {
							throw new RuntimeException(var9);
						}
					}
				}
			}

		}

		public static void kill(LivingEntity source, LivingEntity target, ItemStack weapon) {
			if (weapon.getItem().getClass().isAnnotationPresent(TriggerableItem.class) && (weapon.getItem().getClass().getAnnotation(TriggerableItem.class)).kill()) {
				Method[] var3 = weapon.getItem().getClass().getDeclaredMethods();
				for (Method method : var3) {
					if (method.isAnnotationPresent(TriggerKill.class)) {
						try {
							method.invoke(weapon.getItem(), source, target, weapon);
						} catch (IllegalAccessException | InvocationTargetException var8) {
							throw new RuntimeException(var8);
						}
					}
				}
			}

		}

		public static void death(LivingEntity source, LivingEntity target, ItemStack weapon) {
			if (weapon.getItem().getClass().isAnnotationPresent(TriggerableItem.class) && (weapon.getItem().getClass().getAnnotation(TriggerableItem.class)).death()) {
				Method[] var3 = weapon.getItem().getClass().getDeclaredMethods();

				for (Method method : var3) {
					if (method.isAnnotationPresent(TriggerDeath.class)) {
						try {
							method.invoke(weapon.getItem(), source, target, weapon);
						} catch (IllegalAccessException | InvocationTargetException var8) {
							throw new RuntimeException(var8);
						}
					}
				}
			}

		}
	}
}
