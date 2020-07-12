package net.msrandom.wings.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.WingsAndClaws;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.function.Function;

public class WingsFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.FEATURES, WingsAndClaws.MOD_ID);
    public static final Feature<NoFeatureConfig> DED_NEST = register("ded_nest", new DEDNestStructure());
    public static final Feature<NoFeatureConfig> HB_NEST = register("hb_nest", new HBNestStructure());

    public static final TreeDecoratorType<TreeDecorator> MANGO_BUNCH = registerTreeDecorator("mango_bunch", MangoBunchTreeDecorator::new);

    private static <T extends Feature<?>> T register(String name, T feature) {
        REGISTRY.register(name, () -> feature);
        return feature;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecorator(String name, Function<Dynamic<?>, T> function) {
        @SuppressWarnings("rawtypes") Constructor<TreeDecoratorType> constructor = ObfuscationReflectionHelper.findConstructor(TreeDecoratorType.class, Function.class);

        try {
            return Registry.register(Registry.TREE_DECORATOR_TYPE, name, (TreeDecoratorType<T>) constructor.newInstance(function));
        } catch (ReflectiveOperationException e) {
            throw new ReportedException(CrashReport.makeCrashReport(e, "Creating " + name + " tree decorator"));
        }
    }
}
