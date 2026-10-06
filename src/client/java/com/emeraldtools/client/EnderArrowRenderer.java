package com.emeraldtools.client;

import com.emeraldtools.EmeraldTools;
import com.emeraldtools.entity.EnderArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;

public class EnderArrowRenderer extends ArrowRenderer<EnderArrow, ArrowRenderState> {
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(EmeraldTools.MOD_ID, "textures/entity/projectiles/ender_arrow.png");

	public EnderArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected Identifier getTextureLocation(ArrowRenderState state) {
		return TEXTURE;
	}

	@Override
	public ArrowRenderState createRenderState() {
		return new ArrowRenderState();
	}
}
