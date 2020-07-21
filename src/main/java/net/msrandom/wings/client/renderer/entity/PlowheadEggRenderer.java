package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.PlowheadEggModel;
import net.msrandom.wings.entity.item.PlowheadEggEntity;

public class PlowheadEggRenderer extends EntityRenderer<PlowheadEggEntity> implements FeatureRendererContext<PlowheadEggEntity, PlowheadEggModel> {
    private static final Identifier TEXTURE = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/egg.png");
    private final PlowheadEggModel model = new PlowheadEggModel();

    public PlowheadEggRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager);
    }

    @Override
    public void render(PlowheadEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        matrixStackIn.translate(0, 1.5, 0);
        matrixStackIn.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
        getModel().render(matrixStackIn, bufferIn.getBuffer(RenderLayer.getEntityTranslucent(getTexture(entityIn))), packedLightIn, OverlayTexture.packUv(OverlayTexture.getU(0), OverlayTexture.getV(false)), 1, 1, 1, 1);
        matrixStackIn.pop();
    }

    @Override
    public Identifier getTexture(PlowheadEggEntity entity) {
        return TEXTURE;
    }

    @Override
    public PlowheadEggModel getModel() {
        return model;
    }
}
