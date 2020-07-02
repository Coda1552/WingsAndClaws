package net.msrandom.wings.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.tileentity.model.PlowheadShieldModel;

@OnlyIn(Dist.CLIENT)
public class PlowheadShieldRenderer extends ItemStackTileEntityRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/shield.png");
    private final PlowheadShieldModel model = new PlowheadShieldModel();

    @Override
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.push();
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        model.render(matrixStackIn, bufferIn.getBuffer(this.model.getRenderType(TEXTURE)), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStackIn.scale(1, -1, -1);
        matrixStackIn.pop();
    }
}
