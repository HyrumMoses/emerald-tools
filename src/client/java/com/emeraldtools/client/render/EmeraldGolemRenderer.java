package com.emeraldtools.client.render;

import com.emeraldtools.EmeraldTools;
import com.emeraldtools.entity.EmeraldGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.animal.golem.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.IronGolemFlowerLayer;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;

// Based on IronGolemRenderer, with the emerald model and texture, scaled up to 4 blocks tall.
public class EmeraldGolemRenderer extends MobRenderer<EmeraldGolem, IronGolemRenderState, IronGolemModel> {
	public static final ModelLayerLocation LAYER =
		new ModelLayerLocation(Identifier.fromNamespaceAndPath(EmeraldTools.MOD_ID, "emerald_golem"), "main");
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(EmeraldTools.MOD_ID, "textures/entity/emerald_golem.png");
	// The iron golem model is 43 pixels (about 2.7 blocks) tall; 1.5x brings it to 4 blocks.
	private static final float SCALE = 1.5F;
	private final BlockModelResolver blockModelResolver;

	public EmeraldGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new IronGolemModel(context.bakeLayer(LAYER)), 0.7F * SCALE);
		this.blockModelResolver = context.getBlockModelResolver();
		this.addLayer(new IronGolemFlowerLayer(this));
	}

	@Override
	public Identifier getTextureLocation(IronGolemRenderState state) {
		return TEXTURE;
	}

	@Override
	public IronGolemRenderState createRenderState() {
		return new IronGolemRenderState();
	}

	@Override
	public void extractRenderState(EmeraldGolem entity, IronGolemRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.attackTicksRemaining = entity.getAttackAnimationTick() > 0.0F ? entity.getAttackAnimationTick() - partialTicks : 0.0F;
		state.offerFlowerTick = entity.getOfferFlowerTick();
		if (state.offerFlowerTick > 0) {
			this.blockModelResolver.update(state.flowerBlock, Blocks.POPPY.defaultBlockState(), IronGolemRenderer.BLOCK_DISPLAY_CONTEXT);
		} else {
			state.flowerBlock.clear();
		}

		state.crackiness = entity.getCrackiness();
	}

	@Override
	protected void scale(IronGolemRenderState state, PoseStack poseStack) {
		poseStack.scale(SCALE, SCALE, SCALE);
	}

	// The iron golem's side-to-side sway while walking.
	@Override
	protected void setupRotations(IronGolemRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
		super.setupRotations(state, poseStack, bodyRot, entityScale);
		if (!(state.walkAnimationSpeed < 0.01)) {
			float wp = state.walkAnimationPos + 6.0F;
			float triangleWave = (Math.abs(wp % 13.0F - 6.5F) - 3.25F) / 3.25F;
			poseStack.mulPose(Axis.ZP.rotationDegrees(6.5F * triangleWave));
		}
	}
}
