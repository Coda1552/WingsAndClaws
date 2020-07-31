package net.msrandom.wings;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
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

            //Mangos
            Biomes.JUNGLE.getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).removeIf(configuredFeature -> configuredFeature.feature == Feature.DECORATED && ((DecoratedFeatureConfig) configuredFeature.config).feature.feature == Feature.RANDOM_SELECTOR);
            Biomes.JUNGLE.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(Feature.FANCY_TREE.withConfiguration(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F), Feature.JUNGLE_GROUND_BUSH.withConfiguration(DefaultBiomeFeatures.JUNGLE_GROUND_BUSH_CONFIG).withChance(0.5F), Feature.MEGA_JUNGLE_TREE.withConfiguration(new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState())).baseHeight(10).heightInterval(20).decorators(ImmutableList.of(new TrunkVineTreeDecorator(), new LeaveVineTreeDecorator(), new MangoBunchTreeDecorator())).setSapling((IPlantable) Blocks.JUNGLE_SAPLING).build()).withChance(0.33333334F)), Feature.NORMAL_TREE.withConfiguration((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(8).foliageHeight(3).decorators(ImmutableList.of(new CocoaTreeDecorator(0.2F), new TrunkVineTreeDecorator(), new LeaveVineTreeDecorator(), new MangoBunchTreeDecorator())).ignoreVines().setSapling((IPlantable) Blocks.JUNGLE_SAPLING).build()))).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(50, 0.1F, 1))));

            Biomes.JUNGLE.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WingsEntities.MIMANGO, 20, 1, 1));
        }
    }

    private void registerClient(FMLClientSetupEvent event) {
        ClientEventHandler.init();
    }
}
