package random.wings.item;

import net.minecraft.item.*;
import random.wings.WingsAndClaws;
import random.wings.block.WingsBlocks;
import random.wings.client.renderer.tileentity.NestItemRenderer;
import random.wings.client.renderer.tileentity.PlowheadShieldRenderer;
import random.wings.tileentity.WingsTileEntities;

import java.util.ArrayList;
import java.util.List;

public class WingsItems {
    public static final List<Item> LIST = new ArrayList<>();
    //Anonymous item classes
    public static final ItemGroup GROUP = new ItemGroup(WingsAndClaws.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(DUMPY_EGG_DRAKE_EGG);
        }
    };
    public static final Item GLISTENING_GLACIAL_SHRIMP = add("glistening_glacial_shrimp", new Item(new Item.Properties().group(GROUP)) {
        @Override
        public boolean hasEffect(ItemStack stack) {
            return true;
        }
    });

    //Items
    public static final Item DRAGON_MEAT = add("dragon_meat", new Item(new Item.Properties().food(WingsFoods.DRAGON_MEAT).group(GROUP)));
    public static final Item COOKED_DRAGON_MEAT = add("cooked_dragon_meat", new Item(new Item.Properties().food(WingsFoods.COOKED_DRAGON_MEAT).group(GROUP)));
    public static final Item GLACIAL_SHRIMP = add("glacial_shrimp", new Item(new Item.Properties().group(GROUP)));
    public static final Item ICY_PLOWHEAD_EGG = add("icy_plowhead_egg", new PlowheadEggItem());
    public static final Item DUMPY_EGG_DRAKE_EGG = add("dumpy_egg_drake_egg", new NestEggItem(WingsBlocks.DED_NEST));
    public static final Item HATCHET_BEAK_EGG = add("hatchet_beak_egg", new NestEggItem(WingsBlocks.HB_NEST));
    public static final Item HATCHET_BEAK_CREST = add("hatchet_beak_crest", new Item(new Item.Properties().group(GROUP)));
    public static final Item CREST_HATCHET = add("crest_hatchet", new AxeItem(ItemTier.DIAMOND, 5.0F, -3.0F, new Item.Properties().group(GROUP)));
    public static final Item ICY_PLOWHEAD_SHIELD = add("icy_plowhead_shield", new ShieldItem(new Item.Properties().group(GROUP).setTEISR(() -> PlowheadShieldRenderer::new)));
    public static final Item COMMAND_STAFF = add("command_staff", new CommandStaffItem());

    //Custom block items
    static {
        add("ded_nest", new BlockItem(WingsBlocks.DED_NEST, new Item.Properties().setTEISR(() -> () -> new NestItemRenderer(WingsTileEntities.DED_NEST))));
        add("hb_nest", new BlockItem(WingsBlocks.HB_NEST, new Item.Properties().setTEISR(() -> () -> new NestItemRenderer(WingsTileEntities.HB_NEST))));
    }

    private static Item add(String name, Item item) {
        LIST.add(item.setRegistryName(name));
        return item;
    }
}
