package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;

public abstract class DumpyEggDrakeModel extends CompositeEntityModel<DumpyEggDrakeEntity> {
    public ModelPart body;
    public ModelPart tail1;
    public ModelPart neck;
    public ModelPart legLeft;
    public ModelPart legRight;
    public ModelPart armLeft;
    public ModelPart armRight;
    public ModelPart tail2;
    public ModelPart tailTip;
    public ModelPart headJoint;
    public ModelPart bandana;
    public ModelPart head;
    public ModelPart jaw;
    public ModelPart hornLeft;
    public ModelPart hornRight;

    public DumpyEggDrakeModel() {
        setAngles();
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(body);
    }

    protected abstract void setAngles();

    @Override
    public void animateModel(DumpyEggDrakeEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.animateModel(entityIn, limbSwing, limbSwingAmount, partialTick);
        if (entityIn.isSleeping()) {
            body.rotateAngleZ = 1.6f;
            armLeft.rotateAngleZ = 0.23f;
            armRight.rotateAngleZ = -0.28f;
            legLeft.rotateAngleZ = 0.1f;
            legRight.rotateAngleZ = -0.02f;
            legRight.rotateAngleX = -0.01f;
            tail1.rotateAngleX = -0.5f;
            tail1.rotateAngleY = 0.1f;
            tail2.rotateAngleX = -0.6f;
            headJoint.rotateAngleX = 0.6f;
        } else {
            LivingEntity target = entityIn.getTarget();
            boolean attacking = target != null && entityIn.squaredDistanceTo(target) < 4;
            if (attacking) {
                jaw.rotateAngleX = MathHelper.cos(entityIn.age * 0.4f) * 0.16f + 0.2f;
                legLeft.rotateAngleX = MathHelper.cos(entityIn.age * 0.3f) * -0.01f + 0.05f;
                legRight.rotateAngleX = MathHelper.cos(entityIn.age * 0.3f) * -0.01f + 0.05f;
                tail1.rotateAngleY = 0.2f;
                tail2.rotateAngleY = 0.3f;
            } else {
                jaw.rotateAngleX = 0;
                tail1.rotateAngleY = MathHelper.cos(entityIn.age * 0.1f + 0.2f) * 0.15f;
                tail2.rotateAngleY = MathHelper.cos(entityIn.age * 0.1f + 0.15f) * (0.13f + (limbSwingAmount / 2));
                tailTip.rotateAngleY = MathHelper.cos(entityIn.age * 0.1f + 0.1f) * 0.1f;
                legLeft.rotateAngleX = (MathHelper.cos(limbSwing * 0.5f) * limbSwingAmount * 0.5f) - MathHelper.cos(entityIn.age * 0.3f) * 0.01f + 0.1f;
                legRight.rotateAngleX = (MathHelper.cos(limbSwing * 0.5f) * -limbSwingAmount * 0.5f) - MathHelper.cos(entityIn.age * 0.3f) * 0.01f + 0.1f;
            }
            body.rotateAngleX = MathHelper.cos(entityIn.age * (attacking ? 0.1f : 0.3f)) * (0.01f + (attacking ? 0.1f : 0)) - (0.1f + (attacking ? -0.1f : 0));
            armLeft.rotateAngleX = MathHelper.cos(entityIn.age * 0.1f + 0.3f) * (-(limbSwingAmount / (attacking ? 2 : 4)) - 0.1f) + 0.1f;
            armRight.rotateAngleX = MathHelper.cos(entityIn.age * 0.1f + 0.3f) * ((limbSwingAmount / (attacking ? 2 : 4)) + 0.1f) + 0.1f;
            body.rotateAngleZ = 0;
            armLeft.rotateAngleZ = 0;
            armRight.rotateAngleZ = 0;
            legLeft.rotateAngleZ = 0;
            legRight.rotateAngleZ = 0;
            tail1.rotateAngleX = 0;
            tail2.rotateAngleX = 0;
            headJoint.rotateAngleX = 0;
        }
    }

    @Override
    public void setAngles(DumpyEggDrakeEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.neck.rotateAngleX = Math.min(headPitch + 10, 0) * ((float) Math.PI / 180F);
        this.headJoint.rotateAngleZ = headPitch * ((float) Math.PI / 180F);
    }

    public static class Adult extends DumpyEggDrakeModel {
        @Override
        protected void setAngles() {
            this.textureWidth = 128;
            this.textureHeight = 128;
            this.hornRight = new ModelPart(this, 10, 0);
            this.hornRight.setPivot(-1.5F, -4.0F, -3.0F);
            this.hornRight.addCuboid(-1.0F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
            this.hornRight.rotateAngleZ = -0.4363323129985824F;
            this.tailTip = new ModelPart(this, 92, 19);
            this.tailTip.setPivot(0.0F, 0.0F, 14.0F);
            this.tailTip.addCuboid(-2.5F, -2.5F, 0.0F, 5, 5, 6, 0.0F);
            this.body = new ModelPart(this, 0, 0);
            this.body.setPivot(0.0F, 10.0F, 0.0F);
            this.body.addCuboid(-4.0F, -5.0F, -8.0F, 8, 10, 16, 0.0F);
            this.hornLeft = new ModelPart(this, 10, 0);
            this.hornLeft.setPivot(1.5F, -4.0F, -3.0F);
            this.hornLeft.addCuboid(0.0F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
            this.hornLeft.rotateAngleZ = 0.4363323129985824F;
            this.jaw = new ModelPart(this, 64, 20);
            this.jaw.setPivot(0.0F, 1.5F, 0.0F);
            this.jaw.addCuboid(-2.0F, 0.0F, -5.5F, 4, 1, 6, 0.0F);
            this.legLeft = new ModelPart(this, 86, 0);
            this.legLeft.setPivot(3.0F, 0.0F, 3.5F);
            this.legLeft.addCuboid(-2.0F, 0.0F, -2.5F, 4, 14, 5, 0.0F);
            this.armLeft = new ModelPart(this, 72, 0);
            this.armLeft.setPivot(4.0F, 3.0F, -4.0F);
            this.armLeft.addCuboid(-1.0F, -1.0F, -1.5F, 2, 6, 3, 0.0F);
            this.head = new ModelPart(this, 42, 20);
            this.head.setPivot(0.0F, 1.5F, 0.0F);
            this.head.addCuboid(-2.5F, -4.0F, -6.0F, 5, 4, 6, 0.0F);
            this.tail2 = new ModelPart(this, 65, 19);
            this.tail2.setPivot(0.0F, -0.5F, 13.0F);
            this.tail2.addCuboid(-1.5F, -1.5F, 0.0F, 3, 3, 21, 0.0F);
            this.headJoint = new ModelPart(this, 0, 0);
            this.headJoint.setPivot(0.0F, 0.0F, -8.0F);
            this.headJoint.addCuboid(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
            this.bandana = new ModelPart(this, 0, 27);
            this.bandana.setPivot(0.0F, -3.5F, -4.5F);
            this.bandana.addCuboid(-3.5F, 0.0F, -2.5F, 7, 10, 5, 0.0F);
            this.tail1 = new ModelPart(this, 48, 0);
            this.tail1.setPivot(0.0F, -1.0F, 7.0F);
            this.tail1.addCuboid(-2.5F, -3.0F, 0.0F, 5, 6, 14, 0.0F);
            this.neck = new ModelPart(this, 32, 0);
            this.neck.setPivot(0.0F, -1.0F, -7.0F);
            this.neck.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 8, 0.0F);
            this.legRight = new ModelPart(this, 86, 0);
            this.legRight.setPivot(-3.0F, 0.0F, 3.5F);
            this.legRight.addCuboid(-2.0F, 0.0F, -2.5F, 4, 14, 5, 0.0F);
            this.armRight = new ModelPart(this, 72, 0);
            this.armRight.setPivot(-4.0F, 3.0F, -4.0F);
            this.armRight.addCuboid(-1.0F, -1.0F, -1.5F, 2, 6, 3, 0.0F);
            this.head.addChild(this.hornRight);
            this.tail2.addChild(this.tailTip);
            this.head.addChild(this.hornLeft);
            this.headJoint.addChild(this.jaw);
            this.body.addChild(this.legLeft);
            this.body.addChild(this.armLeft);
            this.headJoint.addChild(this.head);
            this.neck.addChild(this.bandana);
            this.tail1.addChild(this.tail2);
            this.neck.addChild(this.headJoint);
            this.body.addChild(this.tail1);
            this.body.addChild(this.neck);
            this.body.addChild(this.legRight);
            this.body.addChild(this.armRight);
        }
    }

    public static class Child extends DumpyEggDrakeModel {
        @Override
        protected void setAngles() {
            this.textureWidth = 64;
            this.textureHeight = 64;
            this.tail2 = new ModelPart(this, 28, 11);
            this.tail2.setPivot(0.0F, 0.0F, 8.0F);
            this.tail2.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 12, 0.0F);
            this.legLeft = new ModelPart(this, 0, 0);
            this.legLeft.setPivot(2.0F, 0.5F, 0.0F);
            this.legLeft.addCuboid(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
            this.armLeft = new ModelPart(this, 20, 0);
            this.armLeft.setPivot(2.5F, 2.0F, -4.5F);
            this.armLeft.addCuboid(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
            this.neck = new ModelPart(this, 47, 6);
            this.neck.setPivot(0.0F, -0.5F, -7.0F);
            this.neck.addCuboid(-1.5F, -2.0F, -5.0F, 3, 4, 5, 0.0F);
            this.armRight = new ModelPart(this, 20, 0);
            this.armRight.setPivot(-2.5F, 2.0F, -4.5F);
            this.armRight.addCuboid(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
            this.jaw = new ModelPart(this, 53, 2);
            this.jaw.setPivot(0.0F, 0.0F, -0.2F);
            this.jaw.addCuboid(-1.0F, 0.0F, -2.5F, 2, 1, 3, 0.0F);
            this.tail1 = new ModelPart(this, 30, 0);
            this.tail1.setPivot(0.0F, -0.5F, 3.0F);
            this.tail1.addCuboid(-1.5F, -1.5F, 0.0F, 3, 3, 8, 0.0F);
            this.head = new ModelPart(this, 44, 0);
            this.head.setPivot(0.0F, 0.0F, 0.0F);
            this.head.addCuboid(-1.5F, -2.0F, -3.0F, 3, 2, 3, 0.0F);
            this.hornLeft = new ModelPart(this, 53, 0);
            this.hornLeft.setPivot(0.5F, -2.0F, -1.0F);
            this.hornLeft.addCuboid(0.0F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
            this.hornLeft.rotateAngleZ = 0.4363323129985824F;
            this.hornRight = new ModelPart(this, 53, 0);
            this.hornRight.setPivot(-0.5F, -2.0F, -1.0F);
            this.hornRight.addCuboid(-1.0F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
            this.hornRight.rotateAngleZ = -0.4363323129985824F;
            this.headJoint = new ModelPart(this, 34, 0);
            this.headJoint.setPivot(0.0F, 0.5F, -5.0F);
            this.headJoint.addCuboid(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
            this.bandana = new ModelPart(this, 0, 18);
            this.bandana.setPivot(0.0F, -0.5F, -1.5F);
            this.bandana.addCuboid(-2.0F, -2.0F, -2.5F, 4, 7, 3, 0.0F);
            this.legRight = new ModelPart(this, 0, 0);
            this.legRight.setPivot(-2.0F, 0.5F, 0.0F);
            this.legRight.addCuboid(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
            this.body = new ModelPart(this, 0, 0);
            this.body.setPivot(0.0F, 16.5F, 2.0F);
            this.body.addCuboid(-2.5F, -3.0F, -7.0F, 5, 6, 10, 0.0F);
            this.tailTip = new ModelPart(this, 24, 0);
            this.tailTip.setPivot(0.0F, 0.0F, 7.0F);
            this.tailTip.addCuboid(-1.5F, -1.5F, 0.0F, 3, 3, 4, 0.0F);
            this.tail1.addChild(this.tail2);
            this.body.addChild(this.legLeft);
            this.body.addChild(this.armLeft);
            this.body.addChild(this.neck);
            this.body.addChild(this.armRight);
            this.headJoint.addChild(this.jaw);
            this.body.addChild(this.tail1);
            this.headJoint.addChild(this.head);
            this.neck.addChild(this.bandana);
            this.head.addChild(this.hornLeft);
            this.head.addChild(this.hornRight);
            this.neck.addChild(this.headJoint);
            this.body.addChild(this.legRight);
            this.tail2.addChild(this.tailTip);
        }
    }
}
