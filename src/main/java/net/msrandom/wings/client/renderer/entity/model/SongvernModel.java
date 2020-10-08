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
			this.textureWidth = 32;
			this.textureHeight = 32;
			this.rightWing = new ModelRenderer(this, 0, 12);
			this.rightWing.setRotationPoint(-2.0F, -1.5F, 1.0F);
			this.rightWing.addBox(-5.0F, 0.0F, -3.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
			this.leftWing = new ModelRenderer(this, 0, 12);
			this.leftWing.mirror = true;
			this.leftWing.setRotationPoint(2.0F, -1.5F, 1.0F);
			this.leftWing.addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
			this.rightLeg = new ModelRenderer(this, 0, 0);
			this.rightLeg.setRotationPoint(-2.0F, -0.5F, 2.5F);
			this.rightLeg.addBox(-1.0F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
			this.leftLeg = new ModelRenderer(this, 0, 0);
			this.leftLeg.mirror = true;
			this.leftLeg.setRotationPoint(2.0F, -0.5F, 2.5F);
			this.leftLeg.addBox(0.0F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
			this.body = new ModelRenderer(this, 0, 0);
			this.body.setRotationPoint(0.0F, 21.5F, 0.0F);
			this.body.addBox(-2.0F, -1.5F, -5.0F, 4.0F, 3.0F, 9.0F, 0.0F, 0.0F, 0.0F);
			this.tail = new ModelRenderer(this, 9, 17);
			this.tail.setRotationPoint(0.0F, -1.0F, 4.0F);
			this.tail.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, 0.0F, 0.0F);
			this.outerRightWing = new ModelRenderer(this, 10, 12);
			this.outerRightWing.setRotationPoint(-5.0F, 0.0F, -1.0F);
			this.outerRightWing.addBox(-5.0F, 0.0F, -2.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
			this.outerLeftWing = new ModelRenderer(this, 10, 12);
			this.outerLeftWing.mirror = true;
			this.outerLeftWing.setRotationPoint(5.0F, 0.0F, -1.0F);
			this.outerLeftWing.addBox(0.0F, 0.0F, -2.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
			this.body.addChild(this.rightWing);
			this.body.addChild(this.leftWing);
			this.body.addChild(this.rightLeg);
			this.body.addChild(this.leftLeg);
			this.body.addChild(this.tail);
			this.rightWing.addChild(this.outerRightWing);
			this.leftWing.addChild(this.outerLeftWing);
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
	public void setRotationAngles(SongvernEntity entity, float f, float f1, float ageInTicks, float netHeadYaw, float headPitch){
		float speed = 1.0f;
		float degree = 1.0f;
		if (entity.isFlying()) {
			this.body.rotateAngleX = MathHelper.cos(f * speed * 0.4F) * degree * 0.2F * f1 + 0.1F;
			this.leftWing.rotateAngleZ = MathHelper.cos(f * speed * 0.4F) * degree * 1.2F * f1;
			//this.outerLeftWing.rotateAngleZ = MathHelper.cos(f * speed * 0.4F) * degree * 1.5F * f1;
			this.rightWing.rotateAngleZ = MathHelper.cos(f * speed * 0.4F) * degree * -1.2F * f1;
			//this.outerRightWing.rotateAngleZ = MathHelper.cos(f * speed * 0.4F) * degree * -1.5F * f1;
			this.tail.rotateAngleX = MathHelper.cos(f * speed * 0.4F) * degree * 0.7F * f1 - 0.1F;
			this.leftLeg.rotateAngleX = MathHelper.cos(f1 + 0.3F);
			this.rightLeg.rotateAngleX = MathHelper.cos(f1 + 0.3F);
		} else {
			this.body.rotateAngleX = 0;
			this.leftWing.rotateAngleZ = 0;
			this.rightWing.rotateAngleZ = 0;
			this.tail.rotateAngleY = MathHelper.cos(f * speed * 0.4F) * degree * 0.4F * f1;
			this.tail.rotateAngleX = MathHelper.cos(f1 - 0.3F);
			this.leftLeg.rotateAngleX = MathHelper.cos(f * speed * 0.4F) * degree * 0.6F * f1;
			this.rightLeg.rotateAngleX = MathHelper.cos(f * speed * 0.4F) * degree * -0.6F * f1;
			this.leftWing.rotateAngleZ = MathHelper.cos(f1 + 0.92F);
//			this.outterLeftWing.rotateAngleZ = MathHelper.cos() *  * f1 - 2.0F;
			this.leftWing.rotateAngleY = MathHelper.cos(f * speed * 0.4F) * degree * 0.8F * f1;
			this.rightWing.rotateAngleY = MathHelper.cos(f * speed * 0.4F) * degree * 0.8F * f1;
			this.rightWing.rotateAngleZ = MathHelper.cos(f1 - 0.92F);
//			this.outterRightWing.rotateAngleZ = MathHelper.cos() *  * f1 + 2.0F;
		}
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return parts;
	}
}
