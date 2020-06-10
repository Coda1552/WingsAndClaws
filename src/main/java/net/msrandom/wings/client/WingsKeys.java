package net.msrandom.wings.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.msrandom.wings.WingsAndClaws;

public class WingsKeys {
/*    public static final KeyBinding HB_RIGHT = register("right", 68);
    public static final KeyBinding HB_LEFT = register("left", 65);
    public static final KeyBinding HB_ASCEND = register("ascend", 32);
    public static final KeyBinding HB_DESCEND = register("descend", 45);
    public static final KeyBinding HB_FORWARD = register("forward", 87);*/

    private static KeyBinding register(String name, int key) {
        KeyBinding binding = new KeyBinding(name, key, WingsAndClaws.MOD_ID);
        ClientRegistry.registerKeyBinding(binding);
        return binding;
    }

    //Force load class
    public static void init() {}
}
