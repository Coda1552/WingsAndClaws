package coda.wingsandclaws.client.renderer.entity.model;

import coda.wingsandclaws.entity.HatchetBeakEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class HatchetBeakModel extends AgeableModel<HatchetBeakEntity> {
    public ModelRenderer body;
    public ModelRenderer neck;
    public ModelRenderer thighLeft;
    public ModelRenderer thighRight;
    public ModelRenderer hips;
    public ModelRenderer wingRightBone1;
    public ModelRenderer wingLeftBone1;
    public ModelRenderer head;
    public ModelRenderer beakTop;
    public ModelRenderer beakBottom;
    public ModelRenderer crest;
    public ModelRenderer legLeft;
    public ModelRenderer legRight;
    public ModelRenderer tail1;
    public ModelRenderer tail2;
    public ModelRenderer tailTip;
    public ModelRenderer tailThing;
    public ModelRenderer tailThing2;
    public ModelRenderer wingRightBone2;
    public ModelRenderer wingRightSkin1;
    public ModelRenderer wingRightSkin1_1;
    public ModelRenderer wingLeftBone2;
    public ModelRenderer wingLeftSkin1;
    public ModelRenderer wingLeftSkin2;
    private final Iterable<ModelRenderer> parts;

    public HatchetBeakModel() {
        this.textureWidth = 256;
        this.textureHeight = 144;
        this.tailThing2 = new ModelRenderer(this, 0, 68);
        this.tailThing2.setRotationPoint(-2.0F, -2.0F, 16.0F);
        this.tailThing2.addBox(-14.0F, 0.0F, 0.0F, 14.0F, 2.0F, 15.0F, 0.0F, 0.0F, 0.0F);
        this.legLeft = new ModelRenderer(this, 60, 0);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(3.5F, 14.0F, 1.0F);
        this.legLeft.addBox(-2.5F, 1.0F, -3.5F, 5.0F, 14.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.tail1 = new ModelRenderer(this, 82, 54);
        this.tail1.setRotationPoint(0.0F, -0.5F, 26.0F);
        this.tail1.addBox(-3.5F, -5.0F, 0.0F, 7.0F, 9.0F, 26.0F, 0.0F, 0.0F, 0.0F);
        this.wingRightBone1 = new ModelRenderer(this, 84, 40);
        this.wingRightBone1.mirror = true;
        this.wingRightBone1.setRotationPoint(-5.0F, -3.5F, -8.0F);
        this.wingRightBone1.addBox(-34.0F, -3.0F, -4.0F, 34.0F, 6.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.wingRightSkin1 = new ModelRenderer(this, -44, 98);
        this.wingRightSkin1.mirror = true;
        this.wingRightSkin1.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.wingRightSkin1.addBox(-34.0F, 0.0F, 2.0F, 34.0F, 0.0F, 44.0F, 0.0F, 0.0F, 0.0F);
        this.tail2 = new ModelRenderer(this, 148, 54);
        this.tail2.setRotationPoint(0.0F, -0.5F, 26.0F);
        this.tail2.addBox(-2.0F, -4.0F, 0.0F, 4.0F, 6.0F, 26.0F, 0.0F, 0.0F, 0.0F);
        this.neck = new ModelRenderer(this, 92, 0);
        this.neck.setRotationPoint(0.0F, -2.0F, -12.0F);
        this.neck.addBox(-5.0F, -24.0F, -6.0F, 10.0F, 30.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.hips = new ModelRenderer(this, 182, 2);
        this.hips.setRotationPoint(0.0F, -2.5F, 19.0F);
        this.hips.addBox(-5.0F, -6.0F, 0.0F, 10.0F, 12.0F, 26.0F, 0.0F, 0.0F, 0.0F);
        this.thighLeft = new ModelRenderer(this, 170, 0);
        this.thighLeft.mirror = true;
        this.thighLeft.setRotationPoint(1.0F, -1.0F, 12.0F);
        this.thighLeft.addBox(0.0F, -1.0F, -6.0F, 7.0F, 16.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, -4.0F, -7.0F);
        this.body.addBox(-7.0F, -9.0F, -13.0F, 14.0F, 16.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.tailThing = new ModelRenderer(this, 0, 68);
        this.tailThing.mirror = true;
        this.tailThing.setRotationPoint(2.0F, -2.0F, 16.0F);
        this.tailThing.addBox(0.0F, 0.0F, 0.0F, 14.0F, 2.0F, 15.0F, 0.0F, 0.0F, 0.0F);
        this.beakTop = new ModelRenderer(this, 48, 48);
        this.beakTop.setRotationPoint(0.0F, 2.0F, -11.0F);
        this.beakTop.addBox(-2.0F, -8.0F, -14.0F, 4.0F, 6.0F, 14.0F, 0.0F, 0.0F, 0.0F);
        this.thighRight = new ModelRenderer(this, 170, 0);
        this.thighRight.setRotationPoint(-1.0F, -1.0F, 12.0F);
        this.thighRight.addBox(-7.0F, -1.0F, -6.0F, 7.0F, 16.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.wingLeftSkin1 = new ModelRenderer(this, -44, 98);
        this.wingLeftSkin1.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.wingLeftSkin1.addBox(0.0F, 0.0F, 2.0F, 34.0F, 0.0F, 44.0F, 0.0F, 0.0F, 0.0F);
        this.beakBottom = new ModelRenderer(this, 66, 54);
        this.beakBottom.setRotationPoint(0.0F, 0.0F, -12.0F);
        this.beakBottom.addBox(-1.5F, 0.0F, -13.0F, 3.0F, 5.0F, 18.0F, 0.0F, 0.0F, 0.0F);
        this.wingRightBone2 = new ModelRenderer(this, 0, 89);
        this.wingRightBone2.mirror = true;
        this.wingRightBone2.setRotationPoint(-34.0F, 0.0F, 0.0F);
        this.wingRightBone2.addBox(-54.0F, -2.0F, -3.0F, 54.0F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.wingRightSkin1_1 = new ModelRenderer(this, 24, 98);
        this.wingRightSkin1_1.mirror = true;
        this.wingRightSkin1_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingRightSkin1_1.addBox(-54.0F, 0.0F, 2.0F, 54.0F, 0.0F, 44.0F, 0.0F, 0.0F, 0.0F);
        this.wingLeftSkin2 = new ModelRenderer(this, 24, 98);
        this.wingLeftSkin2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wingLeftSkin2.addBox(0.0F, 0.0F, 2.0F, 54.0F, 0.0F, 44.0F, 0.0F, 0.0F, 0.0F);
        this.wingLeftBone2 = new ModelRenderer(this, 0, 89);
        this.wingLeftBone2.setRotationPoint(34.0F, 0.0F, 0.0F);
        this.wingLeftBone2.addBox(0.0F, -2.0F, -3.0F, 54.0F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.wingLeftBone1 = new ModelRenderer(this, 84, 40);
        this.wingLeftBone1.setRotationPoint(5.0F, -3.5F, -8.0F);
        this.wingLeftBone1.addBox(0.0F, -3.0F, -4.0F, 34.0F, 6.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 48);
        this.head.setRotationPoint(0.0F, -20.0F, -2.0F);
        this.head.addBox(-6.0F, -5.0F, -12.0F, 12.0F, 8.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.crest = new ModelRenderer(this, 0, 0);
        this.crest.setRotationPoint(0.0F, -8.0F, -7.0F);
        this.crest.addBox(0.0F, -8.0F, -7.0F, 0.0F, 8.0F, 14.0F, 0.0F, 0.0F, 0.0F);
        this.tailTip = new ModelRenderer(this, 10, 0);
        this.tailTip.setRotationPoint(0.0F, -3.0F, 26.0F);
        this.tailTip.addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tailTip, -0.3490658503988659F, 0.0F, 0.0F);
        this.legRight = new ModelRenderer(this, 60, 0);
        this.legRight.setRotationPoint(-3.5F, 14.0F, 1.0F);
        this.legRight.addBox(-2.5F, 1.0F, -3.5F, 5.0F, 14.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.tail2.addChild(this.tailThing2);
        this.thighLeft.addChild(this.legLeft);
        this.hips.addChild(this.tail1);
        this.body.addChild(this.wingRightBone1);
        this.wingRightBone1.addChild(this.wingRightSkin1);
        this.tail1.addChild(this.tail2);
        this.body.addChild(this.neck);
        this.body.addChild(this.hips);
        this.body.addChild(this.thighLeft);
        this.tail2.addChild(this.tailThing);
        this.head.addChild(this.beakTop);
        this.body.addChild(this.thighRight);
        this.wingLeftBone1.addChild(this.wingLeftSkin1);
        this.head.addChild(this.beakBottom);
        this.wingRightBone1.addChild(this.wingRightBone2);
        this.wingRightBone2.addChild(this.wingRightSkin1_1);
        this.wingLeftBone2.addChild(this.wingLeftSkin2);
        this.wingLeftBone1.addChild(this.wingLeftBone2);
        this.body.addChild(this.wingLeftBone1);
        this.neck.addChild(this.head);
        this.beakTop.addChild(this.crest);
        this.tail2.addChild(this.tailTip);
        this.thighRight.addChild(this.legRight);
        parts = ImmutableList.of(this.body);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return parts;
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return Collections.emptyList();
    }

    @Override
    public void setRotationAngles(HatchetBeakEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float flyTimer = entity.getFlyTimer() / 10F;
        if (entity.isFlying()) {
            limbSwingAmount = MathHelper.clamp(limbSwingAmount, -0.5F, 0.5f);
            float speed = 1.0F;
            float degree = 1.0F;
            this.thighLeft.rotateAngleX = MathHelper.cos(-1.25F + limbSwing * speed * 0.2F) * degree * 0.5F * limbSwingAmount + 0.9F;
            this.thighRight.rotateAngleX = MathHelper.cos(-1.0F + limbSwing * speed * 0.2F) * degree * 0.5F * limbSwingAmount + 0.9F;
            this.legLeft.rotateAngleX = MathHelper.cos(-2.25F + limbSwing * speed * 0.2F) * degree * 0.75F * limbSwingAmount + 0.4F;
            this.legRight.rotateAngleX = MathHelper.cos(-2.0F + limbSwing * speed * 0.2F) * degree * 0.75F * limbSwingAmount + 0.4F;
            this.legLeft.rotationPointY = limbSwingAmount - 0.05F + 14.0F;
            this.legRight.rotationPointY = limbSwingAmount - 0.05F + 14.0F;
            this.tail1.rotateAngleX = MathHelper.cos(-3.5F + limbSwing * speed * 0.2F) * degree * 0.6F * limbSwingAmount - 0.05F;
            this.hips.rotateAngleX = MathHelper.cos(-2.5F + limbSwing * speed * 0.2F) * degree * 0.5F * limbSwingAmount;
            this.tail2.rotateAngleX = MathHelper.cos(-3.5F + limbSwing * speed * 0.2F) * degree * 0.7F * limbSwingAmount - 0.1F;
            this.body.rotationPointY = MathHelper.cos(-1.0F + limbSwing * speed * 0.2F) * degree * 0.5F * limbSwingAmount - 4.0F;
            this.neck.rotateAngleX = MathHelper.cos(-1.0F + limbSwing * speed * 0.2F) * degree * 0.5F * limbSwingAmount + 1.8F;
            this.wingRightBone1.rotateAngleZ = MathHelper.cos(limbSwing * speed * 0.2F) * degree * 3.0F * limbSwingAmount;
            this.wingLeftBone1.rotateAngleZ = MathHelper.cos(limbSwing * speed * 0.2F) * degree * -3.0F * limbSwingAmount;
            this.wingRightBone2.rotateAngleZ = MathHelper.cos(-2.0F + limbSwing * speed * 0.2F) * degree * 1.5F * limbSwingAmount;
            this.wingLeftBone2.rotateAngleZ = MathHelper.cos(-2.0F + limbSwing * speed * 0.2F) * degree * -1.5F * limbSwingAmount;
            this.head.rotateAngleX = MathHelper.cos(limbSwing * speed * 0.2F) * degree * -0.5F * limbSwingAmount - 1.75F;

        } else {
            if (flyTimer == 0) {
                flyTimer = 1F;
            }
            this.body.rotateAngleX = MathHelper.cos(limbSwing * 0.4F) * 0.2F * limbSwingAmount + 0.05F;
            this.thighRight.rotateAngleX = MathHelper.cos(limbSwing * 0.4F) * -limbSwingAmount - 0.05F;
            this.thighLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.4F) * limbSwingAmount - 0.05F;
            this.neck.rotateAngleX = MathHelper.cos(1.0F + limbSwing * 0.4F) * 0.2F * limbSwingAmount + 0.2F;
            this.head.rotateAngleX = MathHelper.cos(limbSwing * 0.4F) * -0.2F * limbSwingAmount - 0.2F;
            this.hips.rotateAngleX = MathHelper.cos(limbSwing * 0.4F) * 0.15F * limbSwingAmount - 0.2F;
            this.hips.rotateAngleY = MathHelper.cos(limbSwing * 0.2F) * 0.35F * limbSwingAmount;
            this.tail1.rotateAngleY = MathHelper.cos(0.5F + limbSwing * 0.2F) * 0.2F * limbSwingAmount;
            this.tail1.rotateAngleX = MathHelper.cos(0.5F + limbSwing * 0.4F) * 0.15F * limbSwingAmount + 0.1F;
            this.tail2.rotateAngleY = MathHelper.cos(0.5F + limbSwing * 0.2F) * 0.2F * limbSwingAmount;
            this.tail2.rotateAngleX = MathHelper.cos(0.5F + limbSwing * 0.4F) * 0.15F * limbSwingAmount + 0.2F;
            this.tailThing.rotateAngleZ = MathHelper.cos(limbSwing * 0.4F) * 0.4F * limbSwingAmount + 0.25F;
            this.tailThing2.rotateAngleZ = MathHelper.cos(limbSwing * 0.4F) * 0.4F * limbSwingAmount - 0.25F;
            this.wingRightBone1.rotateAngleY = MathHelper.cos(limbSwing * 0.4F) * 0.6F * limbSwingAmount;
            this.wingRightBone1.rotateAngleZ = 0;
            this.wingRightBone2.rotateAngleZ = 0;
            this.wingLeftBone1.rotateAngleY = MathHelper.cos(limbSwing * 0.4F) * 0.6F * limbSwingAmount;
            this.wingLeftBone1.rotateAngleZ = 0;
            this.wingLeftBone2.rotateAngleZ = 0;
        }
        this.wingRightBone1.rotateAngleZ -= flyTimer * 0.95F;
        this.wingRightBone2.rotateAngleZ += flyTimer * 2.44F;
        this.wingLeftBone1.rotateAngleZ += flyTimer * 0.95F;
        this.wingLeftBone2.rotateAngleZ -= flyTimer * 2.44F;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}