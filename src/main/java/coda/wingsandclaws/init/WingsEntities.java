package coda.wingsandclaws.init;

import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.entity.item.MimangoEggEntity;
import coda.wingsandclaws.entity.item.PlowheadEggEntity;
import coda.wingsandclaws.entity.item.SpearProjectileEntity;
import coda.wingsandclaws.entity.DumpyEggDrakeEntity;
import coda.wingsandclaws.entity.HatchetBeakEntity;
import coda.wingsandclaws.entity.IcyPlowheadEntity;
import coda.wingsandclaws.entity.MimangoEntity;
import coda.wingsandclaws.entity.HaroldsGreendrakeEntity;
import coda.wingsandclaws.entity.SugarscaleEntity;
import coda.wingsandclaws.item.WingsSpawnEggItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class WingsEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, WingsAndClaws.MOD_ID);
    public static final RegistryObject<EntityType<DumpyEggDrakeEntity>> DUMPY_EGG_DRAKE = create("dumpy_egg_drake",DumpyEggDrakeEntity::new, EntityClassification.CREATURE, 1.2f, 1.3f, 0xddbc8b, 0xbc9161);
    public static final RegistryObject<EntityType<HatchetBeakEntity>> HATCHET_BEAK = create("hatchet_beak", HatchetBeakEntity::new, EntityClassification.CREATURE, 3.35f, 2.5f, 0x54392a, 0x04a08e);
    public static final RegistryObject<EntityType<IcyPlowheadEntity>> ICY_PLOWHEAD = create("icy_plowhead", IcyPlowheadEntity::new, EntityClassification.WATER_CREATURE, 1.5f, 1.0f, 0x3B4782, 0x9CA0AD);
    public static final RegistryObject<EntityType<PlowheadEggEntity>> ICY_PLOWHEAD_EGG = create("icy_plowhead_egg", PlowheadEggEntity::new, EntityClassification.MISC, 0.43f, 0.43f);
    public static final RegistryObject<EntityType<MimangoEggEntity>> MIMANGO_EGG = create("mimango_egg", MimangoEggEntity::new, EntityClassification.MISC, 0.3f, 0.3f);
    public static final RegistryObject<EntityType<MimangoEntity>> MIMANGO = create("mimango", MimangoEntity::new, EntityClassification.CREATURE, 0.35f, 0.35f, 0xab571e, 0xf2cd51);
    public static final RegistryObject<EntityType<SpearProjectileEntity>> ST_PROJECTILE_ENTITY = create("spear_projectile_entity", SpearProjectileEntity::new, EntityClassification.MISC, 0.4f, 0.4f);
    public static final RegistryObject<EntityType<SugarscaleEntity>> SUGARSCALE = create("sugarscale", SugarscaleEntity::new, EntityClassification.WATER_CREATURE, 0.4f, 0.2f, 0x4a4e80, 0x413852);
    public static final RegistryObject<EntityType<HaroldsGreendrakeEntity>> HAROLDS_GREENDRAKE = create("harolds_greendrake", HaroldsGreendrakeEntity::new, EntityClassification.CREATURE, 0.9f, 1.1f, 0x4b5832, 0xb59a2f);
    //public static final EntityType<SaddledThunderTailEntity> SADDLED_THUNDER_TAIL = create("saddled_thunder_tail", SaddledThunderTailEntity::new, EntityClassification.CREATURE, 4.0f, 3.2f, 0xFE9A2E, 0x8A2908);
    //public static final EntityType<SaddledThunderTailEggEntity> SADDLED_THUNDER_TAIL_EGG = create("saddled_thunder_tail_egg", SaddledThunderTailEggEntity::new, EntityClassification.MISC, 0.5f, 0.75f);

    private static <T extends CreatureEntity> RegistryObject<EntityType<T>> create(String name, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height, int pri, int sec) {
        final Item.Properties properties = new Item.Properties().group(WingsItems.GROUP);
        EntityType<T> type = createType(name, factory, classification, width, height);
        WingsItems.REGISTRY.register(name + "_spawn_egg", () -> new SpawnEggItem(type, pri, sec, properties));
        return REGISTRY.register(name, () -> type);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
        return REGISTRY.register(name, () -> createType(name, factory, classification, width, height));
    }

    private static <T extends Entity> EntityType<T> createType(String name, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
        return EntityType.Builder.create(factory, classification).size(width, height).setTrackingRange(128).build(name);
    }
}
