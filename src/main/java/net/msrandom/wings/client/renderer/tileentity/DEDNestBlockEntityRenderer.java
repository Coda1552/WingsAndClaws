package net.msrandom.wings.client.renderer.tileentity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.block.entity.DEDNestBlockEntity;
import net.msrandom.wings.block.entity.NestBlockEntity;
import net.msrandom.wings.client.renderer.tileentity.model.DEDNestModel;

public class DEDNestBlockEntityRenderer extends BlockEntityRenderer<DEDNestBlockEntity> {
    private static final Identifier TEXTURE = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/nest/dumpy_egg_drake.png");
    private final DEDNestModel model = new DEDNestModel();

    public DEDNestBlockEntityRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DEDNestBlockEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        model.setEggCount(tileEntityIn.getEggCount());
        NestBlockEntity.render(model, TEXTURE, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
