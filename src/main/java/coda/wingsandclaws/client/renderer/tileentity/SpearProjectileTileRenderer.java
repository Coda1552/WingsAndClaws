package coda.wingsandclaws.client.renderer.tileentity;

import coda.wingsandclaws.client.renderer.tileentity.model.SpearProjectileTileModel;
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
public class SpearProjectileTileRenderer extends ItemStackTileEntityRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/stt_spear/stt_projectile_entity.png");
    private final SpearProjectileTileModel model = new SpearProjectileTileModel();

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        model.renderToBuffer(matrixStack, ItemRenderer.getFoilBuffer(buffer, model.renderType(TEXTURE), false, stack.hasFoil()), combinedLight, combinedOverlay, 1, 1, 1, 1);
        matrixStack.popPose();
    }
}
