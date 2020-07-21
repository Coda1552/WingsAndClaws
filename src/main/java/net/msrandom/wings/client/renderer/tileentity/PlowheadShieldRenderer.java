package net.msrandom.wings.client.renderer.tileentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.tileentity.model.PlowheadShieldModel;

@Environment(EnvType.CLIENT)
public class PlowheadShieldRenderer extends BuiltinModelItemRenderer {
    private static final Identifier TEXTURE = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/shield.png");
    private final PlowheadShieldModel model = new PlowheadShieldModel();

    @Override
    public void render(ItemStack itemStackIn, ModelTransformation.Mode mode, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(itemStackIn, mode, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.push();
        model.render(matrixStackIn, bufferIn.getBuffer(this.model.getLayer(TEXTURE)), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStackIn.scale(1, -1, -1);
        matrixStackIn.pop();
    }
}
