package net.msrandom.wings.events;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.item.WingsItems;

@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {
    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.isCreative())
            return;
        World world = event.getWorld().getWorld();
        Material material = event.getState().getMaterial();
        if (!world.isRemote && (material == Material.ICE || material == Material.PACKED_ICE)) {
            if (world.rand.nextInt(48) == 0) {
                BlockPos pos = event.getPos();
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, new ItemStack(WingsItems.GLACIAL_SHRIMP)));
            }
        }
    }

    @SubscribeEvent
    public static void livingHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (player.isHandActive()) {
            ItemStack stack = player.getActiveItemStack();
            if (stack.getItem() == WingsItems.ICY_PLOWHEAD_SHIELD) {
                double speed = Math.abs(player.getMotion().x * player.getMotion().x + player.getMotion().z * player.getMotion().z);
                if (!player.abilities.isCreativeMode)
                    stack.damageItem(1, player, entity -> entity.sendBreakAnimation(player.getActiveHand()));
                DamageSource source = event.getSource();
                Entity entity = source.getImmediateSource();
                if (entity != null && !source.isUnblockable()) {
                    Vec3d vec3d = source.getDamageLocation();
                    if (vec3d != null) {
                        Vec3d vec3d1 = player.getLook(1.0F);
                        Vec3d vec3d2 = vec3d.subtractReverse(player.getPositionVec()).normalize();
                        vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
                        if (vec3d2.dotProduct(vec3d1) < 0.0D) {
                            if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).knockBack(player, (float) speed * 3.325f, MathHelper.sin((float) Math.toRadians(-entity.rotationYaw)), MathHelper.cos((float) Math.toRadians(entity.rotationYaw)));
                            } else {
                                double strength = speed * 3.325;
                                Vec3d vec3d3 = entity.getMotion();
                                Vec3d vec3d4 = new Vec3d(MathHelper.sin((float) Math.toRadians(-entity.rotationYaw)), 0.0D, MathHelper.cos((float) Math.toRadians(entity.rotationYaw))).normalize().scale(strength);
                                entity.setMotion(vec3d3.x / 2.0D - vec3d4.x, entity.onGround ? Math.min(0.4D, vec3d3.y / 2.0D + strength) : vec3d3.y, vec3d3.z / 2.0D - vec3d4.z);
                            }
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {
        if (!event.getName().toString().equals("minecraft:chests/desert_pyramid")) {
            return;
        }

        event.getTable().addPool(LootPool.builder().addEntry(
                TableLootEntry.builder(new ResourceLocation(WingsAndClaws.MOD_ID, "inject/desert_pyramid"))
                        .weight(1)).bonusRolls(0, 0).name("wings_inject").build());
    }
}
