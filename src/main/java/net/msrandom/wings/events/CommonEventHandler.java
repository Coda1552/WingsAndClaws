package net.msrandom.wings.events;

import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.item.WingsItems;

import java.util.List;

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
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.isHandActive()) {
			ItemStack stack = event.player.getActiveItemStack();
			if (stack.getItem() == WingsItems.ICY_PLOWHEAD_SHIELD) {
				List<LivingEntity> list = event.player.world.getEntitiesWithinAABB(LivingEntity.class, event.player.getBoundingBox().grow(2));
				if (!list.isEmpty()) {
					double speed = Math.abs(event.player.getMotion().x * event.player.getMotion().x + event.player.getMotion().z * event.player.getMotion().z);
					stack.damageItem(1, event.player, entity -> entity.sendBreakAnimation(event.player.getActiveHand()));
					for (LivingEntity entity : list) {
						if (entity != event.player) {
							entity.knockBack(event.player, (float) speed * 3.325f, MathHelper.sin((float) Math.toRadians(event.player.rotationYaw)), MathHelper.cos((float) Math.toRadians(-event.player.rotationYaw)));
						}
					}
				}
			}
		}
	}
}
