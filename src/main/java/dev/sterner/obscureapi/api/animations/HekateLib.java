package dev.sterner.obscureapi.api.animations;


public class HekateLib {
	public HekateLib() {
	}

	public static void updateScale(Entity entity, float scale, float speed) {
		float scaleIn = entity.getPersistentData().m_128457_("HScale");
		entity.getPersistentData().m_128350_("HScaleLerp", scaleIn);
		scaleIn = scale != scaleIn ? Mth.lerp(speed, scaleIn, scale) : scaleIn;
		entity.getPersistentData().m_128350_("HScale", scaleIn);
	}

	public static void i(@NotNull ModelPart part, float xSwing, float x, float ySwing, float y, float zSwing, float z, float speed, float offset, float timer) {
		part.f_104203_ += HekateLib.render.idle(xSwing, x * -1.0F, speed, offset, timer);
		part.f_104204_ += HekateLib.render.idle(ySwing, y * -1.0F, speed, offset, timer);
		part.f_104205_ += HekateLib.render.idle(zSwing, z, speed, offset, timer);
	}

	public static void i(@NotNull ModelPart part, float xSwing, float x, float ySwing, float y, float zSwing, float z, float speed, float offset, float timer, float mod) {
		part.f_104203_ += HekateLib.render.idle(xSwing, x * -1.0F, speed, offset, timer, mod);
		part.f_104204_ += HekateLib.render.idle(ySwing, y * -1.0F, speed, offset, timer, mod);
		part.f_104205_ += HekateLib.render.idle(zSwing, z, speed, offset, timer, mod);
	}

	public static void i(@NotNull ModelPart part, float xSwing, float x, float ySwing, float y, float zSwing, float z, float speed, float offset, float timer, float mod1, float mod2) {
		part.f_104203_ += HekateLib.render.idle(xSwing, x * -1.0F, speed, offset, timer, mod1, mod2);
		part.f_104204_ += HekateLib.render.idle(ySwing, y * -1.0F, speed, offset, timer, mod1, mod2);
		part.f_104205_ += HekateLib.render.idle(zSwing, z, speed, offset, timer, mod1, mod2);
	}

	public static void m(@NotNull ModelPart part, float xSwing, float x, float ySwing, float y, float zSwing, float z, float speed, float offset, float timer, float mod) {
		part.f_104203_ += HekateLib.render.move(xSwing, x * -1.0F, speed, offset, timer, mod);
		part.f_104204_ += HekateLib.render.move(ySwing, y * -1.0F, speed, offset, timer, mod);
		part.f_104205_ += HekateLib.render.move(zSwing, z, speed, offset, timer, mod);
	}

	public static void m(@NotNull ModelPart part, float xSwing, float x, float ySwing, float y, float zSwing, float z, float speed, float offset, float timer, float mod1, float mod2) {
		part.f_104203_ += HekateLib.render.move(xSwing, x * -1.0F, speed, offset, timer, mod1, mod2);
		part.f_104204_ += HekateLib.render.move(ySwing, y * -1.0F, speed, offset, timer, mod1, mod2);
		part.f_104205_ += HekateLib.render.move(zSwing, z, speed, offset, timer, mod1, mod2);
	}

	public static void k(@NotNull ModelPart part, float x, float y, float z, float mod) {
		part.f_104203_ += HekateLib.render.keyFrame(x * -1.0F, mod);
		part.f_104204_ += HekateLib.render.keyFrame(y * -1.0F, mod);
		part.f_104205_ += HekateLib.render.keyFrame(z, mod);
	}

	public static void k(@NotNull ModelPart part, float x, float y, float z, float mod1, float mod2) {
		part.f_104203_ += HekateLib.render.keyFrame(x * -1.0F, mod1, mod2);
		part.f_104204_ += HekateLib.render.keyFrame(y * -1.0F, mod1, mod2);
		part.f_104205_ += HekateLib.render.keyFrame(z, mod1, mod2);
	}

	public static void r(@NotNull ModelPart part, float mod) {
		mod = 1.0F - mod;
		part.f_104203_ *= mod;
		part.f_104204_ *= mod;
		part.f_104205_ *= mod;
	}

	public static float translate(float value, float target, float speed) {
		return value + (target - value) * speed;
	}

	public static float toRadians(float value) {
		return value * 0.017453292F;
	}

	public static class render {
		public render() {
		}

		public static void tick(Entity entity) {
			if (entity instanceof IAnimatedEntity iProvider) {
				AnimationProvider provider = iProvider.getAnimationProvider();
				float lastTime = provider.currentTime;
				provider.currentTime = (float)Util.m_137569_() / 1.0E9F;
				provider.deltaTime = provider.currentTime - lastTime;
			}

		}

		public static void prepare(ModelPart... parts) {
			ModelPart[] var1 = parts;
			int var2 = parts.length;

			for(int var3 = 0; var3 < var2; ++var3) {
				ModelPart part = var1[var3];
				part.m_171327_(0.0F, 0.0F, 0.0F);
			}

		}

		public static void animation(Entity entity, String animation, float timer, KeyFrame... frames) {
			if (entity instanceof IAnimatedEntity iProvider) {
				AnimationProvider provider = iProvider.getAnimationProvider();
				int tick = provider.getTick(animation);
				int i = 0;
				KeyFrame[] var8 = frames;
				int var9 = frames.length;

				for(int var10 = 0; var10 < var9; ++var10) {
					KeyFrame frame = var8[var10];
					++i;
					float mod = provider.getModifier(animation + i);
					boolean active = tick > 0 && tick > frame.TICK_END && tick <= frame.TICK_START;
					if (!Minecraft.getInstance().m_91104_()) {
						mod += active ? (1.0F - mod) * Math.min(1.0F, frame.SPEED_IN * provider.deltaTime) : (0.0F - mod) * Math.min(1.0F, frame.SPEED_OUT * provider.deltaTime);
					}

					provider.getModifiers().put(animation + i, mod);
					Iterator var14 = frame.PARTS.iterator();

					while(var14.hasNext()) {
						AnimatedPart part = (AnimatedPart)var14.next();
						HekateLib.r(part.PART, mod);
						if (part.SWING) {
							HekateLib.i(part.PART, part.X, part.XBase, part.Y, part.YBase, part.Z, part.ZBase, part.SPEED, part.OFFSET, timer, mod);
						} else {
							HekateLib.k(part.PART, part.X, part.Y, part.Z, mod);
						}
					}
				}
			}

		}

		public static float idle(float swingAngle, float baseAngle, float speed, float offset, float timer) {
			if (swingAngle == 0.0F && baseAngle == 0.0F) {
				return 0.0F;
			} else {
				swingAngle *= 0.017453292F;
				baseAngle *= 0.017453292F;
				return swingAngle * Mth.m_14089_(timer * speed + offset * 6.283F) + baseAngle;
			}
		}

		public static float idle(float swingAngle, float baseAngle, float speed, float offset, float timer, float mod1) {
			if (swingAngle == 0.0F && baseAngle == 0.0F) {
				return 0.0F;
			} else {
				return mod1 <= 0.0F ? 0.0F : idle(swingAngle, baseAngle, speed, offset, timer) * mod1;
			}
		}

		public static float idle(float swingAngle, float baseAngle, float speed, float offset, float timer, float mod1, float mod2) {
			if (swingAngle == 0.0F && baseAngle == 0.0F) {
				return 0.0F;
			} else {
				mod2 = 1.0F - mod2;
				return mod2 <= 0.0F ? 0.0F : idle(swingAngle, baseAngle, speed, offset, timer, mod1) * mod2;
			}
		}

		public static float move(float swingAngle, float baseAngle, float speed, float offset, float timer, float mod1) {
			if (swingAngle == 0.0F && baseAngle == 0.0F) {
				return 0.0F;
			} else {
				swingAngle *= 0.017453292F;
				baseAngle *= 0.017453292F;
				return (mod1 * swingAngle * Mth.m_14089_(timer * speed + offset * 6.283F) + baseAngle) * mod1;
			}
		}

		public static float move(float swingAngle, float baseAngle, float speed, float offset, float timer, float mod1, float mod2) {
			if (swingAngle == 0.0F && baseAngle == 0.0F) {
				return 0.0F;
			} else {
				mod2 = 1.0F - mod2;
				return mod2 <= 0.0F ? 0.0F : move(swingAngle, baseAngle, speed, offset, timer, mod1) * mod2;
			}
		}

		public static float head(float yaw, float mod) {
			return yaw / 57.295776F * mod;
		}

		public static float spin(float timer, float speed, float mod) {
			return timer * speed % 6.282F * mod;
		}

		public static float keyFrame(float angle, float mod) {
			if (angle == 0.0F) {
				return 0.0F;
			} else {
				angle *= 0.017453292F;
				return angle * mod;
			}
		}

		public static float keyFrame(float angle, float mod1, float mod2) {
			if (angle == 0.0F) {
				return 0.0F;
			} else {
				angle *= 0.017453292F;
				mod2 = Math.max(mod2, 0.0F);
				mod2 = Math.min(mod2, 1.0F);
				return angle * mod1 * mod2;
			}
		}
	}

	public static class data {
		public data() {
		}

		public static boolean isAnimationPlaying(Entity entity, String animation) {
			boolean var10000;
			if (entity instanceof IAnimatedEntity provider) {
				if (provider.getAnimationProvider().isPlaying(animation)) {
					var10000 = true;
					return var10000;
				}
			}

			var10000 = false;
			return var10000;
		}

		public static int getAnimationTick(Entity entity, String animation) {
			int var10000;
			if (entity instanceof IAnimatedEntity provider) {
				var10000 = provider.getAnimationProvider().getTick(animation);
			} else {
				var10000 = 0;
			}

			return var10000;
		}
	}

	public static class effect {
		public effect() {
		}

		public static float cyclicPause(float timer, float speed, float offset) {
			return 0.5F + Mth.m_14089_(timer * speed + offset * 6.283F) * 0.5F;
		}
	}

	public static class mod {
		public mod() {
		}

		public static float idle(float limbSwingAmount, float mod) {
			return Math.max(1.0F - limbSwingAmount * mod, 0.0F);
		}

		public static float move(float limbSwingAmount, float mod) {
			return Math.min(limbSwingAmount * mod, 1.0F);
		}

		public static float get(Entity entity, String animation, float speedIn, float speedOut) {
			if (entity instanceof IAnimatedEntity iProvider) {
				AnimationProvider provider = iProvider.getAnimationProvider();
				int tick = provider.getTick(animation);
				float mod = provider.getModifier(animation);
				if (!Minecraft.getInstance().m_91104_()) {
					mod += tick > 0 ? (1.0F - mod) * Math.min(1.0F, speedIn * provider.deltaTime) : (0.0F - mod) * Math.min(1.0F, speedOut * provider.deltaTime);
				}

				provider.getModifiers().put(animation, mod);
				return mod;
			} else {
				return 0.0F;
			}
		}

		public static float scale(Entity entity) {
			float scale = entity.getPersistentData().m_128457_("HScale");
			float scaleLerp = entity.getPersistentData().m_128457_("HScaleLerp");
			return Mth.lerp(Minecraft.getInstance().getTickDelta(), scaleLerp, scale);
		}
	}

	public static class network {
		public network() {
		}

		public static boolean readPacket(int id, CompoundTag animations) {
			assert Minecraft.getInstance().f_91074_ != null;

			Entity entity = Minecraft.getInstance().f_91074_.f_19853_.m_6815_(id);
			if (entity instanceof IAnimatedEntity) {
				IAnimatedEntity provider = (IAnimatedEntity)entity;
				provider.getAnimationProvider().getAnimations().clear();
				Iterator var4 = animations.m_128431_().iterator();

				while(var4.hasNext()) {
					String animation = (String)var4.next();
					provider.getAnimationProvider().getAnimations().add(new Animation(animation, animations.m_128451_(animation)));
				}
			}

			return true;
		}
	}
}
