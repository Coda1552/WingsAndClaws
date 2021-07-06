package coda.wingsandclaws.client.renderer.tileentity;

import coda.wingsandclaws.client.renderer.tileentity.model.PlowheadHornModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import coda.wingsandclaws.WingsAndClaws;

@OnlyIn(Dist.CLIENT)
public class PlowheadHornRenderer extends ItemStackTileEntityRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/horn.png");
    private final PlowheadHornModel model = new PlowheadHornModel();

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        model.renderToBuffer(matrixStackIn, ItemRenderer.getFoilBuffer(bufferIn, model.renderType(TEXTURE), false, itemStackIn.hasFoil()), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStackIn.popPose();
    }


}
