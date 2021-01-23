package net.msrandom.wings.world.gen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.monster.DumpyEggDrakeEntity;
import net.msrandom.wings.tileentity.DEDNestTileEntity;

import java.util.Objects;
import java.util.Random;

public class DEDNestStructure extends Feature<NoFeatureConfig> {
    public DEDNestStructure() {
        super(NoFeatureConfig.field_236558_a_);
    }

    private static Vector3d get(BlockPos pos, Random rand) {
        return Vector3d.copyCentered(pos).add(rand.nextInt(6) + 1.5, 0, rand.nextInt(6) + 1.5);
    }

    private static DumpyEggDrakeEntity spawn(BlockPos pos, Random rand, World world) {
        DumpyEggDrakeEntity entity = Objects.requireNonNull(WingsEntities.DUMPY_EGG_DRAKE.create(world));
        Vector3d p = get(pos, rand);
        entity.setPosition(p.x, p.y, p.z);
        return entity;
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        BlockPos down = pos.down();
        BlockState sand = Blocks.SAND.getDefaultState();
        BlockPos.getAllInBox(down.add(-3, 0, -3), pos.add(3, 0, 3)).forEach(p -> reader.setBlockState(p, sand, 2));
        BlockPos.getAllInBox(down.add(-1, 0, -1), pos.add(1, 0, 1)).forEach(p -> reader.removeBlock(p, false));
        if (reader.setBlockState(down, WingsBlocks.DED_NEST.getDefaultState(), 2)) {
            TileEntity te = reader.getTileEntity(down);
            if (te instanceof DEDNestTileEntity) {
                BlockPos.getAllInBox(pos.add(-4, 0, -4), pos.add(4, 0, 4)).forEach(p -> reader.removeBlock(p, false));
                ((DEDNestTileEntity) te).setEggs(reader.getWorld(), rand.nextInt(3) + 1);
                BlockState slab = Blocks.SANDSTONE_SLAB.getDefaultState();
                reader.setBlockState(down.add(-1, 0, -1), slab, 2);
                reader.setBlockState(down.add(1, 0, -1), slab, 2);
                reader.setBlockState(down.add(-1, 0, 1), slab, 2);
                reader.setBlockState(down.add(1, 0, 1), slab, 2);
                int[][] dirs = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

                for (int[] ints : dirs) {
                    reader.setBlockState(pos.add(ints[0] * 2, 0, ints[1] * 2), slab, 2);
                    reader.setBlockState(pos.add(ints[0], 0, ints[1] * 3), slab, 2);
                    reader.setBlockState(pos.add(ints[0] * 3, 0, ints[1]), slab, 2);
                    reader.setBlockState(pos.add(ints[0] * 2, 0, ints[1] * 3), sand, 2);
                    reader.setBlockState(pos.add(ints[0] * 3, 0, ints[1] * 2), sand, 2);
                }

                BlockPos base = pos.add(-3, 0, -3);
                DumpyEggDrakeEntity male = spawn(base, rand, reader.getWorld());
                male.setGender(TameableDragonEntity.Gender.MALE);
                reader.addEntity(male);

                for (int i = 1; i < (rand.nextBoolean() ? 2 : 3); i++) {
                    DumpyEggDrakeEntity female = spawn(base, rand, reader.getWorld());
                    female.setGender(TameableDragonEntity.Gender.FEMALE);
                    reader.addEntity(female);
                }

                for (int i = 1; i < (rand.nextBoolean() ? 2 : 3); i++) {
                    DumpyEggDrakeEntity child = spawn(base, rand, reader.getWorld());
                    child.setGrowingAge(-24000);
                    child.onInitialSpawn(reader, reader.getDifficultyForLocation(child.getPosition()), SpawnReason.STRUCTURE, null, null);
                    reader.addEntity(child);
                }
            }
            return true;
        }

        return false;
    }
}
