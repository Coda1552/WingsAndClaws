package net.msrandom.wings;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WingsSounds {

    public static final DeferredRegister<SoundEvent> REGISTRY = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, WingsAndClaws.MOD_ID);
    public static final SoundEvent DED_AMBIENT = create("ded.ambient");
    public static final SoundEvent DED_HURT = create("ded.hurt");
    public static final SoundEvent DED_DEATH = create("ded.death");
    public static final SoundEvent PLOWHEAD_AMBIENT = create("plowhead.ambient");
    public static final SoundEvent PLOWHEAD_HURT = create("plowhead.hurt");
    public static final SoundEvent PLOWHEAD_DEATH = create("plowhead.death");
    public static final SoundEvent PLOWHEAD_ANGRY = create("plowhead.angry");

    private static SoundEvent create(String name) {
        SoundEvent event = new SoundEvent(new ResourceLocation(WingsAndClaws.MOD_ID, name));
        REGISTRY.register(name, () -> event);
        return event;
    }
}
