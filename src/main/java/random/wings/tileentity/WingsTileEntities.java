package random.wings.tileentity;

import net.minecraft.tileentity.TileEntityType;
import random.wings.block.NestBlock;
import random.wings.block.WingsBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WingsTileEntities {
    public static final List<TileEntityType<?>> LIST = new ArrayList<>();
    public static final TileEntityType<DEDNestTileEntity> DED_NEST = create("ded_nest", DEDNestTileEntity::new, WingsBlocks.DED_NEST);
    public static final TileEntityType<HBNestTileEntity> HB_NEST = create("hb_nest", HBNestTileEntity::new, WingsBlocks.HB_NEST);

    private static <T extends NestTileEntity> TileEntityType<T> create(String name, Supplier<T> factory, NestBlock<T> block) {
        TileEntityType<T> type = TileEntityType.Builder.create(factory, block).build(null);
        type.setRegistryName(name);
        block.setItem(type);
        LIST.add(type);
        return type;
    }
}
