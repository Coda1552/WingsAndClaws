package random.wings.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.tileentity.model.HBNestModel;
import random.wings.tileentity.HBNestTileEntity;

public class HBNestTileEntityRenderer extends TileEntityRenderer<HBNestTileEntity> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/nest/hatchet_beak/empty.png"), new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/nest/hatchet_beak/egg.png")};
    private final HBNestModel model = new HBNestModel();

    public HBNestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(HBNestTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pop();
        matrixStackIn.translate(0, 1, 0);
        //GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderSystem.enableRescaleNormal();
        this.model.render(matrixStackIn, bufferIn.getBuffer(model.getRenderType(TEXTURES[tileEntityIn.getEggCount()])), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.push();
    }
}
