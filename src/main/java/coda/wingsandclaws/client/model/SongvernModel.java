package coda.wingsandclaws.client.model;


import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import coda.wingsandclaws.entity.SongvernEntity;

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
			this.texWidth = 32;
			this.texHeight = 32;
			this.rightWing = new ModelRenderer(this, 0, 12);
			this.rightWing.setPos(-2.0F, -1.5F, 1.0F);
			this.rightWing.addBox(-5.0F, 0.0F, -3.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
			this.leftWing = new ModelRenderer(this, 0, 12);
			this.leftWing.mirror = true;
			this.leftWing.setPos(2.0F, -1.5F, 1.0F);
			this.leftWing.addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
			this.rightLeg = new ModelRenderer(this, 0, 0);
			this.rightLeg.setPos(-2.0F, -0.5F, 2.5F);
			this.rightLeg.addBox(-1.0F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
			this.leftLeg = new ModelRenderer(this, 0, 0);
			this.leftLeg.mirror = true;
			this.leftLeg.setPos(2.0F, -0.5F, 2.5F);
			this.leftLeg.addBox(0.0F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
			this.body = new ModelRenderer(this, 0, 0);
			this.body.setPos(0.0F, 21.5F, 0.0F);
			this.body.addBox(-2.0F, -1.5F, -5.0F, 4.0F, 3.0F, 9.0F, 0.0F, 0.0F, 0.0F);
			this.tail = new ModelRenderer(this, 9, 17);
			this.tail.setPos(0.0F, -1.0F, 4.0F);
			this.tail.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, 0.0F, 0.0F);
			this.outerRightWing = new ModelRenderer(this, 10, 12);
			this.outerRightWing.setPos(-5.0F, 0.0F, -1.0F);
			this.outerRightWing.addBox(-5.0F, 0.0F, -2.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
			this.outerLeftWing = new ModelRenderer(this, 10, 12);
			this.outerLeftWing.mirror = true;
			this.outerLeftWing.setPos(5.0F, 0.0F, -1.0F);
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
			texWidth = 32;
			texHeight = 16;

			body = new ModelRenderer(this);
			body.setPos(0.0F, 22.0F, -1.0F);
			body.texOffs(0, 0).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

			leftWing = new ModelRenderer(this);
			leftWing.setPos(-1.5F, 0.0F, -0.5F);
			body.addChild(leftWing);
			leftWing.texOffs(4, 9).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 2.0F, 0.0F, true);

			rightWing = new ModelRenderer(this);
			rightWing.setPos(1.5F, 0.0F, -0.5F);
			body.addChild(rightWing);
			rightWing.texOffs(4, 9).addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);

			tail = new ModelRenderer(this);
			tail.setPos(0.0F, -1.0F, 3.0F);
			body.addChild(tail);
			tail.texOffs(0, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

			leftLeg = new ModelRenderer(this);
			leftLeg.setPos(-1.5F, 0.0F, 1.5F);
			body.addChild(leftLeg);
			leftLeg.texOffs(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

			rightLeg = new ModelRenderer(this);
			rightLeg.setPos(1.5F, 0.0F, 1.5F);
			body.addChild(rightLeg);
			rightLeg.texOffs(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		}
	}

	@Override
	public void setupAnim(SongvernEntity entity, float f, float f1, float ageInTicks, float netHeadYaw, float headPitch){
		float speed = 1.0f;
		float degree = 1.0f;
		if (entity.isFlying()) {
			this.body.xRot = MathHelper.cos(f * speed * 0.4F) * degree * 0.2F * f1 + 0.1F;
			this.leftWing.zRot = MathHelper.cos(f * speed * 0.4F) * degree * 1.2F * f1;
			//this.outerLeftWing.rotateAngleZ = MathHelper.cos(f * speed * 0.4F) * degree * 1.5F * f1;
			this.rightWing.zRot = MathHelper.cos(f * speed * 0.4F) * degree * -1.2F * f1;
			//this.outerRightWing.rotateAngleZ = MathHelper.cos(f * speed * 0.4F) * degree * -1.5F * f1;
			this.tail.xRot = MathHelper.cos(f * speed * 0.4F) * degree * 0.7F * f1 - 0.1F;
			this.leftLeg.xRot = MathHelper.cos(f1 + 0.3F);
			this.rightLeg.xRot = MathHelper.cos(f1 + 0.3F);
		} else {
			this.body.xRot = 0;
			this.leftWing.zRot = 0;
			this.rightWing.zRot = 0;
			this.tail.yRot = MathHelper.cos(f * speed * 0.4F) * degree * 0.4F * f1;
			this.tail.xRot = MathHelper.cos(f1 - 0.3F);
			this.leftLeg.xRot = MathHelper.cos(f * speed * 0.4F) * degree * 0.6F * f1;
			this.rightLeg.xRot = MathHelper.cos(f * speed * 0.4F) * degree * -0.6F * f1;
			this.leftWing.zRot = MathHelper.cos(f1 + 0.92F);
//			this.outterLeftWing.rotateAngleZ = MathHelper.cos() *  * f1 - 2.0F;
			this.leftWing.yRot = MathHelper.cos(f * speed * 0.4F) * degree * 0.8F * f1;
			this.rightWing.yRot = MathHelper.cos(f * speed * 0.4F) * degree * 0.8F * f1;
			this.rightWing.zRot = MathHelper.cos(f1 - 0.92F);
//			this.outterRightWing.rotateAngleZ = MathHelper.cos() *  * f1 + 2.0F;
		}
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return parts;
	}
}
