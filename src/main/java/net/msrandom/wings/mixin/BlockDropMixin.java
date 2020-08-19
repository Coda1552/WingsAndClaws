package net.msrandom.wings.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.world.World;
import net.msrandom.wings.item.WingsItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractBlock.class)
public class BlockDropMixin {
    @Inject(at = @At("HEAD"), method = "getDroppedStacks", cancellable = true)
    private void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        Entity entity = builder.get(LootContextParameters.THIS_ENTITY);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative())
                return;
            World world = player.world;
            Material material = state.getMaterial();
            if (!world.isClient && (material == Material.ICE || material == Material.DENSE_ICE)) {
                if (world.random.nextInt(48) == 0) {
                    List<ItemStack> list = new ArrayList<>();
                    list.add(new ItemStack(WingsItems.GLACIAL_SHRIMP));
                    cir.setReturnValue(list);
                }
            }
        }
    }
}
