package dev.sterner.obscureapi.world;

import dev.sterner.obscureapi.api.animations.Animation;
import dev.sterner.obscureapi.api.animations.IAnimatedEntity;
import dev.sterner.obscureapi.utils.EventUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObscureAPIEvents {
	public ObscureAPIEvents() {
	}

	public static void addEntityAttributes(EntityEntityAttributeModificationEvent event) {
		Iterator var1 = event.getTypes().iterator();

		while(var1.hasNext()) {
			EntityType<? extends LivingEntity> type = (EntityType)var1.next();
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.CRITICAL_HIT.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.CRITICAL_DAMAGE.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.DODGE.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.PARRY.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.ACCURACY.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.MAGIC_DAMAGE.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.MAGIC_RESISTANCE.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.PENETRATION.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.REGENERATION.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.HEALING_POWER.get());
			event.add(type, (EntityAttribute)ObscureAPIEntityAttributes.RESILIENCE.get());
		}

	}

	public static void animationHandler(LivingEvent.LivingTickEvent event) {
		LivingEntity var2 = event.getEntity();
		if (var2 instanceof IAnimatedEntity provider) {
			List<Animation> list = new ArrayList(provider.getAnimationProvider().getAnimations());
			Iterator var3 = list.iterator();

			while(var3.hasNext()) {
				Animation animation = (Animation)var3.next();
				animation.tick(provider.getAnimationProvider());
			}
		}

	}

	public static void playerTickEvent(@NotNull TickEvent.@NotNull PlayerTickEvent event) {
		PlayerEntity player = event.player;
		if (player != null && !player.m_9236_().m_5776_() && event.phase != Phase.START) {
			float heal = ObscureAPIEntityAttributes.getRegeneration(player);
			if (!(heal <= 0.0F)) {
				int regen = player.getPersistentData().m_128451_("HPRegen");
				if (regen > 400) {
					player.getPersistentData().putInt("HPRegen", 0);
					player.m_5634_(heal);
				} else {
					player.getPersistentData().putInt("HPRegen", regen + 1);
				}

			}
		}
	}

	public static void livingHealEvent(@NotNull LivingHealEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity != null) {
			float amount = event.getAmount();
			float modifier = ObscureAPIEntityAttributes.getHealingPower(entity);
			event.setAmount(amount * modifier);
		}
	}

	public static void livingAttackEvent(@NotNull LivingAttackEvent event) {
		LivingEntity entity = event.getEntity();
		Entity var4 = event.getSource().m_7639_();
		LivingEntity var10000;
		if (var4 instanceof LivingEntity living) {
			var10000 = living;
		} else {
			var10000 = null;
		}

		LivingEntity source = var10000;
		float amount = event.getAmount();
		if (entity != null && source != null && entity.timeUntilRegen <= 10) {
			float accuracy = ObscureAPIEntityAttributes.getAccuracy(source);
			float parry = ObscureAPIEntityAttributes.getParry(entity);
			if (Math.random() <= (double)(parry - accuracy)) {
				event.setCanceled(true);
				entity.timeUntilRegen = 20;
				source.damage(DamageSource.mob(entity), amount);
			} else {
				float dodge = ObscureAPIEntityAttributes.getDodge(entity);
				if (Math.random() <= (double)(dodge - accuracy)) {
					event.setCanceled(true);
					entity.timeUntilRegen = 20;
				}

			}
		}
	}

	public static void livingHurtEvent(@NotNull LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();
		Entity var4 = event.getSource().m_7639_();
		LivingEntity var10000;
		if (var4 instanceof LivingEntity living) {
			var10000 = living;
		} else {
			var10000 = null;
		}

		LivingEntity source = var10000;
		float amount = event.getAmount();
		if (entity != null && source != null) {
			ItemStack weapon = source.m_21205_();
			if (!event.getSource().m_19387_()) {
				double criticalHit = (double)ObscureAPIEntityAttributes.getCriticalHit(source);
				double criticalDamage = (double)ObscureAPIEntityAttributes.getCriticalDamage(source);
				double resilience = (double)ObscureAPIEntityAttributes.getResilience(entity);
				if (Math.random() <= criticalHit - resilience) {
					float criticalAmount = amount * (float)criticalDamage * (float)(1.0 - resilience);
					EventUtils.Triggers.criticalHit(source, entity, weapon, criticalAmount);
					event.setAmount(criticalAmount);
					System.out.println("Chance:" + criticalHit + " / Resilience:" + resilience + " / Amount: " + amount * (float)criticalDamage * (float)(1.0 - resilience));
				}
			}

			if (event.getSource().m_19387_()) {
				event.setAmount(amount * (1.0F - ObscureAPIEntityAttributes.getMagicResistance(entity)));
			}

			EventUtils.Triggers.hit(source, entity, weapon, event.getAmount());
			EventUtils.Triggers.hurt(source, entity, entity.m_21205_(), event.getAmount());
		}
	}

	public static void livingDamageEvent(@NotNull LivingDamageEvent event) {
		LivingEntity entity = event.getEntity();
		Entity var4 = event.getSource().m_7639_();
		LivingEntity var10000;
		if (var4 instanceof LivingEntity living) {
			var10000 = living;
		} else {
			var10000 = null;
		}

		LivingEntity source = var10000;
		if (entity != null && source != null) {
			if (!entity.isInvulnerableTo(DamageSource.MAGIC)) {
				double magicDamage = (double)(ObscureAPIEntityAttributes.getMagicDamage(source) * (1.0F - ObscureAPIEntityAttributes.getMagicResistance(entity)));
				if (magicDamage > 0.0) {
					event.setAmount(event.getAmount() + (float)magicDamage);
				}
			}

		}
	}

	public static void livingDeathEvent(@NotNull LivingDeathEvent event) {
		LivingEntity entity = event.getEntity();
		Entity var4 = event.getSource().m_7639_();
		LivingEntity var10000;
		if (var4 instanceof LivingEntity living) {
			var10000 = living;
		} else {
			var10000 = null;
		}

		LivingEntity source = var10000;
		if (entity != null && source != null) {
			EventUtils.Triggers.kill(source, entity, source.getMainHandStack());
			EventUtils.Triggers.death(source, entity, entity.getMainHandStack());
		}
	}
}
