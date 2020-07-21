package net.msrandom.wings.client.renderer.tileentity.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class PlowheadHornModel extends Model {
    public ModelPart shape1;
    public ModelPart shape2;

    public PlowheadHornModel() {
        super(RenderLayer::getEntityCutoutNoCull);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape1 = new ModelPart(this, 0, 0);
        this.shape1.setPivot(0.0F, 21.5F, 0.0F);
        this.shape1.addCuboid(-2.5F, -2.5F, -5.0F, 5, 5, 10, 0.0F);
        this.shape2 = new ModelPart(this, 30, 0);
        this.shape2.setPivot(0.0F, 0.0F, 4.0F);
        this.shape2.addCuboid(-1.5F, -2.0F, 0.0F, 3, 4, 8, 0.0F);
        shape2.rotateAngleX = 0.39269908169872414F;
        this.shape1.addChild(this.shape2);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
