package net.coda.wings.client.renderer.tileentity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;


public class SpearProjectileTileModel extends Model {
    public ModelRenderer tip;
    public ModelRenderer tip2;
    public ModelRenderer fabric;
    public ModelRenderer handle;


    public SpearProjectileTileModel() {
        super(RenderType::getEntityCutoutNoCull);
        this.textureWidth = 14;
        this.textureHeight = 27;
        this.tip = new ModelRenderer(this, 4, 0);
        this.tip.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3, 0.0F);
        this.tip.setRotationPoint(0.0F, -13.0F, 0.0F);
        this.fabric = new ModelRenderer(this, 4, 12);
        this.fabric.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.fabric.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.tip2 = new ModelRenderer(this, 4, 8);
        this.tip2.addBox(-0.5F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
        this.tip2.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.handle = new ModelRenderer(this, 0, 0);
        this.handle.addBox(-0.5F, -13.0F, -0.5F, 1, 26, 1, 0.0F);
        this.handle.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.handle.addChild(this.tip);
        this.tip.addChild(this.fabric);
        this.tip.addChild(this.tip2);
    }



    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.handle.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
