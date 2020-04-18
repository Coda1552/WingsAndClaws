package random.wings.item;

import net.minecraft.item.Item;
import random.wings.block.NestBlock;

public class NestEggItem extends Item {
    public NestEggItem(NestBlock block) {
        super(new Item.Properties().group(WingsItems.GROUP));
        block.setItem(this);
    }
}
