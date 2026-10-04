package com.emeraldtools;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;

public final class ModItems {
	public static final TagKey<Item> EMERALD_TOOL_MATERIALS = TagKey.create(Registries.ITEM, id("emerald_tool_materials"));

	// Compared to ToolMaterial.DIAMOND (1561 durability, 8.0 speed, 3.0 damage bonus):
	// half the durability, twice the mining speed and twice the damage bonus.
	// Same mining level and enchantability as diamond.
	public static final ToolMaterial EMERALD = new ToolMaterial(
		BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 780, 16.0F, 6.0F, 10, EMERALD_TOOL_MATERIALS
	);

	// Attack damage is 1 (player base) + baseline + material bonus. To double the diamond
	// tool's total damage, each baseline is 2 * diamondBaseline + 1:
	// sword 7 -> 14, shovel 5.5 -> 11, pickaxe 5 -> 10, axe 9 -> 18, hoe 1 -> 2.
	// Attack speeds match the diamond tools.
	public static final Item EMERALD_SWORD = register("emerald_sword", Item::new, new Item.Properties().sword(EMERALD, 7.0F, -2.4F));
	public static final Item EMERALD_SHOVEL = register("emerald_shovel", p -> new ShovelItem(EMERALD, 4.0F, -3.0F, p), new Item.Properties());
	public static final Item EMERALD_PICKAXE = register("emerald_pickaxe", Item::new, new Item.Properties().pickaxe(EMERALD, 3.0F, -2.8F));
	public static final Item EMERALD_AXE = register("emerald_axe", p -> new AxeItem(EMERALD, 11.0F, -3.0F, p), new Item.Properties());
	public static final Item EMERALD_HOE = register("emerald_hoe", p -> new HoeItem(EMERALD, -5.0F, 0.0F, p), new Item.Properties());

	private ModItems() {
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output ->
			output.insertAfter(Items.DIAMOND_HOE, EMERALD_SHOVEL, EMERALD_PICKAXE, EMERALD_AXE, EMERALD_HOE)
		);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> {
			output.insertAfter(Items.DIAMOND_SWORD, EMERALD_SWORD);
			output.insertAfter(Items.DIAMOND_AXE, EMERALD_AXE);
		});
	}

	private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id(name));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(EmeraldTools.MOD_ID, path);
	}
}
