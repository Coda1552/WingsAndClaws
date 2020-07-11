package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.msrandom.wings.entity.passive.MimangoEntity;

public abstract class MimangoModel extends SegmentedModel<MimangoEntity> {
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer tail;
    public ModelRenderer wingLeft;
    public ModelRenderer wingRight;
    public ModelRenderer snout;
    public ModelRenderer hair1;
    public ModelRenderer hair2;
    public ModelRenderer hair3;

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body);
    }

    @Override
    public void setRotationAngles(MimangoEntity entityIn, float f, float f1, float ageInTicks, float netHeadYaw, float headPitch) {
/*        if(entityIn.isHanging()) {
            this.wingLeft.rotateAngleY = -2.0F;
            this.wingLeft.rotateAngleZ = 1.58F;
            this.wingLeft.rotationPointY = 0.05F;
            this.wingRight.rotateAngleY = 2.0F;
            this.wingRight.rotateAngleZ = -1.58F;
            this.wingRight.rotationPointY = 0.05F;
            this.head.rotateAngleX = 1.5F;
            this.body.rotateAngleX = 1.57F;
        }

        if (entityIn.onGround){
            if(f1 >= 0.1f) {
                float speed = 1.0f;
                float degree = 1.0f;
                this.wingLeft.rotateAngleY = MathHelper.cos((f * speed * 0.0F)) * (degree * 0.0F) * f1 * 0.5F + -2.0F;
                this.wingLeft.rotateAngleZ = MathHelper.cos((f * speed * 0.0F)) * (degree * 0.0F) * f1 * 0.5F + 1.58F;
                this.wingLeft.rotationPointY = MathHelper.cos((f * speed * 0.0F)) * (degree * 0.0F) * f1 * 0.5F + 0.05F;
                this.wingRight.rotateAngleY = MathHelper.cos((f * speed * 0.0F)) * (degree * 0.0F) * f1 * 0.5F + 2.0F;
                this.wingRight.rotateAngleZ = MathHelper.cos((f * speed * 0.0F)) * (degree * 0.0F) * f1 * 0.5F + -1.58F;
                this.wingRight.rotationPointY = MathHelper.cos((f * speed * 0.0F)) * (degree * 0.0F) * f1 * 0.5F + 0.05F;
                this.body.rotateAngleY = MathHelper.cos((f * speed * 0.4F)) * (degree * 0.6F) * f1 * 0.5F;
                this.tail.rotateAngleY = MathHelper.cos((f * speed * 0.4F)) * (degree * 0.8F) * f1 * 0.5F;
                this.head.rotateAngleY = MathHelper.cos((f * speed * 0.4F)) * (degree * 0.4F) * f1 * 0.5F;
            } else {
                float speed = 1.0f;
                float degree = 1.0f;
                this.wingLeft.rotateAngleY = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + -2.0F;
                this.wingLeft.rotateAngleZ = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 1.58F;
                this.wingLeft.rotationPointY = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 0.05F;
                this.wingRight.rotateAngleY = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 2.0F;
                this.wingRight.rotateAngleZ = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + -1.58F;
                this.wingRight.rotationPointY = MathHelper.cos((f * speed * 0.0F) + (float) Math.PI) * (degree * 0.0F) * f1 * 0.5F + 0.05F;
            }
        }*/

        if(entityIn.isFlying()) {

        }
        float speed = 1.0f;
        float degree = 1.0f;

        if(entityIn.isChild()) {
            this.wingLeft.rotationPointY = -1.5F;
            this.wingRight.rotationPointY = -1.5F;
        } else {
            this.wingLeft.rotationPointY = -1.0F;
            this.wingRight.rotationPointY = -1.0F;
        }

        //this.body.rotationPointY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F;
        this.wingLeft.rotateAngleZ = MathHelper.cos((f * speed * 0.8F) + (float) Math.PI) * (degree * 2.0F) * f1 * 0.5F;
        this.wingRight.rotateAngleZ = MathHelper.cos((f * speed * 0.8F) + (float) Math.PI) * (degree * -2.0F) * f1 * 0.5F;
        //this.head.rotationPointY = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.05F) * f1 * 0.5F;
        this.tail.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.4F) * f1 * 0.5F + -0.2F;
        this.body.rotateAngleX = MathHelper.cos((f * speed * 0.4F) + (float) Math.PI) * (degree * 0.1F) * f1 * 0.5F + 0.05F;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public static class Adult extends MimangoModel {
        public Adult() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            this.hair1 = new ModelRenderer(this, 0, 0);
            this.hair1.setRotationPoint(0.0F, -2.0F, -1.0F);
            this.hair1.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair1, 0.3490658503988659F, 0.0F, 0.0F);
            this.wingLeft = new ModelRenderer(this, 9, 0);
            this.wingLeft.setRotationPoint(1.5F, -1.5F, 0.0F);
            this.wingLeft.addBox(0.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair2 = new ModelRenderer(this, 0, 0);
            this.hair2.setRotationPoint(-0.5F, -2.0F, -1.0F);
            this.hair2.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair2, 0.3490658503988659F, -0.08726646259971647F, 0.0F);
            this.snout = new ModelRenderer(this, 0, 14);
            this.snout.setRotationPoint(0.0F, -0.5F, -3.0F);
            this.snout.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F);
            this.wingRight = new ModelRenderer(this, 9, 0);
            this.wingRight.mirror = true;
            this.wingRight.setRotationPoint(-1.5F, -1.5F, 0.0F);
            this.wingRight.addBox(-4.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair3 = new ModelRenderer(this, 0, 0);
            this.hair3.setRotationPoint(0.5F, -2.0F, -1.0F);
            this.hair3.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair3, 0.3490658503988659F, 0.08726646259971647F, 0.0F);
            this.head = new ModelRenderer(this, 0, 8);
            this.head.setRotationPoint(0.0F, -0.5F, -1.0F);
            this.head.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 3, 0.0F);
            this.tail = new ModelRenderer(this, 0, 12);
            this.tail.setRotationPoint(0.0F, 0.0F, 2.0F);
            this.tail.addBox(0.0F, -1.5F, 0.0F, 0, 3, 5, 0.0F);
            this.body = new ModelRenderer(this, 0, 0);
            this.body.setRotationPoint(0.0F, 22.5F, 0.0F);
            this.body.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 4, 0.0F);
            this.head.addChild(this.hair1);
            this.body.addChild(this.wingLeft);
            this.head.addChild(this.hair2);
            this.head.addChild(this.snout);
            this.body.addChild(this.wingRight);
            this.head.addChild(this.hair3);
            this.body.addChild(this.head);
            this.body.addChild(this.tail);
        }
    }

    public static class Baby extends MimangoModel {
        public Baby() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            this.body = new ModelRenderer(this, 0, 0);
            this.body.setRotationPoint(0.0F, 23.0F, 0.0F);
            this.body.addBox(-1.0F, -1.0F, -1.5F, 2, 2, 3, 0.0F);
            this.wingRight = new ModelRenderer(this, 9, 0);
            this.wingRight.mirror = true;
            this.wingRight.setRotationPoint(-1.0F, -1.0F, -0.5F);
            this.wingRight.addBox(-4.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.snout = new ModelRenderer(this, 0, 14);
            this.snout.setRotationPoint(0.0F, -0.5F, -2.0F);
            this.snout.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F);
            this.head = new ModelRenderer(this, 0, 8);
            this.head.setRotationPoint(0.0F, 0.0F, -0.5F);
            this.head.addBox(-0.5F, -2.0F, -2.0F, 1, 2, 2, 0.0F);
            this.hair3 = new ModelRenderer(this, 0, 0);
            this.hair3.setRotationPoint(0.4F, -2.0F, -0.4F);
            this.hair3.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair3, 0.3490658503988659F, 0.08726646259971647F, 0.0F);
            this.hair1 = new ModelRenderer(this, 0, 0);
            this.hair1.setRotationPoint(0.0F, -2.0F, -0.4F);
            this.hair1.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair1, 0.3490658503988659F, 0.0F, 0.0F);
            this.tail = new ModelRenderer(this, 0, 12);
            this.tail.setRotationPoint(0.0F, 0.0F, 1.5F);
            this.tail.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
            this.wingLeft = new ModelRenderer(this, 9, 0);
            this.wingLeft.setRotationPoint(1.0F, -1.0F, -0.5F);
            this.wingLeft.addBox(0.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair2 = new ModelRenderer(this, 0, 0);
            this.hair2.setRotationPoint(-0.4F, -2.0F, -0.4F);
            this.hair2.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
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
    }
}
