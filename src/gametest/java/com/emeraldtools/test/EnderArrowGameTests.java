package com.emeraldtools.test;

import java.util.List;

import com.emeraldtools.ModItems;
import com.emeraldtools.entity.EnderArrow;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class EnderArrowGameTests {
	private static final BlockPos TARGET = new BlockPos(5, 1, 5);

	@GameTest
	public void craftedFromArrowAndEnderPearl(GameTestHelper helper) {
		CraftingInput input = CraftingInput.of(2, 1, List.of(new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.ARROW)));
		var recipe = helper.getLevel().getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());
		helper.assertTrue(recipe.isPresent(), "An arrow and an ender pearl should craft something");
		helper.assertValueEqual(recipe.get().id().identifier().getPath(), "ender_arrow", "recipe");
		helper.succeed();
	}

	@GameTest
	public void firesFromBows(GameTestHelper helper) {
		helper.assertTrue(new ItemStack(ModItems.ENDER_ARROW).is(ItemTags.ARROWS), "Bows only shoot items tagged as arrows");
		helper.succeed();
	}

	@GameTest
	public void teleportsShooterToWhereItLands(GameTestHelper helper) {
		Mob shooter = spawnShooter(helper, helper.getLevel(), helper.absoluteVec(new Vec3(1.5, 2.0, 1.5)));
		EnderArrow arrow = shootDownAt(helper, shooter);
		Vec3 landing = helper.absoluteVec(Vec3.atBottomCenterOf(TARGET.above()));

		helper.succeedWhen(() -> {
			helper.assertTrue(arrow.isRemoved(), "Ender arrow should break when it lands");
			helper.assertTrue(shooter.position().distanceTo(landing) < 0.5,
				"Shooter should be at " + landing + " but was at " + shooter.position());
		});
	}

	@GameTest
	public void doesNotTeleportAcrossDimensions(GameTestHelper helper) {
		ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
		Vec3 start = new Vec3(0.5, 64.0, 0.5);
		nether.setChunkForced(0, 0, true);
		Mob shooter = spawnShooter(helper, nether, start);
		EnderArrow arrow = shootDownAt(helper, shooter);

		helper.succeedWhen(() -> {
			helper.assertTrue(arrow.isRemoved(), "Ender arrow should break when it lands");
			helper.assertTrue(shooter.level() == nether && shooter.position().distanceTo(start) < 0.5,
				"Shooter in another dimension should stay put");
			shooter.discard();
			nether.setChunkForced(0, 0, false);
		});
	}

	private static Mob spawnShooter(GameTestHelper helper, ServerLevel level, Vec3 pos) {
		Mob shooter = EntityType.ZOMBIE.create(level, EntitySpawnReason.COMMAND);
		helper.assertTrue(shooter != null, "Could not create shooter");
		shooter.setNoAi(true);
		shooter.setNoGravity(true);
		shooter.setInvulnerable(true);
		shooter.snapTo(pos.x, pos.y, pos.z, 0.0F, 0.0F);
		level.addFreshEntity(shooter);
		return shooter;
	}

	// Fires an ender arrow straight down onto a stone block in the test area.
	private static EnderArrow shootDownAt(GameTestHelper helper, LivingEntity shooter) {
		helper.setBlock(TARGET, Blocks.STONE);
		Vec3 from = helper.absoluteVec(Vec3.atBottomCenterOf(TARGET.above(3)));
		EnderArrow arrow = new EnderArrow(helper.getLevel(), shooter, new ItemStack(ModItems.ENDER_ARROW), null);
		arrow.setPos(from);
		arrow.shoot(0.0, -1.0, 0.0, 1.0F, 0.0F);
		helper.getLevel().addFreshEntity(arrow);
		return arrow;
	}
}
