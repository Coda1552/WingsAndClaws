package net.msrandom.wings.client.renderer.tileentity.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class HBNestModel extends Model {
    public ModelPart bottom;
    public ModelPart top;
    public ModelPart egg;
    private boolean hasEgg;

    public HBNestModel() {
        super(RenderLayer::getEntityCutoutNoCull);
        this.textureWidth = 128;
        this.textureHeight = 32;
        this.top = new ModelPart(this, 48, 0);
        this.top.setPivot(0.0F, 20.0F, 0.0F);
        this.top.addCuboid(-6.0F, -2.0F, -6.0F, 12, 2, 12, 0.0F);
        this.egg = new ModelPart(this, 96, 0);
        this.egg.setPivot(0.0F, 21.0F, 0.0F);
        this.egg.addCuboid(-3.5F, -12.0F, -3.5F, 7, 12, 7, 0.0F);
        this.setRotateAngle(egg, 0.08726646259971647F, 0.08726646259971647F, 0.08726646259971647F);
        this.bottom = new ModelPart(this, 0, 0);
        this.bottom.setPivot(0.0F, 24.0F, 0.0F);
        this.bottom.addCuboid(-8.0F, -4.0F, -8.0F, 16, 4, 16, 0.0F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.top.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.bottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if (hasEgg) this.egg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setHasEgg(boolean hasEgg) {
        this.hasEgg = hasEgg;
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }
}
