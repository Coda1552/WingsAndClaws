package net.msrandom.wings.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class RoastedHaroldsGreendrakeBlock extends Block {
    public static final IntegerProperty BITES = BlockStateProperties.BITES_0_6;
    protected static final VoxelShape[] SHAPES = new VoxelShape[]{Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D), Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D), Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D), Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D), Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D), Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D), Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D)};
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;


    protected RoastedHaroldsGreendrakeBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(BITES, Integer.valueOf(0)));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[state.get(BITES)];
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            ItemStack itemstack = player.getHeldItem(handIn);
            if (this.func_226911_a_(worldIn, pos, state, player) == ActionResultType.SUCCESS) {
                return ActionResultType.SUCCESS;
            }

            if (itemstack.isEmpty()) {
                return ActionResultType.CONSUME;
            }
        }

        return this.func_226911_a_(worldIn, pos, state, player);
    }

    private ActionResultType func_226911_a_(IWorld worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn) {
        if (!playerIn.canEat(false)) {
            return ActionResultType.PASS;
        } else {
            playerIn.addStat(Stats.EAT_CAKE_SLICE);
            playerIn.getFoodStats().addStats(3, 0.3F);
            int i = state.get(BITES);
            if (i < 6) {
                worldIn.setBlockState(pos, state.with(BITES, Integer.valueOf(i + 1)), 3);
            } else {
                worldIn.removeBlock(pos, false);
            }

            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BITES);
        builder.add(FACING);
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return (7 - blockState.get(BITES)) * 2;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
}
