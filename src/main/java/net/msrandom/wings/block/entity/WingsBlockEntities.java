package net.msrandom.wings.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import net.msrandom.wings.WingsRegisterer;
import net.msrandom.wings.block.NestBlock;
import net.msrandom.wings.block.WingsBlocks;

import java.util.function.Supplier;

public class WingsBlockEntities {
    public static final WingsRegisterer<BlockEntityType<?>> REGISTRY = new WingsRegisterer<>(Registry.BLOCK_ENTITY_TYPE);
    public static final BlockEntityType<DEDNestBlockEntity> DED_NEST = create("ded_nest", DEDNestBlockEntity::new, WingsBlocks.DED_NEST);
    public static final BlockEntityType<HBNestBlockEntity> HB_NEST = create("hb_nest", HBNestBlockEntity::new, WingsBlocks.HB_NEST);

    private static <T extends NestBlockEntity> BlockEntityType<T> create(String name, Supplier<T> factory, NestBlock<T> block) {
        return REGISTRY.register(name, block.setItem(BlockEntityType.Builder.create(factory, block).build(null)));
    }
}
