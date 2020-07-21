package net.msrandom.wings.client.renderer.tileentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class NestItemRenderer extends BuiltinModelItemRenderer {
    private final BlockEntity instance;

    public NestItemRenderer(BlockEntityType<?> te) {
        instance = Objects.requireNonNull(te.instantiate());
    }

    @Override
    public void render(ItemStack itemStackIn, ModelTransformation.Mode mode, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockEntityRenderDispatcher.INSTANCE.renderItem(instance, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
