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
    public static final SoundEvent MIMANGO_AMBIENT = create("mimango.ambient");
    public static final SoundEvent MIMANGO_HURT = create("mimango.hurt");
    public static final SoundEvent MIMANGO_DEATH = create("mimango.death");
    public static final SoundEvent MIMANGO_HIDE = create("mimango.hide");
    public static final SoundEvent MIMANGO_UNHIDE = create("mimango.unhide");
    public static final SoundEvent MIMANGO_HAPPY = create("mimango.happy");
    public static final SoundEvent BATTLE_HORN = create("horn.use");

    public static final SoundEvent MUSIC_BLISSFUL_DUNES = create("music.blissful_dunes");
    public static final SoundEvent MUSIC_ASHEN_LANDS = create("music.ashen_lands");
    public static final SoundEvent MUSIC_ISLAND_HOPPERS = create("music.island_hoppers");
    public static final SoundEvent MUSIC_MANTAS_MELODY = create("music.mantas_melody");

    private static SoundEvent create(String name) {
        SoundEvent event = new SoundEvent(new ResourceLocation(WingsAndClaws.MOD_ID, name));
        REGISTRY.register(name, () -> event);
        return event;
    }
}
