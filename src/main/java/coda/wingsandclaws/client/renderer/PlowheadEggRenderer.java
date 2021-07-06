package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.model.PlowheadEggModel;
import coda.wingsandclaws.entity.item.PlowheadEggEntity;
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

public class PlowheadEggRenderer extends EntityRenderer<PlowheadEggEntity> implements IEntityRenderer<PlowheadEggEntity, PlowheadEggModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/egg.png");
    private final PlowheadEggModel model = new PlowheadEggModel();

    public PlowheadEggRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(PlowheadEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 1.5, 0);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180));
        getModel().renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(entityIn))), packedLightIn, OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(false)), 1, 1, 1, 1);
        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(PlowheadEggEntity entity) {
        return TEXTURE;
    }

    @Override
    public PlowheadEggModel getModel() {
        return model;
    }
}
