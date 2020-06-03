package random.wings.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import random.wings.entity.passive.DumpyEggDrakeEntity;
import random.wings.entity.passive.HatchetBeakEntity;
import random.wings.tileentity.DEDNestTileEntity;
import random.wings.tileentity.HBNestTileEntity;

import java.util.ArrayList;
import java.util.List;

public class WingsBlocks {
    public static final List<Block> LIST = new ArrayList<>();
    public static final NestBlock<DEDNestTileEntity> DED_NEST = new NestBlock<>("ded_nest", Block.Properties.create(Material.SAND).sound(SoundType.SAND).hardnessAndResistance(1, 0), DumpyEggDrakeEntity.class, DEDNestTileEntity.class);
    public static final NestBlock<HBNestTileEntity> HB_NEST = new NestBlock<>("hb_nest", Block.Properties.create(Material.EARTH).sound(SoundType.GROUND).hardnessAndResistance(1, 0), HatchetBeakEntity.class, HBNestTileEntity.class);
}
