package com.emeraldtools.item;

import com.emeraldtools.entity.EnderArrow;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class EnderArrowItem extends ArrowItem {
	public EnderArrowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity owner, @Nullable ItemStack firedFromWeapon) {
		return new EnderArrow(level, owner, itemStack.copyWithCount(1), firedFromWeapon);
	}

	// Fired from a dispenser: there's no shooter to teleport, so it just lands and breaks.
	@Override
	public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
		EnderArrow arrow = new EnderArrow(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), null);
		arrow.pickup = AbstractArrow.Pickup.ALLOWED;
		return arrow;
	}
}
