package net.msrandom.wings.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class DragonEggEntity extends ProjectileItemEntity {
    private EntityType<?> eggType;

    public DragonEggEntity(EntityType<? extends DragonEggEntity> type, World world) {
        super(type, world);
    }

    /*public DragonEggEntity(EntityType<?> type, World worldIn, LivingEntity throwerIn) {
        super(WingsEntities.DRAGON_EGG, throwerIn, worldIn);
        this.eggType = type;
    }*/

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            double d = 0.08D;
            for (int i = 0; i < 8; ++i)
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), getPosX(), getPosY(), getPosZ(), (this.rand.nextDouble() - 0.5) * d, (this.rand.nextDouble() - 0.5) * d, (this.rand.nextDouble() - 0.5) * d);
        }

    }

    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            ((EntityRayTraceResult) result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 0.0F);
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
                        animal.setLocationAndAngles(getPosX(), getPosY(), getPosZ(), this.rotationYaw, 0.0F);
                        this.world.addEntity(animal);
                    }
                }
            }

            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }

    }

    protected Item func_213885_i() {
        return Items.AIR; //since this currently unused on its own
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }
}
