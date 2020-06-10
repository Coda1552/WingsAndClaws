package net.msrandom.wings.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

//Not needed, maybe delete?
public abstract class AnimalDragonEntity extends AnimalEntity implements IDragonEntity {
    protected AnimalDragonEntity(EntityType<? extends AnimalDragonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BreedGoal(this, 0.2));
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        createEgg();
        return null;
    }
}
