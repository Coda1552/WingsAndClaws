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

    protected static final VoxelShape ONE_SHAPE = Block.box(4.0D, 10.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape TWO_SHAPE = Block.box(3.0D, 10.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape THREE_SHAPE = Block.box(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape FOUR_SHAPE = Block.box(2.0D, .0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public MangoBlock() {
        super(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0F).sound(SoundType.CROP));
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 1));
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        int i = state.getValue(STAGE);
        if (i < 4 && ForgeHooks.onCropsGrowPre(worldIn, pos, state, worldIn.random.nextInt(5) == 0)) {
            worldIn.setBlock(pos, state.setValue(STAGE, i + 1), 2);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
        }
    }

    protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() == Blocks.JUNGLE_LOG || state.getBlock() == Blocks.JUNGLE_LEAVES;
    }

    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return this.mayPlaceOn(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Override
    public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        worldIn.setBlock(pos, state.setValue(STAGE, state.getValue(STAGE) + 1), 2);
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(STAGE) < 4;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.canSurvive(worldIn, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    public boolean canBeReplaced(BlockState state, BlockItemUseContext useContext) {
        return /*useContext.getItem().getItem() == this.asItem() && state.get(STAGE) < 4 || super.isReplaceable(state, useContext)*/ false;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch(state.getValue(STAGE)) {
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
        super.createBlockStateDefinition(builder);
    }
}
