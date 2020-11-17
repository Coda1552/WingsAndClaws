package net.msrandom.wings.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.msrandom.wings.tileentity.NestTileEntity;

public abstract class NestTileEntityRenderer<T extends NestTileEntity> extends TileEntityRenderer<T> {
    public NestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5, 1.5, 0.5);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
        //TODO use something not deprecated
        RenderSystem.enableRescaleNormal();
        getModel().render(matrixStackIn, bufferIn.getBuffer(getModel().getRenderType(getTexture())), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStackIn.pop();
    }

    protected abstract Model getModel();

    protected abstract ResourceLocation getTexture();
}
