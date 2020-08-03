package net.msrandom.wings.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.block.entity.WingsBlockEntities;
import net.msrandom.wings.client.renderer.entity.*;
import net.msrandom.wings.client.renderer.tileentity.DEDNestBlockEntityRenderer;
import net.msrandom.wings.client.renderer.tileentity.HBNestBlockEntityRenderer;
import net.msrandom.wings.entity.WingsEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class ClientEventHandler implements ClientModInitializer {
    private static final Map<Item, BuiltinModelItemRenderer> BUILT_IN_MODELS = new HashMap<>();

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(WingsBlocks.MANGO_BUNCH, RenderLayer.getTranslucent());

        BlockEntityRendererRegistry.INSTANCE.register(WingsBlockEntities.DED_NEST, DEDNestBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(WingsBlockEntities.HB_NEST, HBNestBlockEntityRenderer::new);

        registerRenderer(WingsEntities.DUMPY_EGG_DRAKE, DumpyEggDrakeRenderer::new);
        registerRenderer(WingsEntities.HATCHET_BEAK, HatchetBeakRenderer::new);
        registerRenderer(WingsEntities.ICY_PLOWHEAD, IcyPlowheadRenderer::new);
        registerRenderer(WingsEntities.PLOWHEAD_EGG, PlowheadEggRenderer::new);
        registerRenderer(WingsEntities.MIMANGO, MimangoRenderer::new);
        registerRenderer(WingsEntities.MIMANGO_EGG, MimangoEggRenderer::new);
    }

    private static <T extends Entity> void registerRenderer(EntityType<T> entity, Function<EntityRenderDispatcher, EntityRenderer<T>> renderer) {
        EntityRendererRegistry.INSTANCE.register(entity, (dispatcher, context) -> renderer.apply(dispatcher));
    }

    @Environment(EnvType.CLIENT)
    public static Item getWithISTER(Item item, Supplier<Callable<Object>> ister) {
        try {
            //This is bad but it's because we the supplier of callable is a forge thing, eventually change this
            BUILT_IN_MODELS.put(item, (BuiltinModelItemRenderer) ister.get().call());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }
}
