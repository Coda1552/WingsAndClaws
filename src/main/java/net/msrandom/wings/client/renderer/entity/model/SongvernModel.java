package net.msrandom.wings.client.renderer.entity.model;


import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.msrandom.wings.entity.passive.SongvernEntity;

public abstract class SongvernModel extends SegmentedModel<SongvernEntity> {
	public ModelRenderer body;
	public ModelRenderer tail;
	public ModelRenderer rightLeg;
	public ModelRenderer leftLeg;
	public ModelRenderer rightWing;
	public ModelRenderer leftWing;
	Iterable<ModelRenderer> parts;

	protected abstract void setAngles();

    public SongvernModel() {
        setAngles();
        parts = ImmutableList.of(body);
    }

    public static class Adult extends SongvernModel {
        public ModelRenderer outerRightWing;
        public ModelRenderer outerLeftWing;

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

			rightLeg = new ModelRenderer(this);
			rightLeg.setRotationPoint(2.0F, -0.5F, 2.5F);
			body.addChild(rightLeg);
			rightLeg.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F, 0.0F, true);

			leftLeg = new ModelRenderer(this);
			leftLeg.setRotationPoint(-2.0F, -0.5F, 2.5F);
			body.addChild(leftLeg);
			leftLeg.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F, 0.0F, false);

			rightWing = new ModelRenderer(this);
			rightWing.setRotationPoint(2.0F, 3F, -1.5F);
			body.addChild(rightWing);
			rightWing.setTextureOffset(0, 12).addBox(0.0F, 0.1F, 0.5F, 5.0F, 0.0F, 5.0F, 0.0F, true);

			outerRightWing = new ModelRenderer(this);
			outerRightWing.setRotationPoint(5.0F, 0.0F, 0.0F);
			rightWing.addChild(outerRightWing);
			outerRightWing.setTextureOffset(10, 12).addBox(0.0F, 0.1F, 0.5F, 5.0F, 0.0F, 5.0F, 0.0F, true);

			leftWing = new ModelRenderer(this);
			leftWing.setRotationPoint(-2.0F, 3F, -1.5F);
			body.addChild(leftWing);
			leftWing.setTextureOffset(0, 12).addBox(-5.0F, 0.1F, 0.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);

			outerLeftWing = new ModelRenderer(this);
			outerLeftWing.setRotationPoint(-5.0F, 0.0F, 0.0F);
			leftWing.addChild(outerLeftWing);
			outerLeftWing.setTextureOffset(10, 12).addBox(-5.0F, 0.1F, 0.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);
        }
	}

	public static class Child extends SongvernModel {
		@Override
		protected void setAngles() {
			textureWidth = 32;
			textureHeight = 16;

			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, 22.0F, -1.0F);
			body.setTextureOffset(0, 0).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

			leftWing = new ModelRenderer(this);
			leftWing.setRotationPoint(-1.5F, 0.0F, -0.5F);
			body.addChild(leftWing);
			leftWing.setTextureOffset(4, 9).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 2.0F, 0.0F, true);

			rightWing = new ModelRenderer(this);
			rightWing.setRotationPoint(1.5F, 0.0F, -0.5F);
			body.addChild(rightWing);
			rightWing.setTextureOffset(4, 9).addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);

			tail = new ModelRenderer(this);
			tail.setRotationPoint(0.0F, -1.0F, 3.0F);
			body.addChild(tail);
			tail.setTextureOffset(0, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

			leftLeg = new ModelRenderer(this);
			leftLeg.setRotationPoint(-1.5F, 0.0F, 1.5F);
			body.addChild(leftLeg);
			leftLeg.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

			rightLeg = new ModelRenderer(this);
			rightLeg.setRotationPoint(1.5F, 0.0F, 1.5F);
			body.addChild(rightLeg);
			rightLeg.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        }
	}

	@Override
	public void setRotationAngles(SongvernEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		if (entity.isFlying()) {
			this.leftWing.rotateAngleY = 0;
			this.leftWing.rotateAngleZ = 0;
			this.rightWing.rotateAngleY = 0;
			this.rightWing.rotateAngleZ = 0;
			this.leftWing.rotateAngleZ = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * limbSwingAmount;
			this.rightWing.rotateAngleZ = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * -limbSwingAmount;
			this.leftWing.rotateAngleZ = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * limbSwingAmount;
			this.tail.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.2F - 0.2F;
			this.body.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.05F + 0.05F;
			this.leftWing.rotationPointY = -1.5f;
			this.rightWing.rotationPointY = -1.5f;
			this.rightWing.rotationPointZ = -2f;
			this.leftWing.rotationPointZ = -2f;
		}
    }

	@Override
	public Iterable<ModelRenderer> getParts() {
		return parts;
	}
}
