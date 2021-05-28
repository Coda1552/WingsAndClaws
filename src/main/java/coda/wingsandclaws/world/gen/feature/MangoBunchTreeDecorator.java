package coda.wingsandclaws.world.gen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import coda.wingsandclaws.block.MangoBlock;
import coda.wingsandclaws.init.WingsBlocks;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class MangoBunchTreeDecorator extends TreeDecorator {
    @Override
    public void func_225576_a_(ISeedReader world, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions, Set<BlockPos> set, MutableBoundingBox boundingBox) {
        if (random.nextFloat() <= 0.2f) {
            for (BlockPos pos : logPositions) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (random.nextFloat() <= 0.25F) {
                        Direction direction1 = direction.getOpposite();
                        BlockPos blockpos = pos.add(direction1.getXOffset(), 0, direction1.getZOffset());
                        if (TreeFeature.isAirAt(world, blockpos)) {
                            BlockState blockstate = WingsBlocks.MANGO_BUNCH.get().getDefaultState().with(MangoBlock.MANGOES, random.nextInt(4) + 1);
                            this.func_227423_a_(world, blockpos, blockstate, set, boundingBox);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected TreeDecoratorType<?> func_230380_a_() {
        return WingsFeatures.MANGO_BUNCH.get();
    }
}
