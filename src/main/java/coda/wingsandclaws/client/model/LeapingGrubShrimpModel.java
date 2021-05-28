/*
package coda.wingsandclaws.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import coda.wingsandclaws.entity.LeapingGrubShrimpEntity;

@OnlyIn(Dist.CLIENT)
public class LeapingGrubShrimpModel<T extends Entity> extends EntityModel<LeapingGrubShrimpEntity> {
    public ModelRenderer body;
    public ModelRenderer legs1;
    public ModelRenderer antennaright;
    public ModelRenderer antennaleft;
    public ModelRenderer paddleleft;
    public ModelRenderer paddleright;
    public ModelRenderer tail;
    public ModelRenderer legs2;
    public ModelRenderer legs3;
    public ModelRenderer legs4;
    public ModelRenderer tailfan;

    public LeapingGrubShrimpModel() {
        this.textureWidth = 44;
        this.textureHeight = 22;
        this.tail = new ModelRenderer(this, 21, 0);
        this.tail.setRotationPoint(0.0F, -0.5F, 3.0F);
        this.tail.addBox(-1.5F, -0.5F, 0.0F, 3.0F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.body.addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.antennaleft = new ModelRenderer(this, 0, 10);
        this.antennaleft.mirror = true;
        this.antennaleft.setRotationPoint(1.0F, 1.0F, -2.5F);
        this.antennaleft.addBox(0.0F, -5.0F, -1.0F, 0.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(antennaleft, 0.3490658503988659F, 0.0F, 0.0F);
        this.paddleright = new ModelRenderer(this, 0, 11);
        this.paddleright.setRotationPoint(-2.0F, 2.0F, -1.0F);
        this.paddleright.addBox(-5.0F, 0.0F, -0.5F, 5.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.legs3 = new ModelRenderer(this, 0, 0);
        this.legs3.mirror = true;
        this.legs3.setRotationPoint(0.0F, 2.0F, 1.5F);
        this.legs3.addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.antennaright = new ModelRenderer(this, 0, 10);
        this.antennaright.setRotationPoint(-1.0F, 1.0F, -2.5F);
        this.antennaright.addBox(0.0F, -5.0F, -1.0F, 0.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(antennaright, 0.3490658503988659F, 0.0F, 0.0F);
        this.legs2 = new ModelRenderer(this, 0, 0);
        this.legs2.mirror = true;
        this.legs2.setRotationPoint(0.0F, 2.0F, 0.5F);
        this.legs2.addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.legs4 = new ModelRenderer(this, 0, 0);
        this.legs4.mirror = true;
        this.legs4.setRotationPoint(0.0F, 2.0F, 2.5F);
        this.legs4.addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.tailfan = new ModelRenderer(this, 17, 10);
        this.tailfan.setRotationPoint(0.0F, 0.5F, 5.0F);
        this.tailfan.addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.paddleleft = new ModelRenderer(this, 0, 11);
        this.paddleleft.mirror = true;
        this.paddleleft.setRotationPoint(2.0F, 2.0F, -1.0F);
        this.paddleleft.addBox(0.0F, 0.0F, -0.5F, 5.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.legs1 = new ModelRenderer(this, 0, 0);
        this.legs1.mirror = true;
        this.legs1.setRotationPoint(0.0F, 2.0F, -0.5F);
        this.legs1.addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.body.addChild(this.tail);
        this.body.addChild(this.antennaleft);
        this.body.addChild(this.paddleright);
        this.body.addChild(this.legs3);
        this.body.addChild(this.antennaright);
        this.body.addChild(this.legs2);
        this.body.addChild(this.legs4);
        this.tail.addChild(this.tailfan);
        this.body.addChild(this.paddleleft);
        this.body.addChild(this.legs1);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.body).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
    public void setRotationAngles(LeapingGrubShrimpEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    */
/**
     * This is a helper function from Tabula to set the rotation of model parts
     *//*

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
*/
