package net.msrandom.wings.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.Objects;

public class NestItemRenderer extends ItemStackTileEntityRenderer {
    private final TileEntity instance;

    public NestItemRenderer(TileEntityType<?> te) {
        instance = Objects.requireNonNull(te.create());
    }

    @Override
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        TileEntityRendererDispatcher.instance.renderItem(instance, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
