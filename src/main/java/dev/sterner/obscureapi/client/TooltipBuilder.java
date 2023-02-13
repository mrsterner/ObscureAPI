package dev.sterner.obscureapi.client;

public class TooltipBuilder {
	public TooltipBuilder() {
	}

	public static void build(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		List<Text> list = event.getToolTip();
		Player player = event.getEntity();
		if (player != null) {
			TooltipBuilder.Modules.buildPerkNames(stack, list);
			TooltipBuilder.Modules.putTooltip(stack, list, Type.BOTTOM);
			TooltipBuilder.Modules.expandableTooltip(player, stack, list);
			TooltipBuilder.Modules.buildKnowledge(player, stack, list);
			TooltipBuilder.Modules.buildLore(stack, list);
			TooltipBuilder.Modules.buildClass(stack, list);
			TooltipBuilder.Modules.putTooltip(stack, list, Type.TOP);
			TooltipBuilder.Modules.buildIcons(stack, list);
		}
	}

	public static class Modules {
		public Modules() {
		}

		private static void buildIcons(ItemStack stack, List<Text> list) {
			if ((Boolean)Client.foodIcons.get() && stack.m_41720_().m_41472_()) {
				TooltipBuilder.AttributeIcons.putFoodIcons(list, stack);
			}

			if ((Boolean)Client.equipmentIcons.get() && (stack.m_41720_() instanceof TieredItem || stack.m_41720_() instanceof ArmorItem) || hasTooltip(stack.m_41720_(), Type.ICONS_START) || hasTooltip(stack.m_41720_(), Type.ICONS_END)) {
				TooltipBuilder.AttributeIcons.putIcons(list, stack);
			}

		}

		private static void buildClass(ItemStack stack, List<Text> list) {
			if (ClassUtils.isClassItem(stack.m_41720_()) && !ObscureAPI.COLLECTION_MODS.containsKey("obscure_tooltips")) {
				String var10002 = Icons.STAR.get();
				list.add(1, TextUtils.component(var10002 + "§6" + ClassUtils.getItemClass(stack.m_41720_()).getLabel(ClassUtils.getItemType(stack.m_41720_()))));
			}

		}

		private static void buildLore(ItemStack stack, List<Text> list) {
			if (TooltipBuilder.Lore.contains(stack)) {
				list.addAll(1, TextUtils.buildLore(new ArrayList(), 34, TextUtils.translation(TooltipBuilder.Lore.get(stack))));
			}

		}

		private static void buildKnowledge(Player player, ItemStack stack, List<Text> list) {
			if (player.m_21023_((MobEffect)ObscureAPIMobEffects.KNOWLEDGE.get()) && TooltipBuilder.Knowledge.contains(stack)) {
				list.addAll(1, TextUtils.buildKnowledge(new ArrayList(), 30, TextUtils.translation(TooltipBuilder.Knowledge.get(stack))));
			}

		}

		private static void expandableTooltip(Player player, ItemStack stack, List<Text> list) {
			boolean abilities = ClassUtils.hasVisibleAbilities(stack.m_41720_());
			boolean bonuses = ClassUtils.hasVisibleBonuses(stack.m_41720_());
			boolean perks = ItemUtils.hasPerks(stack);
			if (abilities || bonuses || perks || hasTooltip(stack.m_41720_(), Type.EXPAND_TOP) || hasTooltip(stack.m_41720_(), Type.EXPAND_BOTTOM)) {
				if (stack.m_41793_() || ItemUtils.hasPerks(stack)) {
					list.add(1, Text.m_237119_());
				}

				if (Screen.m_96638_()) {
					putTooltip(stack, list, Type.EXPAND_BOTTOM);
					if (perks) {
						buildPerks(stack, list);
					}

					if (abilities) {
						buildAbilities(stack, player, list);
					}

					if (bonuses) {
						buildBonuses(stack.m_41720_(), list);
					}

					putTooltip(stack, list, Type.EXPAND_TOP);
					list.add(1, TextUtils.component(TextUtils.translation("obscure_api.tooltip.collapse")));
				} else {
					list.add(1, TextUtils.component(TextUtils.translation("obscure_api.tooltip.expand")));
				}
			}

		}

		private static void buildAbilities(ItemStack stack, Player player, List<Text> list) {
			Iterator var3 = ClassUtils.getVisibleAbilities(stack.m_41720_()).iterator();

			while(var3.hasNext()) {
				Ability ability = (Ability)var3.next();
				list.addAll(1, TextUtils.buildAbility(new ArrayList(), 30, ability, player));
			}

		}

		private static void buildBonuses(Item item, List<Text> list) {
			Iterator var2 = ClassUtils.getVisibleBonuses(item).iterator();

			while(var2.hasNext()) {
				Bonus bonus = (Bonus)var2.next();
				String var10002 = Icons.STICK.get();
				list.add(1, TextUtils.component(var10002 + " " + bonus.getBonusString()));
				var10002 = TextUtils.translation("icon.stick");
				list.add(1, TextUtils.component(var10002 + TextUtils.translation("obscure_api.ability.bonuses")));
			}

		}

		private static void buildPerkNames(ItemStack stack, List<Text> list) {
			if (ItemUtils.hasPerks(stack)) {
				Iterator var2 = ItemUtils.getOrCreatePerks(stack).m_128431_().iterator();

				while(var2.hasNext()) {
					String perk = (String)var2.next();
					Identifier registry = new Identifier(perk);
					String var10002 = registry.m_135827_();
					list.add(1, Text.m_237115_("perk." + var10002 + "." + registry.m_135815_()).m_130948_(Style.f_131099_.m_131150_(registry)));
				}
			}

		}

		private static void buildPerks(ItemStack stack, List<Text> list) {
			Iterator var2 = ItemUtils.getOrCreatePerks(stack).m_128431_().iterator();

			while(var2.hasNext()) {
				String perk = (String)var2.next();
				list.addAll(1, TextUtils.buildPerk(new ArrayList(), 30, new Identifier(perk)));
			}

		}

		private static void putTooltip(ItemStack stack, List<Text> list, Tooltip.Type type) {
			if (hasTooltip(stack.m_41720_(), type)) {
				Method[] var3 = stack.m_41720_().getClass().getDeclaredMethods();
				int var4 = var3.length;

				for(int var5 = 0; var5 < var4; ++var5) {
					Method method = var3[var5];
					if (method.isAnnotationPresent(Tooltip.class) && ((Tooltip)method.getAnnotation(Tooltip.class)).type().equals(type)) {
						try {
							list.addAll(1, TextUtils.buildLore(new ArrayList(), ((Tooltip)method.getAnnotation(Tooltip.class)).wight(), (String)method.invoke(stack.m_41720_(), stack, Minecraft.getInstance().f_91074_), !type.equals(Type.EXPAND_TOP) && !type.equals(Type.EXPAND_BOTTOM) ? "" : Icons.STICK.get()));
						} catch (IllegalAccessException | InvocationTargetException var8) {
							throw new RuntimeException(var8);
						}
					}
				}

			}
		}

		private static String getLine(ItemStack stack, Tooltip.Type type) {
			StringBuilder tooltip = new StringBuilder();
			if (!hasTooltip(stack.m_41720_(), type)) {
				return tooltip.toString();
			} else {
				Method[] var3 = stack.m_41720_().getClass().getDeclaredMethods();
				int var4 = var3.length;

				for(int var5 = 0; var5 < var4; ++var5) {
					Method method = var3[var5];
					if (method.isAnnotationPresent(Tooltip.class) && ((Tooltip)method.getAnnotation(Tooltip.class)).type().equals(type)) {
						try {
							tooltip.append((String)method.invoke(stack.m_41720_(), stack, Minecraft.getInstance().f_91074_));
							tooltip.append(" ");
						} catch (IllegalAccessException | InvocationTargetException var8) {
							throw new RuntimeException(var8);
						}
					}
				}

				return tooltip.toString();
			}
		}

		private static boolean hasTooltip(Item item, Tooltip.Type type) {
			if (!item.getClass().isAnnotationPresent(ModifiedTooltip.class)) {
				return false;
			} else {
				Method[] var2 = item.getClass().getDeclaredMethods();
				int var3 = var2.length;

				for(int var4 = 0; var4 < var3; ++var4) {
					Method method = var2[var4];
					if (method.isAnnotationPresent(Tooltip.class) && ((Tooltip)method.getAnnotation(Tooltip.class)).type().equals(type)) {
						return true;
					}
				}

				return false;
			}
		}
	}

	private static class AttributeIcons {
		private AttributeIcons() {
		}

		private static void putIcons(List<Text> list, ItemStack stack) {
			String icons = "";
			icons = icons + TooltipBuilder.Modules.getLine(stack, Type.ICONS_START);
			Multimap<Attribute, AttributeModifier> mainhandMap = stack.m_41638_(EquipmentSlot.MAINHAND);
			Multimap<Attribute, AttributeModifier> headMap = stack.m_41638_(EquipmentSlot.HEAD);
			Multimap<Attribute, AttributeModifier> chestMap = stack.m_41638_(EquipmentSlot.CHEST);
			Multimap<Attribute, AttributeModifier> legsMap = stack.m_41638_(EquipmentSlot.LEGS);
			Multimap<Attribute, AttributeModifier> feetMap = stack.m_41638_(EquipmentSlot.FEET);
			if (!mainhandMap.isEmpty()) {
				icons = icons + getIcon(false, Icons.DAMAGE.get(), 1.0, mainhandMap.get(Attributes.f_22281_)) + getAttackSpeedIcon(mainhandMap.get(Attributes.f_22283_));
			}

			if (!headMap.isEmpty()) {
				icons = icons + getIcon(false, Icons.ARMOR.get(), 0.0, headMap.get(Attributes.f_22284_)) + getIcon(false, Icons.ARMOR_TOUGHNESS.get(), 0.0, headMap.get(Attributes.f_22285_)) + getIcon(true, Icons.KNOCKBACK_RESISTANCE.get(), 0.0, headMap.get(Attributes.f_22278_));
			}

			if (!chestMap.isEmpty()) {
				icons = icons + getIcon(false, Icons.ARMOR.get(), 0.0, chestMap.get(Attributes.f_22284_)) + getIcon(false, Icons.ARMOR_TOUGHNESS.get(), 0.0, chestMap.get(Attributes.f_22285_)) + getIcon(true, Icons.KNOCKBACK_RESISTANCE.get(), 0.0, chestMap.get(Attributes.f_22278_));
			}

			if (!legsMap.isEmpty()) {
				icons = icons + getIcon(false, Icons.ARMOR.get(), 0.0, legsMap.get(Attributes.f_22284_)) + getIcon(false, Icons.ARMOR_TOUGHNESS.get(), 0.0, legsMap.get(Attributes.f_22285_)) + getIcon(true, Icons.KNOCKBACK_RESISTANCE.get(), 0.0, legsMap.get(Attributes.f_22278_));
			}

			if (!feetMap.isEmpty()) {
				icons = icons + getIcon(false, Icons.ARMOR.get(), 0.0, feetMap.get(Attributes.f_22284_)) + getIcon(false, Icons.ARMOR_TOUGHNESS.get(), 0.0, feetMap.get(Attributes.f_22285_)) + getIcon(true, Icons.KNOCKBACK_RESISTANCE.get(), 0.0, feetMap.get(Attributes.f_22278_));
			}

			icons = icons + getDurabilityIcon(stack);
			icons = icons + TooltipBuilder.Modules.getLine(stack, Type.ICONS_END);
			list.add(1, TextUtils.component(icons));
		}

		private static String getIcon(boolean percent, String icon, double base, Collection<AttributeModifier> modifier) {
			if (modifier != null && !modifier.isEmpty()) {
				double modAmount = 0.0;
				double modPercent = 0.0;
				Iterator var9 = modifier.iterator();

				while(var9.hasNext()) {
					AttributeModifier attribute = (AttributeModifier)var9.next();
					if (attribute.m_22217_() == Operation.ADDITION) {
						modAmount += attribute.m_22218_();
					} else if (attribute.m_22217_() == Operation.MULTIPLY_BASE) {
						modPercent += attribute.m_22218_();
					}
				}

				modAmount += base + modAmount * modPercent;
				if (modAmount == 0.0) {
					return "";
				} else if (percent) {
					return icon + (modPercent > 0.0 ? "§2" : "") + (new DecimalFormat("##.#")).format(modAmount * 100.0).replace(".0", "") + "% ";
				} else {
					return icon + (modPercent > 0.0 ? "§2" : "") + (new DecimalFormat("##.#")).format(modAmount).replace(".0", "") + " ";
				}
			} else {
				return "";
			}
		}

		private static String getAttackSpeedIcon(Collection<AttributeModifier> modifier) {
			if (modifier != null && !modifier.isEmpty()) {
				double modAmount = 0.0;
				double modPercent = 0.0;
				Iterator var5 = modifier.iterator();

				while(var5.hasNext()) {
					AttributeModifier attribute = (AttributeModifier)var5.next();
					if (attribute.m_22217_() == Operation.ADDITION) {
						modAmount += attribute.m_22218_();
					} else if (attribute.m_22217_() == Operation.MULTIPLY_BASE) {
						modPercent += attribute.m_22218_();
					}
				}

				modAmount += 4.0 + modAmount * modPercent;
				if (modAmount <= 0.6) {
					return Icons.ATTACK_SPEED_VERY_SLOW.get() + " ";
				} else if (modAmount <= 1.0) {
					return Icons.ATTACK_SPEED_SLOW.get() + " ";
				} else if (modAmount <= 2.0) {
					return Icons.ATTACK_SPEED_MEDIUM.get() + " ";
				} else if (modAmount <= 3.0) {
					return Icons.ATTACK_SPEED_FAST.get() + " ";
				} else {
					return Icons.ATTACK_SPEED_VERY_FAST.get() + " ";
				}
			} else {
				return "";
			}
		}

		private static String getDurabilityIcon(ItemStack stack) {
			if (stack.m_41776_() > 0) {
				String var10000 = Icons.DURABILITY.get();
				return var10000 + (stack.m_41776_() - stack.m_41773_()) + "§8/" + stack.m_41776_() + "§f ";
			} else {
				return "";
			}
		}

		private static void putFoodIcons(List<Text> list, ItemStack stack) {
			String icons = "";
			if (((FoodProperties)Objects.requireNonNull(stack.m_41720_().getFoodProperties(stack, Minecraft.getInstance().f_91074_))).m_38744_() > 0) {
				icons = icons + Icons.FOOD.get() + ((FoodProperties)Objects.requireNonNull(stack.m_41720_().getFoodProperties(stack, Minecraft.getInstance().f_91074_))).m_38744_() + " ";
			}

			if (((FoodProperties)Objects.requireNonNull(stack.m_41720_().getFoodProperties(stack, Minecraft.getInstance().f_91074_))).m_38745_() > 0.0F) {
				icons = icons + Icons.FOOD_SATURATION.get() + (int)(((FoodProperties)Objects.requireNonNull(stack.m_41720_().getFoodProperties(stack, Minecraft.getInstance().f_91074_))).m_38745_() * 100.0F) + "% ";
			}

			if (!icons.equals("")) {
				list.add(1, TextUtils.component(icons));
			}

		}
	}

	public static class Knowledge {
		private static final HashMap<Identifier, String> KNOWLEDGE = new HashMap();

		public Knowledge() {
		}

		public static void add(String modID, String itemID, String translationKey) {
			KNOWLEDGE.put(new Identifier(itemID), "knowledge." + modID + "." + translationKey);
		}

		public static boolean contains(ItemStack stack) {
			return KNOWLEDGE.containsKey(ForgeRegistries.ITEMS.getKey(stack.m_41720_()));
		}

		public static String get(ItemStack stack) {
			return (String)KNOWLEDGE.get(ForgeRegistries.ITEMS.getKey(stack.m_41720_()));
		}
	}

	public static class Lore {
		private static final HashMap<Identifier, String> LORE = new HashMap();

		public Lore() {
		}

		public static void add(String modID, String itemID, String translationKey) {
			LORE.put(new Identifier(itemID), "lore." + modID + "." + translationKey);
		}

		public static boolean contains(ItemStack stack) {
			return LORE.containsKey(ForgeRegistries.ITEMS.getKey(stack.m_41720_()));
		}

		public static String get(ItemStack stack) {
			return (String)LORE.get(ForgeRegistries.ITEMS.getKey(stack.m_41720_()));
		}
	}
}
