package random.wings.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import random.wings.item.WingsItems;

public class PlowheadEggEntity extends ProjectileItemEntity {

	public PlowheadEggEntity(EntityType<? extends PlowheadEggEntity> a, World w) {
		super(a, w);
	}

	public PlowheadEggEntity(World worldIn, LivingEntity throwerIn) {
		super(WingsEntities.PLOWHEAD_EGG, throwerIn, worldIn);
	}

	public PlowheadEggEntity(World worldIn, double x, double y, double z) {
		super(WingsEntities.PLOWHEAD_EGG, x, y, z, worldIn);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for(int i = 0; i < 8; ++i) {
				this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	protected void onImpact(RayTraceResult result) {
		if (result.getType() == RayTraceResult.Type.ENTITY) {
			((EntityRayTraceResult)result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
		}

		if (!this.world.isRemote) {
			if (this.rand.nextInt(8) == 0) {
				int i = 1;
				if (this.rand.nextInt(32) == 0) {
					i = 4;
				}

				for(int j = 0; j < i; ++j) {
					IcyPlowheadEntity chickenentity = WingsEntities.ICY_PLOWHEAD.create(this.world);
					chickenentity.setGrowingAge(-24000);
					chickenentity.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
					this.world.addEntity(chickenentity);
				}
			}

			this.world.setEntityState(this, (byte)3);
			this.remove();
		}
	}

	protected Item func_213885_i() {
		return WingsItems.ICY_PLOWHEAD_EGG;
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(WingsItems.ICY_PLOWHEAD_EGG);
	}
}