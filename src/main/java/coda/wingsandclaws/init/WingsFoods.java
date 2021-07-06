package coda.wingsandclaws.init;

import net.minecraft.item.Food;

public class WingsFoods {
    public static final Food DRAGON_MEAT = new Food.Builder().nutrition(3).saturationMod(0.2f).meat().build();
    public static final Food COOKED_DRAGON_MEAT = new Food.Builder().nutrition(8).saturationMod(0.7f).meat().build();
    public static final Food MANGO = new Food.Builder().nutrition(4).saturationMod(0.3f).build();
    public static final Food GOLDEN_MANGO = new Food.Builder().nutrition(6).saturationMod(1.4f).build();
    public static final Food MIMANGO = new Food.Builder().nutrition(2).saturationMod(0.1f).meat().build();
    public static final Food FRIED_MIMANGO = new Food.Builder().nutrition(7).saturationMod(0.5f).meat().build();
    public static final Food BREADED_MIMANGO = new Food.Builder().nutrition(3).saturationMod(0.3f).meat().build();
}
