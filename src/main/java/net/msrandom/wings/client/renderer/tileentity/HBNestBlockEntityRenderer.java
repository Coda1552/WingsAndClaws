package net.msrandom.wings.client.renderer.tileentity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.block.entity.HBNestBlockEntity;
import net.msrandom.wings.block.entity.NestBlockEntity;
import net.msrandom.wings.client.renderer.tileentity.model.HBNestModel;

public class HBNestBlockEntityRenderer extends BlockEntityRenderer<HBNestBlockEntity> {
    private static final Identifier TEXTURE = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/nest/hatchet_beak.png");
    private final HBNestModel model = new HBNestModel();

    public HBNestBlockEntityRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(HBNestBlockEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        model.setHasEgg(tileEntityIn.getEggCount() > 0);
        NestBlockEntity.render(model, TEXTURE, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
