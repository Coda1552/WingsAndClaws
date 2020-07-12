package net.msrandom.wings.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.tileentity.model.HBNestModel;
import net.msrandom.wings.tileentity.HBNestTileEntity;
import net.msrandom.wings.tileentity.NestTileEntity;

public class HBNestTileEntityRenderer extends TileEntityRenderer<HBNestTileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/nest/hatchet_beak.png");
    private final HBNestModel model = new HBNestModel();

    public HBNestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(HBNestTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        model.setHasEgg(tileEntityIn.getEggCount() > 0);
        NestTileEntity.render(model, TEXTURE, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
