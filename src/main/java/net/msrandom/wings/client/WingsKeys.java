package net.msrandom.wings.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.msrandom.wings.WingsAndClaws;

public class WingsKeys {
/*    public static final KeyBinding HB_RIGHT = register("right", 68);
    public static final KeyBinding HB_LEFT = register("left", 65);
    public static final KeyBinding HB_ASCEND = register("ascend", 32);
    public static final KeyBinding HB_DESCEND = register("descend", 45);
    public static final KeyBinding HB_FORWARD = register("forward", 87);*/

    private static KeyBinding register(String name, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(name, key, WingsAndClaws.MOD_ID));
    }

    //Force load class
    public static void init() {}
}
