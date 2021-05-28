package net.coda.wings.client.renderer.tileentity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PlowheadHornModel extends Model {
    public ModelRenderer shape1;
    public ModelRenderer shape2;

    public PlowheadHornModel() {
        super(RenderType::getEntityCutoutNoCull);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 21.5F, 0.0F);
        this.shape1.addBox(-2.5F, -2.5F, -5.0F, 5, 5, 10, 0.0F);
        this.shape2 = new ModelRenderer(this, 30, 0);
        this.shape2.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.shape2.addBox(-1.5F, -2.0F, 0.0F, 3, 4, 8, 0.0F);
        shape2.rotateAngleX = 0.39269908169872414F;
        this.shape1.addChild(this.shape2);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
