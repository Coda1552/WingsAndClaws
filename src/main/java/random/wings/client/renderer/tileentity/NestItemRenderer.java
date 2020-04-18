package random.wings.client.renderer.tileentity;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.Objects;

public class NestItemRenderer extends ItemStackTileEntityRenderer {
    private final TileEntity instance;

    public NestItemRenderer(TileEntityType<?> te) {
        instance = Objects.requireNonNull(te.create());
    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        TileEntityRendererDispatcher.instance.renderAsItem(instance);
    }
}
