package random.wings.events;

import net.minecraft.block.Blocks;
import net.minecraft.block.IceBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import random.wings.WingsAndClaws;
import random.wings.item.WingsItems;

import java.util.List;

@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {
	@SubscribeEvent
	public static void breakBlock(BlockEvent.BreakEvent event) {
		if (event.getPlayer().isCreative())
			return;
		if ((event.getState().getBlock() instanceof IceBlock || event.getState().getBlock() == Blocks.BLUE_ICE) && !event.getWorld().isRemote()) {
			if (event.getWorld().getRandom().nextInt(100) == 0) {
				ItemEntity item = EntityType.ITEM.create(event.getWorld().getWorld());
				if (item != null) {
					item.setPosition(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
					item.setItem(new ItemStack(WingsItems.GLACIAL_SHRIMP));
					event.getWorld().getWorld().addEntity(item);
				}
			}
		}
	}

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.isHandActive() && event.player.getActiveItemStack().getItem() == WingsItems.ICY_PLOWHEAD_SHIELD) {
			if (WingsItems.ICY_PLOWHEAD_SHIELD.getUseAction(event.player.getActiveItemStack()) == UseAction.BLOCK) {
				List<LivingEntity> list = event.player.world.getEntitiesWithinAABB(LivingEntity.class, event.player.getBoundingBox().grow(2));
				if (!list.isEmpty()) {
					double speed = event.player.getMotion().x * event.player.getMotion().x + event.player.getMotion().z * event.player.getMotion().z;
					int damage = Math.max((int) (speed * 8), 4);
					for (LivingEntity entity : list) {
						entity.attackEntityFrom(DamageSource.causeMobDamage(event.player), damage);
						entity.knockBack(event.player, (float) speed * 3.325f, MathHelper.sin(event.player.rotationYaw * ((float) Math.PI / 180F)), -MathHelper.cos(event.player.rotationYaw * ((float) Math.PI / 180F)));
					}
				}
			}
		}
	}
}
