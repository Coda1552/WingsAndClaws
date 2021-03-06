package coda.wingsandclaws.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import coda.wingsandclaws.tileentity.NestTileEntity;

public abstract class NestTileEntityRenderer<T extends NestTileEntity> extends TileEntityRenderer<T> {
    public NestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 1.5, 0.5);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180));
        //TODO use something not deprecated
        RenderSystem.enableRescaleNormal();
        getModel().renderToBuffer(matrixStackIn, bufferIn.getBuffer(getModel().renderType(getTexture())), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStackIn.popPose();
    }

    protected abstract Model getModel();

    protected abstract ResourceLocation getTexture();
}
