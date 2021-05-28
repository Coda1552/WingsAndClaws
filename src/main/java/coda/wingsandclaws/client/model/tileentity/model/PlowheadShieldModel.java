package coda.wingsandclaws.client.renderer.tileentity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PlowheadShieldModel extends Model {
    public ModelRenderer base;
    public ModelRenderer bottom;
    public ModelRenderer handle;
    public ModelRenderer front1;
    public ModelRenderer front1_1;

    public PlowheadShieldModel() {
        super(RenderType::getEntityCutoutNoCull);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.front1 = new ModelRenderer(this, 34, 0);
        this.front1.setRotationPoint(-0.5F, -6.0F, 0.0F);
        this.front1.addBox(0.0F, -3.0F, -3.0F, 12, 6, 3, 0.0F);
        this.setRotateAngle(front1, 0.0F, -0.15707963267948966F, -0.2617993877991494F);
        this.base = new ModelRenderer(this, 0, 0);
        this.base.setRotationPoint(0.0F, 11.0F, -1.0F);
        this.base.addBox(-7.0F, -9.0F, -1.5F, 14, 18, 3, 0.0F);
        this.bottom = new ModelRenderer(this, 34, 12);
        this.bottom.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.bottom.addBox(-5.0F, 0.0F, -1.5F, 10, 4, 3, 0.0F);
        this.front1_1 = new ModelRenderer(this, 34, 0);
        this.front1_1.mirror = true;
        this.front1_1.setRotationPoint(0.5F, -6.0F, 0.0F);
        this.front1_1.addBox(-12.0F, -3.0F, -3.0F, 12, 6, 3, 0.0F);
        this.setRotateAngle(front1_1, 0.0F, 0.15707963267948966F, 0.2617993877991494F);
        this.handle = new ModelRenderer(this, 29, 19);
        this.handle.setRotationPoint(0.0F, 2.0F, 1.5F);
        this.handle.addBox(-1.0F, -3.0F, 0.0F, 2, 6, 6, 0.0F);
        this.base.addChild(this.front1);
        this.base.addChild(this.bottom);
        this.base.addChild(this.front1_1);
        this.base.addChild(this.handle);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
