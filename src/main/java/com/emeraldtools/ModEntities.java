package com.emeraldtools;

import com.emeraldtools.entity.EmeraldGolem;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
	public static final ResourceKey<EntityType<?>> EMERALD_GOLEM_KEY =
		ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EmeraldTools.MOD_ID, "emerald_golem"));

	// 4 blocks tall. The iron golem is 1.4 x 2.7, so the width is scaled up by the same ~1.5x.
	public static final EntityType<EmeraldGolem> EMERALD_GOLEM = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		EMERALD_GOLEM_KEY,
		EntityType.Builder.of(EmeraldGolem::new, MobCategory.MISC).sized(2.1F, 4.0F).clientTrackingRange(10).build(EMERALD_GOLEM_KEY)
	);

	private ModEntities() {
	}

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(EMERALD_GOLEM, EmeraldGolem.createAttributes());
	}
}
