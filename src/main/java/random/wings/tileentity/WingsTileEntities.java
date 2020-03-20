package random.wings.tileentity;

import net.minecraft.tileentity.TileEntityType;
import random.wings.block.WingsBlocks;

public class WingsTileEntities {
    public static final TileEntityType<DEDNestTileEntity> DED_NEST = TileEntityType.Builder.create(DEDNestTileEntity::new, WingsBlocks.DED_NEST).build(null);
    public static final TileEntityType<HBNestTileEntity> HB_NEST = TileEntityType.Builder.create(HBNestTileEntity::new, WingsBlocks.HB_NEST).build(null);
}
