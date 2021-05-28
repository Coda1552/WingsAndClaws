package coda.wingsandclaws.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import coda.wingsandclaws.WingsAndClaws;

public class WingsSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WingsAndClaws.MOD_ID);
    public static final RegistryObject<SoundEvent> DED_AMBIENT = create("entity.dumpy_egg_drake.ambient");
    public static final RegistryObject<SoundEvent> DED_HURT = create("entity.dumpy_egg_drake.hurt");
    public static final RegistryObject<SoundEvent> DED_DEATH = create("entity.dumpy_egg_drake.death");
    public static final RegistryObject<SoundEvent> PLOWHEAD_AMBIENT = create("entity.icy_plowhead.ambient");
    public static final RegistryObject<SoundEvent> PLOWHEAD_HURT = create("entity.icy_plowhead.hurt");
    public static final RegistryObject<SoundEvent> PLOWHEAD_DEATH = create("entity.icy_plowhead.death");
    public static final RegistryObject<SoundEvent> PLOWHEAD_ANGRY = create("entity.icy_plowhead.angry");
    public static final RegistryObject<SoundEvent> MIMANGO_AMBIENT = create("entity.mimango.ambient");
    public static final RegistryObject<SoundEvent> MIMANGO_HURT = create("entity.mimango.hurt");
    public static final RegistryObject<SoundEvent> MIMANGO_DEATH = create("entity.mimango.death");
    public static final RegistryObject<SoundEvent> MIMANGO_HIDE = create("entity.mimango.hide");
    public static final RegistryObject<SoundEvent> MIMANGO_UNHIDE = create("entity.mimango.unhide");
    public static final RegistryObject<SoundEvent> MIMANGO_HAPPY = create("entity.mimango.happy");
    public static final RegistryObject<SoundEvent> BATTLE_HORN = create("item.horn_horn.use");
    public static final RegistryObject<SoundEvent> SONGVERN_AMBIENT = create("entity.songvern.ambient");
    public static final RegistryObject<SoundEvent> SONGVERN_HURT = create("entity.songvern.hurt");
    public static final RegistryObject<SoundEvent> SONGVERN_DEATH = create("entity.songvern.death");
    public static final RegistryObject<SoundEvent> HAROLD_AMBIENT = create("entity.harolds_greendrake.ambient");
    public static final RegistryObject<SoundEvent> HAROLD_HURT = create("entity.harolds_greendrake.hurt");
    public static final RegistryObject<SoundEvent> HAROLD_DEATH = create("entity.harolds_greendrake.death");
    public static final RegistryObject<SoundEvent> STT_AMBIENT = create("entity.saddled_thunder_tail.ambient");
    public static final RegistryObject<SoundEvent> STT_ATTACK = create("entity.saddled_thunder_tail.attack");
    public static final RegistryObject<SoundEvent> STT_DEATH = create("entity.saddled_thunder_tail.death");
    public static final RegistryObject<SoundEvent> STT_HURT = create("entity.saddled_thunder_tail.hurt");
    public static final RegistryObject<SoundEvent> HB_AMBIENT = create("entity.hatchet_beak.ambient");
    public static final RegistryObject<SoundEvent> HB_DEATH = create("entity.hatchet_beak.death");
    public static final RegistryObject<SoundEvent> HB_HURT = create("entity.hatchet_beak.hurt");
    public static final RegistryObject<SoundEvent> PLAYER_WHISTLE = create("entity.player.whistle");

    public static final RegistryObject<SoundEvent> MUSIC_BLISSFUL_DUNES = create("music.blissful_dunes");
    public static final RegistryObject<SoundEvent> MUSIC_ASHEN_LANDS = create("music.ashen_lands");
    public static final RegistryObject<SoundEvent> MUSIC_ISLAND_HOPPERS = create("music.island_hoppers");
    public static final RegistryObject<SoundEvent> MUSIC_MANTAS_MELODY = create("music.mantas_melody");

    private static RegistryObject<SoundEvent> create(String name) {
        return REGISTRY.register(name, () -> new SoundEvent(new ResourceLocation(WingsAndClaws.MOD_ID, name)));
    }
}
