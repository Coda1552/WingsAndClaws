package net.msrandom.wings.world.gen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.world.gen.feature.WingsFeatures;

public class StructureGen {
    public static void generateStructures() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (biome == Biomes.JUNGLE) {
                biome.addStructure(WingsFeatures.MIMANGO_SHRINE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            }

            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, WingsFeatures.MIMANGO_SHRINE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        }
    }
}
