package net.msrandom.wings.world.gen.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.msrandom.wings.WingsAndClaws;

import java.util.Random;

public class MimangoShrineStructure extends Feature<NoFeatureConfig> {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "mimango_shrine");

    public MimangoShrineStructure() {
        super(NoFeatureConfig.field_236558_a_);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        return false;
    }
}
