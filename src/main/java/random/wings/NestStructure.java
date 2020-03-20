package random.wings;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import random.wings.block.WingsBlocks;
import random.wings.entity.WingsEntities;
import random.wings.entity.passive.DumpyEggDrakeEntity;
import random.wings.tileentity.DEDNestTileEntity;

import java.util.Objects;
import java.util.Random;

public class NestStructure extends Feature<NoFeatureConfig> {
    public NestStructure() {
        super(NoFeatureConfig::deserialize);
    }

    private static Vec3d get(BlockPos pos, Random rand) {
        return new Vec3d(pos).add(rand.nextInt(6) + 1.5, 0, rand.nextInt(6) + 1.5);
    }

    private static DumpyEggDrakeEntity spawn(BlockPos pos, Random rand, World world) {
        DumpyEggDrakeEntity entity = Objects.requireNonNull(WingsEntities.DUMPY_EGG_DRAKE.create(world));
        Vec3d p = get(pos, rand);
        entity.setPosition(p.x, p.y, p.z);
        return entity;
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        //pos = pos.down();
        //-108 68 -71
        BlockPos down = pos.down();
        BlockState sand = Blocks.SAND.getDefaultState();
        BlockPos.getAllInBox(down.add(-3, 0, -3), pos.add(3, 0, 3)).forEach(p -> world.setBlockState(p, sand, 2));
        /*world.removeBlock(down.add(-3, 0, -3), false);
        world.removeBlock(down.add(-3, 0, 3), false);
        world.removeBlock(down.add(3, 0, -3), false);
        world.removeBlock(down.add(3, 0, 3), false);*/
        BlockPos.getAllInBox(down.add(-1, 0, -1), pos.add(1, 0, 1)).forEach(p -> world.removeBlock(p, false));
        if (world.setBlockState(down, WingsBlocks.DED_NEST.getDefaultState(), 2)) {
            TileEntity te = world.getTileEntity(down);
            if (te instanceof DEDNestTileEntity) {
                BlockPos.getAllInBox(pos.add(-4, 0, -4), pos.add(4, 0, 4)).forEach(p -> world.removeBlock(p, false));
                ((DEDNestTileEntity) te).setEggs(world.getWorld(), rand.nextInt(3) + 1);
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
                DumpyEggDrakeEntity male = spawn(base, rand, world.getWorld());
                male.setGender(true);
                world.addEntity(male);

                for (int i = 1; i < (rand.nextBoolean() ? 2 : 3); i++) {
                    DumpyEggDrakeEntity female = spawn(base, rand, world.getWorld());
                    female.setGender(false);
                    world.addEntity(female);
                }

                for (int i = 1; i < (rand.nextBoolean() ? 2 : 3); i++) {
                    DumpyEggDrakeEntity child = spawn(base, rand, world.getWorld());
                    child.setGrowingAge(-24000);
                    child.onInitialSpawn(world, world.getDifficultyForLocation(child.getPosition()), SpawnReason.NATURAL, null, null);
                    world.addEntity(child);
                }
            }
            return true;
        }

        return false;
    }
}
