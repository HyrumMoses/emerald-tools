package com.emeraldtools.entity;

import java.util.function.Predicate;

import com.emeraldtools.ModEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

// The iron golem's T-shaped build, made of emerald blocks.
public final class EmeraldGolemSummoning {
	private static final Predicate<BlockState> PUMPKINS = state -> state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN);

	private static final BlockPattern BASE = BlockPatternBuilder.start()
		.aisle("~ ~", "###", "~#~")
		.where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.EMERALD_BLOCK)))
		.where('~', BlockInWorld.hasState(BlockBehaviour.BlockStateBase::isAir))
		.build();

	private static final BlockPattern FULL = BlockPatternBuilder.start()
		.aisle("~^~", "###", "~#~")
		.where('^', BlockInWorld.hasState(PUMPKINS))
		.where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.EMERALD_BLOCK)))
		.where('~', BlockInWorld.hasState(BlockBehaviour.BlockStateBase::isAir))
		.build();

	private EmeraldGolemSummoning() {
	}

	public static boolean canSpawn(LevelReader level, BlockPos topPos) {
		return BASE.find(level, topPos) != null;
	}

	// Mirrors the iron golem branch of CarvedPumpkinBlock.trySpawnGolem.
	public static boolean trySpawn(Level level, BlockPos topPos) {
		BlockPattern.BlockPatternMatch match = FULL.find(level, topPos);
		if (match == null) {
			return false;
		}

		EmeraldGolem golem = ModEntities.EMERALD_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
		if (golem == null) {
			return false;
		}

		golem.setPlayerCreated(true);
		BlockPos spawnPos = match.getBlock(1, 2, 0).getPos();
		CarvedPumpkinBlock.clearPatternBlocks(level, match);
		golem.snapTo(spawnPos.getX() + 0.5, spawnPos.getY() + 0.05, spawnPos.getZ() + 0.5, 0.0F, 0.0F);
		level.addFreshEntity(golem);

		for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0))) {
			CriteriaTriggers.SUMMONED_ENTITY.trigger(player, golem);
		}

		CarvedPumpkinBlock.updatePatternBlocks(level, match);
		return true;
	}
}
