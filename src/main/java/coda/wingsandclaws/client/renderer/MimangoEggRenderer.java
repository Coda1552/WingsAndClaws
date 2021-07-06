package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.entity.item.MimangoEggEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import coda.wingsandclaws.WingsAndClaws;

public class MimangoEggRenderer extends EntityRenderer<MimangoEggEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/mimango/egg.png");
    private final ModelRenderer model;

    public MimangoEggRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.model = new ModelRenderer(64, 32, 0, 0);
        this.model.setPos(0.0F, 22.5F, 0.0F);
        this.model.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
    }

    @Override
    public void render(MimangoEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 1.5, 0);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180));
        model.render(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(entityIn))), packedLightIn, OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(false)));
        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MimangoEggEntity entity) {
        return TEXTURE;
    }
}
