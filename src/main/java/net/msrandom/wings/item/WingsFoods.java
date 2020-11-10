package net.msrandom.wings.item;

import net.minecraft.item.Food;

public class WingsFoods {

    static final Food DRAGON_MEAT = new Food.Builder().hunger(3).saturation(0.2f).meat().build();
    static final Food COOKED_DRAGON_MEAT = new Food.Builder().hunger(8).saturation(0.7f).meat().build();
    static final Food MANGO = new Food.Builder().hunger(4).saturation(0.3f).build();
    static final Food GOLDEN_MANGO = new Food.Builder().hunger(6).saturation(1.4f).build();
    static final Food MIMANGO = new Food.Builder().hunger(2).saturation(0.1f).meat().build();
    static final Food FRIED_MIMANGO = new Food.Builder().hunger(7).saturation(0.5f).meat().build();
    static final Food BREADED_MIMANGO = new Food.Builder().hunger(3).saturation(0.3f).meat().build();
}
