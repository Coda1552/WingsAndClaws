package random.wings.world.gen.feature;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class WingsFeatures {
    public static final List<Feature<?>> LIST = new ArrayList<>();
    public static final Feature<NoFeatureConfig> DED_NEST = new DEDNestStructure();
    public static final Feature<NoFeatureConfig> HB_NEST = new HBNestStructure();
}
