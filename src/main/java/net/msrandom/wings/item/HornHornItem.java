package net.msrandom.wings.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.client.ClientEventHandler;
import net.msrandom.wings.client.renderer.tileentity.PlowheadHornRenderer;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

//TODO mixin enchantment
public class HornHornItem extends ToolItem {
    public HornHornItem() {
        super(ToolMaterials.IRON, new Settings().group(WingsItems.GROUP).maxCount(1));
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            ClientEventHandler.getWithISTER(this, () -> PlowheadHornRenderer::new);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand hand) {
        playerIn.setCurrentHand(hand);
        world.playSound(null, playerIn.getBlockPos(), WingsSounds.BATTLE_HORN, SoundCategory.PLAYERS, 1, 1);
        return TypedActionResult.success(playerIn.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            Vec3d vec = player.getPos().add(player.getRotationVector());
            EntityHitResult entityTrace = world.getEntities(player, new Box(vec.x - 2, vec.y - 2, vec.z - 2, vec.x + 2, vec.y + 2, vec.z + 2)).stream().reduce((a, b) -> a.squaredDistanceTo(player) < b.squaredDistanceTo(player) ? a : b).map(EntityHitResult::new).orElse(null);
            HitResult mop = entityTrace == null || entityTrace.getType() == HitResult.Type.MISS ? rayTrace(world, player, RayTraceContext.FluidHandling.NONE) : entityTrace;
            if (mop.getType() != HitResult.Type.MISS) {
                boolean flag = false;
                if (mop.getType() == HitResult.Type.ENTITY) {
                    assert mop instanceof EntityHitResult;
                    EntityHitResult result = ((EntityHitResult) mop);
                    if (!(result.getEntity() instanceof TameableDragonEntity) || !((TameableDragonEntity) result.getEntity()).isOwner(player)) {
                        flag = world.getFluidState(result.getEntity().getBlockPos()).getFluid() == Fluids.WATER;
                    } else {
                        TameableDragonEntity entity = (TameableDragonEntity) result.getEntity();
                        TameableDragonEntity.WanderState state = entity.getState();
                        TameableDragonEntity.WanderState newState = state == TameableDragonEntity.WanderState.FOLLOW ? TameableDragonEntity.WanderState.WANDER : TameableDragonEntity.WanderState.values()[state.ordinal() + 1];
                        entity.setState(newState);
                        player.sendMessage(new TranslatableText("entity." + WingsAndClaws.MOD_ID + ".state." + newState.name().toLowerCase()), true);
                        player.getItemCooldownManager().set(this, 48);
                        return stack;
                    }
                } else {
                    BlockPos pos = ((BlockHitResult) mop).getBlockPos();
                    for (Direction value : Direction.values()) {
                        flag = world.getFluidState(pos.offset(value)).getFluid() == Fluids.WATER;
                        if (flag) break;
                    }
                }
                if (flag) {
                    IcyPlowheadEntity last = null;
                    for (IcyPlowheadEntity entity : world.getNonSpectatingEntities(IcyPlowheadEntity.class, player.getBoundingBox().expand(64))) {
                        if (entity.isOwner(player) && (last == null || last.squaredDistanceTo(player) > entity.squaredDistanceTo(player))) {
                            last = entity;
                        }
                    }
                    if (last != null) {
                        if (last.getState() == TameableDragonEntity.WanderState.STAY)
                            last.setState(TameableDragonEntity.WanderState.WANDER);
                        last.setHitTarget(mop);
                        last.setHorn(stack);
                        player.getItemCooldownManager().set(this, 48);
                        return stack;
                    }
                }
            }
        }
        return super.finishUsing(stack, world, entityLiving);
    }
}
