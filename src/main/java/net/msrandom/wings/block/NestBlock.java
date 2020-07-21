package net.msrandom.wings.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.msrandom.wings.entity.TameableDragonEntity;

public class NestBlock<T extends NestBlockEntity> extends BlockWithEntity {
    private static final VoxelShape AABB = VoxelShapes.cuboid(0.05, 0, 0.05, 0.95, 0.3, 0.95);
    private final Class<? extends TameableDragonEntity> entity;
    private final Class<T> tile;
    private BlockEntityType<T> type;
    private Item item;

    public NestBlock(Block.Settings properties, Class<? extends TameableDragonEntity> entity, Class<T> tile) {
        super(properties);
        this.entity = entity;
        this.tile = tile;
    }

    public void setItem(BlockEntityType<T> value) {
        this.type = value;
    }

    public void setItem(Item value) {
        this.item = value;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return type.instantiate();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AABB;
    }

    @Override
    public BlockRenderLayer getLayer(BlockState state) {
        return BlockRenderLayer.INVISIBLE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        BlockEntity te = world.getBlockEntity(pos);
        if (tile.isInstance(te)) {
            if (stack.isEmpty()) {
                boolean removed = ((NestBlockEntity) te).removeEgg();
                if (removed) player.giveItemStack(new ItemStack(item));
                return removed ? ActionResult.SUCCESS : ActionResult.PASS;
            } else if (stack.getItem() == item) {
                if (player.isSneaking() && ((NestBlockEntity) te).addEgg()) {
                    player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
                    if (!player.abilities.creativeMode) stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
                boolean removed = ((NestBlockEntity) te).removeEgg();
                if (removed) player.giveItemStack(new ItemStack(item));
                return removed ? ActionResult.SUCCESS : ActionResult.PASS;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack) {
        if (!player.abilities.creativeMode)
            world.getNonSpectatingEntities(entity, player.getBoundingBox().expand(32)).stream().filter(entity -> !entity.isBaby() && entity.getGender() && !entity.isOwner(player) && !entity.isSleeping()).forEach(e -> e.setTarget(player));
        super.afterBreak(world, player, pos, state, te, stack);
    }
}
