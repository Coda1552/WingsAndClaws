package net.msrandom.wings.block;

import com.sun.istack.internal.Nullable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.ServerWorldAccessReader;


public class MangoBlock extends Block {
    public static final IntProperty MANGOES = Properties.PICKLES;

    protected static final VoxelShape ONE_SHAPE = Block.createCuboidShape(6.0D, 10.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    protected static final VoxelShape TWO_SHAPE = Block.createCuboidShape(3.0D, 10.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape THREE_SHAPE = Block.createCuboidShape(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape FOUR_SHAPE = Block.createCuboidShape(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    protected MangoBlock() {
        super(FabricBlockSettings.of(Material.BAMBOO).sounds(BlockSoundGroup.BAMBOO).nonOpaque());
        this.setDefaultState(this.getStateManager().getDefaultState().with(MANGOES, 1));
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getBlockPos());
        if (blockstate.getBlock() == this) {
            return blockstate.with(MANGOES, Math.min(4, blockstate.get(MANGOES) + 1));
        }
        return super.getPlacementState(context);
    }

    @Override
    public int getLightValue(BlockState state) {
        return super.getLightValue(state) + 3 * state.get(MANGOES);
    }

    protected boolean isValidGround(BlockState state, BlockView world, BlockPos pos) {
        return state.getBlock().isIn(BlockTags.LOGS) || state.getBlock().isIn(BlockTags.LEAVES);
    }

    public boolean isValidPosition(BlockState state, ServerWorldAccessReader world, BlockPos pos) {
        BlockPos blockpos = pos.up();
        return this.isValidGround(world.getBlockState(blockpos), world, blockpos);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, ServerWorldAccess world, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.isValidPosition(world, currentPos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            return super.updatePostPlacement(stateIn, facing, facingState, world, currentPos, facingPos);
        }
    }

    public boolean isReplaceable(BlockState state, ItemPlacementContext useContext) {
        return useContext.getItem().getItem() == this.asItem() && state.get(MANGOES) < 4 || super.isReplaceable(state, useContext);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(MANGOES)) {
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(MANGOES);
    }
}
