package random.wings.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import random.wings.block.NestBlock;

public class NestEggItem extends BlockItem {
    public NestEggItem(NestBlock block, Item.Properties properties) {
        super(block, properties.group(WingsItems.GROUP));
        block.setItem(this);
    }
}
