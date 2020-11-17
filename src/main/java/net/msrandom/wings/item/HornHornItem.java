package net.msrandom.wings.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.client.renderer.tileentity.PlowheadHornRenderer;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;

import java.util.Collections;

public class HornHornItem extends ToolItem {
    public HornHornItem() {
        super(-2, -3, ItemTier.IRON, Collections.emptySet(), new Item.Properties().group(WingsItems.GROUP).maxStackSize(1).setISTER(() -> PlowheadHornRenderer::new));
        ItemModelsProperties.registerProperty(this, new ResourceLocation(WingsAndClaws.MOD_ID, "using"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        worldIn.playSound(null, playerIn.getPosition(), WingsSounds.BATTLE_HORN, SoundCategory.PLAYERS, 0.5f, 1);
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            Vector3d vec = player.getPositionVec().add(player.getLookVec());
            EntityRayTraceResult entityTrace = worldIn.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(vec.x - 2, vec.y - 2, vec.z - 2, vec.x + 2, vec.y + 2, vec.z + 2)).stream().reduce((a, b) -> a.getDistanceSq(player) < b.getDistanceSq(player) ? a : b).map(EntityRayTraceResult::new).orElse(null);
            RayTraceResult mop = entityTrace == null || entityTrace.getType() == RayTraceResult.Type.MISS ? rayTrace(worldIn, player, RayTraceContext.FluidMode.NONE) : entityTrace;
            if (mop.getType() == RayTraceResult.Type.MISS) {
                MimangoEntity last = null;
                for (MimangoEntity entity : worldIn.getEntitiesWithinAABB(MimangoEntity.class, player.getBoundingBox().grow(4))) {
                    if (entity.isOwner(player) && (last == null || last.getDistanceSq(player) > entity.getDistanceSq(player))) {
                        last = entity;
                    }
                }
                if (last != null) {
                    changeEntityState(last, player);
                    return stack;
                }
            } else {
                boolean flag = false;
                if (mop.getType() == RayTraceResult.Type.ENTITY) {
                    EntityRayTraceResult result = ((EntityRayTraceResult) mop);
                    if (!(result.getEntity() instanceof TameableDragonEntity) || !((TameableDragonEntity) result.getEntity()).isOwner(player)) {
                        flag = worldIn.getFluidState(result.getEntity().getPosition()).getFluid() == Fluids.WATER;
                    } else {
                        changeEntityState((TameableDragonEntity) result.getEntity(), player);
                        return stack;
                    }
                } else {
                    BlockPos pos = ((BlockRayTraceResult) mop).getPos();
                    for (Direction value : Direction.values()) {
                        flag = worldIn.getFluidState(pos.offset(value)).getFluid() == Fluids.WATER;
                        if (flag) break;
                    }
                }
                if (flag) {
                    IcyPlowheadEntity last = null;
                    for (IcyPlowheadEntity entity : worldIn.getEntitiesWithinAABB(IcyPlowheadEntity.class, player.getBoundingBox().grow(64))) {
                        if (entity.isOwner(player) && (last == null || last.getDistanceSq(player) > entity.getDistanceSq(player))) {
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
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    private void changeEntityState(TameableDragonEntity entity, PlayerEntity player) {
        TameableDragonEntity.WanderState state = entity.getState();
        TameableDragonEntity.WanderState newState = state == TameableDragonEntity.WanderState.FOLLOW ? TameableDragonEntity.WanderState.WANDER : TameableDragonEntity.WanderState.values()[state.ordinal() + 1];
        entity.setState(newState);
        player.sendStatusMessage(new TranslationTextComponent("entity." + WingsAndClaws.MOD_ID + ".state." + newState.name().toLowerCase()), true);
        player.getCooldownTracker().setCooldown(this, 48);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return HashMultimap.create();
    }
}
