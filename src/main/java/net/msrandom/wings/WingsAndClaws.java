package net.msrandom.wings;

import net.minecraft.entity.EntityClassification;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.client.ClientEventHandler;
import net.msrandom.wings.client.WingsKeys;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.item.WingsItems;
import net.msrandom.wings.tileentity.WingsTileEntities;
import net.msrandom.wings.world.gen.feature.MangoBunchTreeDecorator;
import net.msrandom.wings.world.gen.feature.WingsFeatures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Mod(WingsAndClaws.MOD_ID)
public class WingsAndClaws {
    public static final String MOD_ID = "wings";
    public static final Logger LOGGER = LogManager.getLogger();

    public WingsAndClaws() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registerClient);
        bus.addListener(this::registerBiomes);
        WingsBlocks.REGISTRY.register(bus);
        WingsItems.REGISTRY.register(bus);
        WingsSounds.REGISTRY.register(bus);
        WingsEntities.REGISTRY.register(bus);
        WingsTileEntities.REGISTRY.register(bus);
        WingsFeatures.REGISTRY.register(bus);
        WingsKeys.init();
    }

    public void registerBiomes(RegistryEvent.Register<Biome> event) {
        if (event.getRegistry().getRegistrySuperType() == Biome.class) {
            Biomes.DESERT.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, WingsFeatures.DED_NEST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(300))));
            ForgeRegistries.BIOMES.getValues().stream().filter(biome -> BiomeDictionary.getTypes(biome).containsAll(BiomeDictionary.getTypes(Biomes.DESERT))).forEach(biome -> biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.DUMPY_EGG_DRAKE, 1, 1, 1)));

            ForgeRegistries.BIOMES.getValues().stream().filter(biome -> BiomeDictionary.getTypes(biome).containsAll(BiomeDictionary.getTypes(Biomes.FROZEN_OCEAN))).forEach(biome -> biome.getSpawns(EntityClassification.WATER_CREATURE).add(new Biome.SpawnListEntry(WingsEntities.ICY_PLOWHEAD, 30, 1, 2)));
            //EntitySpawnPlacementRegistry.register(WingsEntities.ICY_PLOWHEAD, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IcyPlowheadEntity::canSpawn);

            //Biomes.SHATTERED_SAVANNA.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, WingsFeatures.HB_NEST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(300))));
            //BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA).forEach(biome -> biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.HATCHET_BEAK, 1, 1, 1)));

            //Biomes.JUNGLE.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.MIMANGO, 5, 1, 1));
            //EntitySpawnPlacementRegistry.register(WingsEntities.MIMANGO, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, MimangoEntity::canSpawn);

            //Registry.register(Registry.TREE_DECORATOR_TYPE, )
        }
    }

    private void registerClient(FMLClientSetupEvent event) {
        ClientEventHandler.init();
    }
}
