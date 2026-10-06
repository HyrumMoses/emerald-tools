package com.emeraldtools.client.render;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

// The iron golem's model with thicker arms: 6x30x8 instead of 4x30x6. Part names match
// IronGolemModel so its animations drive this mesh. EmeraldGolemRenderer scales it to 4 blocks.
public final class EmeraldGolemModel {
	private EmeraldGolemModel() {
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild(
			"head",
			CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F).texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F),
			PartPose.offset(0.0F, -7.0F, -2.0F)
		);
		root.addOrReplaceChild(
			"body",
			CubeListBuilder.create()
				.texOffs(0, 40)
				.addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F)
				.texOffs(0, 70)
				.addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.5F)),
			PartPose.offset(0.0F, -7.0F, 0.0F)
		);
		root.addOrReplaceChild(
			"right_arm", CubeListBuilder.create().texOffs(60, 22).addBox(-15.0F, -2.5F, -4.0F, 6.0F, 30.0F, 8.0F), PartPose.offset(0.0F, -7.0F, 0.0F)
		);
		root.addOrReplaceChild(
			"left_arm", CubeListBuilder.create().texOffs(60, 62).addBox(9.0F, -2.5F, -4.0F, 6.0F, 30.0F, 8.0F), PartPose.offset(0.0F, -7.0F, 0.0F)
		);
		root.addOrReplaceChild(
			"right_leg", CubeListBuilder.create().texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), PartPose.offset(-4.0F, 11.0F, 0.0F)
		);
		root.addOrReplaceChild(
			"left_leg", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), PartPose.offset(5.0F, 11.0F, 0.0F)
		);
		return LayerDefinition.create(mesh, 128, 128);
	}
}
