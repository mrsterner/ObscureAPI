package dev.sterner.obscureapi.api;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DynamicProjectile extends LivingEntity {
	private static final UUID BLANK_UUID = UUID.fromString("0-0-0-0-0");
	protected static final TrackedData<Float> SCALE;
	protected static final TrackedData<Float> SCALE_SPEED;
	private static final TrackedData<Optional<UUID>> OWNER_UUID;
	private static final TrackedData<Integer> OWNER_ID;
	private static final TrackedData<ItemStack> RELATED_STACK;
	private static final TrackedData<Boolean> UNRELATED;
	private static final TrackedData<Integer> SPIN;
	private static final TrackedData<Float> OFFSET;
	private static final TrackedData<Integer> LIFETIME;
	private static final TrackedData<Integer> USES;
	private static final TrackedData<Integer> DAMAGE;
	public Entity OWNER = null;

	public DynamicProjectile(EntityType<? extends DynamicProjectile> type, World world) {
		super(type, world);
		this.setNoGravity(true);
	}

	public Packet<ClientPlayPacketListener> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}


	public static void create(EntityType<? extends DynamicProjectile> type, Entity owner, World world, @Nullable ItemStack stack, int damage, float offset, int lifetime, int maxUses) {
		if (world instanceof ServerWorld server) {
			Entity projectile;
			if (stack != null && stack.getEnchantmentWorld((Enchantment)ObscureAPIEnchantments.MIRROR.get()) > 0) {
				projectile = type.create(server);
				Entity projectile2 = type.create(server);
				if (projectile instanceof DynamicProjectile p1) {
					if (projectile2 instanceof DynamicProjectile p2) {
						p1.defineOwner(owner, stack);
						p2.defineOwner(owner, stack);
						p1.getDataTracker().set(OFFSET, offset);
						p2.getDataTracker().set(OFFSET, offset + 0.5F);
						p1.getDataTracker().set(LIFETIME, lifetime);
						p2.getDataTracker().set(LIFETIME, lifetime);
						p1.getDataTracker().set(USES, maxUses);
						p2.getDataTracker().set(USES, maxUses);
						p1.getDataTracker().set(DAMAGE, (int)Math.round((double)damage * 0.5));
						p2.getDataTracker().set(DAMAGE, (int)Math.round((double)damage * 0.5));
						p1.refreshPositionAndAngles(owner.getX(), owner.getY() + (double)(owner.getHeight() / 2.0F), owner.getZ(), 0.0F, 0.0F);
						p2.refreshPositionAndAngles(owner.getX(), owner.getY() + (double)(owner.getHeight() / 2.0F), owner.getZ(), 0.0F, 0.0F);
						world.spawnEntity(p1);
						world.spawnEntity(p2);
					}
				}
			} else {
				projectile = type.create(server);
				if (projectile instanceof DynamicProjectile p) {
					p.defineOwner(owner, stack);
					p.getDataTracker().set(OFFSET, offset);
					p.getDataTracker().set(LIFETIME, lifetime);
					p.getDataTracker().set(USES, maxUses);
					p.getDataTracker().set(DAMAGE, damage);
					p.refreshPositionAndAngles(owner.getX(), owner.getY() + (double)(owner.getHeight() / 2.0F), owner.getZ(), 0.0F, 0.0F);
					world.spawnEntity(p);
				}
			}
		}

	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(SCALE, 1.0F);
		this.getDataTracker().startTracking(SCALE_SPEED, 0.3F);
		this.getDataTracker().startTracking(OWNER_UUID, Optional.of(BLANK_UUID));
		this.getDataTracker().startTracking(OWNER_ID, 0);
		this.getDataTracker().startTracking(RELATED_STACK, ItemStack.EMPTY);
		this.getDataTracker().startTracking(UNRELATED, false);
		this.getDataTracker().startTracking(SPIN, 0);
		this.getDataTracker().startTracking(OFFSET, 0.0F);
		this.getDataTracker().startTracking(LIFETIME, 0);
		this.getDataTracker().startTracking(USES, 0);
		this.getDataTracker().startTracking(DAMAGE, 0);
		super.initDataTracker();
	}

	public void writeCustomDataToNbt(@NotNull NbtCompound tag) {
		NbtCompound data = new NbtCompound();
		data.putFloat("Scale", (Float)this.getDataTracker().get(SCALE));
		data.putFloat("ScaleSpeed", (Float)this.getDataTracker().get(SCALE_SPEED));
		data.putUuid("Owner", (UUID)((Optional)this.getDataTracker().get(OWNER_UUID)).orElse(BLANK_UUID));
		data.putInt("OwnerID", (Integer)this.getDataTracker().get(OWNER_ID));
		data.put("ItemStack", ((ItemStack)this.getDataTracker().get(RELATED_STACK)).writeNbt(new NbtCompound()));
		data.putBoolean("Unrelated", (Boolean)this.getDataTracker().get(UNRELATED));
		data.putInt("Spin", (Integer)this.getDataTracker().get(SPIN));
		data.putFloat("Offset", (Float)this.getDataTracker().get(OFFSET));
		data.putInt("LifeTime", (Integer)this.getDataTracker().get(LIFETIME));
		data.putInt("Uses", (Integer)this.getDataTracker().get(USES));
		data.putInt("Damage", (Integer)this.getDataTracker().get(DAMAGE));
		tag.put("ProjectileData", data);
		super.writeCustomDataToNbt(tag);
	}

	public void readCustomDataFromNbt(@NotNull NbtCompound tag) {
		NbtCompound data = (NbtCompound)tag.get("ProjectileData");
		if (data != null) {
			this.getDataTracker().set(SCALE, data.getFloat("Scale"));
			this.getDataTracker().set(SCALE_SPEED, data.getFloat("ScaleSpeed"));
			this.getDataTracker().set(OWNER_UUID, Optional.of(data.getUuid("Owner")));
			this.getDataTracker().set(OWNER_ID, data.getInt("OwnerID"));
			this.getDataTracker().set(RELATED_STACK, ItemStack.fromNbt(data.getCompound("ItemStack")));
			this.getDataTracker().set(UNRELATED, data.getBoolean("Unrelated"));
			this.getDataTracker().set(SPIN, data.getInt("Spin"));
			this.getDataTracker().set(OFFSET, data.getFloat("Offset"));
			this.getDataTracker().set(LIFETIME, data.getInt("LifeTime"));
			this.getDataTracker().set(USES, data.getInt("Uses"));
			this.getDataTracker().set(DAMAGE, data.getInt("Damage"));
			super.readCustomDataFromNbt(tag);
		}
	}

	public void defineOwner(Entity entity, @Nullable ItemStack stack) {
		if (stack != null) {
			this.getDataTracker().set(RELATED_STACK, stack);
		} else {
			this.getDataTracker().set(UNRELATED, true);
		}

		this.getDataTracker().set(OWNER_UUID, Optional.of(entity.getUuid()));
		this.OWNER = entity;
	}

	@Override
	public void tick() {
		this.OWNER = this.world.getEntityById((Integer)this.getDataTracker().get(OWNER_ID));
		World var2 = this.getWorld();
		if (var2 instanceof ServerWorld server) {
			Entity owner = server.getEntity((UUID)((Optional)this.getDataTracker().get(OWNER_UUID)).orElse(BLANK_UUID));
			if (owner != null) {
				this.getDataTracker().set(OWNER_ID, owner.getId());
				this.OWNER = owner;
			}

			this.getDataTracker().set(SPIN, (Integer)this.getDataTracker().get(SPIN) + 1);
			this.getDataTracker().set(LIFETIME, (Integer)this.getDataTracker().get(LIFETIME) - 1);
			if (this.OWNER == null || !this.OWNER.isAlive() || (Integer)this.getDataTracker().get(USES) <= 0 || (Integer)this.getDataTracker().get(LIFETIME) <= 0) {
				this.discard();
			}

			if (this.OWNER != null) {
				this.updateMotion();
				List<LivingEntity> list = this.world.getEntitiesByClass(LivingEntity.class, (new Box(this.getPos(), this.getPos())).expand((double)this.getAttackRange()), (e) -> {
					return true;
				});
				Iterator var4 = list.iterator();

				while(var4.hasNext()) {
					LivingEntity entities = (LivingEntity)var4.next();
					if (entities != this.OWNER && entities.getPos().distanceTo(this.getPos()) <= (double)this.getAttackRange()) {
						this.attack(entities);
					}
				}
			}
		}

		super.tick();
	}


	protected boolean attack(LivingEntity entity) {
		if (entity.damage(this.OWNER instanceof LivingEntity ? DamageSource.mobProjectile(this, (LivingEntity)this.OWNER) : DamageSource.MAGIC, (float)(Integer)this.getDataTracker().get(DAMAGE))) {
			this.getDataTracker().set(USES, (Integer)this.getDataTracker().get(USES) - 1);
			return true;
		} else {
			return false;
		}
	}

	protected void updateMotion() {
		Vec3d center = this.OWNER.getPos().add(0.0, (double)this.OWNER.getHeight() * 0.33, 0.0);
		float radius = this.getRadius();
		float speed = this.getSpinSpeed();
		float offset = this.getSpinOffset();
		Vec3d orbit = new Vec3d(center.x + Math.cos((double)(speed + offset)) * (double)radius, center.y, center.z + Math.sin((double)(speed + offset)) * (double)radius);
		this.refreshPositionAfterTeleport(orbit);
	}

	protected float getAttackRange() {
		return 2.0F;
	}

	protected float getDefaultRadius() {
		return 4.0F;
	}

	protected float getDefaultSpinSpeed() {
		return 0.06F;
	}

	protected float getSpinSpeed() {
		float speed = this.getDefaultSpinSpeed();
		ItemStack stack = (ItemStack)this.getDataTracker().get(RELATED_STACK);
		return stack.isEmpty() ? (float)(Integer)this.getDataTracker().get(SPIN) * speed : (float)(Integer)this.getDataTracker().get(SPIN) * speed * (1.0F + 0.25F * (float)stack.getEnchantmentWorld((Enchantment)ObscureAPIEnchantments.FAST_SPIN.get()));
	}

	protected float getRadius() {
		float radius = this.getDefaultRadius();
		ItemStack stack = (ItemStack)this.getDataTracker().get(RELATED_STACK);
		return stack.isEmpty() ? radius : radius * (1.0F + 0.2F * (float)stack.getEnchantmentWorld((Enchantment)ObscureAPIEnchantments.DISTANCE.get()));
	}

	protected float getSpinOffset() {
		return 6.315F * (Float)this.getDataTracker().get(OFFSET);
	}

	public boolean damage(@NotNull DamageSource source, float amount) {
		return source != DamageSource.OUT_OF_WORLD ? false : super.damage(source, amount);
	}

	public boolean m_142535_(float l, float d, @NotNull DamageSource source) {
		return false;
	}

	@NotNull
	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@NotNull
	@Override
	public Iterable<ItemStack> getArmorItems() {
		return new ArrayList();
	}

	@NotNull
	public ItemStack getEquippedStack(@NotNull EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
	}

	@Override
	protected void fall(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
	}

	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}

	public boolean isCollidable() {
		return false;
	}

	public boolean isPushable() {
		return false;
	}

	public boolean collides() {
		return false;
	}

	public boolean hasNoGravity() {
		return true;
	}

	public boolean shouldRender(double d0) {
		double d1 = 100.0;
		return d0 < 10000.0;
	}

	static {
		SCALE = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.FLOAT);
		SCALE_SPEED = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.FLOAT);
		OWNER_UUID = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
		OWNER_ID = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.INTEGER);
		RELATED_STACK = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.ITEM_STACK);
		UNRELATED = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.BOOLEAN);
		SPIN = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.INTEGER);
		OFFSET = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.FLOAT);
		LIFETIME = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.INTEGER);
		USES = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.INTEGER);
		DAMAGE = DataTracker.registerData(DynamicProjectile.class, TrackedDataHandlerRegistry.INTEGER);
	}
}
