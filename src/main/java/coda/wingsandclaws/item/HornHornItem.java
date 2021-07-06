package coda.wingsandclaws.item;

import coda.wingsandclaws.init.WingsSounds;
import coda.wingsandclaws.client.renderer.tileentity.PlowheadHornRenderer;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.entity.IcyPlowheadEntity;
import coda.wingsandclaws.init.WingsItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import coda.wingsandclaws.WingsAndClaws;

import java.util.Collections;

public class HornHornItem extends ToolItem {
    public HornHornItem() {
        super(-2, -3, ItemTier.IRON, Collections.emptySet(), new Item.Properties().tab(WingsItems.GROUP).stacksTo(1).setISTER(() -> PlowheadHornRenderer::new));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ItemModelsProperties.register(this, new ResourceLocation(WingsAndClaws.MOD_ID, "using"), (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.startUsingItem(handIn);
        worldIn.playSound(null, playerIn.blockPosition(), WingsSounds.BATTLE_HORN.get(), SoundCategory.PLAYERS, 0.5f, 1);
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            Vector3d vec = player.position().add(player.getLookAngle());
            EntityRayTraceResult entityTrace = world.getEntities(player, new AxisAlignedBB(vec.x - 2, vec.y - 2, vec.z - 2, vec.x + 2, vec.y + 2, vec.z + 2)).stream().reduce((a, b) -> a.distanceToSqr(player) < b.distanceToSqr(player) ? a : b).map(EntityRayTraceResult::new).orElse(null);
            RayTraceResult hit = entityTrace == null || entityTrace.getType() == RayTraceResult.Type.MISS ? getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE) : entityTrace;
            if (hit.getType() != RayTraceResult.Type.MISS) {
                boolean flag = false;
                if (hit.getType() == RayTraceResult.Type.ENTITY && hit instanceof EntityRayTraceResult) {
                    EntityRayTraceResult result = ((EntityRayTraceResult) hit);
                    if (!(result.getEntity() instanceof TameableDragonEntity) || !((TameableDragonEntity) result.getEntity()).isOwnedBy(player)) {
                        flag = world.getFluidState(result.getEntity().blockPosition()).getType() == Fluids.WATER;
                    } else {
                        return stack;
                    }
                } else if (hit instanceof BlockRayTraceResult) {
                    BlockPos pos = ((BlockRayTraceResult) hit).getBlockPos();
                    for (Direction value : Direction.values()) {
                        flag = world.getFluidState(pos.relative(value)).getType() == Fluids.WATER;
                        if (flag) break;
                    }
                }
                if (flag) {
                    IcyPlowheadEntity last = null;
                    for (IcyPlowheadEntity entity : world.getEntitiesOfClass(IcyPlowheadEntity.class, player.getBoundingBox().inflate(64))) {
                        if (entity.isOwnedBy(player) && (last == null || last.distanceToSqr(player) > entity.distanceToSqr(player))) {
                            last = entity;
                        }
                    }
                    if (last != null) {
                        if (last.isOrderedToSit())
                            last.setOrderedToSit(false);
                        last.setTarget(hit);
                        last.setHorn(stack);
                        player.getCooldowns().addCooldown(this, 48);
                        world.addParticle(ParticleTypes.HAPPY_VILLAGER, entityLiving.getX() + 0.5, entityLiving.getY() + 1.5, entityLiving.getZ() + 0.5, 0.0D, 0.0D, 0.0D);

                        for (int i = 0; i < 15; ++i) {
                            double d2 = random.nextGaussian() * 0.02D;
                            double d3 = random.nextGaussian() * 0.02D;
                            double d4 = random.nextGaussian() * 0.02D;
                            double d6 = entityLiving.getX() + random.nextDouble();
                            double d7 = entityLiving.getY() + random.nextDouble();
                            double d8 = entityLiving.getZ() + random.nextDouble();
                            world.addParticle(ParticleTypes.HAPPY_VILLAGER, d6, d7, d8, d2, d3, d4);
                        }
                        return stack;
                    }
                }
            }
        }
        return super.finishUsingItem(stack, world, entityLiving);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return HashMultimap.create();
    }
}
