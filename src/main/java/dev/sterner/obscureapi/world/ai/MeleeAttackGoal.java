package dev.sterner.obscureapi.world.ai;

import dev.sterner.obscureapi.world.ai.attack.Attack;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;

import java.util.EnumSet;
import java.util.List;

public class MeleeAttackGoal extends Goal {
	protected final PathAwareEntity mob;
	protected final List<Attack> attacks;
	private final double speedModifier;
	private final boolean followingTargetEvenIfNotSeen;
	private Path path;
	private int update;
	private long lastCanUseCheck;
	public int attackTick = 0;

	public MeleeAttackGoal(PathAwareEntity mob, double speed, boolean alwaysSees, Attack... attacks) {
		this.mob = mob;
		this.attacks = List.of(attacks);
		this.speedModifier = speed;
		this.followingTargetEvenIfNotSeen = alwaysSees;
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
	}

	public boolean canStart() {
		long i = this.mob.world.getTime();
		if (i - this.lastCanUseCheck < 20L) {
			return false;
		} else {
			this.lastCanUseCheck = i;
			LivingEntity target = this.mob.getTarget();
			if (target != null && target.isAlive()) {
				this.path = this.mob.getNavigation().findPathTo(target, 0);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean shouldContinue() {
		LivingEntity target = this.mob.getTarget();
		if (target != null && target.isAlive()) {
			return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity)target).isCreative();
		} else {
			return false;
		}
	}

	public void start() {
		this.mob.getNavigation().startMovingAlong(this.path, this.speedModifier);
		this.mob.setAttacking(true);
		this.update = 0;
	}

	public void stop() {
		LivingEntity target = this.mob.getTarget();
		if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
			this.mob.setTarget((LivingEntity)null);
		}

		this.mob.setAttacking(false);
		this.mob.getNavigation().stop();
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public void tick() {
		LivingEntity target = this.mob.getTarget();
		if (target != null) {
			this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);
			double distanceToSqr = this.mob.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
			this.update = Math.max(this.update - 1, 0);
			this.attackTick = Math.max(this.attackTick - 1, 0);
			if ((this.followingTargetEvenIfNotSeen || this.mob.getVisibilityCache().canSee(target)) && this.update <= 0) {
				this.update = 4 + this.mob.getRandom().nextInt(7);
				if (distanceToSqr > 1024.0) {
					this.update += 10;
				} else if (distanceToSqr > 256.0) {
					this.update += 5;
				}

				if (!this.mob.getNavigation().startMovingTo(target, this.speedModifier)) {
					this.update += 15;
				}

				this.update = this.getTickCount(this.update);
			}

			double distance = this.mob.getLerpedPos(1.0F).distanceTo(target.getLerpedPos(1.0F));
			this.attacks.forEach((attack) -> {
				if (this.attackTick == 0 && attack.use(this.mob, target, distance)) {
					this.attackTick = attack.TICKS + 1;
				}

			});
			this.attacks.forEach((attack) -> {
				attack.tick(this.mob, target, distance);
			});
		}

	}
}
