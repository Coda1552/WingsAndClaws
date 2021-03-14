package net.msrandom.wings.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.HatchetBeakModel;
import net.msrandom.wings.entity.monster.HatchetBeakEntity;

public class LayerHatchetBeakSaddle extends LayerRenderer<HatchetBeakEntity, HatchetBeakModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/hatchet_beak/saddle.png");

    public LayerHatchetBeakSaddle(IEntityRenderer<HatchetBeakEntity, HatchetBeakModel> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, HatchetBeakEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.hasSaddle()) {
            this.getEntityModel().render(matrixStackIn, bufferIn.getBuffer(this.getEntityModel().getRenderType(TEXTURE)), packedLightIn, OverlayTexture.getPackedUV(OverlayTexture.getU(0), OverlayTexture.getV(entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0)), 1, 1, 1, 1);
        }
    }
}
