package net.msrandom.wings.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import net.msrandom.wings.WingsRegisterer;
import net.msrandom.wings.block.entity.DEDNestBlockEntity;
import net.msrandom.wings.block.entity.HBNestBlockEntity;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

public class WingsBlocks {
    public static final WingsRegisterer<Block> REGISTRY = new WingsRegisterer<>(Registry.BLOCK);
    public static final NestBlock<DEDNestBlockEntity> DED_NEST = register("ded_nest", new NestBlock<>(FabricBlockSettings.of(Material.AGGREGATE).sounds(BlockSoundGroup.SAND).hardness(1), DumpyEggDrakeEntity.class, DEDNestBlockEntity.class));
    public static final NestBlock<HBNestBlockEntity> HB_NEST = register("hb_nest", new NestBlock<>(FabricBlockSettings.of(Material.SOIL).sounds(BlockSoundGroup.GRASS).hardness(1), HatchetBeakEntity.class, HBNestBlockEntity.class));
    public static final MangoBlock MANGO_BUNCH = register("mango_bunch", new MangoBlock());

    private static <T extends Block> T register(String name, T block) {
        REGISTRY.register(name, block);
        return block;
    }
}
