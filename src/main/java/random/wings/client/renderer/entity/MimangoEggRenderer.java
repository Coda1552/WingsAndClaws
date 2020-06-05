package random.wings.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.entity.item.MimangoEggEntity;

public class MimangoEggRenderer extends EntityRenderer<MimangoEggEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/mimango/egg.png");
    private final ModelRenderer model;

    public MimangoEggRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.model = new ModelRenderer(64, 32, 0, 0);
        this.model.setRotationPoint(0.0F, 22.5F, 0.0F);
        this.model.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
    }

    @Override
    public void render(MimangoEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        matrixStackIn.translate(0, 1.5, 0);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
        model.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucent(getEntityTexture(entityIn))), packedLightIn, OverlayTexture.getPackedUV(OverlayTexture.getU(0), OverlayTexture.getV(false)));
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(MimangoEggEntity entity) {
        return TEXTURE;
    }
}
