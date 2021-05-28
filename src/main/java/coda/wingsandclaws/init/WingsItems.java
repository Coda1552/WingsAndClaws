package coda.wingsandclaws.init;

import coda.wingsandclaws.client.renderer.tileentity.WingsISTER;
import coda.wingsandclaws.item.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import coda.wingsandclaws.WingsAndClaws;

import java.util.function.Supplier;

public class WingsItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, WingsAndClaws.MOD_ID);

    public static final ItemGroup GROUP = new ItemGroup(WingsAndClaws.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(DUMPY_EGG_DRAKE_EGG.get());
        }
    };

    public static final RegistryObject<Item> GLISTENING_GLACIAL_SHRIMP = REGISTRY.register("glistening_glacial_shrimp", () -> new HasEffectItem(new Item.Properties().group(GROUP)));
    //Items
    public static final RegistryObject<Item> DRAGON_MEAT = REGISTRY.register("dragon_meat", () -> new Item(new Item.Properties().food(WingsFoods.DRAGON_MEAT).group(GROUP)));
    public static final RegistryObject<Item> COOKED_DRAGON_MEAT = REGISTRY.register("cooked_dragon_meat", () -> new Item(new Item.Properties().food(WingsFoods.COOKED_DRAGON_MEAT).group(GROUP)));
    public static final RegistryObject<Item> GLACIAL_SHRIMP = REGISTRY.register("glacial_shrimp", () -> new Item(new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> ICY_PLOWHEAD_EGG = REGISTRY.register("icy_plowhead_egg", PlowheadEggItem::new);
    public static final RegistryObject<Item> DUMPY_EGG_DRAKE_EGG = REGISTRY.register("dumpy_egg_drake_egg", () -> new NestEggItem(WingsBlocks.DED_NEST.get()));
    public static final RegistryObject<Item> MIMANGO_EGG = REGISTRY.register("mimango_egg", MimangoEggItem::new);
    public static final RegistryObject<Item> HATCHET_BEAK_EGG = REGISTRY.register("hatchet_beak_egg", () -> new NestEggItem(WingsBlocks.HB_NEST.get()));
    public static final RegistryObject<Item> HATCHET_BEAK_CREST = REGISTRY.register("hatchet_beak_crest", () -> new Item(new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> CREST_HATCHET = REGISTRY.register("crest_hatchet", () -> new AxeItem(ItemTier.DIAMOND, 5.0F, -3.0F, new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> ICY_PLOWHEAD_SHIELD = REGISTRY.register("icy_plowhead_shield", () -> new ShieldItem(new Item.Properties().group(GROUP).maxDamage(678).setISTER(() -> WingsISTER::get)));
    public static final RegistryObject<Item> HORN_HORN = REGISTRY.register("horn_horn", HornHornItem::new);
    public static final RegistryObject<Item> PLOWHEAD_HORN = REGISTRY.register("plowhead_horn", () -> new Item(new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> SUGARSCALE_BUCKET = REGISTRY.register("sugarscale_bucket", () -> new FishBucketItem(WingsEntities.SUGARSCALE::get, () -> Fluids.WATER, new Item.Properties().group(GROUP).maxStackSize(1)));
    public static final RegistryObject<Item> SUGARSCALE = REGISTRY.register("sugarscale", () -> new Item(new Item.Properties().group(GROUP)));
    public static final RegistryObject<Item> GOLDEN_MANGO = REGISTRY.register("golden_mango", () -> new Item(new Item.Properties().food(WingsFoods.GOLDEN_MANGO).group(GROUP)));
    public static final RegistryObject<Item> MIMANGO = REGISTRY.register("mimango", () -> new Item(new Item.Properties().food(WingsFoods.MIMANGO).group(GROUP)));
    public static final RegistryObject<Item> FRIED_MIMANGO = REGISTRY.register("fried_mimango", () -> new Item(new Item.Properties().food(WingsFoods.FRIED_MIMANGO).group(GROUP)));
    public static final RegistryObject<Item> BREADED_MIMANGO = REGISTRY.register("breaded_mimango", () -> new Item(new Item.Properties().food(WingsFoods.BREADED_MIMANGO).group(GROUP)));
    public static final RegistryObject<Item> MUSIC_DISC_BLISSFUL_DUNES = registerMusicDisc("music_disc_blissful_dunes",  WingsSounds.MUSIC_BLISSFUL_DUNES);
    public static final RegistryObject<Item> MUSIC_DISC_ASHEN_LANDS = registerMusicDisc("music_disc_ashen_lands",  WingsSounds.MUSIC_ASHEN_LANDS);
    public static final RegistryObject<Item> MUSIC_DISC_ISLAND_HOPPERS = registerMusicDisc("music_disc_island_hoppers",  WingsSounds.MUSIC_ISLAND_HOPPERS);
    public static final RegistryObject<Item> MUSIC_DISC_MANTAS_MELODY = registerMusicDisc("music_disc_mantas_melody",  WingsSounds.MUSIC_MANTAS_MELODY);

    //public static final Item LEAPING_GRUB_SHRIMP_BUCKET = REGISTRY.register("leaping_grub_shrimp_bucket", new FishBucketItem(WingsEntities.LEAPING_GRUB_SHRIMP, Fluids.WATER, new Item.Properties().group(GROUP).maxStackSize(1)));
    //public static final Item LEAPING_GRUB_SHRIMP = REGISTRY.register("leaping_grub_shrimp", new Item(new Item.Properties().group(GROUP)));
    //public static final Item MANGO_SEEDS = REGISTRY.register("mango_seeds", new BlockNamedItem(WingsBlocks.MANGO_BUNCH, (new Item.Properties().group(GROUP))));
    //public static final Item STT_SPEAR = REGISTRY.register("stt_spear", new STSpearItem());
    //public static final Item STT_HORN = REGISTRY.register("stt_horn", new STTHornItem());
    //public static final Item SADDLED_THUNDER_TAIL_EGG = REGISTRY.register("saddled_thunder_tail_egg", new SaddledThunderTailEggItem());
    //public static final Item DUMPY_EGG_DRAKE_TAIL = REGISTRY.register("dumpy_egg_drake_tail", new Item(new Item.Properties().group(GROUP)));
    //public static final Item DUMPY_EGG_DRAKE_MACE = REGISTRY.register("dumpy_egg_drake_mace", new SwordItem(ItemTier.IRON, 3, -2.4F, new Item.Properties().group(GROUP)));

    //Custom block items
    static {
        REGISTRY.register("ded_nest", () -> new BlockItem(WingsBlocks.DED_NEST.get(), new Item.Properties().group(GROUP).setISTER(() -> WingsISTER::get)));
        REGISTRY.register("hb_nest", () -> new BlockItem(WingsBlocks.HB_NEST.get(), new Item.Properties().group(GROUP).setISTER(() -> WingsISTER::get)));
        //REGISTRY.register("golden_mimango_statue", new BlockItem(WingsBlocks.GOLDEN_MIMANGO_STATUE, new Item.Properties().group(GROUP)));
    }

    private static RegistryObject<Item> registerMusicDisc(String name, Supplier<SoundEvent> soundEventSupplier) {
        return REGISTRY.register(name, () -> new MusicDiscItem(1, soundEventSupplier, new Item.Properties().group(GROUP).maxStackSize(1).rarity(Rarity.RARE)));
    }
}