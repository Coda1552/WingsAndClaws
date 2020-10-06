package net.msrandom.wings.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.item.MimangoEggEntity;
import net.msrandom.wings.entity.item.PlowheadEggEntity;
//import net.msrandom.wings.entity.item.SpearProjectileEntity;
import net.msrandom.wings.entity.item.SpearProjectileEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;
import net.msrandom.wings.entity.passive.SugarscaleEntity;
import net.msrandom.wings.item.WingsItems;

public class WingsEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.ENTITIES, WingsAndClaws.MOD_ID);
    //public static final EntityType<DragonEggEntity> DRAGON_EGG = createEgg("dragon_egg", DragonEggEntity::new, () -> WingsEntities.DRAGON_EGG, 0.2f, 0.2f);
    public static final EntityType<DumpyEggDrakeEntity> DUMPY_EGG_DRAKE = create("dumpy_egg_drake", DumpyEggDrakeEntity::new, EntityClassification.CREATURE, 1.2f, 1.3f, 0xddbc8b, 0xbc9161);
    public static final EntityType<HatchetBeakEntity> HATCHET_BEAK = create("hatchet_beak", HatchetBeakEntity::new, EntityClassification.CREATURE, 2.3f, 2.5f, 0x785028, 0x167a6d);
    public static final EntityType<IcyPlowheadEntity> ICY_PLOWHEAD = create("icy_plowhead", IcyPlowheadEntity::new, EntityClassification.WATER_CREATURE, 1.5f, 1.0f, 0x3B4782, 0x9CA0AD);
    public static final EntityType<PlowheadEggEntity> PLOWHEAD_EGG = create("icy_plowhead_egg", PlowheadEggEntity::new, EntityClassification.MISC, 0.43f, 0.43f);
    public static final EntityType<MimangoEggEntity> MIMANGO_EGG = create("mimango_egg", MimangoEggEntity::new, EntityClassification.MISC, 0.3f, 0.3f);
    public static final EntityType<MimangoEntity> MIMANGO = create("mimango", MimangoEntity::new, EntityClassification.CREATURE, 0.35f, 0.35f, 0xab571e, 0xf2cd51);
    public static final EntityType<SpearProjectileEntity> ST_PROJECTILE_ENTITY = create("spear_projectile_entity", SpearProjectileEntity::new, EntityClassification.MISC, 8.0f, 8.0f);
    public static final EntityType<SugarscaleEntity> SUGARSCALE = create("sugarscale", SugarscaleEntity::new, EntityClassification.WATER_CREATURE, 0.4f, 0.2f, 0x4a4e80, 0x413852);

    private static <T extends CreatureEntity> EntityType<T> create(String name, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height, int pri, int sec) {
        final Item.Properties properties = new Item.Properties().group(WingsItems.GROUP);
        EntityType<T> type = create(name, factory, classification, width, height);
        WingsItems.REGISTRY.register(name + "_spawn_egg", () -> new SpawnEggItem(type, pri, sec, properties));
        return type;
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
        EntityType<T> type = EntityType.Builder.create(factory, classification).size(width, height).setTrackingRange(128).build(name);
        REGISTRY.register(name, () -> type);
        return type;
    }
}
