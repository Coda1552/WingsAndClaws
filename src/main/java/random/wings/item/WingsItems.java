package random.wings.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import random.wings.WingsAndClaws;
import random.wings.block.WingsBlocks;
import random.wings.client.renderer.NestItemRenderer;

import java.util.ArrayList;
import java.util.List;

public class WingsItems {
    public static final ItemGroup GROUP = new ItemGroup(WingsAndClaws.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(DUMPY_EGG_DRAKE_EGG);
        }
    };
    public static final List<Item> LIST = new ArrayList<>();
    public static final Item DRAGON_EGG = new DragonEggItem();//add("dragon_egg", );
    public static final Item DUMPY_EGG_DRAKE_EGG = add("dumpy_egg_drake_egg", new Item(new Item.Properties().group(GROUP)));
    public static final Item DRAGON_MEAT = add("dragon_meat", new Item(new Item.Properties().food(WingsFoods.DRAGON_MEAT).group(GROUP)));
    public static final Item COOKED_DRAGON_MEAT = add("cooked_dragon_meat", new Item(new Item.Properties().food(WingsFoods.COOKED_DRAGON_MEAT).group(GROUP)));

    static {
        add("nest", new BlockItem(WingsBlocks.NEST, new Item.Properties().group(WingsItems.GROUP).setTEISR(() -> NestItemRenderer::new)));
    }

    private static Item add(String name, Item item) {
        LIST.add(item.setRegistryName(name));
        return item;
    }
}
