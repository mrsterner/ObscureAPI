package dev.sterner.obscureapi.api;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.function.Predicate;

@ClientOnly
public class DynamicSound extends MovingSoundInstance {
	private final Entity ENTITY;
	private final Predicate<Entity> CONDITION;

	public DynamicSound(Entity entity, SoundEvent sound, SoundCategory source, float volume, float pitch) {
		this(entity, sound, source, volume, pitch, (e) -> true, false);
	}

	public DynamicSound(Entity entity, SoundEvent sound, SoundCategory source, float volume, float pitch, Predicate<Entity> condition, boolean repeat) {
		super(sound, source, SoundInstance.createRandom());
		this.ENTITY = entity;
		this.CONDITION = condition;
		this.repeat = repeat;
		this.repeatDelay = 0;
		this.volume = volume;
		this.pitch = pitch;
		this.x = ((float)entity.getX());
		this.y = ((float)entity.getY());
		this.z = ((float)entity.getZ());
	}

	@Override
	public boolean canPlay() {
		return !this.ENTITY.isSilent() && this.CONDITION.test(this.ENTITY);
	}

	@Override
	public boolean shouldAlwaysPlay() {
		return true;
	}

	public Entity getEntity() {
		return this.ENTITY;
	}

	@Override
	public void tick() {
		if (this.ENTITY.isRemoved()) {
			this.setDone();
		} else {
			this.x = (double)((float)this.ENTITY.getX());
			this.y = (double)((float)this.ENTITY.getY());
			this.z = (double)((float)this.ENTITY.getZ());
		}

	}
}
