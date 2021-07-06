package coda.wingsandclaws.item;

import coda.wingsandclaws.client.renderer.tileentity.SpearProjectileTileRenderer;
import coda.wingsandclaws.entity.item.SpearProjectileEntity;
import coda.wingsandclaws.init.WingsItems;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import coda.wingsandclaws.WingsAndClaws;

public class STSpearItem extends TridentItem {
    public STSpearItem() {
        super(new Item.Properties().tab(WingsItems.GROUP).durability(100).setISTER(() -> SpearProjectileTileRenderer::new));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ItemModelsProperties.register(this, new ResourceLocation(WingsAndClaws.MOD_ID, "throwing"), (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                int j = EnchantmentHelper.getRiptide(stack);
                if (j <= 0 || playerEntity.isInWaterOrRain()) {
                    if (!worldIn.isClientSide) {
                        stack.hurtAndBreak(1, playerEntity, playerEntity1 -> playerEntity1.broadcastBreakEvent(entityLiving.getUsedItemHand()));
                        if (j == 0) {
                            SpearProjectileEntity spearProjectileEntity = new SpearProjectileEntity(worldIn, playerEntity, stack);
                            spearProjectileEntity.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 0.0F, 2.5F + (float) j * 0.5F, 1.0F);
                            if (playerEntity.abilities.instabuild) {
                                spearProjectileEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }

                            worldIn.addFreshEntity(spearProjectileEntity);
                            worldIn.playSound(null, spearProjectileEntity, SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!playerEntity.abilities.instabuild) {
                                playerEntity.inventory.removeItem(stack);
                            }
                        }
                    }
                }

                playerEntity.awardStat(Stats.ITEM_USED.get(this));
                if (j > 0) {
                    float f7 = playerEntity.yRot;
                    float f = playerEntity.xRot;
                    double f1 = -Math.sin(Math.toRadians(f7)) * Math.cos(Math.toRadians(f));
                    double f2 = -Math.sin(Math.toRadians(f));
                    double f3 = Math.cos(Math.toRadians(f7)) * Math.cos(Math.toRadians(f));
                    double f4 = Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                    double f5 = 3 * ((1 + j) / 4.0);
                    f1 = f1 * (f5 / f4);
                    f2 = f2 * (f5 / f4);
                    f3 = f3 * (f5 / f4);
                    playerEntity.push(f1, f2, f3);
                    playerEntity.startAutoSpinAttack(20);
                    if (playerEntity.isOnGround()) {
                        playerEntity.move(MoverType.SELF, new Vector3d(0.0D, 1.1999999, 0.0D));
                    }
                }

                SoundEvent soundEvent = SoundEvents.TRIDENT_RIPTIDE_2;
                playerEntity.awardStat(Stats.ITEM_USED.get(this));
                worldIn.playSound(null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 6.0D, AttributeModifier.Operation.MULTIPLY_BASE));
        }

        return builder.build();
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }
}
