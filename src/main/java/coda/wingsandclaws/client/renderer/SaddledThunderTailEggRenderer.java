package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.model.SaddledThunderTailEggModel;
import coda.wingsandclaws.entity.item.SaddledThunderTailEggEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import coda.wingsandclaws.WingsAndClaws;

public class SaddledThunderTailEggRenderer extends EntityRenderer<SaddledThunderTailEggEntity> implements IEntityRenderer<SaddledThunderTailEggEntity, SaddledThunderTailEggModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/saddled_thunder_tail/egg.png");
    private final SaddledThunderTailEggModel model = new SaddledThunderTailEggModel();

    public SaddledThunderTailEggRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(SaddledThunderTailEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 1.5, 0);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180));
        getModel().renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(entityIn))), packedLightIn, OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(false)), 1, 1, 1, 1);
        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SaddledThunderTailEggEntity entity) {
        return TEXTURE;
    }

    @Override
    public SaddledThunderTailEggModel getModel() {
        return model;
    }
}
