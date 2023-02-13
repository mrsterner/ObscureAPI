package dev.sterner.obscureapi.api.animations;

public class AnimationProvider {
	private final Entity entity;
	private final List<Animation> animations = new ArrayList();
	private final HashMap<String, Float> modifiers = new HashMap();
	public float currentTime = 0.0F;
	public float deltaTime = 0.0F;

	public AnimationProvider(Entity entity) {
		this.entity = entity;
	}

	public List<Animation> getAnimations() {
		return this.animations;
	}

	public HashMap<String, Float> getModifiers() {
		return this.modifiers;
	}

	public float getModifier(String name) {
		return this.modifiers.containsKey(name) ? (Float)this.modifiers.get(name) : 0.0F;
	}

	public void play(String animation, int ticks) {
		if (!this.entity.f_19853_.f_46443_) {
			this.prepare(animation);
			this.animations.add(new Animation(animation, ticks));
			this.sendPacket(this.entity);
		}
	}

	public void stop(Entity entity, String animation) {
		if (!entity.f_19853_.f_46443_) {
			List<Animation> list = new ArrayList(this.animations);
			Iterator var4 = list.iterator();

			while(var4.hasNext()) {
				Animation anim = (Animation)var4.next();
				if (anim.name.equals(animation)) {
					this.animations.remove(anim);
				}
			}

			this.sendPacket(entity);
		}
	}

	public boolean isPlaying(String animation) {
		Iterator var2 = this.animations.iterator();

		Animation anim;
		do {
			if (!var2.hasNext()) {
				return false;
			}

			anim = (Animation)var2.next();
		} while(!anim.name.equals(animation));

		return true;
	}

	public int getTick(String animation) {
		Iterator var2 = this.animations.iterator();

		Animation anim;
		do {
			if (!var2.hasNext()) {
				return 0;
			}

			anim = (Animation)var2.next();
		} while(!anim.name.equals(animation));

		return anim.ticks;
	}

	public void playSound(String animation, int tick, String sound, SoundSource source, float volume, float pitch) {
		if (this.entity.m_9236_().m_5776_() && this.isPlaying(animation) && this.getTick(animation) == tick - 1) {
			this.entity.m_9236_().m_7785_(this.entity.m_20185_(), this.entity.m_20186_(), this.entity.m_20189_(), new SoundEvent(new Identifier(sound), 16.0F), source, volume, pitch, false);
		}

	}

	private void prepare(String animation) {
		List<Animation> list = new ArrayList(this.animations);
		Iterator var3 = list.iterator();

		while(var3.hasNext()) {
			Animation anim = (Animation)var3.next();
			if (anim.name.equals(animation)) {
				this.animations.remove(anim);
			}
		}

	}

	private void sendPacket(Entity entity) {
		ObscureAPI.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new AnimationMessage(this.packAnimations(), entity.m_19879_()));
	}

	private CompoundTag packAnimations() {
		CompoundTag packet = new CompoundTag();
		Iterator var2 = this.animations.iterator();

		while(var2.hasNext()) {
			Animation animation = (Animation)var2.next();
			packet.m_128405_(animation.name, animation.ticks);
		}

		return packet;
	}
}
