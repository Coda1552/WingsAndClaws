package random.wings.client.renderer.tileentity;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import random.wings.client.renderer.entity.PlowheadEggRenderer;
import random.wings.client.renderer.entity.model.PlowheadEggModel;

public class PlowheadItemEggRenderer extends ItemStackTileEntityRenderer {
    private final PlowheadEggModel model;
    private final Runnable bindTexture;

    public PlowheadItemEggRenderer(PlowheadEggRenderer renderer) {
        this.model = renderer.getEntityModel();
        this.bindTexture = renderer::bindCurrentTexture;
    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        super.renderByItem(itemStackIn);
        model.render(bindTexture);
    }
}
