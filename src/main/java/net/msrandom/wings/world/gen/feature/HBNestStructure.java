package net.msrandom.wings.world.gen.feature;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.block.entity.HBNestBlockEntity;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

import java.util.Random;

public class HBNestStructure extends Feature<DefaultFeatureConfig> {
    public HBNestStructure() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        if (pos.getY() >= 120 && world.setBlockState(pos, WingsBlocks.HB_NEST.getDefaultState(), 3)) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof HBNestBlockEntity) ((HBNestBlockEntity) te).addEgg();
            HatchetBeakEntity entity = WingsEntities.HATCHET_BEAK.create(world.getWorld());
            if (entity != null) {
                entity.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
                entity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.STRUCTURE, null, null);
                world.spawnEntity(entity);
            }
            return true;
        }
        return false;
    }
}
