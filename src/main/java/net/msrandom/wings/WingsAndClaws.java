package net.msrandom.wings;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.JungleFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraft.world.gen.trunkplacer.MegaJungleTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.client.ClientEventHandler;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;
import net.msrandom.wings.entity.passive.HaroldsGreendrakeEntity;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;
import net.msrandom.wings.item.WingsItems;
import net.msrandom.wings.tileentity.WingsTileEntities;
import net.msrandom.wings.world.gen.feature.MangoBunchTreeDecorator;
import net.msrandom.wings.world.gen.feature.WingsFeatures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WingsAndClaws.MOD_ID)
@Mod.EventBusSubscriber
public class WingsAndClaws {
    public static final String MOD_ID = "wings";
    public static final Logger LOGGER = LogManager.getLogger();

    public WingsAndClaws() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registerCommon);
        bus.addListener(this::registerClient);

        WingsBlocks.REGISTRY.register(bus);
        WingsItems.REGISTRY.register(bus);
        WingsSounds.REGISTRY.register(bus);
        WingsEntities.REGISTRY.register(bus);
        WingsTileEntities.REGISTRY.register(bus);
        WingsFeatures.FEATURE_REGISTRY.register(bus);
        WingsFeatures.TREE_DECORATOR_REGISTRY.register(bus);

        EntitySpawnPlacementRegistry.register(WingsEntities.HAROLDS_GREENDRAKE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HaroldsGreendrakeEntity::canHaroldsSpawn);
        EntitySpawnPlacementRegistry.register(WingsEntities.SUGARSCALE, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerBiomes(BiomeLoadingEvent event) {
        Biome.Climate climate = event.getClimate();
        switch (event.getCategory()) {
            case DESERT:
                event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.DUMPY_EGG_DRAKE, 1, 1, 1));
                event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> WingsFeatures.DED_NEST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE.configure(new ChanceConfig(300))));
                break;
            case JUNGLE:
                event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).removeIf(configuredFeature -> configuredFeature.get().feature == Feature.DECORATED && ((DecoratedFeatureConfig) configuredFeature.get().config).feature.get().feature == Feature.RANDOM_SELECTOR);
                event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(Features.FANCY_OAK.withChance(0.1F), Features.JUNGLE_BUSH.withChance(0.5F), Feature.TREE.withConfiguration(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()), new JungleFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 2), new MegaJungleTrunkPlacer(10, 2, 19), new TwoLayerFeature(1, 1, 2)).setDecorators(ImmutableList.of(TrunkVineTreeDecorator.field_236879_b_, LeaveVineTreeDecorator.field_236871_b_, new MangoBunchTreeDecorator())).build()).withChance(0.33333334F)), Feature.TREE.withConfiguration(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(4, 8, 0), new TwoLayerFeature(1, 0, 1)).setDecorators(ImmutableList.of(new CocoaTreeDecorator(0.2F), TrunkVineTreeDecorator.field_236879_b_, LeaveVineTreeDecorator.field_236871_b_, new MangoBunchTreeDecorator())).setIgnoreVines().build()).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(50, 0.1F, 1))))));
                event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.MIMANGO, 30, 3, 5));
                break;
            case OCEAN:
                float temperature = climate.temperature;
                if (temperature <= 0.0) {
                    event.getSpawns().getSpawner(EntityClassification.WATER_CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.ICY_PLOWHEAD, 30, 1, 2));
                } else if (temperature >= 0.5) {
                    event.getSpawns().getSpawner(EntityClassification.WATER_CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.SUGARSCALE, 20, 1, 2));
                }
                break;
            case FOREST:
                if (climate.downfall == 0.8f && climate.temperature == 0.7f) {
                    event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.HAROLDS_GREENDRAKE, 15, 3, 4));
                }
                break;
        }

        //biome1.addStructure(WingsFeatures.MIMANGO_SHRINE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        //biome1.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, WingsFeatures.MIMANGO_SHRINE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(15, 0.1f, 0))));

    }

    private void registerClient(FMLClientSetupEvent event) {
        ClientEventHandler.init();
    }

    private void registerCommon(FMLCommonSetupEvent event) {
        registerEntityAttributes();
    }
    
    private void registerEntityAttributes() {
        GlobalEntityTypeAttributes.put(WingsEntities.MIMANGO, MimangoEntity.registerMimangoAttributes().create());
        GlobalEntityTypeAttributes.put(WingsEntities.HATCHET_BEAK, HatchetBeakEntity.registerHBAttributes().create());
        GlobalEntityTypeAttributes.put(WingsEntities.DUMPY_EGG_DRAKE, DumpyEggDrakeEntity.registerDEDAttributes().create());
        GlobalEntityTypeAttributes.put(WingsEntities.ICY_PLOWHEAD, IcyPlowheadEntity.registerPlowheadAttributes().create());
        GlobalEntityTypeAttributes.put(WingsEntities.HAROLDS_GREENDRAKE, HaroldsGreendrakeEntity.registerGreendrakeAttributes().create());
        GlobalEntityTypeAttributes.put(WingsEntities.PLOWHEAD_EGG, LivingEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(WingsEntities.MIMANGO_EGG, LivingEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(WingsEntities.SUGARSCALE, LivingEntity.registerAttributes().create());
    }
}
