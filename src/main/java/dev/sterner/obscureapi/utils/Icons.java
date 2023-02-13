package dev.sterner.obscureapi.utils;

public enum Icons {
	ARMOR("icon.armor"),
	ARMOR_HALF("icon.armor_half"),
	ARMOR_EMPTY("icon.armor_empty"),
	ARMOR_TOUGHNESS("icon.armor_toughness"),
	ARROW_DOWN("icon.arrow_down"),
	ARROW_UP("icon.arrow_up"),
	ATTACK_SPEED("attack_speed"),
	ATTACK_SPEED_FAST("icon.attack_speed.fast"),
	ATTACK_SPEED_MEDIUM("icon.attack_speed.medium"),
	ATTACK_SPEED_SLOW("icon.attack_speed.slow"),
	ATTACK_SPEED_VERY_FAST("icon.attack_speed.very_fast"),
	ATTACK_SPEED_VERY_SLOW("icon.attack_speed.very_slow"),
	BOSS("icon.boss"),
	DAMAGE("icon.damage"),
	DIALOGUE("icon.dialogue"),
	DOWN("icon.down"),
	DURABILITY("icon.durability"),
	ENERGY("icon.energy"),
	EXP("icon.exp"),
	FOOD("icon.food"),
	FOOD_HALF("icon.food_half"),
	FOOD_EMPTY("icon.food_empty"),
	FOOD_SATURATION("icon.food_saturation"),
	HEART("icon.heart"),
	HEART_HALF("icon.heart_half"),
	HEART_EMPTY("icon.heart_empty"),
	INFO("icon.info"),
	KNOCKBACK_RESISTANCE("icon.knockback_resistance"),
	REPLY("icon.reply"),
	RMB("icon.rmb"),
	SOUL("icon.soul"),
	STAR("icon.star"),
	STICK("icon.stick"),
	UP("icon.up");

	private final String KEY;

	private Icons(String key) {
		this.KEY = key;
	}

	public String get() {
		return TextUtils.translation(this.KEY);
	}
}
