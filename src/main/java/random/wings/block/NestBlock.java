package random.wings.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import random.wings.entity.DumpyEggDrakeEntity;
import random.wings.item.WingsItems;
import random.wings.tileentity.NestTileEntity;
import random.wings.tileentity.WingsTileEntities;

import javax.annotation.Nullable;

public class NestBlock extends ContainerBlock {
    private static final VoxelShape AABB = VoxelShapes.create(0.05, 0, 0.05, 0.95, 0.3, 0.95);

    protected NestBlock() {
        super(Block.Properties.create(Material.SAND).sound(SoundType.SAND).hardnessAndResistance(1, 0));
        String name = "nest";
        setRegistryName(name);
        WingsBlocks.LIST.add(this);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return WingsTileEntities.NEST.create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack stack = player.getHeldItem(handIn);
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof NestTileEntity) {
            if (stack.isEmpty()) {
                boolean removed = ((NestTileEntity) te).removeEgg();
                if (removed) player.setHeldItem(handIn, new ItemStack(WingsItems.DUMPY_EGG_DRAKE_EGG));
                return removed;
            } else if (stack.getItem() == WingsItems.DUMPY_EGG_DRAKE_EGG) {
                boolean added = ((NestTileEntity) te).addEgg();
                if (added) {
                    if (!player.abilities.isCreativeMode) stack.shrink(1);
                } else {
                    boolean removed = stack.getCount() < stack.getMaxStackSize() && ((NestTileEntity) te).removeEgg();
                    if (removed) stack.grow(1);
                    return removed;
                }
                return true;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (!player.abilities.isCreativeMode)
            worldIn.getEntitiesWithinAABB(DumpyEggDrakeEntity.class, player.getBoundingBox().grow(32)).stream().filter(entity -> !entity.isChild() && entity.getGender() == -1 && !entity.isSleeping()).forEach(e -> e.setAttackTarget(player));
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
