package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.item.MimangoEggEntity;

public class MimangoEggRenderer extends EntityRenderer<MimangoEggEntity> {
    private static final Identifier TEXTURE = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/mimango/egg.png");
    private final ModelPart model;

    public MimangoEggRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager);
        this.model = new ModelPart(64, 32, 0, 0);
        this.model.setPivot(0.0F, 22.5F, 0.0F);
        this.model.addCuboid(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
    }

    @Override
    public void render(MimangoEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        matrixStackIn.translate(0, 1.5, 0);
        matrixStackIn.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
        model.render(matrixStackIn, bufferIn.getBuffer(RenderLayer.getEntityTranslucent(getTexture(entityIn))), packedLightIn, OverlayTexture.packUv(OverlayTexture.getU(0), OverlayTexture.getV(false)));
        matrixStackIn.pop();
    }

    @Override
    public Identifier getTexture(MimangoEggEntity entity) {
        return TEXTURE;
    }
}
