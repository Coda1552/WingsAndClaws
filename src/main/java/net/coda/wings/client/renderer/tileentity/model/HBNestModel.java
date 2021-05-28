package net.coda.wings.client.renderer.tileentity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class HBNestModel extends Model {
    public ModelRenderer bottom;
    public ModelRenderer top;
    public ModelRenderer egg;
    private boolean hasEgg;

    public HBNestModel() {
        super(RenderType::getEntityCutoutNoCull);
        this.textureWidth = 128;
        this.textureHeight = 32;
        this.top = new ModelRenderer(this, 48, 0);
        this.top.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.top.addBox(-6.0F, -2.0F, -6.0F, 12, 2, 12, 0.0F);
        this.egg = new ModelRenderer(this, 96, 0);
        this.egg.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.egg.addBox(-3.5F, -12.0F, -3.5F, 7, 12, 7, 0.0F);
        this.setRotateAngle(egg, 0.08726646259971647F, 0.08726646259971647F, 0.08726646259971647F);
        this.bottom = new ModelRenderer(this, 0, 0);
        this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bottom.addBox(-8.0F, -4.0F, -8.0F, 16, 4, 16, 0.0F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.top.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.bottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if (hasEgg) this.egg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setHasEgg(boolean hasEgg) {
        this.hasEgg = hasEgg;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
