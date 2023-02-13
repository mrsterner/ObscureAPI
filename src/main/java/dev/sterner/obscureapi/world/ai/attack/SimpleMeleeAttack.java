package dev.sterner.obscureapi.world.ai.attack;

import dev.sterner.obscureapi.api.animations.AnimationProvider;
import dev.sterner.obscureapi.api.animations.IAnimatedEntity;
import net.minecraft.entity.LivingEntity;

public class SimpleMeleeAttack extends Attack {
	public final double START_DISTANCE;
	public final double HURT_DISTANCE;

	public SimpleMeleeAttack(String name, int ticks, int hurtTick, int reload, int reloadRandom, double startDistance, double hurtDistance) {
		super(name, ticks, hurtTick, reload, reloadRandom);
		this.START_DISTANCE = startDistance;
		this.HURT_DISTANCE = hurtDistance;
	}

	public boolean use(LivingEntity entity, LivingEntity target, double distance) {
		if (entity instanceof IAnimatedEntity iAnimatedEntity) {
			AnimationProvider provider = iAnimatedEntity.getAnimationProvider();
			if (this.reload == 0 && !provider.isPlaying(this.NAME) && distance < this.START_DISTANCE) {
				provider.play(this.NAME, this.TICKS);
				this.reload = this.RELOAD + entity.getRandom().nextInt(this.RELOAD_RANDOM);
				return true;
			}
		}

		return false;
	}

	public void tick(LivingEntity entity, LivingEntity target, double distance) {
		super.tick(entity, target, distance);
		if (entity instanceof IAnimatedEntity iAnimatedEntity) {
			AnimationProvider provider = iAnimatedEntity.getAnimationProvider();
			if (provider.getTick(this.NAME) == this.HURT_TICK && distance <= this.HURT_DISTANCE) {
				this.doHurt(target, entity);
			}
		}

	}

	public void doHurt(LivingEntity target, LivingEntity entity) {
		entity.tryAttack(target);
	}
}
