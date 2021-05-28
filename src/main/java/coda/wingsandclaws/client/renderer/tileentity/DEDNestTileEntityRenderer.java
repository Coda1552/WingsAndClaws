package coda.wingsandclaws.client.renderer.tileentity;

import coda.wingsandclaws.client.renderer.tileentity.model.DEDNestModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.tileentity.DEDNestTileEntity;

public class DEDNestTileEntityRenderer extends NestTileEntityRenderer<DEDNestTileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/nest/dumpy_egg_drake.png");
    private final DEDNestModel model = new DEDNestModel();

    public DEDNestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DEDNestTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        model.setEggCount(tileEntityIn.getEggCount());
        super.render(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    @Override
    protected Model getModel() {
        return model;
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
