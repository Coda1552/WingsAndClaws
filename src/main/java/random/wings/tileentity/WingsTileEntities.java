package random.wings.tileentity;

import net.minecraft.tileentity.TileEntityType;
import random.wings.block.WingsBlocks;

public class WingsTileEntities {
    public static final TileEntityType<NestTileEntity> NEST = TileEntityType.Builder.create(NestTileEntity::new, WingsBlocks.NEST).build(null);
}
