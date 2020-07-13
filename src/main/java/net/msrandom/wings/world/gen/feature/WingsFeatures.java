package net.msrandom.wings.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.ResourceLocation;
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

    @SuppressWarnings("rawtypes")
    private static final Constructor<TreeDecoratorType> DECORATOR_CONSTRUCTOR = ObfuscationReflectionHelper.findConstructor(TreeDecoratorType.class, Function.class);
    public static final TreeDecoratorType<TreeDecorator> MANGO_BUNCH = registerTreeDecorator("mango_bunch", MangoBunchTreeDecorator::new);

    private static <T extends Feature<?>> T register(String name, T feature) {
        REGISTRY.register(name, () -> feature);
        return feature;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private static <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecorator(String name, Function<Dynamic<?>, T> function) {
        ResourceLocation id = new ResourceLocation(WingsAndClaws.MOD_ID, name);
        try {
            return Registry.register(Registry.TREE_DECORATOR_TYPE, id, (TreeDecoratorType<T>) DECORATOR_CONSTRUCTOR.newInstance(function));
        } catch (Throwable e) {
            throw new ReportedException(CrashReport.makeCrashReport(e, "Creating " + id + " tree decorator"));
        }
    }
}
