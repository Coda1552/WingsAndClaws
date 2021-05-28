package net.coda.wings.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.coda.wings.client.renderer.entity.model.SaddledThunderTailEggModel;
import net.coda.wings.entity.item.SaddledThunderTailEggEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.coda.wings.WingsAndClaws;

public class SaddledThunderTailEggRenderer extends EntityRenderer<SaddledThunderTailEggEntity> implements IEntityRenderer<SaddledThunderTailEggEntity, SaddledThunderTailEggModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/saddled_thunder_tail/egg.png");
    private final SaddledThunderTailEggModel model = new SaddledThunderTailEggModel();

    public SaddledThunderTailEggRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(SaddledThunderTailEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        matrixStackIn.translate(0, 1.5, 0);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
        getEntityModel().render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucent(getEntityTexture(entityIn))), packedLightIn, OverlayTexture.getPackedUV(OverlayTexture.getU(0), OverlayTexture.getV(false)), 1, 1, 1, 1);
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(SaddledThunderTailEggEntity entity) {
        return TEXTURE;
    }

    @Override
    public SaddledThunderTailEggModel getEntityModel() {
        return model;
    }
}
