package coda.wingsandclaws.client.renderer.tileentity;

import coda.wingsandclaws.client.renderer.tileentity.model.PlowheadShieldModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import coda.wingsandclaws.WingsAndClaws;

@OnlyIn(Dist.CLIENT)
public class PlowheadShieldRenderer extends ItemStackTileEntityRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/shield.png");
    private final PlowheadShieldModel model = new PlowheadShieldModel();

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(this.model.renderType(TEXTURE)), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStackIn.scale(1, -1, -1);
        matrixStackIn.popPose();
    }
}
