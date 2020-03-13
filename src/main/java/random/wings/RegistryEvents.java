package random.wings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.IceBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import random.wings.block.WingsBlocks;
import random.wings.client.ClientEventHandler;
import random.wings.entity.WingsEntities;
import random.wings.item.WingsItems;
import random.wings.tileentity.WingsTileEntities;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
    private static final Feature<NoFeatureConfig> NEST = new NestStructure();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(WingsBlocks.LIST.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        WingsEntities.registerEggs();
        event.getRegistry().registerAll(WingsItems.LIST.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(NEST.setRegistryName("nest"));
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(WingsSounds.LIST.toArray(new SoundEvent[0]));
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        Biomes.DESERT.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(NEST, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_HEIGHTMAP, new ChanceConfig(300)));
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(WingsEntities.LIST.toArray(new EntityType[0]));
        ForgeRegistries.BIOMES.getValues().stream().filter(biome -> BiomeDictionary.getTypes(biome).containsAll(BiomeDictionary.getTypes(Biomes.DESERT))).forEach(biome -> biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.DUMPY_EGG_DRAKE, 1, 1, 1)));
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(WingsTileEntities.NEST.setRegistryName("nest"));
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        ClientEventHandler.init();
    }
}
