package com.emeraldtools;

import com.emeraldtools.entity.EnderArrow;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
	// Same size and tracking settings as vanilla arrows.
	public static final EntityType<EnderArrow> ENDER_ARROW = register(
		"ender_arrow",
		EntityType.Builder.<EnderArrow>of(EnderArrow::new, MobCategory.MISC)
			.noLootTable()
			.sized(0.5F, 0.5F)
			.eyeHeight(0.13F)
			.clientTrackingRange(4)
			.updateInterval(20)
	);

	private ModEntities() {
	}

	public static void initialize() {
	}

	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
		ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EmeraldTools.MOD_ID, name));
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
	}
}
