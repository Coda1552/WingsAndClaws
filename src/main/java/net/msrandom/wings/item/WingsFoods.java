package net.msrandom.wings.item;

import net.minecraft.item.FoodComponent;

public class WingsFoods {
    static final FoodComponent DRAGON_MEAT = new FoodComponent.Builder().hunger(3).saturationModifier(0.2f).meat().build();
    static final FoodComponent COOKED_DRAGON_MEAT = new FoodComponent.Builder().hunger(8).saturationModifier(0.7f).meat().build();
    static final FoodComponent MANGO = new FoodComponent.Builder().hunger(4).saturationModifier(0.3f).build();
}
