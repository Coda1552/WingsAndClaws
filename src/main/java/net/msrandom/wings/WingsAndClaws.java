package net.msrandom.wings;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.MegaJungleTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.block.entity.WingsBlockEntities;
import net.msrandom.wings.client.WingsKeys;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;
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
        LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
            if (id.toString().equals("minecraft:chests/desert_pyramid")) {
                supplier.withPool(FabricLootPoolBuilder.builder().rolls(ConstantLootTableRange.create(1)).withEntry(ItemEntry.builder(WingsItems.MUSIC_DISC_BLISSFUL_DUNES).weight(16).build()).withEntry(EmptyEntry.Serializer().weight(84).build()).build());
            }
        });

        WingsBlocks.REGISTRY.register();
        WingsEntities.REGISTRY.register();
        WingsItems.REGISTRY.register();
        WingsSounds.REGISTRY.register();
        WingsBlockEntities.REGISTRY.register();
        WingsFeatures.REGISTRY.register();
        WingsKeys.init();
        registerBiomeSpawns();
        registerEntityAttributes();
    }

    private void registerBiomeSpawns() {
        Biomes.DESERT.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, WingsFeatures.DED_NEST.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(300))));
        Registry.BIOME.stream().filter(biome -> biome.getCategory() == Biome.Category.DESERT).forEach(biome -> biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(WingsEntities.DUMPY_EGG_DRAKE, 1, 1, 1)));

        Biomes.FROZEN_OCEAN.getEntitySpawnList(SpawnGroup.WATER_CREATURE).add(new Biome.SpawnEntry(WingsEntities.ICY_PLOWHEAD, 30, 1, 2));
        //EntitySpawnPlacementRegistry.register(WingsEntities.ICY_PLOWHEAD, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IcyPlowheadEntity::canSpawn);

        //Biomes.SHATTERED_SAVANNA.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, WingsFeatures.HB_NEST.configure(FeatureConfig.DEFAULT).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(300))));
        //BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA).forEach(biome -> biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(WingsEntities.HATCHET_BEAK, 1, 1, 1)));
        Biomes.JUNGLE.getFeaturesForStep(GenerationStep.Feature.VEGETAL_DECORATION).removeIf(configuredFeature -> configuredFeature.feature == Feature.DECORATED && ((DecoratedFeatureConfig) configuredFeature.config).feature.feature == Feature.RANDOM_SELECTOR);
        Biomes.JUNGLE.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(Feature.TREE.configure(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F), Feature.TREE.configure(DefaultBiomeFeatures.JUNGLE_GROUND_BUSH_CONFIG).withChance(0.5F), Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()), new JungleFoliagePlacer(2, 0, 0, 0, 2), new MegaJungleTrunkPlacer(10, 2, 19), new TwoLayersFeatureSize(1, 1, 2)).decorators(ImmutableList.of(TrunkVineTreeDecorator.field_24965, LeaveVineTreeDecorator.field_24961, new MangoBunchTreeDecorator())).build()).withChance(0.33333334F)), Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 8, 0), new TwoLayersFeatureSize(1, 0, 1)).decorators(ImmutableList.of(new CocoaBeansTreeDecorator(0.2F), TrunkVineTreeDecorator.field_24965, LeaveVineTreeDecorator.field_24961, new MangoBunchTreeDecorator())).ignoreVines().build()).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(50, 0.1F, 1))))));
        Biomes.JUNGLE.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(WingsEntities.MIMANGO, 5, 1, 1));
    }

    private void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(WingsEntities.MIMANGO, MimangoEntity.registerMimangoAttributes());
        FabricDefaultAttributeRegistry.register(WingsEntities.HATCHET_BEAK, HatchetBeakEntity.registerHBAttributes());
        FabricDefaultAttributeRegistry.register(WingsEntities.DUMPY_EGG_DRAKE, DumpyEggDrakeEntity.registerDEDAttributes());
        FabricDefaultAttributeRegistry.register(WingsEntities.ICY_PLOWHEAD, IcyPlowheadEntity.registerPlowheadAttributes());
    }
}
