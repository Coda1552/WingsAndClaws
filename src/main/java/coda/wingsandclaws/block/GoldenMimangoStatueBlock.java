package coda.wingsandclaws.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class GoldenMimangoStatueBlock extends Block {
    public GoldenMimangoStatueBlock(Properties properties) {
        super(properties);
    }

    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }
}
