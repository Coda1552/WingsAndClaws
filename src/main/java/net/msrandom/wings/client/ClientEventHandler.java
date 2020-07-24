package net.msrandom.wings.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayerLookup;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.block.entity.WingsBlockEntities;
import net.msrandom.wings.client.renderer.entity.*;
import net.msrandom.wings.client.renderer.tileentity.DEDNestBlockEntityRenderer;
import net.msrandom.wings.client.renderer.tileentity.HBNestBlockEntityRenderer;
import net.msrandom.wings.entity.WingsEntities;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ClientEventHandler implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RenderLayerLookup.setRenderLayer(WingsBlocks.MANGO_BUNCH, RenderLayer.getTranslucent());

        ClientRegistry.bindBlockEntityRenderer(WingsBlockEntities.DED_NEST, DEDNestBlockEntityRenderer::new);
        ClientRegistry.bindBlockEntityRenderer(WingsBlockEntities.HB_NEST, HBNestBlockEntityRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.DUMPY_EGG_DRAKE, DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.HATCHET_BEAK, HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ICY_PLOWHEAD, IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.PLOWHEAD_EGG, PlowheadEggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO, MimangoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO_EGG, MimangoEggRenderer::new);
    }

    @Environment(EnvType.CLIENT)
    public static Item.Properties getWithISTER(Item.Properties properties, Supplier<Callable<Object>> ister) {
        return properties.setISTER((Supplier) ister);
    }
}
