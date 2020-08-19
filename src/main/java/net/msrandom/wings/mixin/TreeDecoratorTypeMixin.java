package net.msrandom.wings.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.decorator.TreeDecorator;
import net.minecraft.world.gen.decorator.TreeDecoratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeDecoratorType.class)
public interface TreeDecoratorTypeMixin {
    @Invoker
    static <P extends TreeDecorator> TreeDecoratorType<P> callMethod_28895(String string, Codec<P> codec) {
        throw new RuntimeException();
    }
}
