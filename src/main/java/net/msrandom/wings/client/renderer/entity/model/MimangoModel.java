package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;
import net.msrandom.wings.entity.passive.MimangoEntity;

public abstract class MimangoModel extends CompositeEntityModel<MimangoEntity> {
    public ModelPart body;
    public ModelPart head;
    public ModelPart tail;
    public ModelPart wingLeft;
    public ModelPart wingRight;
    public ModelPart snout;
    public ModelPart hair1;
    public ModelPart hair2;
    public ModelPart hair3;
    private Iterable<ModelPart> parts;

    @Override
    public Iterable<ModelPart> getParts() {
        if (parts == null) return parts = ImmutableList.of(body);
        return parts;
    }

    @Override
    public void setAngles(MimangoEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn.isHiding()) {
            this.wingLeft.yaw = -2.0F;
            this.wingLeft.roll = 1.58F;
            this.wingLeft.pivotY = 0.05F;
            this.wingRight.yaw = 2.0F;
            this.wingRight.roll = -1.58F;
            this.wingRight.pivotY = 0.05F;
            this.head.pitch = 1.5F;
            this.body.pitch = 1.57F;
        } else {
            this.head.pitch = 0;
            this.body.pitch = 0;

            if (entityIn.isFlying()) {
                this.wingLeft.yaw = 0;
                this.wingLeft.roll = 0;
                this.wingRight.yaw = 0;
                this.wingRight.roll = 0;
                this.wingLeft.roll = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * limbSwingAmount;
                this.wingRight.roll = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * -limbSwingAmount;
                this.tail.pitch = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.2F - 0.2F;
                this.body.pitch = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.05F + 0.05F;
            } else {
                this.wingLeft.yaw = -2.0F;
                this.wingLeft.roll = 1.58F;
                this.wingRight.yaw = 2.0F;
                this.wingRight.roll = -1.58F;
                if (limbSwingAmount >= 0.2f) {
                    this.body.yaw = MathHelper.cos(limbSwing * 0.4F) * limbSwingAmount * 0.3F;
                    this.tail.yaw = MathHelper.cos(limbSwing * 0.4F) * limbSwingAmount * 0.4F;
                    this.head.yaw = MathHelper.cos(limbSwing * 0.4F) * limbSwingAmount * 0.2F;
                }
            }
        }
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }

    public static class Adult extends MimangoModel {
        public Adult() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            this.hair1 = new ModelPart(this, 0, 0);
            this.hair1.setPivot(0.0F, -2.0F, -1.0F);
            this.hair1.addCuboid(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair1, 0.3490658503988659F, 0.0F, 0.0F);
            this.wingLeft = new ModelPart(this, 9, 0);
            this.wingLeft.setPivot(1.5F, -1.5F, 0.0F);
            this.wingLeft.addCuboid(0.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair2 = new ModelPart(this, 0, 0);
            this.hair2.setPivot(-0.5F, -2.0F, -1.0F);
            this.hair2.addCuboid(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair2, 0.3490658503988659F, -0.08726646259971647F, 0.0F);
            this.snout = new ModelPart(this, 0, 14);
            this.snout.setPivot(0.0F, -0.5F, -3.0F);
            this.snout.addCuboid(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F);
            this.wingRight = new ModelPart(this, 9, 0);
            this.wingRight.mirror = true;
            this.wingRight.setPivot(-1.5F, -1.5F, 0.0F);
            this.wingRight.addCuboid(-4.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair3 = new ModelPart(this, 0, 0);
            this.hair3.setPivot(0.5F, -2.0F, -1.0F);
            this.hair3.addCuboid(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair3, 0.3490658503988659F, 0.08726646259971647F, 0.0F);
            this.head = new ModelPart(this, 0, 8);
            this.head.setPivot(0.0F, -0.5F, -1.0F);
            this.head.addCuboid(-1.0F, -2.0F, -3.0F, 2, 2, 3, 0.0F);
            this.tail = new ModelPart(this, 0, 12);
            this.tail.setPivot(0.0F, 0.0F, 2.0F);
            this.tail.addCuboid(0.0F, -1.5F, 0.0F, 0, 3, 5, 0.0F);
            this.body = new ModelPart(this, 0, 0);
            this.body.setPivot(0.0F, 22.5F, 0.0F);
            this.body.addCuboid(-1.5F, -1.5F, -2.0F, 3, 3, 4, 0.0F);
            this.head.addChild(this.hair1);
            this.body.addChild(this.wingLeft);
            this.head.addChild(this.hair2);
            this.head.addChild(this.snout);
            this.body.addChild(this.wingRight);
            this.head.addChild(this.hair3);
            this.body.addChild(this.head);
            this.body.addChild(this.tail);
        }

        @Override
        public void setAngles(MimangoEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            if (!entityIn.isHiding()) {
                if (entityIn.isFlying()) {
                    this.wingLeft.pivotY = -1.5F;
                    this.wingRight.pivotY = -1.5F;
                    this.body.pivotY = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.05F + 22.5F;
                    this.head.pivotY = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.25F - 0.5F;
                } else {
                    this.wingLeft.pivotY = -1.45F;
                    this.wingRight.pivotY = -1.45F;
                    this.body.pivotY = 22.5F;
                    this.head.pivotY = -0.5F;
                }
            }
        }
    }

    public static class Baby extends MimangoModel {
        public Baby() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            this.body = new ModelPart(this, 0, 0);
            this.body.setPivot(0.0F, 23.0F, 0.0F);
            this.body.addCuboid(-1.0F, -1.0F, -1.5F, 2, 2, 3, 0.0F);
            this.wingRight = new ModelPart(this, 9, 0);
            this.wingRight.mirror = true;
            this.wingRight.setPivot(-1.0F, -1.0F, -0.5F);
            this.wingRight.addCuboid(-4.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.snout = new ModelPart(this, 0, 14);
            this.snout.setPivot(0.0F, -0.5F, -2.0F);
            this.snout.addCuboid(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F);
            this.head = new ModelPart(this, 0, 8);
            this.head.setPivot(0.0F, 0.0F, -0.5F);
            this.head.addCuboid(-0.5F, -2.0F, -2.0F, 1, 2, 2, 0.0F);
            this.hair3 = new ModelPart(this, 0, 0);
            this.hair3.setPivot(0.4F, -2.0F, -0.4F);
            this.hair3.addCuboid(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair3, 0.3490658503988659F, 0.08726646259971647F, 0.0F);
            this.hair1 = new ModelPart(this, 0, 0);
            this.hair1.setPivot(0.0F, -2.0F, -0.4F);
            this.hair1.addCuboid(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair1, 0.3490658503988659F, 0.0F, 0.0F);
            this.tail = new ModelPart(this, 0, 12);
            this.tail.setPivot(0.0F, 0.0F, 1.5F);
            this.tail.addCuboid(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
            this.wingLeft = new ModelPart(this, 9, 0);
            this.wingLeft.setPivot(1.0F, -1.0F, -0.5F);
            this.wingLeft.addCuboid(0.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair2 = new ModelPart(this, 0, 0);
            this.hair2.setPivot(-0.4F, -2.0F, -0.4F);
            this.hair2.addCuboid(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair2, 0.3490658503988659F, -0.08726646259971647F, 0.0F);
            this.body.addChild(this.wingRight);
            this.head.addChild(this.snout);
            this.body.addChild(this.head);
            this.head.addChild(this.hair3);
            this.head.addChild(this.hair1);
            this.body.addChild(this.tail);
            this.body.addChild(this.wingLeft);
            this.head.addChild(this.hair2);
        }

        @Override
        public void setAngles(MimangoEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            if (!entityIn.isHiding()) {
                if (entityIn.isFlying()) {
                    this.wingLeft.pivotY = -1;
                    this.wingRight.pivotY = -1;
                    this.body.pivotY = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.05F + 23F;
                    this.head.pivotY = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.25F - 0.5F;
                } else {
                    this.wingLeft.pivotY = -9.95F;
                    this.wingRight.pivotY = -9.95F;
                    this.body.pivotY = 22.5F;
                    this.head.pivotY = 0;
                }
            }
        }
    }
}
