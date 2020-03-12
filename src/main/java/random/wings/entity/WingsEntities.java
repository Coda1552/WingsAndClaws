package random.wings.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Tuple;
import random.wings.item.WingsItems;

import java.util.*;

public class WingsEntities {
    public static final List<EntityType<?>> LIST = new ArrayList<>();
    public static final EntityType<DragonEggEntity> DRAGON_EGG = create("dragon_egg", DragonEggEntity::new, 0.2f, 0.2f);
    private static final Map<EntityType<?>, Tuple<Integer, Integer>> EGGS = new HashMap<>();
    public static final EntityType<DumpyEggDrakeEntity> DUMPY_EGG_DRAKE = create("dumpy_egg_drake", DumpyEggDrakeEntity::new, 1.2f, 1.3f, 0xE2A336, 0xCEA575);
    public static final EntityType<HatchetBeakEntity> HATCHET_BEAK = create("hatchet_beak", HatchetBeakEntity::new, 2.3f, 1.0f, 0x785028, 0x167a6d);

    private static <T extends AnimalEntity> EntityType<T> create(String name, EntityType.IFactory<T> factory, float width, float height, int pri, int sec) {
        EntityType<T> type = create(name, factory, EntityClassification.CREATURE, width, height);
        EGGS.put(type, new Tuple<>(pri, sec));
        return type;
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType.IFactory<T> factory, float width, float height) {
        return create(name, factory, EntityClassification.MISC, width, height);
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
        EntityType<T> type = EntityType.Builder.create(factory, classification).size(width, height).setTrackingRange(32).build(name);
        type.setRegistryName(name);
        LIST.add(type);
        return type;
    }

    public static void registerEggs() {
        final Item.Properties properties = new Item.Properties().group(WingsItems.GROUP);
        EGGS.forEach((type, egg) -> WingsItems.LIST.add(new SpawnEggItem(type, egg.getA(), egg.getB(), properties).setRegistryName(Objects.requireNonNull(type.getRegistryName()).getPath() + "_spawn_egg")));
    }
}
