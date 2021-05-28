package coda.wingsandclaws.entity.passive;

import coda.wingsandclaws.entity.goal.LeapingGrubShrimpJumpGoal;
import coda.wingsandclaws.item.WingsItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class LeapingGrubShrimpEntity extends AbstractFishEntity {
    public LeapingGrubShrimpEntity(EntityType<? extends LeapingGrubShrimpEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new LeapingGrubShrimpJumpGoal(this, 2));
    }

    public static AttributeModifierMap.MutableAttribute registerLGSAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5).createMutableAttribute(Attributes.MAX_HEALTH, 8);
    }

    @Override
    protected ItemStack getFishBucket() {
        return new ItemStack(WingsItems.SUGARSCALE_BUCKET.get());
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COD_AMBIENT;
    }
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COD_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_COD_HURT;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }
}
