/*
package net.msrandom.wings.entity.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class DragonEggEntity extends ThrownItemEntity {
    private EntityType<?> eggType;

    public DragonEggEntity(EntityType<? extends DragonEggEntity> type, World world) {
        super(type, world);
    }

    */
/*public DragonEggEntity(EntityType<?> type, World world, LivingEntity throwerIn) {
        super(WingsEntities.DRAGON_EGG, throwerIn, world);
        this.eggType = type;
    }*//*


    @Environment(EnvType.CLIENT)
    public void handleStatus(byte id) {
        if (id == 3) {
            double d = 0.08D;
            for (int i = 0; i < 8; ++i)
                this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getItem()), getX(), getY(), getZ(), (this.random.nextDouble() - 0.5) * d, (this.random.nextDouble() - 0.5) * d, (this.random.nextDouble() - 0.5) * d);
        }
    }

    protected void onImpact(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult) result).getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), 0.0F);
        }

        if (!this.world.isClient) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    Entity entity = this.eggType.create(this.world);
                    if (entity instanceof AnimalEntity) {
                        AnimalEntity animal = (AnimalEntity) entity;
                        animal.setBreedingAge(-24000);
                        animal.refreshPositionAndAngles(getX(), getY(), getZ(), this.yaw, 0.0F);
                        this.world.spawnEntity(animal);
                    }
                }
            }

            this.world.sendEntityStatus(this, (byte) 3);
            this.remove();
        }

    }

    protected Item func_213885_i() {
        return Items.AIR; //since this currently unused on its own
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }
}
*/
