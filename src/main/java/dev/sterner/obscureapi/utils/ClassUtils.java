package dev.sterner.obscureapi.utils;

import dev.sterner.obscureapi.ObscureAPI;
import dev.sterner.obscureapi.api.classes.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ClassUtils {
	public ClassUtils() {
	}

	public static boolean isClassItem(@NotNull Item item) {
		return item.getClass().isAnnotationPresent(ClassItem.class);
	}

	public static ObscureClass getItemClass(Item item) {
		if (isClassItem(item)) {
			Identifier registry = new Identifier(((ClassItem)item.getClass().getAnnotation(ClassItem.class)).itemClass());
			return ObscureAPI.Classes.get(registry);
		} else {
			return ObscureAPI.Classes.BLANK;
		}
	}

	public static ObscureType getItemType(Item item) {
		if (isClassItem(item)) {
			String type = ((ClassItem)item.getClass().getAnnotation(ClassItem.class)).itemType();
			return new ObscureType(type);
		} else {
			return new ObscureType("none");
		}
	}

	public static boolean hasAbilities(Item item) {
		return isClassItem(item) && !getAbilities(item).isEmpty();
	}

	public static boolean hasVisibleAbilities(Item item) {
		return isClassItem(item) && !getVisibleAbilities(item).isEmpty();
	}

	public static boolean hasBonuses(Item item) {
		return isClassItem(item) && !getBonuses(item).isEmpty();
	}

	public static boolean hasVisibleBonuses(Item item) {
		return isClassItem(item) && !getVisibleBonuses(item).isEmpty();
	}

	public static List<Ability> getAbilities(Item item) {
		List<Ability> abilities = new ArrayList();
		Field[] var2 = item.getClass().getDeclaredFields();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			Field field = var2[var4];
			if (field.isAnnotationPresent(ClassAbility.class) && field.getType().isAssignableFrom(Ability.class)) {
				try {
					abilities.add(0, (Ability)field.get(item));
				} catch (IllegalAccessException var7) {
					throw new RuntimeException(var7);
				}
			}
		}

		return abilities;
	}

	public static List<Ability> getVisibleAbilities(Item item) {
		List<Ability> abilities = new ArrayList();
		Field[] var2 = item.getClass().getDeclaredFields();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			Field field = var2[var4];
			if (field.isAnnotationPresent(ClassAbility.class) && ((ClassAbility)field.getAnnotation(ClassAbility.class)).visible() && field.getType().isAssignableFrom(Ability.class)) {
				try {
					abilities.add(0, (Ability)field.get(item));
				} catch (IllegalAccessException var7) {
					throw new RuntimeException(var7);
				}
			}
		}

		return abilities;
	}

	public static List<Bonus> getBonuses(Item item) {
		List<Bonus> bonuses = new ArrayList();
		Field[] var2 = item.getClass().getDeclaredFields();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			Field field = var2[var4];
			if (field.isAnnotationPresent(ClassBonus.class) && field.getType().isAssignableFrom(Bonus.class)) {
				try {
					bonuses.add(0, (Bonus)field.get(item));
				} catch (IllegalAccessException var7) {
					throw new RuntimeException(var7);
				}
			}
		}

		return bonuses;
	}

	public static List<Bonus> getVisibleBonuses(Item item) {
		List<Bonus> bonuses = new ArrayList();
		Field[] var2 = item.getClass().getDeclaredFields();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			Field field = var2[var4];
			if (field.isAnnotationPresent(ClassBonus.class) && ((ClassBonus)field.getAnnotation(ClassBonus.class)).visible() && field.getType().isAssignableFrom(Bonus.class)) {
				try {
					bonuses.add(0, (Bonus)field.get(item));
				} catch (IllegalAccessException var7) {
					throw new RuntimeException(var7);
				}
			}
		}

		return bonuses;
	}
}
