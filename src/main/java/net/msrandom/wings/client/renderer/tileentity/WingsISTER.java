package net.msrandom.wings.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by WolfShotz 12/7/2020
 *
 * A Proposed Solution to the problematic class loading regarding servers during item registration with isters.
 *
 * The issue: {@link Item.Properties#setISTER(Supplier)} expects a method reference in order to not piss off class loading.
 * However this makes it impossible to pass in arguements that the ister may need.
 *
 * This solution fixes these issues.
 *
 * call {@link #addDelegate(Item, ItemStackTileEntityRenderer)} during ClientSetupEvent to add a rendering delegate for an item
 */
public class WingsISTER extends ItemStackTileEntityRenderer {
    private static final WingsISTER INSTANCE = new WingsISTER();

    private final Map<Item, ItemStackTileEntityRenderer> CHILDREN = new HashMap<>();

    private WingsISTER() {
    }

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transforms, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        CHILDREN.get(stack.getItem()).func_239207_a_(stack, transforms, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    public void addDelegate(Item item, ItemStackTileEntityRenderer child)
    {
        if (item.getItemStackTileEntityRenderer() != this) {
            throw new IllegalArgumentException("This item's ISTER does not belong here!");
        }
        CHILDREN.put(item, child);
    }

    public static WingsISTER get() {
        return INSTANCE;
    }
}
