package random.wings.item;

import net.minecraft.item.Food;

public class WingsFoods {

    static final Food DRAGON_MEAT = new Food.Builder().hunger(3).saturation(0.2f).meat().build();
    static final Food COOKED_DRAGON_MEAT = new Food.Builder().hunger(8).saturation(0.7f).meat().build();
}
