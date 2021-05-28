package coda.wingsandclaws.init;

import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.init.WingsEntities;
import coda.wingsandclaws.world.gen.feature.MangoBunchTreeDecorator;
import coda.wingsandclaws.world.gen.feature.NestFeature;
import coda.wingsandclaws.world.gen.feature.WingsNbtStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WingsFeatures {
    public static final DeferredRegister<Feature<?>> FEATURE_REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, WingsAndClaws.MOD_ID);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_REGISTRY = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, WingsAndClaws.MOD_ID);

    public static final RegistryObject<Feature<NoFeatureConfig>> DED_NEST = FEATURE_REGISTRY.register("ded_nest", () -> new NestFeature(new ResourceLocation(WingsAndClaws.MOD_ID, "dumpy_egg_drake"), WingsEntities.DUMPY_EGG_DRAKE::get));
    public static final RegistryObject<Feature<NoFeatureConfig>> HB_NEST = FEATURE_REGISTRY.register("hb_nest", () -> new NestFeature(new ResourceLocation(WingsAndClaws.MOD_ID, "hatchet_beak"), WingsEntities.HATCHET_BEAK::get));

    public static final RegistryObject<Feature<NoFeatureConfig>> MIMANGO_SHRINE = FEATURE_REGISTRY.register("mimango_shrine", () -> new WingsNbtStructure(new ResourceLocation(WingsAndClaws.MOD_ID, "mimango_shrine")));

    public static final RegistryObject<TreeDecoratorType<TreeDecorator>> MANGO_BUNCH = TREE_DECORATOR_REGISTRY.register("mango_bunch", () -> new TreeDecoratorType<>(MangoBunchTreeDecorator.field_236874_c_));
}
