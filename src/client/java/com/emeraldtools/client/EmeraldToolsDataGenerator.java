package com.emeraldtools.client;

import java.util.concurrent.CompletableFuture;

import com.emeraldtools.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EmeraldToolsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModelProvider::new);
		pack.addProvider(RecipeGenerator::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(EnglishLanguageProvider::new);
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
		}
	}

	// Same shapes as the diamond tool recipes, with emeralds in place of diamonds.
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
		}
	}
}
