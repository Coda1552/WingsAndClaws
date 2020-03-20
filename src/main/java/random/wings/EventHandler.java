package random.wings;

import net.minecraft.block.Blocks;
import net.minecraft.block.IceBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import random.wings.item.WingsItems;

@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
	@SubscribeEvent
	public static void blockBreakEvent(BlockEvent.BreakEvent event) {
		if(event.getPlayer().isCreative())
			return;
		if((event.getState().getBlock() instanceof IceBlock || event.getState().getBlock() == Blocks.BLUE_ICE) && !event.getWorld().isRemote()){
			if(event.getWorld().getRandom().nextInt(100) == 0){
				ItemEntity itm = EntityType.ITEM.create(event.getWorld().getWorld());
				if(itm != null) {
					itm.setPosition(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
					itm.setItem(new ItemStack(WingsItems.GLACIAL_SHRIMP));
					event.getWorld().getWorld().addEntity(itm);
				}
			}
		}
	}
}
