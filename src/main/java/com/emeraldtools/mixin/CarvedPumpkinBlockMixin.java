package com.emeraldtools.mixin;

import com.emeraldtools.entity.EmeraldGolemSummoning;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Lets a carved pumpkin or jack o'lantern on a T of emerald blocks build an emerald golem.
@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin {
	@Inject(at = @At("HEAD"), method = "trySpawnGolem", cancellable = true)
	private void emeraldTools$trySpawnEmeraldGolem(Level level, BlockPos topPos, CallbackInfo info) {
		if (EmeraldGolemSummoning.trySpawn(level, topPos)) {
			info.cancel();
		}
	}

	// Dispensers only place pumpkins where this returns true.
	@Inject(at = @At("RETURN"), method = "canSpawnGolem", cancellable = true)
	private void emeraldTools$canSpawnEmeraldGolem(LevelReader level, BlockPos topPos, CallbackInfoReturnable<Boolean> info) {
		if (!info.getReturnValueZ() && EmeraldGolemSummoning.canSpawn(level, topPos)) {
			info.setReturnValue(true);
		}
	}
}
