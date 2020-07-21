package net.msrandom.wings.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.client.renderer.tileentity.PlowheadHornRenderer;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

import java.util.Collections;

public class HornHornItem extends ToolItem {
    public HornHornItem() {
        super(-2, -3, ItemTier.IRON, Collections.emptySet(), new Item.Properties().group(WingsItems.GROUP).maxStackSize(1).setISTER(() -> PlowheadHornRenderer::new));
        this.addPropertyOverride(new Identifier(WingsAndClaws.MOD_ID, "using"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerIn, Hand hand) {
        playerIn.setActiveHand(hand);
        world.playSound(null, playerIn.getBlockPos(), WingsSounds.BATTLE_HORN, SoundCategory.PLAYERS, 1, 1);
        return ActionResult.resultSuccess(playerIn.getStackInHand(hand));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            Vec3d vec = player.getPositionVec().add(player.getLookVec());
            EntityRayTraceResult entityTrace = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(vec.x - 2, vec.y - 2, vec.z - 2, vec.x + 2, vec.y + 2, vec.z + 2)).stream().reduce((a, b) -> a.squaredDistanceTo(player) < b.squaredDistanceTo(player) ? a : b).map(EntityRayTraceResult::new).orElse(null);
            RayTraceResult mop = entityTrace == null || entityTrace.getType() == RayTraceResult.Type.MISS ? rayTrace(world, player, RayTraceContext.FluidMode.NONE) : entityTrace;
            if (mop.getType() != RayTraceResult.Type.MISS) {
                boolean flag = false;
                if (mop.getType() == RayTraceResult.Type.ENTITY) {
                    EntityRayTraceResult result = ((EntityRayTraceResult) mop);
                    if (!(result.getEntity() instanceof TameableDragonEntity) || !((TameableDragonEntity) result.getEntity()).isOwner(player)) {
                        flag = world.getFluidState(result.getEntity().getBlockPos()).getFluid() == Fluids.WATER;
                    } else {
                        TameableDragonEntity entity = (TameableDragonEntity) result.getEntity();
                        TameableDragonEntity.WanderState state = entity.getState();
                        TameableDragonEntity.WanderState newState = state == TameableDragonEntity.WanderState.FOLLOW ? TameableDragonEntity.WanderState.WANDER : TameableDragonEntity.WanderState.values()[state.ordinal() + 1];
                        entity.setState(newState);
                        player.sendStatusMessage(new TranslationTextComponent("entity." + WingsAndClaws.MOD_ID + ".state." + newState.name().toLowerCase()), true);
                        player.getCooldownTracker().setCooldown(this, 48);
                        return stack;
                    }
                } else {
                    BlockPos pos = ((BlockRayTraceResult) mop).getPos();
                    for (Direction value : Direction.values()) {
                        flag = world.getFluidState(pos.offset(value)).getFluid() == Fluids.WATER;
                        if (flag) break;
                    }
                }
                if (flag) {
                    IcyPlowheadEntity last = null;
                    for (IcyPlowheadEntity entity : world.getEntitiesWithinAABB(IcyPlowheadEntity.class, player.getBoundingBox().grow(64))) {
                        if (entity.isOwner(player) && (last == null || last.squaredDistanceTo(player) > entity.squaredDistanceTo(player))) {
                            last = entity;
                        }
                    }
                    if (last != null) {
                        if (last.getState() == TameableDragonEntity.WanderState.STAY)
                            last.setState(TameableDragonEntity.WanderState.WANDER);
                        last.setTarget(mop);
                        last.setHorn(stack);
                        player.getCooldownTracker().setCooldown(this, 48);
                        return stack;
                    }
                }
            }
        }
        return super.onItemUseFinish(stack, world, entityLiving);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return HashMultimap.create();
    }
}
