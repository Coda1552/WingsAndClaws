package random.wings.client.renderer;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import random.wings.tileentity.NestTileEntity;
import random.wings.tileentity.WingsTileEntities;

import java.util.Objects;

public class NestItemRenderer extends ItemStackTileEntityRenderer {
    private final NestTileEntity instance = Objects.requireNonNull(WingsTileEntities.NEST.create());

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        TileEntityRendererDispatcher.instance.renderAsItem(instance);
    }
}
