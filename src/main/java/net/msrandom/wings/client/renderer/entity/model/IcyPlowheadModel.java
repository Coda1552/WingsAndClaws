package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

public abstract class IcyPlowheadModel extends CompositeEntityModel<IcyPlowheadEntity> {
    public ModelPart body;
    public ModelPart armLeft;
    public ModelPart legLeft;
    public ModelPart armRight;
    public ModelPart legRight;
    public ModelPart tail1;
    public ModelPart head;
    public ModelPart tail2;
    public ModelPart tail3;
    public ModelPart headLeft;
    public ModelPart headRight;
    protected Iterable<ModelPart> parts;

    @Override
    public Iterable<ModelPart> getParts() {
        return parts == null ? parts = ImmutableList.of(body) : parts;
    }

    @Override
    public void setAngles(IcyPlowheadEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (limbSwingAmount < 0.1) {
            this.armLeft.rotateAngleZ = MathHelper.cos(entityIn.age * 0.03f) * 0.05f;
            this.armRight.rotateAngleZ = MathHelper.cos(entityIn.age * 0.03f) * -0.05f;
            this.legLeft.rotateAngleZ = MathHelper.cos(entityIn.age * 0.05f) * -0.05f;
            this.legRight.rotateAngleZ = MathHelper.cos(entityIn.age * 0.05f) * 0.05f;

            this.armLeft.rotateAngleY = MathHelper.cos(entityIn.age * 0.03f) * 0.05f;
            this.armRight.rotateAngleY = MathHelper.cos(entityIn.age * 0.03f) * -0.05f;
            this.legLeft.rotateAngleY = MathHelper.cos(entityIn.age * 0.03f) * 0.05f;
            this.legRight.rotateAngleY = MathHelper.cos(entityIn.age * 0.03f) * -0.05f;

            this.body.rotateAngleY = MathHelper.cos(entityIn.age * 0.05f) * 0.03f;
            this.head.rotateAngleY = MathHelper.cos(entityIn.age * 0.03f + 0.3f) * 0.04f;
            this.tail1.rotateAngleY = MathHelper.cos(entityIn.age * 0.1f + 0.2f) * -0.03f;
            this.tail2.rotateAngleY = MathHelper.cos(entityIn.age * 0.05f) * 0.05f;
            this.tail3.rotateAngleY = MathHelper.cos(entityIn.age * 0.03f) * -0.06f;
        } else {
            this.body.rotateAngleY = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * 0.1F;
            this.tail1.rotateAngleY = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * 0.25F;
            this.tail2.rotateAngleY = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * 0.35F;
            this.tail3.rotateAngleY = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * 0.45F;
            this.armLeft.rotateAngleZ = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * 0.75F;
            this.armLeft.rotateAngleY = MathHelper.cos(1.0F + limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount;
            this.armRight.rotateAngleZ = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * -0.75F;
            this.armRight.rotateAngleY = MathHelper.cos(1.0F + limbSwing * 0.4f + (float) Math.PI) * -limbSwingAmount;
            this.legRight.rotateAngleZ = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * 0.75F;
            this.legRight.rotateAngleY = MathHelper.cos(1.0F + limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount + 0.3F;
            this.legLeft.rotateAngleZ = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * -0.75F;
            this.legLeft.rotateAngleY = MathHelper.cos(1.0F + limbSwing * 0.4f + (float) Math.PI) * -limbSwingAmount + -0.3F;
            this.head.rotateAngleY = MathHelper.cos(limbSwing * 0.4f + (float) Math.PI) * limbSwingAmount * -0.1F;
        }
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public static class Adult extends IcyPlowheadModel {
        public Adult() {
            this.textureWidth = 128;
            this.textureHeight = 128;
            this.tail1 = new ModelPart(this, 0, 83);
            this.tail1.setPivot(0.0F, -1.0F, 18.0F);
            this.tail1.addCuboid(-3.0F, -5.0F, 0.0F, 6, 10, 22, 0.0F);
            this.legRight = new ModelPart(this, 50, 0);
            this.legRight.mirror = true;
            this.legRight.setPivot(-7.0F, 6.0F, 12.0F);
            this.legRight.addCuboid(-10.0F, -1.0F, -3.0F, 10, 2, 6, 0.0F);
            this.body = new ModelPart(this, 25, 31);
            this.body.setPivot(0.0F, 17.0F, 0.0F);
            this.body.addCuboid(-7.0F, -7.0F, -18.0F, 14, 14, 36, 0.0F);
            this.tail3 = new ModelPart(this, 48, -9);
            this.tail3.setPivot(0.0F, 0.0F, 22.0F);
            this.tail3.addCuboid(0.0F, -6.0F, 0.0F, 0, 12, 22, 0.0F);
            this.legLeft = new ModelPart(this, 50, 0);
            this.legLeft.setPivot(7.0F, 6.0F, 12.0F);
            this.legLeft.addCuboid(0.0F, -1.0F, -3.0F, 10, 2, 6, 0.0F);
            this.tail2 = new ModelPart(this, 58, 87);
            this.tail2.setPivot(0.0F, -1.0F, 22.0F);
            this.tail2.addCuboid(-2.0F, -3.0F, 0.0F, 4, 6, 22, 0.0F);
            this.armRight = new ModelPart(this, 0, 53);
            this.armRight.mirror = true;
            this.armRight.setPivot(-7.0F, 6.0F, -9.0F);
            this.armRight.addCuboid(-12.0F, -1.0F, -4.0F, 12, 2, 8, 0.0F);
            this.headLeft = new ModelPart(this, 0, 24);
            this.headLeft.mirror = true;
            this.headLeft.setPivot(5.0F, -1.5F, -6.0F);
            this.headLeft.addCuboid(-2.0F, -6.0F, -10.0F, 4, 8, 20, 0.0F);
            this.setRotateAngle(headLeft, 0.2617993877991494F, 0.3490658503988659F, 0.0F);
            this.headRight = new ModelPart(this, 0, 24);
            this.headRight.setPivot(-5.0F, -1.5F, -6.0F);
            this.headRight.addCuboid(-2.0F, -6.0F, -10.0F, 4, 8, 20, 0.0F);
            this.setRotateAngle(headRight, 0.2617993877991494F, -0.3490658503988659F, 0.0F);
            this.head = new ModelPart(this, 0, 0);
            this.head.setPivot(0.0F, 2.0F, -18.0F);
            this.head.addCuboid(-4.0F, -5.0F, -14.0F, 8, 10, 14, 0.0F);
            this.armLeft = new ModelPart(this, 0, 53);
            this.armLeft.setPivot(7.0F, 6.0F, -9.0F);
            this.armLeft.addCuboid(0.0F, -1.0F, -4.0F, 12, 2, 8, 0.0F);
            this.body.addChild(this.tail1);
            this.body.addChild(this.legRight);
            this.tail2.addChild(this.tail3);
            this.body.addChild(this.legLeft);
            this.tail1.addChild(this.tail2);
            this.body.addChild(this.armRight);
            this.head.addChild(this.headLeft);
            this.head.addChild(this.headRight);
            this.body.addChild(this.head);
            this.body.addChild(this.armLeft);
        }
    }

    public static class Child extends IcyPlowheadModel {
        public Child() {
            this.textureWidth = 128;
            this.textureHeight = 128;
            this.body = new ModelPart(this, 0, 0);
            this.body.setPivot(0.0F, 19.5F, 0.0F);
            this.body.addCuboid(-4.5F, -4.5F, -10.0F, 9, 9, 20, 0.0F);
            this.armRight = new ModelPart(this, 38, 0);
            this.armRight.mirror = true;
            this.armRight.setPivot(-4.5F, 3.5F, -5.0F);
            this.armRight.addCuboid(-9.0F, -1.0F, -3.0F, 9, 2, 6, 0.0F);
            this.armLeft = new ModelPart(this, 38, 0);
            this.armLeft.setPivot(4.5F, 3.5F, -5.0F);
            this.armLeft.addCuboid(0.0F, -1.0F, -3.0F, 9, 2, 6, 0.0F);
            this.headRight = new ModelPart(this, 0, 29);
            this.headRight.setPivot(-4.5F, -2.5F, -2.0F);
            this.headRight.addCuboid(-2.0F, -4.0F, -10.0F, 4, 6, 14, 0.0F);
            this.setRotateAngle(headRight, 0.2617993877991494F, -0.3490658503988659F, 0.0F);
            this.head = new ModelPart(this, 48, 19);
            this.head.setPivot(0.0F, 0.5F, -10.0F);
            this.head.addCuboid(-3.0F, -4.0F, -10.0F, 6, 8, 10, 0.0F);
            this.legLeft = new ModelPart(this, 100, 0);
            this.legLeft.setPivot(4.5F, 3.5F, 5.5F);
            this.legLeft.addCuboid(0.0F, -1.0F, -2.5F, 7, 2, 5, 0.0F);
            this.tail3 = new ModelPart(this, 3, 42);
            this.tail3.setPivot(0.0F, 0.0F, 16.0F);
            this.tail3.addCuboid(0.0F, -4.0F, 0.0F, 0, 8, 9, 0.0F);
            this.tail2 = new ModelPart(this, 64, 28);
            this.tail2.setPivot(0.0F, 0.0F, 16.0F);
            this.tail2.addCuboid(-1.0F, -2.0F, 0.0F, 2, 4, 16, 0.0F);
            this.headLeft = new ModelPart(this, 0, 29);
            this.headLeft.setPivot(4.5F, -2.5F, -2.0F);
            this.headLeft.addCuboid(-2.0F, -4.0F, -10.0F, 4, 6, 14, 0.0F);
            this.setRotateAngle(headLeft, 0.2617993877991494F, 0.3490658503988659F, 0.0F);
            this.legRight = new ModelPart(this, 100, 0);
            this.legRight.mirror = true;
            this.legRight.setPivot(-4.5F, 3.5F, 5.5F);
            this.legRight.addCuboid(-7.0F, -1.0F, -2.5F, 7, 2, 5, 0.0F);
            this.tail1 = new ModelPart(this, 76, 0);
            this.tail1.setPivot(0.0F, -0.5F, 10.0F);
            this.tail1.addCuboid(-2.0F, -3.0F, 0.0F, 4, 6, 16, 0.0F);
            this.body.addChild(this.armRight);
            this.body.addChild(this.armLeft);
            this.head.addChild(this.headRight);
            this.body.addChild(this.head);
            this.body.addChild(this.legLeft);
            this.tail2.addChild(this.tail3);
            this.tail1.addChild(this.tail2);
            this.head.addChild(this.headLeft);
            this.body.addChild(this.legRight);
            this.body.addChild(this.tail1);
        }
    }
}
