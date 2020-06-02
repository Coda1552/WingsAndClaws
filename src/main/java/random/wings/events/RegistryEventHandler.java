package random.wings.events;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import random.wings.WingsAndClaws;
import random.wings.WingsSounds;
import random.wings.block.WingsBlocks;
import random.wings.client.ClientEventHandler;
import random.wings.entity.WingsEntities;
import random.wings.entity.passive.MimangoEntity;
import random.wings.item.WingsItems;
import random.wings.tileentity.WingsTileEntities;
import random.wings.world.gen.feature.WingsFeatures;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEventHandler {
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
        event.getRegistry().registerAll(WingsFeatures.LIST.toArray(new Feature[0]));
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(WingsSounds.LIST.toArray(new SoundEvent[0]));
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        Biomes.DESERT.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, WingsFeatures.DED_NEST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(300))));
        Biomes.SHATTERED_SAVANNA.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, WingsFeatures.HB_NEST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(300))));
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(WingsEntities.LIST.toArray(new EntityType[0]));
        ForgeRegistries.BIOMES.getValues().stream().filter(biome -> BiomeDictionary.getTypes(biome).containsAll(BiomeDictionary.getTypes(Biomes.DESERT))).forEach(biome -> biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.DUMPY_EGG_DRAKE, 1, 1, 1)));
        BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA).forEach(biome -> biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.HATCHET_BEAK, 1, 1, 1)));
        ForgeRegistries.BIOMES.getValues().stream().filter(biome -> BiomeDictionary.getTypes(biome).containsAll(BiomeDictionary.getTypes(Biomes.COLD_OCEAN))).forEach(biome -> biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.ICY_PLOWHEAD, 1, 1, 2)));
        Biomes.JUNGLE.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.MIMANGO, 5, 1, 1));
        EntitySpawnPlacementRegistry.register(WingsEntities.MIMANGO, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, MimangoEntity::canSpawn);
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(WingsTileEntities.LIST.toArray(new TileEntityType[0]));
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        ClientEventHandler.init();
    }
}
