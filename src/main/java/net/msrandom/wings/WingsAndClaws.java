package net.msrandom.wings;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.block.entity.WingsBlockEntities;
import net.msrandom.wings.client.WingsKeys;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.item.WingsItems;
import net.msrandom.wings.world.gen.feature.MangoBunchTreeDecorator;
import net.msrandom.wings.world.gen.feature.WingsFeatures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WingsAndClaws implements ModInitializer {
    public static final String MOD_ID = "wings";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        bus.addListener(this::registerClient);
        bus.addListener(this::registerBiomes);
        WingsBlocks.REGISTRY.register();
        WingsItems.REGISTRY.register();
        WingsSounds.REGISTRY.register();
        WingsEntities.REGISTRY.register();
        WingsBlockEntities.REGISTRY.register();
        WingsFeatures.REGISTRY.register();
        WingsKeys.init();
    }

    public void registerBiomes(RegistryEvent.Register<Biome> event) {
        if (event.getRegistry().getRegistrySuperType() == Biome.class) {
            Biomes.DESERT.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, WingsFeatures.DED_NEST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(300))));
            Registry.BIOMES.getValues().stream().filter(biome -> BiomeDictionary.getTypes(biome).containsAll(BiomeDictionary.getTypes(Biomes.DESERT))).forEach(biome -> biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.DUMPY_EGG_DRAKE, 1, 1, 1)));

            Registry.BIOMES.getValues().stream().filter(biome -> BiomeDictionary.getTypes(biome).containsAll(BiomeDictionary.getTypes(Biomes.FROZEN_OCEAN))).forEach(biome -> biome.getSpawns(EntityClassification.WATER_CREATURE).add(new Biome.SpawnListEntry(WingsEntities.ICY_PLOWHEAD, 30, 1, 2)));
            //EntitySpawnPlacementRegistry.register(WingsEntities.ICY_PLOWHEAD, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IcyPlowheadEntity::canSpawn);

            //Biomes.SHATTERED_SAVANNA.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, WingsFeatures.HB_NEST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(300))));
            //BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA).forEach(biome -> biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.HATCHET_BEAK, 1, 1, 1)));
            Biomes.JUNGLE.getFeaturesForStep(GenerationStep.Feature.VEGETAL_DECORATION).removeIf(configuredFeature -> configuredFeature.feature == Feature.DECORATED && ((DecoratedFeatureConfig) configuredFeature.config).feature.feature == Feature.RANDOM_SELECTOR);
            Biomes.JUNGLE.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(Feature.FANCY_TREE.withConfiguration(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F), Feature.JUNGLE_GROUND_BUSH.withConfiguration(DefaultBiomeFeatures.JUNGLE_GROUND_BUSH_CONFIG).withChance(0.5F), Feature.MEGA_JUNGLE_TREE.withConfiguration(new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState())).baseHeight(10).heightInterval(20).decorators(ImmutableList.of(new TrunkVineTreeDecorator(), new LeaveVineTreeDecorator(), new MangoBunchTreeDecorator())).setSapling((IPlantable) Blocks.JUNGLE_SAPLING).build()).withChance(0.33333334F)), Feature.NORMAL_TREE.withConfiguration((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(8).foliageHeight(3).decorators(ImmutableList.of(new CocoaTreeDecorator(0.2F), new TrunkVineTreeDecorator(), new LeaveVineTreeDecorator(), new MangoBunchTreeDecorator())).ignoreVines().setSapling((IPlantable) Blocks.JUNGLE_SAPLING).build()))).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(50, 0.1F, 1))));
            Biomes.JUNGLE.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(WingsEntities.MIMANGO, 5, 1, 1));
        }
    }
}
