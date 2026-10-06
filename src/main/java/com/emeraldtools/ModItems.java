package com.emeraldtools;

import java.util.Map;
import java.util.function.Function;

import com.emeraldtools.item.EnderArrowItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.level.block.DispenserBlock;

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

	public static final TagKey<Item> REPAIRS_EMERALD_ARMOR = TagKey.create(Registries.ITEM, id("repairs_emerald_armor"));
	public static final ResourceKey<EquipmentAsset> EMERALD_ARMOR_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, id("emerald"));

	// Compared to ArmorMaterials.DIAMOND: twice the armor points and toughness.
	// Same enchantability, knockback resistance (none) and equip sound as diamond.
	// Durability is set per piece in armor() to exactly half of the diamond piece.
	public static final ArmorMaterial EMERALD_ARMOR_MATERIAL = new ArmorMaterial(
		ArmorMaterials.DIAMOND.durability() / 2,
		Map.of(ArmorType.BOOTS, 6, ArmorType.LEGGINGS, 12, ArmorType.CHESTPLATE, 16, ArmorType.HELMET, 6, ArmorType.BODY, 22),
		10, SoundEvents.ARMOR_EQUIP_DIAMOND, 4.0F, 0.0F, REPAIRS_EMERALD_ARMOR, EMERALD_ARMOR_ASSET
	);

	public static final Item EMERALD_HELMET = armor("emerald_helmet", ArmorType.HELMET);
	public static final Item EMERALD_CHESTPLATE = armor("emerald_chestplate", ArmorType.CHESTPLATE);
	public static final Item EMERALD_LEGGINGS = armor("emerald_leggings", ArmorType.LEGGINGS);
	public static final Item EMERALD_BOOTS = armor("emerald_boots", ArmorType.BOOTS);

	// Shoots from a bow or crossbow and teleports the shooter to wherever it lands.
	public static final Item ENDER_ARROW = register("ender_arrow", EnderArrowItem::new, new Item.Properties());

	private ModItems() {
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output ->
			output.insertAfter(Items.DIAMOND_HOE, EMERALD_SHOVEL, EMERALD_PICKAXE, EMERALD_AXE, EMERALD_HOE)
		);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> {
			output.insertAfter(Items.DIAMOND_SWORD, EMERALD_SWORD);
			output.insertAfter(Items.DIAMOND_AXE, EMERALD_AXE);
			output.insertAfter(Items.DIAMOND_BOOTS, EMERALD_HELMET, EMERALD_CHESTPLATE, EMERALD_LEGGINGS, EMERALD_BOOTS);
			output.insertAfter(Items.SPECTRAL_ARROW, ENDER_ARROW);
		});
		DispenserBlock.registerProjectileBehavior(ENDER_ARROW);
	}

	private static Item armor(String name, ArmorType type) {
		int durability = type.getDurability(ArmorMaterials.DIAMOND.durability()) / 2;
		return register(name, Item::new, new Item.Properties().humanoidArmor(EMERALD_ARMOR_MATERIAL, type).durability(durability));
	}

	private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id(name));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(EmeraldTools.MOD_ID, path);
	}
}
