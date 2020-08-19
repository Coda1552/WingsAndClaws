package net.msrandom.wings.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.WingsRegisterer;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.block.entity.WingsBlockEntities;
import net.msrandom.wings.client.ClientEventHandler;
import net.msrandom.wings.client.renderer.tileentity.NestItemRenderer;
import net.msrandom.wings.client.renderer.tileentity.PlowheadShieldRenderer;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class WingsItems {
    public static final WingsRegisterer<Item> REGISTRY = new WingsRegisterer<>(Registry.ITEM);
    //Anonymous item classes
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(WingsAndClaws.MOD_ID, "group"), () -> new ItemStack(WingsItems.DUMPY_EGG_DRAKE_EGG));
    public static final Item GLISTENING_GLACIAL_SHRIMP = REGISTRY.register("glistening_glacial_shrimp", new Item(new Item.Settings().group(GROUP)) {
        @Override
        public boolean hasGlint(ItemStack stack) {
            return true;
        }
    });

    //Items
    public static final Item DRAGON_MEAT = REGISTRY.register("dragon_meat", new Item(new Item.Settings().food(WingsFoods.DRAGON_MEAT).group(GROUP)));
    public static final Item COOKED_DRAGON_MEAT = REGISTRY.register("cooked_dragon_meat", new Item(new Item.Settings().food(WingsFoods.COOKED_DRAGON_MEAT).group(GROUP)));
    public static final Item GLACIAL_SHRIMP = REGISTRY.register("glacial_shrimp", new Item(new Item.Settings().group(GROUP)));
    public static final Item ICY_PLOWHEAD_EGG = REGISTRY.register("icy_plowhead_egg", new PlowheadEggItem());
    public static final Item DUMPY_EGG_DRAKE_EGG = REGISTRY.register("dumpy_egg_drake_egg", new NestEggItem(WingsBlocks.DED_NEST));
    public static final Item MIMANGO_EGG = REGISTRY.register("mimango_egg", new MimangoEggItem());
    //public static final Item HATCHET_BEAK_EGG = REGISTRY.register("hatchet_beak_egg", new NestEggItem(WingsBlocks.HB_NEST));
    //public static final Item HATCHET_BEAK_CREST = REGISTRY.register("hatchet_beak_crest", new Item(new Item.Settings().group(GROUP)));
    //public static final Item CREST_HATCHET = REGISTRY.register("crest_hatchet", new AxeItem(ToolMaterials.DIAMOND, 5.0F, -3.0F, new Item.Settings().group(GROUP)));
    public static final Item ICY_PLOWHEAD_SHIELD = REGISTRY.register("icy_plowhead_shield", getWithISTER(new ShieldItem(new Item.Settings().group(GROUP).maxDamage(678)), () -> PlowheadShieldRenderer::new));
    public static final Item HORN_HORN = REGISTRY.register("horn_horn", new HornHornItem());
    public static final Item PLOWHEAD_HORN = REGISTRY.register("plowhead_horn", new Item(new Item.Settings().group(GROUP)));

    public static final Item MUSIC_DISC_BLISSFUL_DUNES = REGISTRY.register("music_disc_blissful_dunes", new MusicDiscItem(1, WingsSounds.MUSIC_BLISSFUL_DUNES, new Item.Settings().group(GROUP).maxCount(1).rarity(Rarity.RARE)) {
    });

    //Custom block items
    static {
        REGISTRY.register("ded_nest", getWithISTER(new BlockItem(WingsBlocks.DED_NEST, new Item.Settings()), () -> () -> new NestItemRenderer(WingsBlockEntities.DED_NEST)));
        REGISTRY.register("hb_nest", getWithISTER(new BlockItem(WingsBlocks.HB_NEST, new Item.Settings()), () -> () -> new NestItemRenderer(WingsBlockEntities.HB_NEST)));
        REGISTRY.register("mango", new BlockItem(WingsBlocks.MANGO_BUNCH, new Item.Settings().group(GROUP).food(WingsFoods.MANGO)));
    }

    private static Item getWithISTER(Item item, Supplier<Callable<Object>> ister) {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? ClientEventHandler.getWithISTER(item, ister) : item;
    }
}
