package net.msrandom.wings.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.item.MimangoEggEntity;
import net.msrandom.wings.entity.item.PlowheadEggEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;
import net.msrandom.wings.item.WingsItems;

public class WingsEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.ENTITIES, WingsAndClaws.MOD_ID);
    //public static final EntityType<DragonEggEntity> DRAGON_EGG = createEgg("dragon_egg", DragonEggEntity::new, () -> WingsEntities.DRAGON_EGG, 0.2f, 0.2f);
    public static final EntityType<DumpyEggDrakeEntity> DUMPY_EGG_DRAKE = createCreature("dumpy_egg_drake", DumpyEggDrakeEntity::new, 1.2f, 1.3f, 0xddbc8b, 0xbc9161);
    public static final EntityType<HatchetBeakEntity> HATCHET_BEAK = createCreature("hatchet_beak", HatchetBeakEntity::new, 2.3f, 2.5f/*, 0x785028, 0x167a6d*/);
    public static final EntityType<IcyPlowheadEntity> ICY_PLOWHEAD = createCreature("icy_plowhead", IcyPlowheadEntity::new, 1.5f, 1.0f, 0x3B4782, 0x9CA0AD);
    public static final EntityType<PlowheadEggEntity> PLOWHEAD_EGG = createEgg("icy_plowhead_egg", PlowheadEggEntity::new, 0.43f, 0.43f);
    public static final EntityType<MimangoEggEntity> MIMANGO_EGG = createEgg("mimango_egg", MimangoEggEntity::new, 0.3f, 0.3f);
    public static final EntityType<MimangoEntity> MIMANGO = createCreature("mimango", MimangoEntity::new, 0.35f, 0.35f/*, 0xFFDD32, 0x97CB00*/);

    //Eggs used to have a custom client factory, but that just calls the default factory so it's useless, so it's been removed
    private static <T extends Entity> EntityType<T> createEgg(String name, EntityType.IFactory<T> factory, float width, float height) {
        return create(name, factory, EntityClassification.MISC, width, height);
    }

    private static <T extends AnimalEntity> EntityType<T> createCreature(String name, EntityType.IFactory<T> factory, float width, float height, int pri, int sec) {
        final Item.Properties properties = new Item.Properties().group(WingsItems.GROUP);
        EntityType<T> type = createCreature(name, factory, width, height);
        WingsItems.REGISTRY.register(name + "_spawn_egg", () -> new SpawnEggItem(type, pri, sec, properties));
        return type;
    }

    private static <T extends Entity> EntityType<T> createCreature(String name, EntityType.IFactory<T> factory, float width, float height) {
        return create(name, factory, EntityClassification.CREATURE, width, height);
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
        EntityType<T> type = EntityType.Builder.create(factory, classification).size(width, height).setTrackingRange(128).build(name);
        REGISTRY.register(name, () -> type);
        return type;
    }
}
