package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.msrandom.wings.entity.passive.HaroldsGreendrakeEntity;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class HaroldsGreendrakeModel extends AgeableModel<HaroldsGreendrakeEntity> {
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer armRight;
    public ModelRenderer armLeft;
    public ModelRenderer legRight;
    public ModelRenderer legLeft;
    public ModelRenderer snout;
    public ModelRenderer eyeRight;
    public ModelRenderer hornLeft;
    public ModelRenderer hornRight;
    public ModelRenderer eyeLeft;

    public HaroldsGreendrakeModel() {
        this.textureWidth = 80;
        this.textureHeight = 80;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, -3.0F, -7.5F);
        this.head.addBox(-4.5F, -3.5F, -6.0F, 9.0F, 7.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.armLeft = new ModelRenderer(this, 0, 30);
        this.armLeft.setRotationPoint(2.0F, -3.0F, -5.0F);
        this.armLeft.addBox(0.0F, 0.0F, -2.0F, 4.0F, 11.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.legRight = new ModelRenderer(this, 0, 30);
        this.legRight.mirror = true;
        this.legRight.setRotationPoint(-2.0F, -3.0F, 4.0F);
        this.legRight.addBox(-4.0F, 0.0F, -2.0F, 4.0F, 11.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.legLeft = new ModelRenderer(this, 0, 30);
        this.legLeft.setRotationPoint(2.0F, -3.0F, 4.0F);
        this.legLeft.addBox(0.0F, 0.0F, -2.0F, 4.0F, 11.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.hornRight = new ModelRenderer(this, 0, 15);
        this.hornRight.mirror = true;
        this.hornRight.setRotationPoint(-2.5F, -3.0F, -3.5F);
        this.hornRight.addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hornRight, -0.2617993877991494F, 0.0F, 0.0F);
        this.hornLeft = new ModelRenderer(this, 0, 15);
        this.hornLeft.setRotationPoint(2.5F, -3.0F, -3.5F);
        this.hornLeft.addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(hornLeft, -0.2617993877991494F, 0.0F, 0.0F);
        this.eyeLeft = new ModelRenderer(this, 14, 15);
        this.eyeLeft.mirror = true;
        this.eyeLeft.setRotationPoint(4.0F, -1.0F, -3.0F);
        this.eyeLeft.addBox(0.0F, -0.8F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.snout = new ModelRenderer(this, 9, 21);
        this.snout.setRotationPoint(0.0F, 1.0F, -6.0F);
        this.snout.addBox(-3.5F, -2.5F, -3.0F, 7.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 3, 33);
        this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.body.addBox(-5.0F, -10.0F, -8.0F, 10.0F, 12.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.armRight = new ModelRenderer(this, 0, 30);
        this.armRight.setRotationPoint(-2.0F, -3.0F, -5.0F);
        this.armRight.addBox(-4.0F, 0.0F, -2.0F, 4.0F, 11.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.eyeRight = new ModelRenderer(this, 14, 15);
        this.eyeRight.setRotationPoint(-4.0F, -1.0F, -3.0F);
        this.eyeRight.addBox(-2.0F, -0.8F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.body.addChild(this.head);
        this.body.addChild(this.armLeft);
        this.body.addChild(this.legRight);
        this.body.addChild(this.legLeft);
        this.head.addChild(this.hornRight);
        this.head.addChild(this.hornLeft);
        this.head.addChild(this.eyeLeft);
        this.head.addChild(this.snout);
        this.body.addChild(this.armRight);
        this.head.addChild(this.eyeRight);
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return Collections.emptyList();
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(body);
    }

    @Override
    public void setRotationAngles(HaroldsGreendrakeEntity entityIn, float f, float f1, float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = 1.0f;
        float degree = 1.0f;
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.armRight.rotateAngleX = MathHelper.cos(f * speed * 0.4F) * degree * -0.8F * f1;
        this.armLeft.rotateAngleX = MathHelper.cos(f * speed * 0.4F) * degree * 0.8F * f1;
        this.legRight.rotateAngleX = MathHelper.cos(f * speed * 0.4F) * degree * 0.8F * f1;
        this.legLeft.rotateAngleX = MathHelper.cos(f * speed * 0.4F) * degree * -0.8F * f1;
        this.head.rotateAngleZ = MathHelper.cos(f * speed * 0.4F) * degree * 0.2F * f1;
        this.body.rotateAngleY = MathHelper.cos(f * speed * 0.4F) * degree * 0.1F * f1;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
