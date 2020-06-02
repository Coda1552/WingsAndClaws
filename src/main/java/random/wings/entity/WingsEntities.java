package random.wings.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Tuple;
import random.wings.entity.item.MimangoEggEntity;
import random.wings.entity.item.PlowheadEggEntity;
import random.wings.entity.monster.IcyPlowheadEntity;
import random.wings.entity.passive.DumpyEggDrakeEntity;
import random.wings.entity.passive.HatchetBeakEntity;
import random.wings.entity.passive.MimangoEntity;
import random.wings.item.WingsItems;

import java.util.*;
import java.util.function.Supplier;

public class WingsEntities {
    public static final List<EntityType<?>> LIST = new ArrayList<>();
    //public static final EntityType<DragonEggEntity> DRAGON_EGG = createEgg("dragon_egg", DragonEggEntity::new, () -> WingsEntities.DRAGON_EGG, 0.2f, 0.2f);
    private static final Map<EntityType<?>, Tuple<Integer, Integer>> EGGS = new HashMap<>();
    public static final EntityType<DumpyEggDrakeEntity> DUMPY_EGG_DRAKE = create("dumpy_egg_drake", DumpyEggDrakeEntity::new, 1.2f, 1.3f, 0xE2A336, 0xCEA575);
    public static final EntityType<HatchetBeakEntity> HATCHET_BEAK = create("hatchet_beak", HatchetBeakEntity::new, 2.3f, 2.5f, 0x785028, 0x167a6d);
    public static final EntityType<IcyPlowheadEntity> ICY_PLOWHEAD = create("icy_plowhead", IcyPlowheadEntity::new, 1.5f, 1.0f, 0x3B4782, 0x9CA0AD);
    public static final EntityType<PlowheadEggEntity> PLOWHEAD_EGG = createEgg("icy_plowhead_egg", PlowheadEggEntity::new, () -> WingsEntities.PLOWHEAD_EGG, 0.25f, 0.25F);
    public static final EntityType<MimangoEggEntity> MIMANGO_EGG = createEgg("mimango_egg", MimangoEggEntity::new, () -> WingsEntities.MIMANGO_EGG, 0.1f, 0.1f);
    public static final EntityType<MimangoEntity> MIMANGO = create("mimango", MimangoEntity::new, 0.1f, 0.1F, 0xFFDD32, 0x97CB00);

    private static <T extends AnimalEntity> EntityType<T> create(String name, EntityType.IFactory<T> factory, float width, float height, int pri, int sec) {
        EntityType<T> type = create(name, factory, width, height);
        EGGS.put(type, new Tuple<>(pri, sec));
        return type;
    }

    private static <T extends Entity> EntityType<T> createEgg(String name, EntityType.IFactory<T> factory, Supplier<EntityType<T>> reference, float width, float height) {
        EntityType<T> type = EntityType.Builder.create(factory, EntityClassification.MISC).size(width, height).setTrackingRange(32).setCustomClientFactory((s, world) -> reference.get().create(world)).build(name);
        type.setRegistryName(name);
        LIST.add(type);
        return type;
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType.IFactory<T> factory, float width, float height) {
        EntityType<T> type = EntityType.Builder.create(factory, EntityClassification.CREATURE).size(width, height).setTrackingRange(32).build(name);
        type.setRegistryName(name);
        LIST.add(type);
        return type;
    }

    public static void registerEggs() {
        final Item.Properties properties = new Item.Properties().group(WingsItems.GROUP);
        EGGS.forEach((type, egg) -> WingsItems.LIST.add(new SpawnEggItem(type, egg.getA(), egg.getB(), properties).setRegistryName(Objects.requireNonNull(type.getRegistryName()).getPath() + "_spawn_egg")));
    }
}
