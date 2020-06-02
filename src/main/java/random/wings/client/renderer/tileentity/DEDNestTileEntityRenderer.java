package random.wings.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.tileentity.model.DEDNestModel;
import random.wings.tileentity.DEDNestTileEntity;

public class DEDNestTileEntityRenderer extends TileEntityRenderer<DEDNestTileEntity> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[4];

    static {
        for (int i = 0; i < TEXTURES.length; i++) {
            TEXTURES[i] = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/nest/dumpy_egg_drake/eggs_" + i + ".png");
        }
    }

    private final DEDNestModel model = new DEDNestModel();

    public DEDNestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DEDNestTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0, 1, 0);
        //matrixStackIn.rotate();
        RenderSystem.enableRescaleNormal();
        this.model.render(matrixStackIn, bufferIn.getBuffer(model.getRenderType(TEXTURES[tileEntityIn.getEggCount()])), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
