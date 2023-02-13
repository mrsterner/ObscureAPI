package dev.sterner.obscureapi.client;

public abstract class ScalableMobRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {
	public ScalableMobRenderer(EntityRendererProvider.Context context, M model, float f) {
		super(context, model, f);
	}

	public void m_7392_(@NotNull T entity, float arg1, float arg2, @NotNull MatrixStack pose, @NotNull MultiBufferSource buffer, int arg3) {
		if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(entity, this, arg2, pose, buffer, arg3))) {
			pose.push();
			this.f_115290_.f_102608_ = this.m_115342_(entity, arg2);
			boolean shouldSit = entity.m_20159_() && entity.m_20202_() != null && entity.m_20202_().shouldRiderSit();
			this.f_115290_.f_102609_ = shouldSit;
			this.f_115290_.f_102610_ = entity.m_6162_();
			float f = Mth.m_14189_(arg2, entity.f_20884_, entity.f_20883_);
			float f1 = Mth.m_14189_(arg2, entity.f_20886_, entity.f_20885_);
			float f2 = f1 - f;
			float f7;
			if (shouldSit) {
				Entity var12 = entity.m_20202_();
				if (var12 instanceof LivingEntity) {
					LivingEntity livingentity = (LivingEntity)var12;
					f = Mth.m_14189_(arg2, livingentity.f_20884_, livingentity.f_20883_);
					f2 = f1 - f;
					f7 = Mth.m_14177_(f2);
					if (f7 < -85.0F) {
						f7 = -85.0F;
					}

					if (f7 >= 85.0F) {
						f7 = 85.0F;
					}

					f = f1 - f7;
					if (f7 * f7 > 2500.0F) {
						f += f7 * 0.2F;
					}

					f2 = f1 - f;
				}
			}

			float f6 = Mth.lerp(arg2, entity.f_19860_, entity.m_146909_());
			if (m_194453_(entity)) {
				f6 *= -1.0F;
				f2 *= -1.0F;
			}

			float f4;
			if (entity.m_20089_() == Pose.SLEEPING) {
				Direction direction = entity.m_21259_();
				if (direction != null) {
					f4 = entity.m_20236_(Pose.STANDING) - 0.1F;
					pose.translate((double)((float)(-direction.m_122429_()) * f4), 0.0, (double)((float)(-direction.m_122431_()) * f4));
				}
			}

			f7 = this.m_6930_(entity, arg2);
			f4 = mod.scale(entity);
			this.m_7523_(entity, pose, f7, f, arg2);
			pose.scale(-f4, -f4, f4);
			this.m_7546_(entity, pose, arg2);
			pose.translate(0.0, -1.5010000467300415, 0.0);
			float f8 = 0.0F;
			float f5 = 0.0F;
			if (!shouldSit && entity.m_6084_()) {
				f8 = Mth.lerp(arg2, entity.f_20923_, entity.f_20924_);
				f5 = entity.f_20925_ - entity.f_20924_ * (1.0F - arg2);
				if (entity.m_6162_()) {
					f5 *= 3.0F;
				}

				if (f8 > 1.0F) {
					f8 = 1.0F;
				}
			}

			this.f_115290_.m_6839_(entity, f5, f8, arg2);
			this.f_115290_.m_6973_(entity, f5, f8, f7, f2, f6);
			Minecraft minecraft = Minecraft.getInstance();
			boolean flag = this.m_5933_(entity);
			boolean flag1 = !flag && minecraft.f_91074_ != null && !entity.m_20177_(minecraft.f_91074_);
			boolean flag2 = minecraft.m_91314_(entity);
			RenderType rendertype = this.m_7225_(entity, flag, flag1, flag2);
			if (rendertype != null) {
				VertexConsumer vertexconsumer = buffer.m_6299_(rendertype);
				int i = m_115338_(entity, this.m_6931_(entity, arg2));
				this.f_115290_.m_7695_(pose, vertexconsumer, arg3, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
			}

			if (!entity.m_5833_()) {
				Iterator var26 = this.f_115291_.iterator();

				while(var26.hasNext()) {
					RenderLayer<T, M> renderlayer = (RenderLayer)var26.next();
					renderlayer.m_6494_(pose, buffer, arg3, entity, f5, f8, arg2, f7, f2, f6);
				}
			}

			pose.pop();
			this.finalRender(entity, arg1, arg2, pose, buffer, arg3);
			MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(entity, this, arg2, pose, buffer, arg3));
		}
	}

	public void finalRender(T entity, float arg1, float arg2, MatrixStack pose, MultiBufferSource buffer, int arg3) {
		RenderNameTagEvent renderNameTagEvent = new RenderNameTagEvent(entity, entity.m_5446_(), this, pose, buffer, arg3, arg2);
		MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
		if (renderNameTagEvent.getResult() != Result.DENY && (renderNameTagEvent.getResult() == Result.ALLOW || this.m_6512_(entity))) {
			this.m_7649_(entity, renderNameTagEvent.getContent(), pose, buffer, arg3);
		}

	}
}
