package net.msrandom.wings.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.WingsEntities;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

public class WingsFeatures {
    public static final DeferredRegister<Feature<?>> FEATURE_REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, WingsAndClaws.MOD_ID);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_REGISTRY = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, WingsAndClaws.MOD_ID);

    public static final Feature<NoFeatureConfig> DED_NEST = register("ded_nest", new NestFeature(new ResourceLocation(WingsAndClaws.MOD_ID, "dumpy_egg_drake"), WingsEntities.DUMPY_EGG_DRAKE));
    public static final Feature<NoFeatureConfig> HB_NEST = register("hb_nest", new NestFeature(new ResourceLocation(WingsAndClaws.MOD_ID, "hatchet_beak"), WingsEntities.HATCHET_BEAK));

    public static final Feature<NoFeatureConfig> MIMANGO_SHRINE = register("mimango_shrine", new WingsNbtStructure(new ResourceLocation(WingsAndClaws.MOD_ID, "mimango_shrine")));

    @SuppressWarnings("rawtypes")
    private static final Constructor<TreeDecoratorType> DECORATOR_CONSTRUCTOR = ObfuscationReflectionHelper.findConstructor(TreeDecoratorType.class, Codec.class);
    public static final TreeDecoratorType<TreeDecorator> MANGO_BUNCH = registerTreeDecorator("mango_bunch", MangoBunchTreeDecorator.field_236874_c_);

    private static <T extends Feature<?>> T register(String name, T feature) {
        FEATURE_REGISTRY.register(name, () -> feature);
        return feature;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private static <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecorator(String name, Codec<?> function) {
        ResourceLocation id = new ResourceLocation(WingsAndClaws.MOD_ID, name);
        try {
            TreeDecoratorType<T> type = DECORATOR_CONSTRUCTOR.newInstance(function);
            TREE_DECORATOR_REGISTRY.register(name, () -> type);
            return type;
        } catch (Throwable e) {
            throw new ReportedException(CrashReport.makeCrashReport(e, "Creating " + id + " tree decorator"));
        }
    }
}
