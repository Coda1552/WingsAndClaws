package net.msrandom.wings.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.WingsSounds;
import net.msrandom.wings.client.renderer.tileentity.PlowheadHornRenderer;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;

import java.util.Collections;

public class HornHornItem extends ToolItem {
    public HornHornItem() {
        super(-2, -3, ItemTier.IRON, Collections.emptySet(), new Item.Properties().group(WingsItems.GROUP).maxStackSize(1).setISTER(() -> PlowheadHornRenderer::new));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ItemModelsProperties.registerProperty(this, new ResourceLocation(WingsAndClaws.MOD_ID, "using"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        worldIn.playSound(null, playerIn.getPosition(), WingsSounds.BATTLE_HORN, SoundCategory.PLAYERS, 0.5f, 1);
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            Vector3d vec = player.getPositionVec().add(player.getLookVec());
            EntityRayTraceResult entityTrace = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(vec.x - 2, vec.y - 2, vec.z - 2, vec.x + 2, vec.y + 2, vec.z + 2)).stream().reduce((a, b) -> a.getDistanceSq(player) < b.getDistanceSq(player) ? a : b).map(EntityRayTraceResult::new).orElse(null);
            RayTraceResult hit = entityTrace == null || entityTrace.getType() == RayTraceResult.Type.MISS ? rayTrace(world, player, RayTraceContext.FluidMode.NONE) : entityTrace;
            if (hit.getType() != RayTraceResult.Type.MISS) {
                boolean flag = false;
                if (hit.getType() == RayTraceResult.Type.ENTITY && hit instanceof EntityRayTraceResult) {
                    EntityRayTraceResult result = ((EntityRayTraceResult) hit);
                    if (!(result.getEntity() instanceof TameableDragonEntity) || !((TameableDragonEntity) result.getEntity()).isOwner(player)) {
                        flag = world.getFluidState(result.getEntity().getPosition()).getFluid() == Fluids.WATER;
                    } else {
                        return stack;
                    }
                } else if (hit instanceof BlockRayTraceResult) {
                    BlockPos pos = ((BlockRayTraceResult) hit).getPos();
                    for (Direction value : Direction.values()) {
                        flag = world.getFluidState(pos.offset(value)).getFluid() == Fluids.WATER;
                        if (flag) break;
                    }
                }
                if (flag) {
                    IcyPlowheadEntity last = null;
                    for (IcyPlowheadEntity entity : world.getEntitiesWithinAABB(IcyPlowheadEntity.class, player.getBoundingBox().grow(64))) {
                        if (entity.isOwner(player) && (last == null || last.getDistanceSq(player) > entity.getDistanceSq(player))) {
                            last = entity;
                        }
                    }
                    if (last != null) {
                        if (last.isSitting())
                            last.func_233687_w_(false);
                        last.setTarget(hit);
                        last.setHorn(stack);
                        player.getCooldownTracker().setCooldown(this, 48);
                        world.addParticle(ParticleTypes.HAPPY_VILLAGER, entityLiving.getPosX() + 0.5, entityLiving.getPosY() + 1.5, entityLiving.getPosZ() + 0.5, 0.0D, 0.0D, 0.0D);

                        for (int i = 0; i < 15; ++i) {
                            double d2 = random.nextGaussian() * 0.02D;
                            double d3 = random.nextGaussian() * 0.02D;
                            double d4 = random.nextGaussian() * 0.02D;
                            double d6 = entityLiving.getPosX() + random.nextDouble();
                            double d7 = entityLiving.getPosY() + random.nextDouble();
                            double d8 = entityLiving.getPosZ() + random.nextDouble();
                            world.addParticle(ParticleTypes.HAPPY_VILLAGER, d6, d7, d8, d2, d3, d4);
                        }
                        return stack;
                    }
                }
            }
        }
        return super.onItemUseFinish(stack, world, entityLiving);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return HashMultimap.create();
    }
}
