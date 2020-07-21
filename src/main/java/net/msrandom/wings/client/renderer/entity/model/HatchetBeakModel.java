package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

public class HatchetBeakModel extends CompositeEntityModel<HatchetBeakEntity> {
    public ModelPart body;
    public ModelPart neck1;
    public ModelPart thighLeft;
    public ModelPart thighRight;
    public ModelPart hips;
    public ModelPart wingRightBone1;
    public ModelPart wingLeftBone1;
    public ModelPart neck2;
    public ModelPart head;
    public ModelPart mouth;
    public ModelPart jaw;
    public ModelPart beakTop;
    public ModelPart beakBottom;
    public ModelPart legLeft;
    public ModelPart legRight;
    public ModelPart tail1;
    public ModelPart tail2;
    public ModelPart tailLeft;
    public ModelPart tailRight;
    public ModelPart tailTip;
    public ModelPart wingRightBone2;
    public ModelPart wingRightSkin1;
    public ModelPart wingRightSkin1_1;
    public ModelPart wingLeftBone2;
    public ModelPart wingLeftSkin1;
    public ModelPart wingLeftSkin2;

    public HatchetBeakModel() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.wingRightSkin1_1 = new ModelPart(this, 0, 150);
        this.wingRightSkin1_1.mirror = true;
        this.wingRightSkin1_1.setPivot(0.0F, 0.0F, 0.0F);
        this.wingRightSkin1_1.addCuboid(-44.0F, 0.0F, 2.0F, 44, 0, 44, 0.0F);
        this.tailRight = new ModelPart(this, 0, 66);
        this.tailRight.setPivot(-2.0F, 0.0F, 25.0F);
        this.tailRight.addCuboid(-10.0F, 0.0F, -8.0F, 10, 1, 16, 0.0F);
        this.tailTip = new ModelPart(this, 10, 19);
        this.tailTip.setPivot(0.0F, -1.0F, 26.0F);
        this.tailTip.addCuboid(-1.5F, 0.0F, 0.0F, 3, 4, 8, 0.0F);
        this.setRotateAngle(tailTip, -0.3490658503988659F, 0.0F, 0.0F);
        this.beakTop = new ModelPart(this, 228, 11);
        this.beakTop.setPivot(0.0F, -4.0F, -12.0F);
        this.beakTop.addCuboid(-0.5F, -6.0F, 0.0F, 1, 6, 10, 0.0F);
        this.setRotateAngle(beakTop, -0.17453292519943295F, 0.0F, 0.0F);
        this.thighLeft = new ModelPart(this, 108, 0);
        this.thighLeft.mirror = true;
        this.thighLeft.setPivot(1.0F, -1.0F, 12.0F);
        this.thighLeft.addCuboid(0.0F, -1.0F, -6.0F, 7, 14, 12, 0.0F);
        this.legLeft = new ModelPart(this, 222, 46);
        this.legLeft.mirror = true;
        this.legLeft.setPivot(3.5F, 14.0F, 0.5F);
        this.legLeft.addCuboid(-2.5F, -1.0F, -3.5F, 5, 16, 9, 0.0F);
        this.neck2 = new ModelPart(this, 196, 0);
        this.neck2.setPivot(0.0F, -1.0F, -13.0F);
        this.neck2.addCuboid(-4.0F, -4.0F, -13.0F, 8, 8, 13, 0.0F);
        this.wingRightBone1 = new ModelPart(this, 0, 52);
        this.wingRightBone1.mirror = true;
        this.wingRightBone1.setPivot(-5.0F, -5.0F, -8.0F);
        this.wingRightBone1.addCuboid(-44.0F, -3.0F, -4.0F, 44, 6, 8, 0.0F);
        this.wingLeftBone1 = new ModelPart(this, 0, 52);
        this.wingLeftBone1.setPivot(5.0F, -5.0F, -8.0F);
        this.wingLeftBone1.addCuboid(0.0F, -3.0F, -4.0F, 44, 6, 8, 0.0F);
        this.body = new ModelPart(this, 0, 0);
        this.body.setPivot(0.0F, -4.0F, -7.0F);
        this.body.addCuboid(-7.0F, -9.0F, -13.0F, 14, 16, 32, 0.0F);
        this.wingLeftSkin2 = new ModelPart(this, 0, 150);
        this.wingLeftSkin2.setPivot(0.0F, 0.0F, 0.0F);
        this.wingLeftSkin2.addCuboid(0.0F, 0.0F, 2.0F, 44, 0, 44, 0.0F);
        this.tail2 = new ModelPart(this, 170, 57);
        this.tail2.setPivot(0.0F, -0.5F, 26.0F);
        this.tail2.addCuboid(-3.0F, -4.0F, 0.0F, 6, 8, 26, 0.0F);
        this.beakBottom = new ModelPart(this, 0, 14);
        this.beakBottom.setPivot(0.0F, 3.0F, -11.0F);
        this.beakBottom.addCuboid(-0.5F, 0.0F, 0.0F, 1, 5, 8, 0.0F);
        this.setRotateAngle(beakBottom, 0.17453292519943295F, 0.0F, 0.0F);
        this.wingRightSkin1 = new ModelPart(this, 0, 100);
        this.wingRightSkin1.mirror = true;
        this.wingRightSkin1.setPivot(0.0F, 0.0F, 1.0F);
        this.wingRightSkin1.addCuboid(-44.0F, 0.0F, 2.0F, 44, 0, 44, 0.0F);
        this.mouth = new ModelPart(this, 184, 40);
        this.mouth.setPivot(0.0F, 1.0F, -11.0F);
        this.mouth.addCuboid(-2.5F, -4.0F, -12.0F, 5, 4, 12, 0.0F);
        this.wingLeftBone2 = new ModelPart(this, 0, 83);
        this.wingLeftBone2.setPivot(44.0F, 0.0F, 0.0F);
        this.wingLeftBone2.addCuboid(0.0F, -2.0F, -3.0F, 44, 4, 6, 0.0F);
        this.jaw = new ModelPart(this, 0, 0);
        this.jaw.setPivot(0.0F, 1.0F, -11.0F);
        this.jaw.addCuboid(-1.5F, 0.0F, -11.0F, 3, 3, 11, 0.0F);
        this.hips = new ModelPart(this, 150, 0);
        this.hips.setPivot(0.0F, -2.5F, 19.0F);
        this.hips.addCuboid(-5.0F, -6.0F, 0.0F, 10, 12, 26, 0.0F);
        this.tailLeft = new ModelPart(this, 0, 66);
        this.tailLeft.mirror = true;
        this.tailLeft.setPivot(2.0F, 0.0F, 25.0F);
        this.tailLeft.addCuboid(0.0F, 0.0F, -8.0F, 10, 1, 16, 0.0F);
        this.tail1 = new ModelPart(this, 102, 52);
        this.tail1.setPivot(0.0F, -0.5F, 26.0F);
        this.tail1.addCuboid(-4.0F, -5.0F, 0.0F, 8, 10, 26, 0.0F);
        this.wingLeftSkin1 = new ModelPart(this, 0, 100);
        this.wingLeftSkin1.setPivot(0.0F, 0.0F, 1.0F);
        this.wingLeftSkin1.addCuboid(0.0F, 0.0F, 2.0F, 44, 0, 44, 0.0F);
        this.wingRightBone2 = new ModelPart(this, 0, 83);
        this.wingRightBone2.setPivot(-44.0F, 0.0F, 0.0F);
        this.wingRightBone2.addCuboid(-44.0F, -2.0F, -3.0F, 44, 4, 6, 0.0F);
        this.neck1 = new ModelPart(this, 60, 0);
        this.neck1.setPivot(0.0F, -2.0F, -12.0F);
        this.neck1.addCuboid(-5.0F, -6.0F, -14.0F, 10, 12, 14, 0.0F);
        this.head = new ModelPart(this, 211, 27);
        this.head.setPivot(0.0F, -0.1F, -12.0F);
        this.head.addCuboid(-4.5F, -4.0F, -11.0F, 9, 8, 11, 0.0F);
        this.thighRight = new ModelPart(this, 108, 0);
        this.thighRight.setPivot(-1.0F, -1.0F, 12.0F);
        this.thighRight.addCuboid(-7.0F, -1.0F, -6.0F, 7, 14, 12, 0.0F);
        this.legRight = new ModelPart(this, 222, 46);
        this.legRight.setPivot(-3.5F, 14.0F, 0.5F);
        this.legRight.addCuboid(-2.5F, -1.0F, -3.5F, 5, 16, 9, 0.0F);
        this.wingRightBone2.addChild(this.wingRightSkin1_1);
        this.tail2.addChild(this.tailRight);
        this.tail2.addChild(this.tailTip);
        this.mouth.addChild(this.beakTop);
        this.body.addChild(this.thighLeft);
        this.thighLeft.addChild(this.legLeft);
        this.neck1.addChild(this.neck2);
        this.body.addChild(this.wingRightBone1);
        this.body.addChild(this.wingLeftBone1);
        this.wingLeftBone2.addChild(this.wingLeftSkin2);
        this.tail1.addChild(this.tail2);
        this.jaw.addChild(this.beakBottom);
        this.wingRightBone1.addChild(this.wingRightSkin1);
        this.head.addChild(this.mouth);
        this.wingLeftBone1.addChild(this.wingLeftBone2);
        this.head.addChild(this.jaw);
        this.body.addChild(this.hips);
        this.tail2.addChild(this.tailLeft);
        this.hips.addChild(this.tail1);
        this.wingLeftBone1.addChild(this.wingLeftSkin1);
        this.wingRightBone1.addChild(this.wingRightBone2);
        this.body.addChild(this.neck1);
        this.neck2.addChild(this.head);
        this.body.addChild(this.thighRight);
        this.thighRight.addChild(this.legRight);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(body);
    }

    @Override
    public void setAngles(HatchetBeakEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.rotationPointX = -4;
        this.wingLeftBone1.rotationPointX = -5;
        this.wingRightBone1.rotationPointX = -5;
        this.thighLeft.rotationPointX = -1;
        this.thighRight.rotationPointX = -1;

        if (entityIn.isFlying()) {
            wingLeftBone1.rotateAngleZ = wingLeftBone2.rotateAngleZ = wingRightBone1.rotateAngleZ = wingRightBone2.rotateAngleZ = 0;
            wingLeftBone1.rotationPointY = -5;
        } else {
            wingLeftBone1.rotationPointX = 5;
            wingLeftBone1.rotateAngleZ = 0.8f;
            wingRightBone1.rotateAngleZ = -wingLeftBone1.rotateAngleZ;
            wingLeftBone2.rotateAngleZ = -1.6f;
            wingRightBone2.rotateAngleZ = -wingRightBone2.rotateAngleZ;
            this.thighLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 1.2F * limbSwingAmount * 0.5F;
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
            this.head.rotateAngleX = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * 0.2F * limbSwingAmount * 0.5F + 0.5F;
        }
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
