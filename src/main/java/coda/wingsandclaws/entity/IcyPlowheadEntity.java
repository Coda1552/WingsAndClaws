package coda.wingsandclaws.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import coda.wingsandclaws.init.WingsSounds;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.entity.item.PlowheadEggEntity;
import coda.wingsandclaws.init.WingsItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.entity.ai.controller.MovementController.Action;

public class IcyPlowheadEntity extends TameableDragonEntity {
	//private static final Ingredient TEMPTATIONS = Ingredient.fromItems(WingsItems.GLISTENING_GLACIAL_SHRIMP);
	private static final EntitySize SLEEPING_SIZE = EntitySize.scalable(1.2f, 0.5f);
	private final Map<ToolType, ItemStack> tools = new HashMap<>();
	private ItemStack horn = ItemStack.EMPTY;
	private BlockPos iceBlock;
	private int alarmedTimer;
	private boolean attacking;
	private Vector3d oldPos;
	private int ramTime;
	private BlockPos target;
	private BlockPos sleepTarget;
	private int startedCharging;

	public IcyPlowheadEntity(EntityType<? extends IcyPlowheadEntity> type, World worldIn) {
		super(type, worldIn);
		this.moveControl = new MoveHelperController(this);
		this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 1, 40));
		this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
	}

	@Override
	public void tick() {
		this.setAirSupply(300);
		super.tick();
	}

	protected PathNavigator createNavigation(World worldIn) {
		return new SwimmerPathNavigator(this, worldIn);
	}

    public static AttributeModifierMap.MutableAttribute registerPlowheadAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.MAX_HEALTH, 30).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.ATTACK_KNOCKBACK, 3);
    }

	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return isSleeping() ? SLEEPING_SIZE : super.getDimensions(poseIn);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return isSleeping() ? null : WingsSounds.PLOWHEAD_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return WingsSounds.PLOWHEAD_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return WingsSounds.PLOWHEAD_DEATH.get();
	}

	@Override
	protected float getSoundVolume() {
		return 0.15f;
	}

	@Override
	public void setTame(boolean tamed) {
		super.setTame(tamed);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(isTame() ? 44 : 30);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
        if (!isBaby() && source.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) source.getEntity();
            if (EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(entity) && !isOwnedBy(entity) && (!(entity instanceof TameableEntity) || getOwnerUUID() == null || !getOwnerUUID().equals(((TameableEntity) entity).getOwnerUUID()))) {
                setTarget((LivingEntity) source.getEntity());
            }
        }

		if (isSleeping()) {
			oldPos = position();
			alarmedTimer = 200;
		}
		return super.hurt(source, amount);
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide && !this.isTame() && stack.getItem() == WingsItems.GLISTENING_GLACIAL_SHRIMP.get()) {
			if (!player.abilities.instabuild) stack.shrink(1);
			if (this.random.nextInt(3) == 0) {
				this.tame(player);
				this.setTarget((LivingEntity)null);
				this.level.broadcastEntityEvent(this, (byte) 7);
			} else {
				this.level.broadcastEntityEvent(this, (byte) 6);
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == WingsItems.GLACIAL_SHRIMP.get();
	}

	@Override
	public void aiStep() {
		if (oldPos != null) {
			setPos(oldPos.x, oldPos.y, oldPos.z);
			oldPos = null;
		}
		if (!isSleeping()) {
			if (this.wasTouchingWater) {
				this.xRot = MathHelper.wrapDegrees((float) this.getDeltaMovement().y() * 345);
			} else {
				this.xRot = 0;
			}

			if (!level.isClientSide) {
				if (isTame()) {
					if (!isOrderedToSit()) {
						LivingEntity owner = getOwner();
						if (owner != null) {
							getNavigation().moveTo(owner, 0.2);
							if (onGround) {
								double x = owner.getX() - getX();
								double z = owner.getZ() - getZ();
								setDeltaMovement(MathHelper.clamp(x, -0.2, 0.2), 0, MathHelper.clamp(z, -0.2, 0.2));

								yRot = (float) Math.toDegrees(Math.atan2(z, x) - Math.PI / 2);
								yBodyRot = yRot;
							}
						}
					}
				} else {
					LivingEntity attackTarget = getTarget();

					if (attackTarget == null) {
						PlayerEntity player = level.getNearestPlayer(this, 16);
						if (player != null && EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(player)) {
							setTarget(player);
							attacking = true;
						}
					}

					if (attackTarget != null && attackTarget.isAlive()) {
						getNavigation().moveTo(attackTarget, 0.4);
						if (distanceToSqr(attackTarget) <= 4) {
							doHurtTarget(attackTarget);
						}
					} else if (attacking) {
						getNavigation().stop();
						attacking = false;
						setTarget((LivingEntity)null);
					}
				}

				if (target != null) {
					if (startedCharging == 0) {
						playSound(WingsSounds.PLOWHEAD_ANGRY.get(), getSoundVolume(), getVoicePitch());
						startedCharging = 120;
					}
					getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 0.4);

					double speed = getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().y * getDeltaMovement().y + getDeltaMovement().z + getDeltaMovement().z;
					if (speed > 0.05) {
						for (Entity entity : level.getEntities(this, getBoundingBox().inflate(1), entity -> entity instanceof LivingEntity)) {
							entity.hurt(DamageSource.mobAttack(this), (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
						}
					}

					if (horizontalCollision) {
						breakBlock(new BlockPos(position().add(getDeltaMovement())), false);
						target = null;
					} else if (distanceToSqr(target.getX(), target.getY(), target.getZ()) <= 4) {
						breakBlock(target, true);
						startedCharging = 0;
						target = null;
					}
				} else {
					if (iceBlock != null) {
						if (startedCharging == 0) {
							playSound(WingsSounds.PLOWHEAD_ANGRY.get(), getSoundVolume(), getVoicePitch());
							startedCharging = 120;
						} else if (startedCharging == 1) {
							iceBlock = null;
							return;
						} else --startedCharging;
						getNavigation().moveTo(iceBlock.getX(), iceBlock.getY(), iceBlock.getZ(), 0.4);

						double speed = getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().y * getDeltaMovement().y + getDeltaMovement().z + getDeltaMovement().z;
						if (speed > 0.05) {
							for (Entity entity : level.getEntities(this, getBoundingBox().inflate(1), entity -> entity instanceof LivingEntity)) {
								entity.hurt(DamageSource.mobAttack(this), (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
							}
						}
						if (iceBlock.distSqr(getX(), getY(), getZ(), true) <= 4) {
							breakBlock(iceBlock, false);
							iceBlock = null;
							ramTime = random.nextInt(1200) + 1200;
							startedCharging = 0;
						}
					}
				}

				if (alarmedTimer-- <= 0) alarmedTimer = 0;
				if ((ramTime <= 0 || --ramTime == 0) && iceBlock == null && tickCount % 20 == 0) {
					List<BlockPos> possible = new ArrayList<>();
					BlockPos start = blockPosition();
					Vector3d vec3d = position();
					BlockPos.Mutable mutable = start.mutable();
					BlockPos.betweenClosedStream(start.offset(-16, -16, -16), start.offset(16, 16, 16)).forEach(pos -> {
						Material material = level.getBlockState(pos).getMaterial();
						if (material == Material.ICE || material == Material.ICE_SOLID) {
							BlockRayTraceResult rayTrace = this.level.clip(new RayTraceContext(vec3d, Vector3d.atCenterOf(pos), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
							if (rayTrace.getType() != RayTraceResult.Type.MISS && rayTrace.getBlockPos().equals(pos)) {
								BlockPos.Mutable p = mutable.set(pos);
								boolean flag = level.getFluidState(p.move(Direction.DOWN)).getType() == Fluids.WATER;
								for (int i = 2; i < Direction.values().length; ++i) {
									flag |= level.getFluidState(p.move(Direction.values()[i])).getType() == Fluids.WATER;
									if (flag) {
										possible.add(pos.immutable());
										break;
									}
								}
							}
						}
					});
					if (possible.size() > 0)
						iceBlock = possible.get(random.nextInt(possible.size()));
				}
			}
			super.aiStep();
		} else this.travel(new Vector3d(this.xxa, this.yya, this.zza));
	}

	@Override
	public boolean isSleeping() {
		if (level.getDayTime() > 13000 && level.getDayTime() < 23000) {
			if (level.getBlockState(new BlockPos(getX(), getY() - 1, getZ())).getBlock() == Blocks.WATER) {
				if (sleepTarget == null) {
					BlockPos p = blockPosition().offset(random.nextInt(64) - 32, -1, random.nextInt(64) - 32);
					while (level.getBlockState(p).getBlock() == Blocks.WATER) {
						p = p.below();
					}
					sleepTarget = p;
				}
				getNavigation().moveTo(sleepTarget.getX(), sleepTarget.getY(), sleepTarget.getZ(), 0.2);
				return false;
			}
			return alarmedTimer == 0;
		} else if (sleepTarget != null) {
			sleepTarget = null;
			setDeltaMovement(getDeltaMovement().add(0, 0.2, 0));
		}
		return false;
	}

	private void breakBlock(BlockPos pos, boolean selected) {
		if (wasEyeInWater) {
			BlockState state = level.getBlockState(pos);
			level.levelEvent(2001, pos, Block.getId(state));
			Material material = state.getMaterial();
			if (material == Material.ICE || material == Material.ICE_SOLID) {
				if (!selected || random.nextBoolean())
					spawnAtLocation(new ItemStack(WingsItems.GLACIAL_SHRIMP.get()), (float) (pos.getY() - getY()));
			} else if (state.canOcclude()) {
				ToolType type = state.getHarvestTool();
				if (type == null) type = ToolType.PICKAXE;
				ItemStack stack = tools.computeIfAbsent(type, k -> {
					if (k == ToolType.AXE) return new ItemStack(Items.IRON_AXE);
					if (k == ToolType.PICKAXE) return new ItemStack(Items.IRON_PICKAXE);
					if (k == ToolType.SHOVEL) return new ItemStack(Items.IRON_SHOVEL);
					return ItemStack.EMPTY;
				});
				setupTool(stack);
				if (selected) {
					for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
						if (entry.getKey() == Enchantments.BLOCK_EFFICIENCY) {
							List<BlockPos> positions = BlockPos.betweenClosedStream(pos.offset(-1, -1, -1), pos.offset(1, 1, 1)).map(BlockPos::immutable).collect(Collectors.toList());
							int k = 0;
							for (int i = 0; i < entry.getValue(); i++) {
								for (int i1 = k; i1 < positions.size(); i1++) {
									BlockPos p = positions.get(i1);
									if (level.getBlockState(p).canOcclude()) {
										breakBlock(p, false);
										k = i1;
										break;
									}
								}
							}
							break;
						}
					}
				}

				for (ItemStack drop : state.getDrops(new LootContext.Builder((ServerWorld) level).withRandom(random).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(pos)).withParameter(LootParameters.TOOL, stack))) {
					spawnAtLocation(drop, (float) (pos.getY() - getY()));
				}
			}
			level.removeBlock(pos, false);
		}
	}

	private void setupTool(ItemStack stack) {
		if (!horn.isEmpty()) {
			for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(horn).entrySet())
				stack.enchant(entry.getKey(), entry.getValue());
			horn = ItemStack.EMPTY;
		}
	}

	@Override
	public void createEgg() {
		level.addFreshEntity(new PlowheadEggEntity(level, getX(), getY(), getZ()));
	}

	@Override
	public ItemStack getEgg() {
		return new ItemStack(WingsItems.ICY_PLOWHEAD_EGG.get());
	}

	public void setTarget(RayTraceResult target) {
		if (target.getType() == RayTraceResult.Type.ENTITY) {
			Entity entity = ((EntityRayTraceResult) target).getEntity();
			if (entity instanceof LivingEntity) setTarget((LivingEntity) entity);
		} else if (target.getType() == RayTraceResult.Type.BLOCK) this.target = ((BlockRayTraceResult) target).getBlockPos();
	}

	@Override
	public void travel(Vector3d p_213352_1_) {
		if (this.isEffectiveAi() && this.isInWater()) {
			this.moveRelative(0.1F, p_213352_1_);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
			if (this.horizontalCollision) {
				this.setDeltaMovement(this.getDeltaMovement().x, 0.1F, this.getDeltaMovement().z);
			}
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(p_213352_1_);
		}
	}

	public void setHorn(ItemStack horn) {
		this.horn = horn;
	}

	private static class MoveHelperController extends MovementController {
		private final IcyPlowheadEntity plowhead;

		MoveHelperController(IcyPlowheadEntity plowhead) {
			super(plowhead);
			this.plowhead = plowhead;
		}

		public void tick() {
			this.plowhead.setDeltaMovement(this.plowhead.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
			if (this.operation == Action.MOVE_TO && !this.plowhead.getNavigation().isDone()) {
				double d0 = this.wantedX - this.plowhead.getX();
				double d1 = this.wantedY - this.plowhead.getY();
				double d2 = this.wantedZ - this.plowhead.getZ();
				double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
				d1 = d1 / d3;
				float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
				this.plowhead.yRot = this.rotlerp(this.plowhead.yRot, f, 90.0F);
				this.plowhead.yBodyRot = this.plowhead.yRot;
				float f1 = (float) (this.speedModifier * this.plowhead.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
				this.plowhead.setSpeed(MathHelper.lerp(0.125F, this.plowhead.getSpeed(), f1));
				this.plowhead.setDeltaMovement(this.plowhead.getDeltaMovement().add(MathHelper.clamp(d0, speedModifier / -10, speedModifier / 10), (double) this.plowhead.getSpeed() * d1 * 0.5, MathHelper.clamp(d2, speedModifier / -10, speedModifier / 10)));
			} else {
				this.plowhead.setSpeed(0.0F);
			}
		}
	}
}
