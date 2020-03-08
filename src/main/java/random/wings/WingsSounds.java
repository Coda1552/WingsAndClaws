package random.wings;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class WingsSounds {

    public static final List<SoundEvent> LIST = new ArrayList<>();
    public static final SoundEvent DED_AMBIENT = create("ded.ambient");
    public static final SoundEvent DED_HURT = create("ded.hurt");
    public static final SoundEvent DED_DEATH = create("ded.death");

    private static SoundEvent create(String name) {
        ResourceLocation id = new ResourceLocation(WingsAndClaws.MOD_ID, name);
        SoundEvent event = new SoundEvent(id);
        event.setRegistryName(id);
        LIST.add(event);
        return event;
    }
}
