package random.wings.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import random.wings.item.WingsItems;

public class DragonEggEntity extends ProjectileItemEntity {
    private EntityType<?> eggType;

    public DragonEggEntity(EntityType<? extends DragonEggEntity> p_i50154_1_, World p_i50154_2_) {
        super(p_i50154_1_, p_i50154_2_);
    }

    public DragonEggEntity(EntityType<?> type, World worldIn, LivingEntity throwerIn) {
        super(WingsEntities.DRAGON_EGG, throwerIn, worldIn);
        this.eggType = type;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            double d = 0.08D;
            for (int i = 0; i < 8; ++i)
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.posX, this.posY, this.posZ, (this.rand.nextDouble() - 0.5) * d, (this.rand.nextDouble() - 0.5) * d, (this.rand.nextDouble() - 0.5) * d);
        }

    }

    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            ((EntityRayTraceResult) result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.world.isRemote) {
            if (this.rand.nextInt(8) == 0) {
                int i = 1;
                if (this.rand.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    Entity entity = this.eggType.create(this.world);
                    if (entity instanceof AnimalEntity) {
                        AnimalEntity animal = (AnimalEntity) entity;
                        animal.setGrowingAge(-24000);
                        animal.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                        this.world.addEntity(animal);
                    }
                }
            }

            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }

    }

    protected Item func_213885_i() {
        return WingsItems.DRAGON_EGG;
    }
}
