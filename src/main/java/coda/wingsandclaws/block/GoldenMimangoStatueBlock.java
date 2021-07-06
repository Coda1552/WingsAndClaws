package coda.wingsandclaws.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import net.minecraft.block.AbstractBlock.Properties;

public class GoldenMimangoStatueBlock extends Block {
    public GoldenMimangoStatueBlock(Properties properties) {
        super(properties);
    }

    public int getLightBlock(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }
}
