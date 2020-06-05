package random.wings.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import random.wings.WingsAndClaws;
import random.wings.entity.passive.DumpyEggDrakeEntity;
import random.wings.entity.passive.HatchetBeakEntity;
import random.wings.tileentity.DEDNestTileEntity;
import random.wings.tileentity.HBNestTileEntity;

public class WingsBlocks {
    public static final DeferredRegister<Block> REGISTRY = new DeferredRegister<>(ForgeRegistries.BLOCKS, WingsAndClaws.MOD_ID);
    public static final NestBlock<DEDNestTileEntity> DED_NEST = register("ded_nest", new NestBlock<>(Block.Properties.create(Material.SAND).sound(SoundType.SAND).hardnessAndResistance(1, 0), DumpyEggDrakeEntity.class, DEDNestTileEntity.class));
    public static final NestBlock<HBNestTileEntity> HB_NEST = register("hb_nest", new NestBlock<>(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND).hardnessAndResistance(1, 0), HatchetBeakEntity.class, HBNestTileEntity.class));

    private static <T extends Block> T register(String name, T block) {
        REGISTRY.register(name, () -> block);
        return block;
    }
}
