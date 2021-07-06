package coda.wingsandclaws.world.gen.feature;

import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.init.WingsFeatures;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import net.minecraft.world.gen.feature.structure.Structure.IStartFactory;

public class MimangoShrineStructure extends Structure<NoFeatureConfig> {
    public MimangoShrineStructure() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
            super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
        }

        @Override
        public void generatePieces(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int x, int z, Biome p_230364_6_, NoFeatureConfig p_230364_7_) {
            pieces.add(new Piece(
                    p_230364_3_,
                    new BlockPos(x * 16, 90, z * 16),
                    new ResourceLocation(WingsAndClaws.MOD_ID, "mimango_shrine"),
                    Rotation.getRandom(this.random)
            ));
            this.calculateBoundingBox();
        }
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static class Piece extends TemplateStructurePiece {
        private final ResourceLocation structure;
        private final Rotation rotation;

        public Piece(TemplateManager templateManager, BlockPos position, ResourceLocation structure, Rotation rotation) {
            super(WingsFeatures.SHRINE_PIECE_TYPE, 0);
            this.templatePosition = position;
            this.structure = structure;
            this.rotation = rotation;
            readTemplate(templateManager);
        }

        public Piece(TemplateManager templateManager, CompoundNBT nbt) {
            super(WingsFeatures.SHRINE_PIECE_TYPE, nbt);
            this.structure = new ResourceLocation(nbt.getString("Template"));
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            readTemplate(templateManager);
        }

        private void readTemplate(TemplateManager manager) {
            Template template = manager.getOrCreate(structure);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            setup(template, templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
        }

        @Override
        protected void addAdditionalSaveData(CompoundNBT tag) {
            super.addAdditionalSaveData(tag);
            tag.putString("Template", structure.toString());
            tag.putString("Rot", rotation.name());
        }
    }
}
