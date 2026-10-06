package com.emeraldtools.entity;

import com.emeraldtools.ModEntities;
import com.emeraldtools.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// An arrow that teleports whoever shot it to wherever it lands, like a thrown ender pearl.
// The arrow is used up on landing, so it can't be picked up again.
public class EnderArrow extends AbstractArrow {
	// Same as the damage a thrown ender pearl deals on landing.
	public static final float TELEPORT_DAMAGE = 5.0F;

	public EnderArrow(EntityType<? extends EnderArrow> type, Level level) {
		super(type, level);
	}

	public EnderArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(ModEntities.ENDER_ARROW, owner, level, pickupItemStack, firedFromWeapon);
	}

	public EnderArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(ModEntities.ENDER_ARROW, x, y, z, level, pickupItemStack, firedFromWeapon);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide() && !this.isInGround()) {
			this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		this.land();
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		this.land();
	}

	private void land() {
		if (!(this.level() instanceof ServerLevel level)) {
			return;
		}

		level.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 32, 0.0, 1.0, 0.0, 1.0);
		Entity owner = this.getOwner();
		if (owner != null && canTeleport(owner, level)) {
			teleport(owner, level, this.position());
		}
		this.discard();
	}

	// Unlike an ender pearl, the arrow never pulls its shooter across dimensions.
	private static boolean canTeleport(Entity owner, ServerLevel level) {
		if (owner.level().dimension() != level.dimension()) {
			return false;
		}
		return owner instanceof LivingEntity living ? living.isAlive() && !living.isSleeping() : owner.isAlive();
	}

	private static void teleport(Entity owner, ServerLevel level, Vec3 pos) {
		if (owner instanceof ServerPlayer player) {
			if (!player.connection.isAcceptingMessages()) {
				return;
			}
			ServerPlayer moved = player.teleport(
				new TeleportTransition(level, pos, Vec3.ZERO, 0.0F, 0.0F, Relative.union(Relative.ROTATION, Relative.DELTA), TeleportTransition.DO_NOTHING)
			);
			if (moved != null) {
				moved.resetFallDistance();
				moved.resetCurrentImpulseContext();
				moved.hurtServer(level, level.damageSources().enderPearl(), TELEPORT_DAMAGE);
			}
		} else {
			Entity moved = owner.teleport(
				new TeleportTransition(level, pos, owner.getDeltaMovement(), owner.getYRot(), owner.getXRot(), TeleportTransition.DO_NOTHING)
			);
			if (moved != null) {
				moved.resetFallDistance();
			}
			if (moved instanceof LivingEntity living) {
				living.resetCurrentImpulseContext();
			}
		}
		level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(ModItems.ENDER_ARROW);
	}
}
