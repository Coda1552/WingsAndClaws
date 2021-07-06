package coda.wingsandclaws.world.gen.feature;

import net.minecraft.block.Blocks;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.*;

import java.util.Random;

public class WingsNbtStructure extends Feature<NoFeatureConfig> {
    private final ResourceLocation structure;

    public WingsNbtStructure(ResourceLocation structure) {
        super(NoFeatureConfig.CODEC);
        this.structure = structure;
    }

    @Override
    public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        Rotation rotation = Rotation.getRandom(rand);
        TemplateManager templatemanager = reader.getLevel().getServer().getStructureManager();
        Template template = templatemanager.getOrCreate(structure);
        ChunkPos chunkpos = new ChunkPos(pos);
        MutableBoundingBox mutableboundingbox = new MutableBoundingBox(chunkpos.getMinBlockX(), 0, chunkpos.getMinBlockZ(), chunkpos.getMaxBlockX(), 256, chunkpos.getMaxBlockZ());
        PlacementSettings placementsettings = new PlacementSettings().setRotation(rotation).setBoundingBox(mutableboundingbox).setRandom(rand).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR);
        BlockPos blockpos = template.getSize(rotation);
        int j = rand.nextInt(16 - blockpos.getX());
        int k = rand.nextInt(16 - blockpos.getZ());
        int l = 256;

        for(int i1 = 0; i1 < blockpos.getX(); ++i1) {
            for(int j1 = 0; j1 < blockpos.getZ(); ++j1) {
                l = Math.min(l, reader.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + i1 + j, pos.getZ() + j1 + k));
            }
        }

        BlockPos blockpos1 = template.getZeroPositionWithTransform(pos.offset(j, l - pos.getY(), k), Mirror.NONE, rotation);
        IntegrityProcessor integrityprocessor = new IntegrityProcessor(0.9F);
        placementsettings.clearProcessors().addProcessor(integrityprocessor);
        if (template.placeInWorld(reader, blockpos1, blockpos1, placementsettings, rand, 4)) {
            for(Template.BlockInfo blockInfo : template.filterBlocks(blockpos1, placementsettings, Blocks.STRUCTURE_BLOCK)) {
                if (blockInfo.nbt != null) {
                    StructureMode structuremode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
                    if (structuremode == StructureMode.DATA) {
                        reader.setBlock(blockInfo.pos, Blocks.AIR.defaultBlockState(), 4);
                        this.handleDataMarker(blockInfo.nbt.getString("metadata"), blockInfo.pos, reader, rand, mutableboundingbox);
                    }
                }
            }
        }
        return true;
    }

    protected void handleDataMarker(String function, BlockPos pos, ISeedReader reader, Random rand, MutableBoundingBox mutableboundingbox) {

    }
}
