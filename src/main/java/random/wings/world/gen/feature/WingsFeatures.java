package random.wings.world.gen.feature;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import random.wings.WingsAndClaws;

public class WingsFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.FEATURES, WingsAndClaws.MOD_ID);
    public static final Feature<NoFeatureConfig> DED_NEST = register("ded_nest", new DEDNestStructure());
    public static final Feature<NoFeatureConfig> HB_NEST = register("hb_nest", new HBNestStructure());

    private static <T extends Feature<?>> T register(String name, T feature) {
        REGISTRY.register(name, () -> feature);
        return feature;
    }
}
