package dev.sterner.obscureapi.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LivingEntity.class, priority = 1287)
public abstract class LivingEntityMixin extends Entity  {
	public LivingEntityMixin(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@Shadow
	public abstract void damageArmor(DamageSource source, float amount);

	@Shadow
	public abstract int getArmor();

	@Shadow
	public abstract double getAttributeValue(EntityAttribute entityAttribute);



	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	protected float applyArmorToDamage(DamageSource source, float amount) {
		if (source.bypassesArmor()) {
			return amount;
		} else {
			Entity var5 = source.getAttacker();
			float var10000;
			if (var5 instanceof LivingEntity living) {
				var10000 = ObscureAPIAttributes.getPenetration(living);
			} else {
				var10000 = 0.0F;
			}

			float penetration = var10000;
			this.damageArmor(source, amount);
			amount = CombatRules.m_19272_(amount, (float)this.getArmor() * (1.0F - penetration), (float)this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS) * (1.0F - penetration));
			return amount;
		}
	}
}
