package net.msrandom.wings.world.gen.feature;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.TreeDecoratorType;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.msrandom.wings.WingsRegisterer;
import net.msrandom.wings.mixin.TreeDecoratorTypeMixin;

public class WingsFeatures {
    public static final WingsRegisterer<Feature<?>> REGISTRY = new WingsRegisterer<>(Registry.FEATURE);
    public static final Feature<DefaultFeatureConfig> DED_NEST = register("ded_nest", new DEDNestStructure());
    public static final Feature<DefaultFeatureConfig> HB_NEST = register("hb_nest", new HBNestStructure());

    public static final TreeDecoratorType<MangoBunchTreeDecorator> MANGO_BUNCH = TreeDecoratorTypeMixin.callMethod_28895("mango_bunch", MangoBunchTreeDecorator.CODEC);

    private static <T extends Feature<?>> T register(String name, T feature) {
        REGISTRY.register(name, feature);
        return feature;
    }
}
