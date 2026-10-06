package com.emeraldtools.client;

import java.util.concurrent.CompletableFuture;

import com.emeraldtools.ModEntities;
import com.emeraldtools.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class EmeraldToolsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModelProvider::new);
		pack.addProvider(RecipeGenerator::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(EnglishLanguageProvider::new);
		pack.addProvider(EquipmentAssetGenerator::new);
		pack.addProvider(EntityLootGenerator::new);
	}

	private static class ModelProvider extends FabricModelProvider {
		ModelProvider(FabricPackOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
		}

		@Override
		public void generateItemModels(ItemModelGenerators itemModelGenerators) {
			itemModelGenerators.generateFlatItem(ModItems.EMERALD_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
			itemModelGenerators.generateFlatItem(ModItems.EMERALD_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
			itemModelGenerators.generateFlatItem(ModItems.EMERALD_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
			itemModelGenerators.generateFlatItem(ModItems.EMERALD_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
			itemModelGenerators.generateFlatItem(ModItems.EMERALD_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
			itemModelGenerators.generateTrimmableItem(ModItems.EMERALD_HELMET, ModItems.EMERALD_ARMOR_ASSET, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
			itemModelGenerators.generateTrimmableItem(ModItems.EMERALD_CHESTPLATE, ModItems.EMERALD_ARMOR_ASSET, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
			itemModelGenerators.generateTrimmableItem(ModItems.EMERALD_LEGGINGS, ModItems.EMERALD_ARMOR_ASSET, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
			itemModelGenerators.generateTrimmableItem(ModItems.EMERALD_BOOTS, ModItems.EMERALD_ARMOR_ASSET, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
		}
	}

	// Same shapes as the diamond tool and armor recipes, with emeralds in place of diamonds.
	private static class RecipeGenerator extends FabricRecipeProvider {
		RecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
			super(output, registries);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
			return new RecipeProvider(registries, output) {
				@Override
				public void buildRecipes() {
					tool(RecipeCategory.COMBAT, ModItems.EMERALD_SWORD, "X", "X", "#");
					tool(RecipeCategory.TOOLS, ModItems.EMERALD_SHOVEL, "X", "#", "#");
					tool(RecipeCategory.TOOLS, ModItems.EMERALD_PICKAXE, "XXX", " # ", " # ");
					tool(RecipeCategory.TOOLS, ModItems.EMERALD_AXE, "XX", "X#", " #");
					tool(RecipeCategory.TOOLS, ModItems.EMERALD_HOE, "XX", " #", " #");
					armor(ModItems.EMERALD_HELMET, "XXX", "X X");
					armor(ModItems.EMERALD_CHESTPLATE, "X X", "XXX", "XXX");
					armor(ModItems.EMERALD_LEGGINGS, "XXX", "X X", "X X");
					armor(ModItems.EMERALD_BOOTS, "X X", "X X");
				}

				private void armor(Item result, String... pattern) {
					var builder = this.shaped(RecipeCategory.COMBAT, result)
						.define('X', Items.EMERALD);
					for (String row : pattern) {
						builder.pattern(row);
					}
					builder.unlockedBy("has_emerald", this.has(Items.EMERALD)).save(this.output);
				}

				private void tool(RecipeCategory category, Item result, String... pattern) {
					var builder = this.shaped(category, result)
						.define('#', Items.STICK)
						.define('X', ModItems.EMERALD_TOOL_MATERIALS);
					for (String row : pattern) {
						builder.pattern(row);
					}
					builder.unlockedBy("has_emerald", this.has(ModItems.EMERALD_TOOL_MATERIALS)).save(this.output);
				}
			};
		}

		@Override
		public String getName() {
			return "Emerald Tools Recipes";
		}
	}

	private static class ItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {
		ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
			super(output, registries);
		}

		@Override
		protected void addTags(HolderLookup.Provider registries) {
			valueLookupBuilder(ModItems.EMERALD_TOOL_MATERIALS).add(Items.EMERALD);
			// Vanilla tool tags control which enchantments apply, among other things.
			valueLookupBuilder(ItemTags.SWORDS).add(ModItems.EMERALD_SWORD);
			valueLookupBuilder(ItemTags.SHOVELS).add(ModItems.EMERALD_SHOVEL);
			valueLookupBuilder(ItemTags.PICKAXES).add(ModItems.EMERALD_PICKAXE);
			valueLookupBuilder(ItemTags.AXES).add(ModItems.EMERALD_AXE);
			valueLookupBuilder(ItemTags.HOES).add(ModItems.EMERALD_HOE);
			valueLookupBuilder(ItemTags.CLUSTER_MAX_HARVESTABLES).add(ModItems.EMERALD_PICKAXE);
			valueLookupBuilder(ModItems.REPAIRS_EMERALD_ARMOR).add(Items.EMERALD);
			// Vanilla armor tags control which enchantments apply and which armor can be trimmed.
			valueLookupBuilder(ItemTags.HEAD_ARMOR).add(ModItems.EMERALD_HELMET);
			valueLookupBuilder(ItemTags.CHEST_ARMOR).add(ModItems.EMERALD_CHESTPLATE);
			valueLookupBuilder(ItemTags.LEG_ARMOR).add(ModItems.EMERALD_LEGGINGS);
			valueLookupBuilder(ItemTags.FOOT_ARMOR).add(ModItems.EMERALD_BOOTS);
			valueLookupBuilder(ItemTags.TRIMMABLE_ARMOR)
				.add(ModItems.EMERALD_HELMET, ModItems.EMERALD_CHESTPLATE, ModItems.EMERALD_LEGGINGS, ModItems.EMERALD_BOOTS);
		}
	}

	private static class EnglishLanguageProvider extends FabricLanguageProvider {
		EnglishLanguageProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
			super(output, registries);
		}

		@Override
		public void generateTranslations(HolderLookup.Provider registries, TranslationBuilder translationBuilder) {
			translationBuilder.add(ModItems.EMERALD_SWORD, "Emerald Sword");
			translationBuilder.add(ModItems.EMERALD_SHOVEL, "Emerald Shovel");
			translationBuilder.add(ModItems.EMERALD_PICKAXE, "Emerald Pickaxe");
			translationBuilder.add(ModItems.EMERALD_AXE, "Emerald Axe");
			translationBuilder.add(ModItems.EMERALD_HOE, "Emerald Hoe");
			translationBuilder.add(ModItems.EMERALD_TOOL_MATERIALS, "Emerald Tool Materials");
			translationBuilder.add(ModItems.EMERALD_HELMET, "Emerald Helmet");
			translationBuilder.add(ModItems.EMERALD_CHESTPLATE, "Emerald Chestplate");
			translationBuilder.add(ModItems.EMERALD_LEGGINGS, "Emerald Leggings");
			translationBuilder.add(ModItems.EMERALD_BOOTS, "Emerald Boots");
			translationBuilder.add(ModItems.REPAIRS_EMERALD_ARMOR, "Repairs Emerald Armor");
			translationBuilder.add(ModEntities.EMERALD_GOLEM, "Emerald Golem");
		}
	}

	// Tells the client which textures to draw for worn emerald armor.
	private static class EquipmentAssetGenerator implements DataProvider {
		private final PackOutput.PathProvider pathProvider;

		EquipmentAssetGenerator(FabricPackOutput output) {
			this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
		}

		@Override
		public CompletableFuture<?> run(CachedOutput output) {
			EquipmentClientInfo emerald = EquipmentClientInfo.builder()
				.addHumanoidLayers(ModItems.EMERALD_ARMOR_ASSET.identifier())
				.build();
			return DataProvider.saveStable(output, EquipmentClientInfo.CODEC, emerald, pathProvider.json(ModItems.EMERALD_ARMOR_ASSET));
		}

		@Override
		public String getName() {
			return "Emerald Tools Equipment Assets";
		}
	}

	// Like the iron golem's drops: 0-2 poppies, plus 3-5 emeralds in place of iron ingots.
	private static class EntityLootGenerator extends FabricEntityLootSubProvider {
		EntityLootGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
			super(output, registries);
		}

		@Override
		public void generate() {
			add(ModEntities.EMERALD_GOLEM, LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(Blocks.POPPY).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))));
		}
	}
}
