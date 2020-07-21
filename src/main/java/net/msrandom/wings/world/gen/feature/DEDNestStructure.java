package net.msrandom.wings.world.gen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;

import java.util.Objects;
import java.util.Random;

public class DEDNestStructure extends Feature<DefaultFeatureConfig> {
    public DEDNestStructure() {
        super(DefaultFeatureConfig.CODEC);
    }

    private static Vec3d get(BlockPos pos, Random random) {
        return new Vec3d(pos).add(random.nextInt(6) + 1.5, 0, random.nextInt(6) + 1.5);
    }

    private static DumpyEggDrakeEntity spawn(BlockPos pos, Random random, World world) {
        DumpyEggDrakeEntity entity = Objects.requireNonNull(WingsEntities.DUMPY_EGG_DRAKE.create(world));
        Vec3d p = get(pos, random);
        entity.setPos(p.x, p.y, p.z);
        return entity;
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos down = pos.down();
        BlockState sand = Blocks.SAND.getDefaultState();
        BlockPos.iterate(down.add(-3, 0, -3), pos.add(3, 0, 3)).forEach(p -> world.setBlockState(p, sand, 2));
        BlockPos.iterate(down.add(-1, 0, -1), pos.add(1, 0, 1)).forEach(p -> world.removeBlock(p, false));
        if (world.setBlockState(down, WingsBlocks.DED_NEST.getDefaultState(), 2)) {
            BlockEntity te = world.getBlockEntity(down);
            if (te instanceof DEDNestBlockEntity) {
                BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 0, 4)).forEach(p -> world.removeBlock(p, false));
                ((DEDNestBlockEntity) te).setEggs(world.getWorld(), random.nextInt(3) + 1);
                BlockState slab = Blocks.SANDSTONE_SLAB.getDefaultState();
                world.setBlockState(down.add(-1, 0, -1), slab, 2);
                world.setBlockState(down.add(1, 0, -1), slab, 2);
                world.setBlockState(down.add(-1, 0, 1), slab, 2);
                world.setBlockState(down.add(1, 0, 1), slab, 2);
                int[][] dirs = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

                for (int[] ints : dirs) {
                    world.setBlockState(pos.add(ints[0] * 2, 0, ints[1] * 2), slab, 2);
                    world.setBlockState(pos.add(ints[0], 0, ints[1] * 3), slab, 2);
                    world.setBlockState(pos.add(ints[0] * 3, 0, ints[1]), slab, 2);
                    world.setBlockState(pos.add(ints[0] * 2, 0, ints[1] * 3), sand, 2);
                    world.setBlockState(pos.add(ints[0] * 3, 0, ints[1] * 2), sand, 2);
                }

                BlockPos base = pos.add(-3, 0, -3);
                DumpyEggDrakeEntity male = spawn(base, random, world.getWorld());
                male.setGender(true);
                world.spawnEntity(male);

                for (int i = 1; i < (random.nextBoolean() ? 2 : 3); i++) {
                    DumpyEggDrakeEntity female = spawn(base, random, world.getWorld());
                    female.setGender(false);
                    world.spawnEntity(female);
                }

                for (int i = 1; i < (random.nextBoolean() ? 2 : 3); i++) {
                    DumpyEggDrakeEntity child = spawn(base, random, world.getWorld());
                    child.setBreedingAge(-24000);
                    child.initialize(world, world.getLocalDifficulty(child.getBlockPos()), SpawnReason.STRUCTURE, null, null);
                    world.spawnEntity(child);
                }
            }
            return true;
        }

        return false;
    }
}
