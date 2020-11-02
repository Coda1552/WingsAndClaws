//
//package net.msrandom.wings.item;
//
//import com.google.common.collect.Multimap;
//import net.minecraft.enchantment.EnchantmentHelper;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.MoverType;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.ai.attributes.AttributeModifier;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.projectile.AbstractArrowEntity;
//import net.minecraft.inventory.EquipmentSlotType;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.TridentItem;
//import net.minecraft.stats.Stats;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.util.SoundEvents;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import net.msrandom.wings.WingsAndClaws;
//import net.msrandom.wings.client.renderer.tileentity.PlowheadHornRenderer;
//import net.msrandom.wings.client.renderer.tileentity.SpearProjectileTileRenderer;
//import net.msrandom.wings.entity.item.SpearProjectileEntity;
//
//public class STSpearItem extends TridentItem {
//
//
//    public STSpearItem() {
//        super(new Item.Properties().group(WingsItems.GROUP).maxDamage(100).setISTER(() -> SpearProjectileTileRenderer::new));
//        this.addPropertyOverride(new ResourceLocation(WingsAndClaws.MOD_ID, "st_spear_throwing"), (stack, world, entity) -> entity !=null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
//        if (entityLiving instanceof PlayerEntity) {
//            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
//            int i = this.getUseDuration(stack) - timeLeft;
//            if (i >= 10) {
//                int j = EnchantmentHelper.getRiptideModifier(stack);
//                if (j <= 0 || playerEntity.isWet()) {
//                    if (!worldIn.isRemote) {
//                        stack.damageItem(1, playerEntity, playerEntity1 -> playerEntity1.sendBreakAnimation(entityLiving.getActiveHand()));
//                        if (j == 0) {
//                            SpearProjectileEntity spearProjectileEntity = new SpearProjectileEntity(worldIn, playerEntity, stack);
//                            spearProjectileEntity.shoot(playerEntity, playerEntity.rotationPitch, playerEntity.rotationYaw, 0.0F, 2.5F + (float) j * 0.5F, 1.0F);
//                            if (playerEntity.abilities.isCreativeMode) {
//                                spearProjectileEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
//                            }
//
//                            worldIn.addEntity(spearProjectileEntity);
//                            worldIn.playMovingSound(null, spearProjectileEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
//                            if (!playerEntity.abilities.isCreativeMode) {
//                                playerEntity.inventory.deleteStack(stack);
//                            }
//                        }
//                    }
//                }
//
//                playerEntity.addStat(Stats.ITEM_USED.get(this));
//                if (j > 0) {
//                    float f7 = playerEntity.rotationYaw;
//                    float f = playerEntity.rotationPitch;
//                    double f1 = -Math.sin(Math.toRadians(f7)) * Math.cos(Math.toRadians(f));
//                    double f2 = -Math.sin(Math.toRadians(f));
//                    double f3 = Math.cos(Math.toRadians(f7)) * Math.cos(Math.toRadians(f));
//                    double f4 = Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
//                    double f5 = 3 * ((1 + j) / 4.0);
//                    f1 = f1 * (f5 / f4);
//                    f2 = f2 * (f5 / f4);
//                    f3 = f3 * (f5 / f4);
//                    playerEntity.addVelocity(f1, f2, f3);
//                    playerEntity.startSpinAttack(20);
//                    if (playerEntity.onGround) {
//                        playerEntity.move(MoverType.SELF, new Vec3d(0.0D, 1.1999999, 0.0D));
//                    }
//                }
//
//                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
//                playerEntity.addStat(Stats.ITEM_USED.get(this));
//                worldIn.playMovingSound(null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
//            }
//        }
//    }
//
//    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
//        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
//
//        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
//            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 6.0D, AttributeModifier.Operation.MULTIPLY_BASE));
//        }
//
//        return multimap;
//    }
//
//    @Override
//    public int getItemEnchantability() {
//        return 0;
//    }
//}
//
//
//
