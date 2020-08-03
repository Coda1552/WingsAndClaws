package net.msrandom.wings.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;

//Not needed, maybe delete?
public abstract class AnimalDragonEntity extends AnimalEntity implements IDragonEntity {
    protected AnimalDragonEntity(EntityType<? extends AnimalDragonEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new AnimalMateGoal(this, 0.2));
    }

    @Override
    public PassiveEntity createChild(PassiveEntity ageable) {
        createEgg();
        return null;
    }
}
