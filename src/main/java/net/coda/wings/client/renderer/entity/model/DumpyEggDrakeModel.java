package net.coda.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.coda.wings.entity.monster.DumpyEggDrakeEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public abstract class DumpyEggDrakeModel extends SegmentedModel<DumpyEggDrakeEntity> {
    public ModelRenderer body;
    public ModelRenderer tail1;
    public ModelRenderer neck;
    public ModelRenderer legLeft;
    public ModelRenderer legRight;
    public ModelRenderer armLeft;
    public ModelRenderer armRight;
    public ModelRenderer tail2;
    public ModelRenderer tailTip;
    public ModelRenderer headJoint;
    public ModelRenderer bandana;
    public ModelRenderer head;
    public ModelRenderer jaw;
    public ModelRenderer hornLeft;
    public ModelRenderer hornRight;
    public final ImmutableList<ModelRenderer> parts;

    public DumpyEggDrakeModel() {
        setAngles();
        this.parts = ImmutableList.of(body);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return parts;
    }

    protected abstract void setAngles();

    @Override
    public void setLivingAnimations(DumpyEggDrakeEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTick);
        float ageInTicks = entity.ticksExisted + partialTick;
        float t = entity.sleepTimer.get(partialTick);
        body.rotateAngleZ = 1.6f * t;
        armLeft.rotateAngleZ = 0.23f * t;
        armRight.rotateAngleZ = -0.28f * t;
        legLeft.rotateAngleZ = 0.1f * t;
        legRight.rotateAngleZ = -0.02f * t;
        legRight.rotateAngleX = -0.01f * t;
        tail1.rotateAngleX = -0.5f * t;
        tail1.rotateAngleY = 0.1f * t;
        tail2.rotateAngleX = -0.6f * t;
        headJoint.rotateAngleX = 0.6f * t;

        if (!entity.isSleeping()) {
            LivingEntity target = entity.getAttackTarget();
            boolean attacking = target != null && entity.getDistanceSq(target) < 4;
            if (attacking) {
                jaw.rotateAngleX = MathHelper.cos(ageInTicks * 0.4f) * 0.16f + 0.2f;
                legLeft.rotateAngleX = MathHelper.cos(ageInTicks * 0.3f) * -0.01f + 0.05f;
                legRight.rotateAngleX = MathHelper.cos(ageInTicks * 0.3f) * -0.01f + 0.05f;
                tail1.rotateAngleY = 0.2f;
                tail2.rotateAngleY = 0.3f;
            } else {
                jaw.rotateAngleX = 0;
                tail1.rotateAngleY = MathHelper.cos(ageInTicks * 0.1f + 0.2f) * 0.15f;
                tail2.rotateAngleY = MathHelper.cos(ageInTicks * 0.1f + 0.15f) * (0.13f + (limbSwingAmount / 2));
                tailTip.rotateAngleY = MathHelper.cos(ageInTicks * 0.1f + 0.1f) * 0.1f;
                legLeft.rotateAngleX = (MathHelper.cos(limbSwing * 0.5f) * limbSwingAmount * 0.5f) - MathHelper.cos(ageInTicks * 0.3f) * 0.01f + 0.1f;
                legRight.rotateAngleX = (MathHelper.cos(limbSwing * 0.5f) * -limbSwingAmount * 0.5f) - MathHelper.cos(ageInTicks * 0.3f) * 0.01f + 0.1f;
            }
            body.rotateAngleX = MathHelper.cos(ageInTicks * (attacking ? 0.1f : 0.3f)) * (0.01f + (attacking ? 0.1f : 0)) - (0.1f + (attacking ? -0.1f : 0));
            armLeft.rotateAngleX = MathHelper.cos(ageInTicks * 0.1f + 0.3f) * (-(limbSwingAmount / (attacking ? 2 : 4)) - 0.1f) + 0.1f;
            armRight.rotateAngleX = MathHelper.cos(ageInTicks * 0.1f + 0.3f) * ((limbSwingAmount / (attacking ? 2 : 4)) + 0.1f) + 0.1f;
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
    public void setRotationAngles(DumpyEggDrakeEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.neck.rotateAngleX = Math.min(headPitch + 10, 0) * ((float) Math.PI / 180F);
        this.headJoint.rotateAngleZ = headPitch * ((float) Math.PI / 180F);
    }

    public static class Adult extends DumpyEggDrakeModel {
        @Override
        protected void setAngles() {
            this.textureWidth = 128;
            this.textureHeight = 128;
            this.hornRight = new ModelRenderer(this, 10, 0);
            this.hornRight.setRotationPoint(-1.5F, -4.0F, -3.0F);
            this.hornRight.addBox(-1.0F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
            this.hornRight.rotateAngleZ = -0.4363323129985824F;
            this.tailTip = new ModelRenderer(this, 92, 19);
            this.tailTip.setRotationPoint(0.0F, 0.0F, 14.0F);
            this.tailTip.addBox(-2.5F, -2.5F, 0.0F, 5, 5, 6, 0.0F);
            this.body = new ModelRenderer(this, 0, 0);
            this.body.setRotationPoint(0.0F, 10.0F, 0.0F);
            this.body.addBox(-4.0F, -5.0F, -8.0F, 8, 10, 16, 0.0F);
            this.hornLeft = new ModelRenderer(this, 10, 0);
            this.hornLeft.setRotationPoint(1.5F, -4.0F, -3.0F);
            this.hornLeft.addBox(0.0F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
            this.hornLeft.rotateAngleZ = 0.4363323129985824F;
            this.jaw = new ModelRenderer(this, 64, 20);
            this.jaw.setRotationPoint(0.0F, 1.5F, 0.0F);
            this.jaw.addBox(-2.0F, 0.0F, -5.5F, 4, 1, 6, 0.0F);
            this.legLeft = new ModelRenderer(this, 86, 0);
            this.legLeft.setRotationPoint(3.0F, 0.0F, 3.5F);
            this.legLeft.addBox(-2.0F, 0.0F, -2.5F, 4, 14, 5, 0.0F);
            this.armLeft = new ModelRenderer(this, 72, 0);
            this.armLeft.setRotationPoint(4.0F, 3.0F, -4.0F);
            this.armLeft.addBox(-1.0F, -1.0F, -1.5F, 2, 6, 3, 0.0F);
            this.head = new ModelRenderer(this, 42, 20);
            this.head.setRotationPoint(0.0F, 1.5F, 0.0F);
            this.head.addBox(-2.5F, -4.0F, -6.0F, 5, 4, 6, 0.0F);
            this.tail2 = new ModelRenderer(this, 65, 19);
            this.tail2.setRotationPoint(0.0F, -0.5F, 13.0F);
            this.tail2.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 21, 0.0F);
            this.headJoint = new ModelRenderer(this, 0, 0);
            this.headJoint.setRotationPoint(0.0F, 0.0F, -8.0F);
            this.headJoint.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
            this.bandana = new ModelRenderer(this, 0, 27);
            this.bandana.setRotationPoint(0.0F, -3.5F, -4.5F);
            this.bandana.addBox(-3.5F, 0.0F, -2.5F, 7, 10, 5, 0.0F);
            this.tail1 = new ModelRenderer(this, 48, 0);
            this.tail1.setRotationPoint(0.0F, -1.0F, 7.0F);
            this.tail1.addBox(-2.5F, -3.0F, 0.0F, 5, 6, 14, 0.0F);
            this.neck = new ModelRenderer(this, 32, 0);
            this.neck.setRotationPoint(0.0F, -1.0F, -7.0F);
            this.neck.addBox(-3.0F, -3.0F, -8.0F, 6, 6, 8, 0.0F);
            this.legRight = new ModelRenderer(this, 86, 0);
            this.legRight.setRotationPoint(-3.0F, 0.0F, 3.5F);
            this.legRight.addBox(-2.0F, 0.0F, -2.5F, 4, 14, 5, 0.0F);
            this.armRight = new ModelRenderer(this, 72, 0);
            this.armRight.setRotationPoint(-4.0F, 3.0F, -4.0F);
            this.armRight.addBox(-1.0F, -1.0F, -1.5F, 2, 6, 3, 0.0F);
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
            this.tail2 = new ModelRenderer(this, 28, 11);
            this.tail2.setRotationPoint(0.0F, 0.0F, 8.0F);
            this.tail2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 12, 0.0F);
            this.legLeft = new ModelRenderer(this, 0, 0);
            this.legLeft.setRotationPoint(2.0F, 0.5F, 0.0F);
            this.legLeft.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
            this.armLeft = new ModelRenderer(this, 20, 0);
            this.armLeft.setRotationPoint(2.5F, 2.0F, -4.5F);
            this.armLeft.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
            this.neck = new ModelRenderer(this, 47, 6);
            this.neck.setRotationPoint(0.0F, -0.5F, -7.0F);
            this.neck.addBox(-1.5F, -2.0F, -5.0F, 3, 4, 5, 0.0F);
            this.armRight = new ModelRenderer(this, 20, 0);
            this.armRight.setRotationPoint(-2.5F, 2.0F, -4.5F);
            this.armRight.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
            this.jaw = new ModelRenderer(this, 53, 2);
            this.jaw.setRotationPoint(0.0F, 0.0F, -0.2F);
            this.jaw.addBox(-1.0F, 0.0F, -2.5F, 2, 1, 3, 0.0F);
            this.tail1 = new ModelRenderer(this, 30, 0);
            this.tail1.setRotationPoint(0.0F, -0.5F, 3.0F);
            this.tail1.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 8, 0.0F);
            this.head = new ModelRenderer(this, 44, 0);
            this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.head.addBox(-1.5F, -2.0F, -3.0F, 3, 2, 3, 0.0F);
            this.hornLeft = new ModelRenderer(this, 53, 0);
            this.hornLeft.setRotationPoint(0.5F, -2.0F, -1.0F);
            this.hornLeft.addBox(0.0F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
            this.hornLeft.rotateAngleZ = 0.4363323129985824F;
            this.hornRight = new ModelRenderer(this, 53, 0);
            this.hornRight.setRotationPoint(-0.5F, -2.0F, -1.0F);
            this.hornRight.addBox(-1.0F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
            this.hornRight.rotateAngleZ = -0.4363323129985824F;
            this.headJoint = new ModelRenderer(this, 34, 0);
            this.headJoint.setRotationPoint(0.0F, 0.5F, -5.0F);
            this.headJoint.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
            this.bandana = new ModelRenderer(this, 0, 18);
            this.bandana.setRotationPoint(0.0F, -0.5F, -1.5F);
            this.bandana.addBox(-2.0F, -2.0F, -2.5F, 4, 7, 3, 0.0F);
            this.legRight = new ModelRenderer(this, 0, 0);
            this.legRight.setRotationPoint(-2.0F, 0.5F, 0.0F);
            this.legRight.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
            this.body = new ModelRenderer(this, 0, 0);
            this.body.setRotationPoint(0.0F, 16.5F, 2.0F);
            this.body.addBox(-2.5F, -3.0F, -7.0F, 5, 6, 10, 0.0F);
            this.tailTip = new ModelRenderer(this, 24, 0);
            this.tailTip.setRotationPoint(0.0F, 0.0F, 7.0F);
            this.tailTip.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 4, 0.0F);
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
