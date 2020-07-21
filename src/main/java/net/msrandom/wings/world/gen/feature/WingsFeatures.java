package net.msrandom.wings.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.TreeDecorator;
import net.minecraft.world.gen.decorator.TreeDecoratorType;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.msrandom.wings.WingsAndClaws;

import java.lang.reflect.Constructor;
import java.util.function.Function;

public class WingsFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.FEATURES, WingsAndClaws.MOD_ID);
    public static final Feature<DefaultFeatureConfig> DED_NEST = register("ded_nest", new DEDNestStructure());
    public static final Feature<DefaultFeatureConfig> HB_NEST = register("hb_nest", new HBNestStructure());

    @SuppressWarnings("rawtypes")
    private static final Constructor<TreeDecoratorType> DECORATOR_CONSTRUCTOR = ObfuscationReflectionHelper.findConstructor(TreeDecoratorType.class, Function.class);
    public static final TreeDecoratorType<TreeDecorator> MANGO_BUNCH = registerTreeDecorator("mango_bunch", MangoBunchTreeDecorator::new);

    private static <T extends Feature<?>> T register(String name, T feature) {
        REGISTRY.register(name, () -> feature);
        return feature;
    }

    @SuppressWarnings("unchecked")
    private static <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecorator(String name, Function<Codec<?>, T> function) {
        Identifier id = new Identifier(WingsAndClaws.MOD_ID, name);
        try {
            return Registry.register(Registry.TREE_DECORATOR_TYPE, id, (TreeDecoratorType<T>) DECORATOR_CONSTRUCTOR.newInstance(function));
        } catch (Throwable e) {
            throw new CrashException(CrashReport.create(e, "Creating " + id + " tree decorator"));
        }
    }
}
