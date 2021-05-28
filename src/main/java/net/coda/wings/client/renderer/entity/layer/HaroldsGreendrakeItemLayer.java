package net.coda.wings.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.coda.wings.client.renderer.entity.model.HaroldsGreendrakeModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.coda.wings.entity.passive.HaroldsGreendrakeEntity;

public class HaroldsGreendrakeItemLayer extends LayerRenderer<HaroldsGreendrakeEntity, HaroldsGreendrakeModel> {
    public HaroldsGreendrakeItemLayer(IEntityRenderer<HaroldsGreendrakeEntity, HaroldsGreendrakeModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, HaroldsGreendrakeEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        final ItemStack item = entitylivingbaseIn.getHeldItemMainhand();
        if (!item.isEmpty()) {
            matrixStack.translate(0, 1.2, -0.6);
            matrixStack.rotate(Vector3f.XN.rotationDegrees(30));
            getEntityModel().head.translateRotate(matrixStack);
            Minecraft.getInstance().getItemRenderer().renderItem(entitylivingbaseIn, item, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false, matrixStack, bufferIn, entitylivingbaseIn.world, packedLightIn, OverlayTexture.NO_OVERLAY);
        }
    }
}
