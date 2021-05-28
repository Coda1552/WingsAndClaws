package coda.wingsandclaws.world.gen.feature;

import coda.wingsandclaws.entity.util.TameableDragonEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Supplier;

public class NestFeature extends WingsNbtStructure {
    private final Supplier<EntityType<? extends TameableDragonEntity>> entity;

    public NestFeature(ResourceLocation path, Supplier<EntityType<? extends TameableDragonEntity>> entity) {
        super(new ResourceLocation(path.getNamespace(), "nest/" + path.getPath()));
        this.entity = entity;
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, ISeedReader reader, Random rand, MutableBoundingBox mutableboundingbox) {
        super.handleDataMarker(function, pos, reader, rand, mutableboundingbox);
        ServerWorld world = reader.getWorld();
        switch (function) {
            case "baby":
                if (rand.nextInt(4) != 0) {
                    TameableDragonEntity entity = this.entity.get().create(world);
                    if (entity != null) {
                        entity.setGrowingAge(-24000);
                        entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        entity.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);
                        reader.addEntity(entity);
                    }
                }
                break;
            case "adult":
                if (rand.nextInt(3) != 0) {
                    TameableDragonEntity entity = this.entity.get().create(world);
                    if (entity != null) {
                        entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        entity.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);
                        reader.addEntity(entity);
                    }
                }
        }
    }
}
