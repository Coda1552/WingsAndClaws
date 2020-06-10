package net.msrandom.wings.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.tileentity.model.DEDNestModel;
import net.msrandom.wings.tileentity.DEDNestTileEntity;
import net.msrandom.wings.tileentity.NestTileEntity;

public class DEDNestTileEntityRenderer extends TileEntityRenderer<DEDNestTileEntity> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[4];
    private final DEDNestModel model = new DEDNestModel();

    public DEDNestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DEDNestTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        model.setEggCount(tileEntityIn.getEggCount());
        NestTileEntity.render(tileEntityIn, model, TEXTURES, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    static {
        for (int i = 0; i < TEXTURES.length; i++) {
            TEXTURES[i] = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/nest/dumpy_egg_drake/eggs_" + i + ".png");
        }
    }
}
