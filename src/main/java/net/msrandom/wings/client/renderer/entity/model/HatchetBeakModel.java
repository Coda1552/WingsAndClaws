package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

public class HatchetBeakModel extends SegmentedModel<HatchetBeakEntity> {
    public ModelRenderer body;
    public ModelRenderer neck1;
    public ModelRenderer thighLeft;
    public ModelRenderer thighRight;
    public ModelRenderer hips;
    public ModelRenderer wingRightBone1;
    public ModelRenderer wingLeftBone1;
    public ModelRenderer neck2;
    public ModelRenderer head;
    public ModelRenderer mouth;
    public ModelRenderer jaw;
    public ModelRenderer beakTop;
    public ModelRenderer beakBottom;
    public ModelRenderer legLeft;
    public ModelRenderer legRight;
    public ModelRenderer tail1;
    public ModelRenderer tail2;
    public ModelRenderer tailLeft;
    public ModelRenderer tailRight;
    public ModelRenderer tailTip;
    public ModelRenderer wingRightBone2;
    public ModelRenderer wingRightSkin1;
    public ModelRenderer wingRightSkin1_1;
    public ModelRenderer wingLeftBone2;
    public ModelRenderer wingLeftSkin1;
    public ModelRenderer wingLeftSkin2;

    public HatchetBeakModel() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.tail1 = new ModelRenderer(this, 102, 52);
        this.tail1.setRotationPoint(0.0F, -0.5F, 26.0F);
        this.tail1.addBox(-4.0F, -5.0F, 0.0F, 8.0F, 10.0F, 26.0F, 0.0F, 0.0F, 0.0F);
        this.wingRightSkin1_1 = new ModelRenderer(this, 0, 150);
        this.wingRightSkin1_1.mirror = true;
        this.wingRightSkin1_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingRightSkin1_1.addBox(-44.0F, 0.0F, 2.0F, 44.0F, 0.0F, 44.0F, 0.0F, 0.0F, 0.0F);
        this.mouth = new ModelRenderer(this, 184, 40);
        this.mouth.setRotationPoint(0.0F, 1.0F, -11.0F);
        this.mouth.addBox(-2.5F, -4.0F, -12.0F, 5.0F, 4.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.tailLeft = new ModelRenderer(this, 0, 66);
        this.tailLeft.mirror = true;
        this.tailLeft.setRotationPoint(2.0F, 0.0F, 25.0F);
        this.tailLeft.addBox(0.0F, 0.0F, -8.0F, 10.0F, 1.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.wingRightSkin1 = new ModelRenderer(this, 0, 100);
        this.wingRightSkin1.mirror = true;
        this.wingRightSkin1.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.wingRightSkin1.addBox(-44.0F, 0.0F, 2.0F, 44.0F, 0.0F, 44.0F, 0.0F, 0.0F, 0.0F);
        this.neck2 = new ModelRenderer(this, 196, 0);
        this.neck2.setRotationPoint(0.0F, -1.0F, -13.0F);
        this.neck2.addBox(-4.0F, -4.0F, -13.0F, 8.0F, 8.0F, 13.0F, 0.0F, 0.0F, 0.0F);
        this.jaw = new ModelRenderer(this, 0, 0);
        this.jaw.setRotationPoint(0.0F, 1.0F, -11.0F);
        this.jaw.addBox(-1.5F, 0.0F, -11.0F, 3.0F, 3.0F, 11.0F, 0.0F, 0.0F, 0.0F);
        this.wingLeftSkin2 = new ModelRenderer(this, 0, 150);
        this.wingLeftSkin2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingLeftSkin2.addBox(0.0F, 0.0F, 2.0F, 44.0F, 0.0F, 44.0F, 0.0F, 0.0F, 0.0F);
        this.legLeft = new ModelRenderer(this, 222, 46);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(3.5F, 14.0F, 0.5F);
        this.legLeft.addBox(-2.5F, -1.0F, -3.5F, 5.0F, 16.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.legRight = new ModelRenderer(this, 222, 46);
        this.legRight.setRotationPoint(-3.5F, 14.0F, 0.5F);
        this.legRight.addBox(-2.5F, -1.0F, -3.5F, 5.0F, 16.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.tailRight = new ModelRenderer(this, 0, 66);
        this.tailRight.setRotationPoint(-2.0F, 0.0F, 25.0F);
        this.tailRight.addBox(-10.0F, 0.0F, -8.0F, 10.0F, 1.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.wingLeftBone2 = new ModelRenderer(this, 0, 83);
        this.wingLeftBone2.setRotationPoint(44.0F, 0.0F, 0.0F);
        this.wingLeftBone2.addBox(0.0F, -2.0F, -3.0F, 44.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, -4.0F, -7.0F);
        this.body.addBox(-7.0F, -9.0F, -13.0F, 14.0F, 16.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.wingRightBone2 = new ModelRenderer(this, 0, 83);
        this.wingRightBone2.setRotationPoint(-44.0F, 0.0F, 0.0F);
        this.wingRightBone2.addBox(-44.0F, -2.0F, -3.0F, 44.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.tailTip = new ModelRenderer(this, 10, 19);
        this.tailTip.setRotationPoint(0.0F, -1.0F, 26.0F);
        this.tailTip.addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tailTip, -0.3490658503988659F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 211, 27);
        this.head.setRotationPoint(0.0F, -0.1F, -12.0F);
        this.head.addBox(-4.5F, -4.0F, -11.0F, 9.0F, 8.0F, 11.0F, 0.0F, 0.0F, 0.0F);
        this.wingLeftBone1 = new ModelRenderer(this, 0, 52);
        this.wingLeftBone1.setRotationPoint(5.0F, -5.0F, -8.0F);
        this.wingLeftBone1.addBox(0.0F, -3.0F, -4.0F, 44.0F, 6.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.tail2 = new ModelRenderer(this, 170, 57);
        this.tail2.setRotationPoint(0.0F, -0.5F, 26.0F);
        this.tail2.addBox(-3.0F, -4.0F, 0.0F, 6.0F, 8.0F, 26.0F, 0.0F, 0.0F, 0.0F);
        this.beakTop = new ModelRenderer(this, 228, 11);
        this.beakTop.setRotationPoint(0.0F, -4.0F, -12.0F);
        this.beakTop.addBox(-0.5F, -6.0F, 0.0F, 1.0F, 6.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(beakTop, -0.17453292519943295F, 0.0F, 0.0F);
        this.beakBottom = new ModelRenderer(this, 0, 14);
        this.beakBottom.setRotationPoint(0.0F, 3.0F, -11.0F);
        this.beakBottom.addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(beakBottom, 0.17453292519943295F, 0.0F, 0.0F);
        this.hips = new ModelRenderer(this, 150, 0);
        this.hips.setRotationPoint(0.0F, -2.5F, 19.0F);
        this.hips.addBox(-5.0F, -6.0F, 0.0F, 10.0F, 12.0F, 26.0F, 0.0F, 0.0F, 0.0F);
        this.wingLeftSkin1 = new ModelRenderer(this, 0, 100);
        this.wingLeftSkin1.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.wingLeftSkin1.addBox(0.0F, 0.0F, 2.0F, 44.0F, 0.0F, 44.0F, 0.0F, 0.0F, 0.0F);
        this.neck1 = new ModelRenderer(this, 60, 0);
        this.neck1.setRotationPoint(0.0F, -2.0F, -12.0F);
        this.neck1.addBox(-5.0F, -6.0F, -14.0F, 10.0F, 12.0F, 14.0F, 0.0F, 0.0F, 0.0F);
        this.thighLeft = new ModelRenderer(this, 108, 0);
        this.thighLeft.mirror = true;
        this.thighLeft.setRotationPoint(1.0F, -1.0F, 12.0F);
        this.thighLeft.addBox(0.0F, -1.0F, -6.0F, 7.0F, 14.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.thighRight = new ModelRenderer(this, 108, 0);
        this.thighRight.setRotationPoint(-1.0F, -1.0F, 12.0F);
        this.thighRight.addBox(-7.0F, -1.0F, -6.0F, 7.0F, 14.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.wingRightBone1 = new ModelRenderer(this, 0, 52);
        this.wingRightBone1.mirror = true;
        this.wingRightBone1.setRotationPoint(-5.0F, -5.0F, -8.0F);
        this.wingRightBone1.addBox(-44.0F, -3.0F, -4.0F, 44.0F, 6.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.hips.addChild(this.tail1);
        this.wingRightBone2.addChild(this.wingRightSkin1_1);
        this.head.addChild(this.mouth);
        this.tail2.addChild(this.tailLeft);
        this.wingRightBone1.addChild(this.wingRightSkin1);
        this.neck1.addChild(this.neck2);
        this.head.addChild(this.jaw);
        this.wingLeftBone2.addChild(this.wingLeftSkin2);
        this.thighLeft.addChild(this.legLeft);
        this.thighRight.addChild(this.legRight);
        this.tail2.addChild(this.tailRight);
        this.wingLeftBone1.addChild(this.wingLeftBone2);
        this.wingRightBone1.addChild(this.wingRightBone2);
        this.tail2.addChild(this.tailTip);
        this.neck2.addChild(this.head);
        this.body.addChild(this.wingLeftBone1);
        this.tail1.addChild(this.tail2);
        this.mouth.addChild(this.beakTop);
        this.jaw.addChild(this.beakBottom);
        this.body.addChild(this.hips);
        this.wingLeftBone1.addChild(this.wingLeftSkin1);
        this.body.addChild(this.neck1);
        this.body.addChild(this.thighLeft);
        this.body.addChild(this.thighRight);
        this.body.addChild(this.wingRightBone1);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body);
    }

    @Override
    public void setRotationAngles(HatchetBeakEntity entityIn, float f, float f1, float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = 1.0f;
        float degree = 1.0f;

        if (entityIn.isFlying()) {
//            this.body.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F;
//            this.neck1.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F;
//            this.neck2.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F;
//            this.head.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F;
//            this.hips.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F;
//            this.tail1.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F;
//            this.tail2.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F;
            this.tailLeft.rotateAngleZ = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 0.2F;
            this.tailRight.rotateAngleZ = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + -0.2F;
            this.thighLeft.rotateAngleX = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 1.2F;
//            this.thighLeft.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F + 0.15F;
//            this.thighRight.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F + 0.15F;
            this.thighRight.rotateAngleX = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 1.2F;
            this.wingRightBone1.rotateAngleZ = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.9F) * f1 * 0.5F;
            this.wingRightBone2.rotateAngleZ = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 1.2F) * f1 * 0.5F;
            this.wingLeftBone1.rotateAngleZ = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * -0.9F) * f1 * 0.5F;
            this.wingLeftBone2.rotateAngleZ = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * -1.2F) * f1 * 0.5F;
        } else {

            this.wingRightBone1.rotateAngleZ = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + -0.85F;
            this.wingRightBone2.rotateAngleZ = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 1.7F;
            this.wingLeftBone1.rotateAngleZ = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 0.85F;
            this.wingLeftBone2.rotateAngleZ = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + -1.7F;
            this.thighLeft.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 1.2F) * f1 * 0.5F;
            this.thighRight.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * -1.2F) * f1 * 0.5F;
            this.tail1.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.15F) * f1 * 0.5F + 0.2F;
            this.hips.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F + -0.35F;
            this.tail2.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.2F) * f1 * 0.5F + 0.2F;
            this.wingRightBone1.rotateAngleY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.4F) * f1 * 0.5F;
            this.wingLeftBone1.rotateAngleY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.4F) * f1 * 0.5F;
//            this.body.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.2F) * f1 * 0.5F;
//            this.wingLeftBone1.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * -0.2F) * f1 * 0.5F;
//            this.wingRightBone1.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * -0.2F) * f1 * 0.5F;
//            this.thighLeft.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * -0.2F) * f1 * 0.5F;
//            this.thighRight.offsetY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * -0.2F) * f1 * 0.5F;
            this.neck1.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.2F) * f1 * 0.5F + -0.4F;
            this.neck2.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.2F) * f1 * 0.5F + 0.1F;
            this.head.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.2F) * f1 * 0.5F + 0.5F;

/*          this.thighLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 1.2F * limbSwingAmount * 0.5F;
            this.thighRight.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * -1.2F * limbSwingAmount * 0.5F;
            this.tail1.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.15F * limbSwingAmount * 0.5F + 0.2F;
            this.hips.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.1F * limbSwingAmount * 0.5F + -0.35F;
            this.tail2.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.2F * limbSwingAmount * 0.5F + 0.2F;
            this.wingRightBone1.rotateAngleY = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.4F * limbSwingAmount * 0.5F;
            this.wingLeftBone1.rotateAngleY = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.4F * limbSwingAmount * 0.5F;
            this.body.rotationPointX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.2F * limbSwingAmount * 0.5F - 4;
            this.wingLeftBone1.rotationPointX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * -0.2F * limbSwingAmount * 0.5F - 5F;
            this.wingRightBone1.rotationPointX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * -0.2F * limbSwingAmount * 0.5F - 5F;
            this.thighLeft.rotationPointX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * -0.2F * limbSwingAmount * 0.5F - 1;
            this.thighRight.rotationPointX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * -0.2F * limbSwingAmount * 0.5F - 1;
            this.neck1.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.2F * limbSwingAmount * 0.5F + -0.4F;
            this.neck2.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.2F * limbSwingAmount * 0.5F + 0.1F;
            this.head.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.2F * limbSwingAmount * 0.5F + 0.5F;*/
        }
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
