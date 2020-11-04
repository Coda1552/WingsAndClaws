package net.msrandom.wings.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;
import net.msrandom.wings.tileentity.DEDNestTileEntity;
import net.msrandom.wings.tileentity.HBNestTileEntity;

public class WingsBlocks {
    public static final DeferredRegister<Block> REGISTRY = new DeferredRegister<>(ForgeRegistries.BLOCKS, WingsAndClaws.MOD_ID);
    public static final NestBlock<DEDNestTileEntity> DED_NEST = register("ded_nest", new NestBlock<>(Block.Properties.create(Material.SAND).sound(SoundType.SAND).hardnessAndResistance(1, 0), DumpyEggDrakeEntity.class, DEDNestTileEntity.class));
    public static final NestBlock<HBNestTileEntity> HB_NEST = register("hb_nest", new NestBlock<>(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND).hardnessAndResistance(1, 0), HatchetBeakEntity.class, HBNestTileEntity.class));
    public static final MangoBlock MANGO_BUNCH = register("mango_bunch", new MangoBlock());
//    public static final Block GOLDEN_MIMANGO_STATUE = register("golden_mimango_statue", new GoldenMimangoStatueBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0f, 6.0f).sound(SoundType.METAL).notSolid()));
//    public static final Block GILDED_STONE_BRICKS = register("gilded_stone_bricks", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block GILDED_STONE_BRICK_SLAB = register("gilded_stone_brick_slab", new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block GILDED_STONE_BRICK_STAIRS = register("gilded_stone_brick_stairs", new StairsBlock(GILDED_STONE_BRICKS::getDefaultState, Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block GILDED_STONE_BRICK_WALL = register("gilded_stone_brick_wall", new WallBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block CRACKED_GILDED_STONE_BRICKS = register("cracked_gilded_stone_bricks", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block CHISELED_GILDED_STONE_BRICKS = register("chiseled_gilded_stone_bricks", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block MOSSY_GILDED_STONE_BRICKS = register("mossy_gilded_stone_bricks", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block MOSSY_GILDED_STONE_BRICK_SLAB = register("mossy_gilded_stone_brick_slab", new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block MOSSY_GILDED_STONE_BRICK_STAIRS = register("mossy_gilded_stone_brick_stairs", new StairsBlock(MOSSY_GILDED_STONE_BRICKS::getDefaultState, Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));
//    public static final Block MOSSY_GILDED_STONE_BRICK_WALL = register("mossy_gilded_stone_brick_wall", new WallBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 6.0f)));

    private static <T extends Block> T register(String name, T block) {
        REGISTRY.register(name, () -> block);
        return block;
    }
}
