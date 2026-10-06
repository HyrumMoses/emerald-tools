package com.emeraldtools.client;

import com.emeraldtools.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class EmeraldToolsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.ENDER_ARROW, EnderArrowRenderer::new);
	}
}
