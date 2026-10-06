package com.emeraldtools.client;

import com.emeraldtools.ModEntities;
import com.emeraldtools.client.render.EmeraldGolemModel;
import com.emeraldtools.client.render.EmeraldGolemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

public class EmeraldToolsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModelLayerRegistry.registerModelLayer(EmeraldGolemRenderer.LAYER, EmeraldGolemModel::createBodyLayer);
		EntityRendererRegistry.register(ModEntities.EMERALD_GOLEM, EmeraldGolemRenderer::new);
	}
}
