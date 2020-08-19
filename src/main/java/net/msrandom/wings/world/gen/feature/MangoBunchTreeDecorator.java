package net.msrandom.wings.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.decorator.TreeDecorator;
import net.minecraft.world.gen.decorator.TreeDecoratorType;
import net.minecraft.world.gen.feature.Feature;
import net.msrandom.wings.block.MangoBlock;
import net.msrandom.wings.block.WingsBlocks;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class MangoBunchTreeDecorator extends TreeDecorator {
    public static final Codec<MangoBunchTreeDecorator> CODEC = Codec.unit(new MangoBunchTreeDecorator());

    @Override
    protected TreeDecoratorType<?> getType() {
        return WingsFeatures.MANGO_BUNCH;
    }

    @Override
    public void generate(WorldAccess world, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions, Set<BlockPos> set, BlockBox box) {
        if (random.nextFloat() <= 0.2f) {
            for (BlockPos pos : logPositions) {
                for (Direction direction : Direction.Type.HORIZONTAL) {
                    if (random.nextFloat() <= 0.25F) {
                        Direction direction1 = direction.getOpposite();
                        BlockPos blockpos = pos.add(direction1.getOffsetX(), 0, direction1.getOffsetZ());
                        if (Feature.method_27370(world, blockpos)) {
                            BlockState blockstate = WingsBlocks.MANGO_BUNCH.getDefaultState().with(MangoBlock.MANGOES, random.nextInt(4) + 1);
                            this.setBlockStateAndEncompassPosition(world, blockpos, blockstate, set, box);
                        }
                    }
                }
            }
        }
    }
}
