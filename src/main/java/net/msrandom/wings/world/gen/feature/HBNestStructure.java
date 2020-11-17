package net.msrandom.wings.world.gen.feature;

import net.minecraft.entity.SpawnReason;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;
import net.msrandom.wings.tileentity.HBNestTileEntity;

import java.util.Random;

public class HBNestStructure extends Feature<NoFeatureConfig> {
    public HBNestStructure() {
        super(NoFeatureConfig.field_236558_a_);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (pos.getY() >= 120 && reader.setBlockState(pos, WingsBlocks.HB_NEST.getDefaultState(), 3)) {
            TileEntity te = reader.getTileEntity(pos);
            if (te instanceof HBNestTileEntity) ((HBNestTileEntity) te).addEgg();
            HatchetBeakEntity entity = WingsEntities.HATCHET_BEAK.create(reader.getWorld());
            if (entity != null) {
                entity.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
                entity.onInitialSpawn(reader, reader.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);
                reader.addEntity(entity);
            }
            return true;
        }
        return false;
    }
}
