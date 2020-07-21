package net.msrandom.wings.client.renderer.tileentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.tileentity.model.PlowheadHornModel;

@Environment(EnvType.CLIENT)
public class PlowheadHornRenderer extends BuiltinModelItemRenderer {
    private static final Identifier TEXTURE = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/horn.png");
    private final PlowheadHornModel model = new PlowheadHornModel();

    @Override
    public void render(ItemStack itemStack, ModelTransformation.Mode mode, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.push();
        model.render(matrixStack, ItemRenderer.getArmorVertexConsumer(vertexConsumerProvider, model.getLayer(TEXTURE), false, itemStack.hasGlint()), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStack.pop();
    }
}
