package random.wings.block;

import net.minecraft.block.Block;
import random.wings.entity.passive.DumpyEggDrakeEntity;
import random.wings.entity.passive.HatchetBeakEntity;
import random.wings.tileentity.DEDNestTileEntity;
import random.wings.tileentity.HBNestTileEntity;

import java.util.ArrayList;
import java.util.List;

public class WingsBlocks {
    public static final List<Block> LIST = new ArrayList<>();
    public static final NestBlock DED_NEST = new NestBlock("ded_nest", DumpyEggDrakeEntity.class, DEDNestTileEntity.class);
    public static final NestBlock HB_NEST = new NestBlock("hb_nest", HatchetBeakEntity.class, HBNestTileEntity.class);
}
