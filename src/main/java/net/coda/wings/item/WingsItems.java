package net.coda.wings.item;

import net.coda.wings.block.WingsBlocks;
import net.coda.wings.client.WingsSounds;
import net.coda.wings.client.renderer.tileentity.WingsISTER;
import net.coda.wings.entity.WingsEntities;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.coda.wings.WingsAndClaws;

import java.util.function.Supplier;

public class WingsItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, WingsAndClaws.MOD_ID);

    public static final ItemGroup GROUP = new ItemGroup(WingsAndClaws.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(DUMPY_EGG_DRAKE_EGG.get());
        }
    };

    public static final RegistryObject<Item> GLISTENING_GLACIAL_SHRIMP = register("glistening_glacial_shrimp", new HasEffectItem(new Item.Properties().group(GROUP)));
    //Items
    public static final RegistryObject<Item> DRAGON_MEAT = register("dragon_meat",  new Item(new Item.Properties().food(WingsFoods.DRAGON_MEAT).group(GROUP)));
    public static final RegistryObject<Item> COOKED_DRAGON_MEAT = register("cooked_dragon_meat",  new Item(new Item.Properties().food(WingsFoods.COOKED_DRAGON_MEAT).group(GROUP)));
    public static final RegistryObject<Item> GLACIAL_SHRIMP = register("glacial_shrimp",  new Item(new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> ICY_PLOWHEAD_EGG = register("icy_plowhead_egg",  new PlowheadEggItem());
    public static final RegistryObject<Item> DUMPY_EGG_DRAKE_EGG = register("dumpy_egg_drake_egg",  new NestEggItem(WingsBlocks.DED_NEST));
    public static final RegistryObject<Item> MIMANGO_EGG = register("mimango_egg",  new MimangoEggItem());
    public static final RegistryObject<Item> HATCHET_BEAK_EGG = register("hatchet_beak_egg",  new NestEggItem(WingsBlocks.HB_NEST));
    public static final RegistryObject<Item> HATCHET_BEAK_CREST = register("hatchet_beak_crest",  new Item(new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> CREST_HATCHET = register("crest_hatchet",  new AxeItem(ItemTier.DIAMOND, 5.0F, -3.0F, new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> ICY_PLOWHEAD_SHIELD = register("icy_plowhead_shield",  new ShieldItem(new Item.Properties().group(GROUP).maxDamage(678).setISTER(() -> WingsISTER::get)));
    public static final RegistryObject<Item> HORN_HORN = register("horn_horn",  new HornHornItem());
    public static final RegistryObject<Item> PLOWHEAD_HORN = register("plowhead_horn",  new Item(new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> SUGARSCALE_BUCKET = register("sugarscale_bucket",  new FishBucketItem(WingsEntities.SUGARSCALE::get, () -> Fluids.WATER, new Item.Properties().group(GROUP).maxStackSize(1)));
    public static final RegistryObject<Item> SUGARSCALE = register("sugarscale",  new Item(new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> GOLDEN_MANGO = register("golden_mango",  new Item(new Item.Properties().food(WingsFoods.GOLDEN_MANGO).group(GROUP)));
    public static final RegistryObject<Item> MIMANGO = register("mimango",  new Item(new Item.Properties().food(WingsFoods.MIMANGO).group(GROUP)));
    public static final RegistryObject<Item> FRIED_MIMANGO = register("fried_mimango",  new Item(new Item.Properties().food(WingsFoods.FRIED_MIMANGO).group(GROUP)));
    public static final RegistryObject<Item> BREADED_MIMANGO = register("breaded_mimango",  new Item(new Item.Properties().food(WingsFoods.BREADED_MIMANGO).group(GROUP)));
    public static final RegistryObject<Item> MUSIC_DISC_BLISSFUL_DUNES = registerMusicDisc("music_disc_blissful_dunes",  () -> WingsSounds.MUSIC_BLISSFUL_DUNES);
    public static final RegistryObject<Item> MUSIC_DISC_ASHEN_LANDS = registerMusicDisc("music_disc_ashen_lands",  () -> WingsSounds.MUSIC_ASHEN_LANDS);
    public static final RegistryObject<Item> MUSIC_DISC_ISLAND_HOPPERS = registerMusicDisc("music_disc_island_hoppers",  () -> WingsSounds.MUSIC_ISLAND_HOPPERS);
    public static final RegistryObject<Item> MUSIC_DISC_MANTAS_MELODY = registerMusicDisc("music_disc_mantas_melody",  () -> WingsSounds.MUSIC_MANTAS_MELODY);

    //public static final Item LEAPING_GRUB_SHRIMP_BUCKET = register("leaping_grub_shrimp_bucket", new FishBucketItem(WingsEntities.LEAPING_GRUB_SHRIMP, Fluids.WATER, new Item.Properties().group(GROUP).maxStackSize(1)));
    //public static final Item LEAPING_GRUB_SHRIMP = register("leaping_grub_shrimp", new Item(new Item.Properties().group(GROUP)));
    //public static final Item MANGO_SEEDS = register("mango_seeds", new BlockNamedItem(WingsBlocks.MANGO_BUNCH, (new Item.Properties().group(GROUP))));
    //public static final Item STT_SPEAR = register("stt_spear", new STSpearItem());
    //public static final Item STT_HORN = register("stt_horn", new STTHornItem());
    //public static final Item SADDLED_THUNDER_TAIL_EGG = register("saddled_thunder_tail_egg", new SaddledThunderTailEggItem());
    //public static final Item DUMPY_EGG_DRAKE_TAIL = register("dumpy_egg_drake_tail", new Item(new Item.Properties().group(GROUP)));
    //public static final Item DUMPY_EGG_DRAKE_MACE = register("dumpy_egg_drake_mace", new SwordItem(ItemTier.IRON, 3, -2.4F, new Item.Properties().group(GROUP)));


    //Custom block items
    static {
        register("ded_nest", new BlockItem(WingsBlocks.DED_NEST, new Item.Properties().group(GROUP).setISTER(() -> WingsISTER::get)));
        register("hb_nest", new BlockItem(WingsBlocks.HB_NEST, new Item.Properties().group(GROUP).setISTER(() -> WingsISTER::get)));
        //register("golden_mimango_statue", new BlockItem(WingsBlocks.GOLDEN_MIMANGO_STATUE, new Item.Properties().group(GROUP)));
    }

    private static RegistryObject<Item> register(String name, Item item) {
        return REGISTRY.register(name, () -> item);
    }

    private static RegistryObject<Item> registerMusicDisc(String name, Supplier<SoundEvent> soundEventSupplier) {
        return register(name, new MusicDiscItem(1, soundEventSupplier, new Item.Properties().group(GROUP).maxStackSize(1).rarity(Rarity.RARE)));
    }
}