package random.wings.client.renderer.tileentity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class DEDNestModel extends Model {
    public ModelRenderer bottom;
    public ModelRenderer top;
    public ModelRenderer egg1;
    public ModelRenderer egg2;
    public ModelRenderer egg3;

    public DEDNestModel() {
        super(RenderType::getEntityCutoutNoCull);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.egg1 = new ModelRenderer(this, 42, 0);
        this.egg1.setRotationPoint(-2.0F, 21.0F, 1.5F);
        this.egg1.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(egg1, -0.1884955592153876F, 0.1884955592153876F, -0.3141592653589793F);
        this.bottom = new ModelRenderer(this, 0, 0);
        this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bottom.addBox(-7.0F, -2.0F, -7.0F, 14, 2, 14, 0.0F);
        this.egg2 = new ModelRenderer(this, 30, 16);
        this.egg2.setRotationPoint(0.0F, 21.0F, 0.5F);
        this.egg2.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(egg2, -0.439822971502571F, 0.9424777960769379F, 0.25132741228718347F);
        this.top = new ModelRenderer(this, 0, 16);
        this.top.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.top.addBox(-5.0F, -2.0F, -5.0F, 10, 2, 10, 0.0F);
        this.egg3 = new ModelRenderer(this, 46, 16);
        this.egg3.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.egg3.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(egg3, 0.7740535232594852F, 0.18675022996339324F, 0.0F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.egg1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.bottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.egg2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.top.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.egg3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
