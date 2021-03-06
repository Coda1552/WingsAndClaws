package coda.wingsandclaws;

import coda.wingsandclaws.client.ClientEventHandler;
import coda.wingsandclaws.entity.*;
import coda.wingsandclaws.init.*;
import coda.wingsandclaws.network.CallHatchetBeaksPacket;
import coda.wingsandclaws.network.HatchetBeakAttackPacket;
import coda.wingsandclaws.network.INetworkPacket;
import coda.wingsandclaws.world.gen.feature.MangoBunchTreeDecorator;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraft.world.gen.trunkplacer.MegaJungleTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

@Mod(WingsAndClaws.MOD_ID)
@Mod.EventBusSubscriber
public class WingsAndClaws {
    public static final String MOD_ID = "wingsandclaws";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleChannel NETWORK = INetworkPacket.makeChannel("network", "1");
    private static int currentNetworkId;

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
        WingsFeatures.STRUCTURE_REGISTRY.register(bus);
        WingsFeatures.TREE_DECORATOR_REGISTRY.register(bus);

        registerMessage(CallHatchetBeaksPacket.class, CallHatchetBeaksPacket::new, LogicalSide.SERVER);
        registerMessage(HatchetBeakAttackPacket.class, HatchetBeakAttackPacket::new, LogicalSide.SERVER);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerBiomes(BiomeLoadingEvent event) {
        Biome.Climate climate = event.getClimate();
        switch (event.getCategory()) {
            case DESERT:
                event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.DUMPY_EGG_DRAKE.get(), 1, 1, 1));
                event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> WingsFeatures.DED_NEST.get().configured(IFeatureConfig.NONE).decorated(Placement.HEIGHTMAP.configured(NoPlacementConfig.INSTANCE)).decorated(Placement.CHANCE.configured(new ChanceConfig(300))));
                break;
            case JUNGLE:
                event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).removeIf(configuredFeature -> configuredFeature.get().feature == Feature.DECORATED && ((DecoratedFeatureConfig) configuredFeature.get().config).feature.get().feature == Feature.RANDOM_SELECTOR);
                event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> Feature.RANDOM_SELECTOR.configured(new MultipleRandomFeatureConfig(ImmutableList.of(Features.FANCY_OAK.weighted(0.1F), Features.JUNGLE_BUSH.weighted(0.5F), Feature.TREE.configured(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()), new JungleFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 2), new MegaJungleTrunkPlacer(10, 2, 19), new TwoLayerFeature(1, 1, 2)).decorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE, new MangoBunchTreeDecorator())).build()).weighted(0.33333334F)), Feature.TREE.configured(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()), new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()), new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3), new StraightTrunkPlacer(4, 8, 0), new TwoLayerFeature(1, 0, 1)).decorators(ImmutableList.of(new CocoaTreeDecorator(0.2F), TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE, new MangoBunchTreeDecorator())).ignoreVines().build()).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(50, 0.1F, 1))))));
                event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.MIMANGO.get(), 30, 3, 5));
                event.getGeneration().getStructures().add(() -> WingsFeatures.MIMANGO_SHRINE.get().configured(IFeatureConfig.NONE));
                break;
            case OCEAN:
                float temperature = climate.temperature;
                if (temperature <= 0.0) {
                    event.getSpawns().getSpawner(EntityClassification.WATER_CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.ICY_PLOWHEAD.get(), 30, 1, 2));
                } else if (temperature >= 0.5) {
                    event.getSpawns().getSpawner(EntityClassification.WATER_CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.SUGARSCALE.get(), 20, 1, 2));
                }
                break;
            case FOREST:
                if (climate.downfall == 0.8f && climate.temperature == 0.7f) {
                    event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.HAROLDS_GREENDRAKE.get(), 15, 3, 4));
                }
                break;
            case SAVANNA:
                if (event.getDepth() >= 0.36f && event.getScale() >= 1.22f) {
                    event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> WingsFeatures.HB_NEST.get().configured(IFeatureConfig.NONE).decorated(Placement.HEIGHTMAP.configured(NoPlacementConfig.INSTANCE)).decorated(Placement.CHANCE.configured(new ChanceConfig(300))));
                    event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.HATCHET_BEAK.get(), 1, 1, 1));
                }
                break;
/*            case PLAINS:
                event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(WingsEntities.SADDLED_THUNDER_TAIL, 1, 3, 10));
                break;*/
        }
    }

    private void registerClient(FMLClientSetupEvent event) {
        ClientEventHandler.init();
    }

    @SuppressWarnings("unchecked")
    private void registerCommon(FMLCommonSetupEvent event) {
        registerEntityAttributes();

        EntitySpawnPlacementRegistry.register(WingsEntities.HAROLDS_GREENDRAKE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HaroldsGreendrakeEntity::canHaroldsSpawn);
        EntitySpawnPlacementRegistry.register(WingsEntities.SUGARSCALE.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::checkFishSpawnRules);
    }
    
    private void registerEntityAttributes() {
        GlobalEntityTypeAttributes.put(WingsEntities.MIMANGO.get(), MimangoEntity.registerMimangoAttributes().build());
        GlobalEntityTypeAttributes.put(WingsEntities.HATCHET_BEAK.get(), HatchetBeakEntity.registerHBAttributes().build());
        GlobalEntityTypeAttributes.put(WingsEntities.DUMPY_EGG_DRAKE.get(), DumpyEggDrakeEntity.registerDEDAttributes().build());
        GlobalEntityTypeAttributes.put(WingsEntities.ICY_PLOWHEAD.get(), IcyPlowheadEntity.registerPlowheadAttributes().build());
        GlobalEntityTypeAttributes.put(WingsEntities.HAROLDS_GREENDRAKE.get(), HaroldsGreendrakeEntity.registerGreendrakeAttributes().build());
        GlobalEntityTypeAttributes.put(WingsEntities.ICY_PLOWHEAD_EGG.get(), LivingEntity.createLivingAttributes().build());
        GlobalEntityTypeAttributes.put(WingsEntities.MIMANGO_EGG.get(), LivingEntity.createLivingAttributes().build());
        GlobalEntityTypeAttributes.put(WingsEntities.SUGARSCALE.get(), SugarscaleEntity.createAttributes().build());
        /*GlobalEntityTypeAttributes.put(WingsEntities.SADDLED_THUNDER_TAIL_EGG, LivingEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(WingsEntities.SADDLED_THUNDER_TAIL, SaddledThunderTailEntity.registerSTTAttributes().create());*/
    }

    private <T extends INetworkPacket> void registerMessage(Class<T> message, Supplier<T> supplier, LogicalSide side) {
        NETWORK.registerMessage(currentNetworkId++, message, INetworkPacket::write, buffer -> {
            T msg = supplier.get();
            msg.read(buffer);
            return msg;
        }, (msg, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> msg.handle(context.getDirection().getOriginationSide().isServer() ? getClientPlayer() : context.getSender()));
            context.setPacketHandled(true);
        }, Optional.of(side.isClient() ? NetworkDirection.PLAY_TO_CLIENT : NetworkDirection.PLAY_TO_SERVER));
    }

    @OnlyIn(Dist.CLIENT)
    private static PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
