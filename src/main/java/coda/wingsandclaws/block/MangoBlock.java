package coda.wingsandclaws.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class MangoBlock extends CropsBlock implements IGrowable {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 1, 4);

    protected static final VoxelShape ONE_SHAPE = Block.makeCuboidShape(4.0D, 10.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape TWO_SHAPE = Block.makeCuboidShape(3.0D, 10.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape THREE_SHAPE = Block.makeCuboidShape(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape FOUR_SHAPE = Block.makeCuboidShape(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public MangoBlock() {
        super(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0F).sound(SoundType.CROP));
        this.setDefaultState(this.stateContainer.getBaseState().with(STAGE, 1));
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        int i = state.get(STAGE);
        if (i < 4 && ForgeHooks.onCropsGrowPre(worldIn, pos, state, worldIn.rand.nextInt(5) == 0)) {
            worldIn.setBlockState(pos, state.with(STAGE, i + 1), 2);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
        }
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() == Blocks.JUNGLE_LOG || state.getBlock() == Blocks.JUNGLE_LEAVES;
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.up();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        worldIn.setBlockState(pos, state.with(STAGE, state.get(STAGE) + 1), 2);
    }

    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(STAGE) < 4;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.isValidPosition(worldIn, currentPos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return useContext.getItem().getItem() == this.asItem() && state.get(STAGE) < 4 || super.isReplaceable(state, useContext);
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch(state.get(STAGE)) {
            case 1:
            default:
                return ONE_SHAPE;
            case 2:
                return TWO_SHAPE;
            case 3:
                return THREE_SHAPE;
            case 4:
                return FOUR_SHAPE;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
        super.fillStateContainer(builder);
    }
}
