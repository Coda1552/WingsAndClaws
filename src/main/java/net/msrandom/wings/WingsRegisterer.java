package net.msrandom.wings;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class WingsRegisterer<T> {
    private final Map<String, T> map = new HashMap<>();
    private final Registry<T> registry;

    public WingsRegisterer(Registry<T> registry) {
        this.registry = registry;
    }

    public void register(String name, T value) {
        map.put(name, value);
    }

    public void register() {
        map.forEach((k, v) -> Registry.register(registry, new Identifier(WingsAndClaws.MOD_ID, k), v));
    }
}
