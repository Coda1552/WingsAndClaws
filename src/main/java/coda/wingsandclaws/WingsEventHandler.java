package coda.wingsandclaws;

import coda.wingsandclaws.client.WingsKeyBindings;
import coda.wingsandclaws.entity.item.MimangoEggEntity;
import coda.wingsandclaws.entity.MimangoEntity;
import coda.wingsandclaws.init.WingsItems;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import coda.wingsandclaws.network.CallHatchetBeaksPacket;
import coda.wingsandclaws.network.HatchetBeakAttackPacket;
import coda.wingsandclaws.resources.TamePointsManager;

@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WingsEventHandler {
    private static int hatchetBeakCallTimer;
    private static int hatchetBeakAttackTimer;

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.isCreative())
            return;
        IWorld world = event.getWorld();
        Material material = event.getState().getMaterial();
        if (world instanceof World && !world.isClientSide() && (material == Material.ICE || material == Material.ICE_SOLID)) {
            if (world.getRandom().nextInt(48) == 0) {
                BlockPos pos = event.getPos();
                world.addFreshEntity(new ItemEntity((World) world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, new ItemStack(WingsItems.GLACIAL_SHRIMP.get())));
            }
        }
    }

    @SubscribeEvent
    public static void livingHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (player.isUsingItem()) {
            ItemStack stack = player.getUseItem();
            if (stack.getItem() == WingsItems.ICY_PLOWHEAD_SHIELD.get()) {
                double speed = Math.abs(player.getDeltaMovement().x * player.getDeltaMovement().x + player.getDeltaMovement().z * player.getDeltaMovement().z);
                if (!player.abilities.instabuild)
                    stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(player.getUsedItemHand()));
                DamageSource source = event.getSource();
                Entity entity = source.getDirectEntity();
                if (entity != null && !source.isBypassArmor()) {
                    Vector3d vec3d = source.getSourcePosition();
                    if (vec3d != null) {
                        Vector3d vec3d1 = player.getViewVector(1.0F);
                        Vector3d vec3d2 = vec3d.vectorTo(player.position()).normalize();
                        vec3d2 = new Vector3d(vec3d2.x, 0.0D, vec3d2.z);
                        if (vec3d2.dot(vec3d1) < 0.0D) {
                            if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).knockback((float) speed * 3.325f, MathHelper.sin((float) Math.toRadians(-entity.yRot)), MathHelper.cos((float) Math.toRadians(entity.yRot)));
                            } else {
                                double strength = speed * 3.325;
                                Vector3d vec3d3 = entity.getDeltaMovement();
                                Vector3d vec3d4 = new Vector3d(MathHelper.sin((float) Math.toRadians(-entity.yRot)), 0.0D, MathHelper.cos((float) Math.toRadians(entity.yRot))).normalize().scale(strength);
                                entity.setDeltaMovement(vec3d3.x / 2.0D - vec3d4.x, entity.isOnGround() ? Math.min(0.4D, vec3d3.y / 2.0D + strength) : vec3d3.y, vec3d3.z / 2.0D - vec3d4.z);
                            }
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().getBlockState(event.getPos()).is(BlockTags.LEAVES)) {
            MimangoEggEntity egg = MimangoEggEntity.getEgg(event.getWorld(), event.getPos());
            if (egg != null) {
                ItemStack eggItem = new ItemStack(WingsItems.MIMANGO_EGG.get());
                if (!event.getPlayer().addItem(eggItem)) {
                    event.getPlayer().drop(eggItem, false);
                }
                egg.remove();
            }
        }
    }

    @SubscribeEvent
    public static void spawnEntity(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof OcelotEntity) {
            ((OcelotEntity) entity).targetSelector.addGoal(0, new NearestAttackableTargetGoal<>((MobEntity) entity, MimangoEntity.class, true));
        }
    }

    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {
        String lootLocation = event.getName().toString().replace("minecraft:chests/", "");

        switch (lootLocation) {
            case "desert_pyramid":
            case "nether_bridge":
            case "buried_treasure":
            case "shipwreck_treasure":
                event.getTable().addPool(LootPool.lootPool().add(
                        TableLootEntry.lootTableReference(new ResourceLocation(WingsAndClaws.MOD_ID, "inject/" + lootLocation))
                                .setWeight(1)).bonusRolls(0, 0).name("wingsandclaws_inject").build());
                break;
            default:
                break;
        }
    }

    @SubscribeEvent
    public static void addDataPackRegistries(AddReloadListenerEvent event) {
        event.addListener(TamePointsManager.INSTANCE);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (hatchetBeakCallTimer == 0) {
            if (WingsKeyBindings.CALL_HATCHET_BEAK.isDown()) {
                WingsAndClaws.NETWORK.sendToServer(new CallHatchetBeaksPacket());
                hatchetBeakCallTimer = 200;
            }
        } else {
            --hatchetBeakCallTimer;
        }

        if (hatchetBeakAttackTimer == 0) {
            if (WingsKeyBindings.HATCHET_BEAK_ATTACK.isDown()) {
                WingsAndClaws.NETWORK.sendToServer(new HatchetBeakAttackPacket());
                hatchetBeakAttackTimer = 10;
            }
        } else {
            --hatchetBeakAttackTimer;
        }
    }
}
