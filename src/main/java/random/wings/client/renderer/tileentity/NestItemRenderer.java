package random.wings.client.renderer.tileentity;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import random.wings.tileentity.DEDNestTileEntity;
import random.wings.tileentity.WingsTileEntities;

import java.util.Objects;

public class NestItemRenderer extends ItemStackTileEntityRenderer {
    private final DEDNestTileEntity instance = Objects.requireNonNull(WingsTileEntities.DED_NEST.create());

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        TileEntityRendererDispatcher.instance.renderAsItem(instance);
    }
}
