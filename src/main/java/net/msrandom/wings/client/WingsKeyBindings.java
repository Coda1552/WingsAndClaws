package net.msrandom.wings.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class WingsKeyBindings {
    public static final List<KeyBinding> LIST = new ArrayList<>();
    public static final KeyBinding CALL_HATCHET_BEAK = register("callHatchetBeak", 75);
    public static final KeyBinding HATCHET_BEAK_ATTACK = register("hatchetBeakAttack", 86);
    public static final KeyBinding HATCHET_BEAK_DESCENT = register("hatchetBeakDescent", 89);

    private static KeyBinding register(String name, int key) {
        KeyBinding keyBinding = new KeyBinding("key." + name, key, "key.categories.wings");
        LIST.add(keyBinding);
        return keyBinding;
    }
}
