package coda.wingsandclaws.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HasEffectItem extends Item {
    public HasEffectItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
