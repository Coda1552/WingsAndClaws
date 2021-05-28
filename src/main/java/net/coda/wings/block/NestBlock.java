package net.coda.wings.block;

import net.coda.wings.entity.TameableDragonEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.coda.wings.tileentity.NestTileEntity;

import javax.annotation.Nullable;

public class NestBlock<T extends NestTileEntity> extends ContainerBlock {
    private static final VoxelShape AABB = VoxelShapes.create(0, 0, 0, 1, 0.4, 1);
    private final Class<? extends TameableDragonEntity> entity;
    private final Class<T> tile;
    private TileEntityType<T> type;
    private Item item;

    public NestBlock(Block.Properties properties, Class<? extends TameableDragonEntity> entity, Class<T> tile) {
        super(properties);
        this.entity = entity;
        this.tile = tile;
    }

    public void setItem(TileEntityType<T> value) {
        this.type = value;
    }

    public void setItem(Item value) {
        this.item = value;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return type.create();
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack stack = player.getHeldItem(handIn);
        TileEntity te = worldIn.getTileEntity(pos);
        if (tile.isInstance(te)) {
            if (stack.isEmpty()) {
                boolean removed = ((NestTileEntity) te).removeEgg();
                if (removed) {
                    ItemStack egg = new ItemStack(item);
                    if (!player.addItemStackToInventory(egg)) {
                        player.dropItem(egg, false);
                    }
                }
                return removed ? ActionResultType.SUCCESS : ActionResultType.PASS;
            } else if (stack.getItem() == item) {
                if (player.isSneaking() && ((NestTileEntity) te).addEgg()) {
                    player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
                    if (!player.abilities.isCreativeMode) stack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
                boolean removed = ((NestTileEntity) te).removeEgg();
                if (removed) {
                    ItemStack egg = new ItemStack(item);
                    if (!player.addItemStackToInventory(egg)) {
                        player.dropItem(egg, false);
                    }
                }
                return removed ? ActionResultType.SUCCESS : ActionResultType.PASS;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (!player.abilities.isCreativeMode)
            worldIn.getEntitiesWithinAABB(entity, player.getBoundingBox().grow(32)).stream().filter(entity -> !entity.isChild() && entity.getGender() == TameableDragonEntity.Gender.MALE && !entity.isOwner(player) && !entity.isSleeping()).forEach(e -> e.setAttackTarget(player));
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
