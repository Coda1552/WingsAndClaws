package net.msrandom.wings.client.renderer.tileentity.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class PlowheadShieldModel extends Model {
    public ModelPart base;
    public ModelPart bottom;
    public ModelPart handle;
    public ModelPart front1;
    public ModelPart front1_1;

    public PlowheadShieldModel() {
        super(RenderLayer::getEntityCutoutNoCull);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.front1 = new ModelPart(this, 34, 0);
        this.front1.setPivot(-0.5F, -6.0F, 0.0F);
        this.front1.addCuboid(0.0F, -3.0F, -3.0F, 12, 6, 3, 0.0F);
        this.setRotateAngle(front1, 0.0F, -0.15707963267948966F, -0.2617993877991494F);
        this.base = new ModelPart(this, 0, 0);
        this.base.setPivot(0.0F, 11.0F, -1.0F);
        this.base.addCuboid(-7.0F, -9.0F, -1.5F, 14, 18, 3, 0.0F);
        this.bottom = new ModelPart(this, 34, 12);
        this.bottom.setPivot(0.0F, 9.0F, 0.0F);
        this.bottom.addCuboid(-5.0F, 0.0F, -1.5F, 10, 4, 3, 0.0F);
        this.front1_1 = new ModelPart(this, 34, 0);
        this.front1_1.mirror = true;
        this.front1_1.setPivot(0.5F, -6.0F, 0.0F);
        this.front1_1.addCuboid(-12.0F, -3.0F, -3.0F, 12, 6, 3, 0.0F);
        this.setRotateAngle(front1_1, 0.0F, 0.15707963267948966F, 0.2617993877991494F);
        this.handle = new ModelPart(this, 29, 19);
        this.handle.setPivot(0.0F, 2.0F, 1.5F);
        this.handle.addCuboid(-1.0F, -3.0F, 0.0F, 2, 6, 6, 0.0F);
        this.base.addChild(this.front1);
        this.base.addChild(this.bottom);
        this.base.addChild(this.front1_1);
        this.base.addChild(this.handle);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }
}
