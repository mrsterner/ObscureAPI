package dev.sterner.obscureapi.utils;

import com.google.gson.JsonObject;
import dev.sterner.obscureapi.api.classes.Ability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TextUtils {
	public static final Style ICONS;

	public TextUtils() {
	}

	@NotNull
	public static MutableText component(String text) {
		return Text.literal(text).fillStyle(ICONS);
	}

	@NotNull
	public static MutableText translatableText(String text) {
		return Text.translatable(text).fillStyle(ICONS);
	}

	@NotNull
	public static String translation(String text) {
		return Text.translatable(text).fillStyle(ICONS).getString();
	}

	public static List<Text> buildTooltip(List<Text> list, int lineWight, String text, String lineStart) {
		return buildTooltip(list, lineWight, text, lineStart, lineStart);
	}

	public static List<Text> buildTooltip(List<Text> list, int lineWight, String text, String firstLineStart, String lineStart) {
		String[] words = format(text).split(" ");
		StringBuilder line = new StringBuilder(firstLineStart);
		int index = 0;
		String[] var8 = words;
		int var9 = words.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			String word = var8[var10];
			++index;
			if (index >= words.length) {
				if (length(line, word) > lineWight) {
					list.add(component(line.toString()));
					list.add(component(lineStart + word));
				} else {
					list.add(component(line.append(word).toString()));
				}
			} else if (word.equals(">")) {
				list.add(component(line.toString()));
				line = new StringBuilder(lineStart);
			} else {
				if (length(line) > 2 && length(line, word) > lineWight) {
					list.add(component(line.toString()));
					line = new StringBuilder(lineStart);
				}

				line.append(word).append(" ");
			}
		}

		return list;
	}

	public static List<Text> buildLore(List<Text> list, int lineWight, String text) {
		return buildLore(list, lineWight, text, "");
	}

	public static List<Text> buildLore(List<Text> list, int lineWight, String text, String lineStart) {
		String[] words = format(text).split(" ");
		StringBuilder line = new StringBuilder(lineStart + "§7");
		int index = 0;
		String[] var7 = words;
		int var8 = words.length;

		for(int var9 = 0; var9 < var8; ++var9) {
			String word = var7[var9];
			++index;
			if (index >= words.length) {
				if (length(line, word) > lineWight) {
					list.add(component(line.toString()));
					list.add(component(lineStart + "§7" + word));
				} else {
					list.add(component(line.append(word).toString()));
				}
			} else if (word.equals(">")) {
				list.add(component(line.toString()));
				line = new StringBuilder(lineStart + "§7");
			} else {
				if (length(line) > 2 && length(line, word) > lineWight) {
					list.add(component(line.toString()));
					line = new StringBuilder(lineStart + "§7");
				}

				line.append(word).append(" ");
			}
		}

		return list;
	}

	@NotNull
	public static List<Text> buildKnowledge(@NotNull List<Text> list, int lineWight, String text) {
		String[] words = format(text).split(" ");
		list.add(component(translation("tooltip.key.knowledge")));
		StringBuilder line = new StringBuilder("§9   ");
		int index = 0;
		String[] var6 = words;
		int var7 = words.length;

		for(int var8 = 0; var8 < var7; ++var8) {
			String word = var6[var8];
			++index;
			if (index >= words.length) {
				if (length(line, word) > lineWight) {
					list.add(component(line.toString()));
					list.add(component("§9   " + word));
				} else {
					list.add(component(line.append(word).toString()));
				}
			} else if (word.equals(">")) {
				list.add(component(line.toString()));
				line = new StringBuilder("§9   ");
			} else {
				if (length(line) > 2 && length(line, word) > lineWight) {
					list.add(component(line.toString()));
					line = new StringBuilder("§9   ");
				}

				line.append(word).append(" ");
			}
		}

		return list;
	}

	public static List<Text> buildAbility(List<Text> list, int lineWight, @NotNull Ability ability, PlayerEntity player) {
		boolean title = true;
		Ability.Style style = ability.getStyle();
		String text = format(translation(ability.getDescriptionId()));
		String[] words = text.split(" ");
		String var10002 = Icons.STICK.get();
		StringBuilder line = new StringBuilder(var10002 + style.TITLE);
		int index = 0;
		String[] var10 = words;
		int var11 = words.length;

		for(int var12 = 0; var12 < var11; ++var12) {
			String word = var10[var12];
			++index;
			var10002 = ability.getVariableString(player, 1);
			String var10000 = word.replace("{#1}", var10002 + ability.getModifierString(1) + style.LINE);
			var10002 = ability.getVariableString(player, 2);
			var10000 = var10000.replace("{#2}", var10002 + ability.getModifierString(2) + style.LINE);
			var10002 = ability.getVariableString(player, 3);
			word = var10000.replace("{#3}", var10002 + ability.getModifierString(3) + style.LINE);
			if (index >= words.length) {
				if (length(line, word) > lineWight) {
					list.add(component(line.toString()));
					list.add(component(Icons.STICK.get() + " " + style.LINE + word));
				} else {
					list.add(component(line.append(word).toString()));
				}
			} else if (word.equals(">")) {
				if (title) {
					line.append(ability.getCostString(player));
					title = false;
				}

				list.add(component(line.toString()));
				var10002 = Icons.STICK.get();
				line = new StringBuilder(var10002 + " " + style.LINE);
			} else {
				if (!title && length(line) > 2 && length(line, word) > lineWight) {
					list.add(component(line.toString()));
					var10002 = Icons.STICK.get();
					line = new StringBuilder(var10002 + " " + style.LINE);
				}

				line.append(word).append(" ");
			}
		}

		return list;
	}

	public static List<Text> buildPerk(List<Text> list, int lineWight, @NotNull Identifier registry) {
		String var10000 = registry.getNamespace();
		String text = format(translation("perk." + var10000 + "." + registry.getPath() + ".desc"));
		String[] words = text.split(" ");
		StringBuilder line = new StringBuilder(Icons.STICK.get() + "§f");
		int index = 0;
		String[] var7 = words;
		int var8 = words.length;

		for(int var9 = 0; var9 < var8; ++var9) {
			String word = var7[var9];
			++index;
			if (index >= words.length) {
				if (length(line, word) > lineWight) {
					list.add(component(line.toString()));
					String var10001 = Icons.STICK.get();
					list.add(component(var10001 + " §7" + word));
				} else {
					list.add(component(line.append(word).toString()));
				}
			} else if (word.equals(">")) {
				list.add(component(line.toString()));
				line = new StringBuilder(Icons.STICK.get() + " §7");
			} else {
				if (length(line) > 2 && length(line, word) > lineWight) {
					list.add(component(line.toString()));
					line = new StringBuilder(Icons.STICK.get() + " §7");
				}

				line.append(word).append(" ");
			}
		}

		return list;
	}

	public static int length(@NotNull StringBuilder builder) {
		return length(builder.toString());
	}

	public static int length(StringBuilder builder, String other) {
		return length(builder) + length(other);
	}

	public static int length(String string, String other) {
		return length(string) + length(other);
	}

	public static int length(@NotNull String string) {
		if (string.length() < 2) {
			return string.length();
		} else {
			Formatting[] var1 = Formatting.values();
			int var2 = var1.length;

			for(int var3 = 0; var3 < var2; ++var3) {
				Formatting chatFormatting = var1[var3];
				string = string.replace("§" + chatFormatting.getCode(), "");
			}

			return string.length();
		}
	}

	public static String getString(@NotNull JsonObject jsonObject, String element) {
		return jsonObject.has(element) ? jsonObject.get(element).getAsString() : "null";
	}

	@NotNull
	public static String format(@NotNull String text) {
		return text.replace("{#}", " > ").replace("{BR}", " > ").replace("{BLACK}", "§0").replace("{WHITE}", "§f").replace("{GRAY}", "§7").replace("{DARK_GRAY}", "§8").replace("{GOLD}", "§6").replace("{GREEN}", "§a").replace("{DARK_GREEN}", "§2").replace("{RED}", "§c").replace("{BLUE}", "§9").replace("{I}", "§f" + Icons.INFO.get()).replace("{U}", "§f" + Icons.RMB.get()).replace("{B}", "§f" + Icons.BOSS.get()).replace("{H1}", "§f" + Icons.HEART.get()).replace("{H2}", "§f" + Icons.HEART_HALF.get()).replace("{H3}", "§f" + Icons.HEART_EMPTY.get());
	}

	@NotNull
	public static String format(@NotNull JsonObject jsonObject, String element) {
		return jsonObject.has(element) ? format(jsonObject.get(element).getAsString()) : "null";
	}

	static {
		ICONS = Style.EMPTY.withFont(new Identifier("obscure_api", "icons"));
	}
}
