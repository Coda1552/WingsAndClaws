package net.msrandom.wings.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.block.NestBlock;
import net.msrandom.wings.block.WingsBlocks;

import java.util.function.Supplier;

public class WingsTileEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, WingsAndClaws.MOD_ID);
    public static final BlockEntityType<DEDNestBlockEntity> DED_NEST = create("ded_nest", DEDNestBlockEntity::new, WingsBlocks.DED_NEST);
    public static final BlockEntityType<HBNestBlockEntity> HB_NEST = create("hb_nest", HBNestBlockEntity::new, WingsBlocks.HB_NEST);

    private static <T extends NestBlockEntity> BlockEntityType<T> create(String name, Supplier<T> factory, NestBlock<T> block) {
        BlockEntityType<T> type = BlockEntityType.Builder.create(factory, block).build(null);
        block.setItem(type);
        REGISTRY.register(name, () -> type);
        return type;
    }
}
