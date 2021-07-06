package coda.wingsandclaws.client.model;

import coda.wingsandclaws.entity.MimangoEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

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
    private Iterable<ModelRenderer> parts;

    @Override
    public Iterable<ModelRenderer> parts() {
        if (parts == null) return parts = ImmutableList.of(body);
        return parts;
    }

    @Override
    public void setupAnim(MimangoEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn.isHiding()) {
            this.wingLeft.yRot = -2.0F;
            this.wingLeft.zRot = 1.58F;
            this.wingLeft.y = 0.05F;
            this.wingRight.yRot = 2.0F;
            this.wingRight.zRot = -1.58F;
            this.wingRight.y = 0.05F;
            this.head.xRot = 1.5F;
            this.body.xRot = 1.57F;
        } else {
            this.head.xRot = 0;
            this.body.xRot = 0;

            if (entityIn.isFlying()) {
                this.wingLeft.yRot = 0;
                this.wingLeft.zRot = 0;
                this.wingRight.yRot = 0;
                this.wingRight.zRot = 0;
                this.wingLeft.zRot = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * limbSwingAmount;
                this.wingRight.zRot = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * -limbSwingAmount;
                this.tail.xRot = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.2F - 0.2F;
                this.body.xRot = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.05F + 0.05F;
            } else {
                this.wingLeft.yRot = -2.0F;
                this.wingLeft.zRot = 1.58F;
                this.wingRight.yRot = 2.0F;
                this.wingRight.zRot = -1.58F;
                if (limbSwingAmount >= 0.2f) {
                    this.body.yRot = MathHelper.cos(limbSwing * 0.4F) * limbSwingAmount * 0.3F;
                    this.tail.yRot = MathHelper.cos(limbSwing * 0.4F) * limbSwingAmount * 0.4F;
                    this.head.yRot = MathHelper.cos(limbSwing * 0.4F) * limbSwingAmount * 0.2F;
                }
            }
        }
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public static class Adult extends MimangoModel {
        public Adult() {
            this.texWidth = 64;
            this.texHeight = 32;
            this.hair1 = new ModelRenderer(this, 0, 0);
            this.hair1.setPos(0.0F, -2.0F, -1.0F);
            this.hair1.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair1, 0.3490658503988659F, 0.0F, 0.0F);
            this.wingLeft = new ModelRenderer(this, 9, 0);
            this.wingLeft.setPos(1.5F, -1.5F, 0.0F);
            this.wingLeft.addBox(0.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair2 = new ModelRenderer(this, 0, 0);
            this.hair2.setPos(-0.5F, -2.0F, -1.0F);
            this.hair2.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair2, 0.3490658503988659F, -0.08726646259971647F, 0.0F);
            this.snout = new ModelRenderer(this, 0, 14);
            this.snout.setPos(0.0F, -0.5F, -3.0F);
            this.snout.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F);
            this.wingRight = new ModelRenderer(this, 9, 0);
            this.wingRight.mirror = true;
            this.wingRight.setPos(-1.5F, -1.5F, 0.0F);
            this.wingRight.addBox(-4.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair3 = new ModelRenderer(this, 0, 0);
            this.hair3.setPos(0.5F, -2.0F, -1.0F);
            this.hair3.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair3, 0.3490658503988659F, 0.08726646259971647F, 0.0F);
            this.head = new ModelRenderer(this, 0, 8);
            this.head.setPos(0.0F, -0.5F, -1.0F);
            this.head.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 3, 0.0F);
            this.tail = new ModelRenderer(this, 0, 12);
            this.tail.setPos(0.0F, 0.0F, 2.0F);
            this.tail.addBox(0.0F, -1.5F, 0.0F, 0, 3, 5, 0.0F);
            this.body = new ModelRenderer(this, 0, 0);
            this.body.setPos(0.0F, 22.5F, 0.0F);
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

        @Override
        public void setupAnim(MimangoEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            if (!entityIn.isHiding()) {
                if (entityIn.isFlying()) {
                    this.wingLeft.y = -1.5F;
                    this.wingRight.y = -1.5F;
                    this.body.y = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.05F + 22.5F;
                    this.head.y = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.25F - 0.5F;
                } else {
                    this.wingLeft.y = -0.6F;
                    this.wingRight.y = -0.6F;
                    this.body.y = 22.5F;
                    this.head.y = -0.5F;
                }
            }
        }
    }

    public static class Baby extends MimangoModel {
        public Baby() {
            this.texWidth = 64;
            this.texHeight = 32;
            this.wingRight = new ModelRenderer(this, 9, 0);
            this.wingRight.mirror = true;
            this.wingRight.setPos(-1.0F, -1.0F, -0.5F);
            this.wingRight.addBox(-4.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.tail = new ModelRenderer(this, 0, 12);
            this.tail.setPos(0.0F, 0.0F, 1.5F);
            this.tail.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
            this.hair2 = new ModelRenderer(this, 0, 0);
            this.hair2.setPos(-0.4F, -2.0F, -0.4F);
            this.hair2.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair2, 0.3490658503988659F, -0.08726646259971647F, 0.0F);
            this.head = new ModelRenderer(this, 0, 8);
            this.head.setPos(0.0F, 0.0F, -0.5F);
            this.head.addBox(-1.0F, -2.0F, -2.0F, 2, 2, 2, 0.0F);
            this.snout = new ModelRenderer(this, 0, 14);
            this.snout.setPos(0.0F, -0.5F, -2.0F);
            this.snout.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F);
            this.hair1 = new ModelRenderer(this, 0, 0);
            this.hair1.setPos(0.0F, -2.0F, -0.4F);
            this.hair1.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair1, 0.3490658503988659F, 0.0F, 0.0F);
            this.wingLeft = new ModelRenderer(this, 9, 0);
            this.wingLeft.setPos(1.0F, -1.0F, -0.5F);
            this.wingLeft.addBox(0.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.body = new ModelRenderer(this, 0, 0);
            this.body.setPos(0.0F, 23.0F, 0.0F);
            this.body.addBox(-1.0F, -1.0F, -1.5F, 2, 2, 3, 0.0F);
            this.hair3 = new ModelRenderer(this, 0, 0);
            this.hair3.setPos(0.4F, -2.0F, -0.4F);
            this.hair3.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair3, 0.3490658503988659F, 0.08726646259971647F, 0.0F);
            this.body.addChild(this.wingRight);
            this.body.addChild(this.tail);
            this.head.addChild(this.hair2);
            this.body.addChild(this.head);
            this.head.addChild(this.snout);
            this.head.addChild(this.hair1);
            this.body.addChild(this.wingLeft);
            this.head.addChild(this.hair3);
        }

        @Override
        public void setupAnim(MimangoEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            if (!entityIn.isHiding()) {
                if (entityIn.isFlying()) {
                    this.wingLeft.y = -1;
                    this.wingRight.y = -1;
                    this.body.y = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.05F + 23F;
                    this.head.y = MathHelper.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount * 0.25F - 0.5F;
                } else {
                    this.wingLeft.y = -0.95F;
                    this.wingRight.y = -0.95F;
                    this.body.y = 22.5F;
                    this.head.y = 0;
                }
            }
        }
    }
}
