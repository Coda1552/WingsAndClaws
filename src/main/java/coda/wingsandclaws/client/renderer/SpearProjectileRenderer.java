package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.model.SpearProjectileModel;
import coda.wingsandclaws.entity.item.SpearProjectileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import coda.wingsandclaws.WingsAndClaws;

public class SpearProjectileRenderer extends EntityRenderer<SpearProjectileEntity> implements IEntityRenderer<SpearProjectileEntity, SpearProjectileModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/stt_spear/stt_projectile_entity.png");
    private final SpearProjectileModel model = new SpearProjectileModel();

    public SpearProjectileRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(SpearProjectileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 90.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot) + 90.0F));
        IVertexBuilder ivertexbuilder = net.minecraft.client.renderer.ItemRenderer.getFoilBuffer(bufferIn, this.model.renderType(this.getTextureLocation(entityIn)), false, entityIn.isInWater());
        getModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
    }

    @Override
    public SpearProjectileModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTextureLocation(SpearProjectileEntity entity) {
        return TEXTURE;
    }
}
