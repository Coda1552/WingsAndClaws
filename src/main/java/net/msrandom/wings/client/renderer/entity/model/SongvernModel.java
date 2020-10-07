package net.msrandom.wings.client.renderer.entity.model;


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.msrandom.wings.entity.passive.SongvernEntity;

public abstract class SongvernModel extends SegmentedModel<SongvernEntity> {
	public ModelRenderer body;
	public ModelRenderer tail;
	public ModelRenderer right_leg;
	public ModelRenderer left_leg;
	public ModelRenderer right_wing;
	public ModelRenderer right_wing_outer;
	public ModelRenderer left_wing;
	public ModelRenderer left_wing_outer;
	public ModelRenderer right_legChild;
	public ModelRenderer left_legChild;
	public ModelRenderer right_wingChild;
	public ModelRenderer left_wingChild;
	public ModelRenderer bodyChild;
	public ModelRenderer tailChild;
	Iterable<ModelRenderer> parts;

	protected abstract void setAngles();

	public static class Adult extends SongvernModel {
		@Override
		protected void setAngles() {
			textureWidth = 32;
			textureHeight = 32;

			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, 21.5F, -2.5F);
			body.setTextureOffset(0, 0).addBox(-2.0F, -1.5F, -5.0F, 4.0F, 3.0F, 9.0F, 0.0F, false);

			tail = new ModelRenderer(this);
			tail.setRotationPoint(0.0F, -1.0F, 4.5F);
			body.addChild(tail);
			tail.setTextureOffset(9, 17).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 1.0F, 6.0F, 0.0F, false);

			right_leg = new ModelRenderer(this);
			right_leg.setRotationPoint(2.0F, -0.5F, 2.5F);
			body.addChild(right_leg);
			right_leg.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F, 0.0F, true);

			left_leg = new ModelRenderer(this);
			left_leg.setRotationPoint(-2.0F, -0.5F, 2.5F);
			body.addChild(left_leg);
			left_leg.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F, 0.0F, false);

			right_wing = new ModelRenderer(this);
			right_wing.setRotationPoint(2.0F, -0.5F, -2.5F);
			body.addChild(right_wing);
			right_wing.setTextureOffset(0, 12).addBox(0.0F, 0.0F, 0.5F, 5.0F, 0.0F, 5.0F, 0.0F, true);

			right_wing_outer = new ModelRenderer(this);
			right_wing_outer.setRotationPoint(5.0F, 0.0F, 0.0F);
			right_wing.addChild(right_wing_outer);
			right_wing_outer.setTextureOffset(10, 12).addBox(0.0F, 0.0F, 0.5F, 5.0F, 0.0F, 5.0F, 0.0F, true);

			left_wing = new ModelRenderer(this);
			left_wing.setRotationPoint(-2.0F, -0.5F, -2.5F);
			body.addChild(left_wing);
			left_wing.setTextureOffset(0, 12).addBox(-5.0F, 0.0F, 0.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);

			left_wing_outer = new ModelRenderer(this);
			left_wing_outer.setRotationPoint(-5.0F, 0.0F, 0.0F);
			left_wing.addChild(left_wing_outer);
			left_wing_outer.setTextureOffset(10, 12).addBox(-5.0F, 0.0F, 0.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);
		}
	}

	public static class Child extends SongvernModel {
		@Override
		protected void setAngles() {
			textureWidth = 32;
			textureHeight = 16;

			bodyChild = new ModelRenderer(this);
			bodyChild.setRotationPoint(0.0F, 22.0F, -1.0F);
			bodyChild.setTextureOffset(0, 0).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

			left_wingChild = new ModelRenderer(this);
			left_wingChild.setRotationPoint(-1.5F, 0.0F, -0.5F);
			bodyChild.addChild(left_wing);
			left_wingChild.setTextureOffset(4, 9).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 2.0F, 0.0F, true);

			right_wingChild = new ModelRenderer(this);
			right_wingChild.setRotationPoint(1.5F, 0.0F, -0.5F);
			bodyChild.addChild(right_wing);
			right_wingChild.setTextureOffset(4, 9).addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);

			tailChild = new ModelRenderer(this);
			tailChild.setRotationPoint(0.0F, -1.0F, 3.0F);
			bodyChild.addChild(tail);
			tailChild.setTextureOffset(0, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

			left_legChild = new ModelRenderer(this);
			left_legChild.setRotationPoint(-1.5F, 0.0F, 1.5F);
			bodyChild.addChild(left_leg);
			left_legChild.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

			right_legChild = new ModelRenderer(this);
			right_legChild.setRotationPoint(1.5F, 0.0F, 1.5F);
			bodyChild.addChild(right_leg);
			right_legChild.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		}
	}

	@Override
	public void setRotationAngles(SongvernEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return parts;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}