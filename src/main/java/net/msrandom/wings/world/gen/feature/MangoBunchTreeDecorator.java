package net.msrandom.wings.world.gen.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.msrandom.wings.block.MangoBlock;
import net.msrandom.wings.block.WingsBlocks;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class MangoBunchTreeDecorator extends TreeDecorator {
    public MangoBunchTreeDecorator() {
        super(WingsFeatures.MANGO_BUNCH);
    }

    public <T> MangoBunchTreeDecorator(@SuppressWarnings("unused") Dynamic<T> dynamic) {
        this();
    }

    @Override
    public void func_225576_a_(IWorld world, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions, Set<BlockPos> set, MutableBoundingBox boundingBox) {
        for (BlockPos pos : logPositions) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (random.nextFloat() <= 0.25F) {
                    Direction direction1 = direction.getOpposite();
                    BlockPos blockpos = pos.add(direction1.getXOffset(), 0, direction1.getZOffset());
                    if (AbstractTreeFeature.isAir(world, blockpos)) {
                        BlockState blockstate = WingsBlocks.MANGO_BUNCH.getDefaultState().with(MangoBlock.MANGOES, random.nextInt(4) + 1);
                        this.func_227423_a_(world, blockpos, blockstate, set, boundingBox);
                    }
                }
            }
        }
    }

    @Override
    public <T> T serialize(DynamicOps<T> dynamicOps) {
        return new Dynamic<>(dynamicOps, dynamicOps.createMap(ImmutableMap.of(dynamicOps.createString("type"), dynamicOps.createString(Registry.TREE_DECORATOR_TYPE.getKey(this.field_227422_a_).toString())))).getValue();
    }
}
