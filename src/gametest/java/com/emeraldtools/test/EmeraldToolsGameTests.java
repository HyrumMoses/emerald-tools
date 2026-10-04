package com.emeraldtools.test;

import com.emeraldtools.ModItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

// Compares each emerald tool against its diamond counterpart.
public class EmeraldToolsGameTests {
	@GameTest
	public void sword(GameTestHelper helper) {
		assertStats(helper, ModItems.EMERALD_SWORD, Items.DIAMOND_SWORD, Blocks.COBWEB.defaultBlockState(), false);
		helper.succeed();
	}

	@GameTest
	public void shovel(GameTestHelper helper) {
		assertStats(helper, ModItems.EMERALD_SHOVEL, Items.DIAMOND_SHOVEL, Blocks.DIRT.defaultBlockState(), true);
		helper.succeed();
	}

	@GameTest
	public void pickaxe(GameTestHelper helper) {
		assertStats(helper, ModItems.EMERALD_PICKAXE, Items.DIAMOND_PICKAXE, Blocks.STONE.defaultBlockState(), true);
		helper.assertTrue(new ItemStack(ModItems.EMERALD_PICKAXE).isCorrectToolForDrops(Blocks.OBSIDIAN.defaultBlockState()),
			"Emerald pickaxe should harvest obsidian, like diamond");
		helper.succeed();
	}

	@GameTest
	public void axe(GameTestHelper helper) {
		assertStats(helper, ModItems.EMERALD_AXE, Items.DIAMOND_AXE, Blocks.OAK_LOG.defaultBlockState(), true);
		helper.succeed();
	}

	@GameTest
	public void hoe(GameTestHelper helper) {
		assertStats(helper, ModItems.EMERALD_HOE, Items.DIAMOND_HOE, Blocks.HAY_BLOCK.defaultBlockState(), true);
		helper.succeed();
	}

	// Swords use fixed, material-independent mining speeds, so only tools check mining speed.
	private static void assertStats(GameTestHelper helper, Item emerald, Item diamond, BlockState block, boolean checkMiningSpeed) {
		ItemStack emeraldStack = new ItemStack(emerald);
		ItemStack diamondStack = new ItemStack(diamond);

		assertEqual(helper, "durability", diamondStack.getMaxDamage() / 2, emeraldStack.getMaxDamage());
		assertEqual(helper, "attack damage", 2 * attackDamage(diamondStack), attackDamage(emeraldStack));
		assertEqual(helper, "attack speed", attackSpeed(diamondStack), attackSpeed(emeraldStack));
		if (checkMiningSpeed) {
			assertEqual(helper, "mining speed", 2 * diamondStack.getDestroySpeed(block), emeraldStack.getDestroySpeed(block));
		}
	}

	// Damage as shown in the tooltip: the player's base of 1 plus the item's modifiers.
	private static double attackDamage(ItemStack stack) {
		return modifiers(stack).compute(Attributes.ATTACK_DAMAGE, 1.0, EquipmentSlot.MAINHAND);
	}

	private static double attackSpeed(ItemStack stack) {
		return modifiers(stack).compute(Attributes.ATTACK_SPEED, 4.0, EquipmentSlot.MAINHAND);
	}

	private static ItemAttributeModifiers modifiers(ItemStack stack) {
		return stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
	}

	private static void assertEqual(GameTestHelper helper, String stat, double expected, double actual) {
		helper.assertTrue(Math.abs(expected - actual) < 1e-6, stat + ": expected " + expected + " but was " + actual);
	}
}
