package coda.wingsandclaws.init;

import coda.wingsandclaws.block.MangoBlock;
import coda.wingsandclaws.block.NestBlock;
import coda.wingsandclaws.block.RoastedHaroldsGreendrakeBlock;
import coda.wingsandclaws.entity.DumpyEggDrakeEntity;
import coda.wingsandclaws.entity.HatchetBeakEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.tileentity.DEDNestTileEntity;
import coda.wingsandclaws.tileentity.HBNestTileEntity;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class WingsBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, WingsAndClaws.MOD_ID);
    public static final RegistryObject<NestBlock<DEDNestTileEntity>> DED_NEST = register("ded_nest", () -> new NestBlock<>(Block.Properties.create(Material.SAND).sound(SoundType.SAND).hardnessAndResistance(1, 0), DumpyEggDrakeEntity.class, DEDNestTileEntity.class), null);
    public static final RegistryObject<NestBlock<HBNestTileEntity>> HB_NEST = register("hb_nest", () -> new NestBlock<>(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND).hardnessAndResistance(1, 0), HatchetBeakEntity.class, HBNestTileEntity.class), null);
    public static final RegistryObject<MangoBlock> MANGO_BUNCH = register("mango_bunch", MangoBlock::new, new Item.Properties().group(WingsItems.GROUP).food(WingsFoods.MANGO));
    // public static final RegistryObject<Block> GOLDEN_MIMANGO_STATUE = register("golden_mimango_statue", () -> new GoldenMimangoStatueBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0f, 6.0f).sound(SoundType.METAL).notSolid()));
    public static final RegistryObject<Block> GILDED_STONE_BRICKS = register("gilded_stone_bricks", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> GILDED_STONE_BRICK_SLAB = register("gilded_stone_brick_slab", () -> new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> GILDED_STONE_BRICK_STAIRS = register("gilded_stone_brick_stairs", () -> new StairsBlock(() -> GILDED_STONE_BRICKS.get().getDefaultState(), Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> GILDED_STONE_BRICK_WALL = register("gilded_stone_brick_wall", () -> new WallBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> CRACKED_GILDED_STONE_BRICKS = register("cracked_gilded_stone_bricks", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> CHISELED_GILDED_STONE_BRICKS = register("chiseled_gilded_stone_bricks", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> MOSSY_GILDED_STONE_BRICKS = register("mossy_gilded_stone_bricks", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> MOSSY_GILDED_STONE_BRICK_SLAB = register("mossy_gilded_stone_brick_slab", () -> new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> MOSSY_GILDED_STONE_BRICK_STAIRS = register("mossy_gilded_stone_brick_stairs", () -> new StairsBlock(() -> MOSSY_GILDED_STONE_BRICKS.get().getDefaultState(), Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> MOSSY_GILDED_STONE_BRICK_WALL = register("mossy_gilded_stone_brick_wall", () -> new WallBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
    public static final RegistryObject<Block> ROASTED_HAROLDS_GREENDRAKE = register("roasted_harolds_greendrake", () -> new RoastedHaroldsGreendrakeBlock(Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.5f).sound(SoundType.CLOTH)));

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, new Item.Properties().group(WingsItems.GROUP));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Item.Properties itemProperties) {
        return register(name, block, BlockItem::new, itemProperties);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, BiFunction<Block, Item.Properties, BlockItem> item, Item.Properties itemProperties) {
        final RegistryObject<T> registryObject = REGISTRY.register(name, block);
        if (itemProperties != null) WingsItems.REGISTRY.register(name, () -> item == null ? new BlockItem(registryObject.get(), itemProperties) : item.apply(registryObject.get(), itemProperties));
        return registryObject;
    }
}
