package net.msrandom.wings.client;

import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.*;
import net.msrandom.wings.client.renderer.tileentity.DEDNestTileEntityRenderer;
import net.msrandom.wings.client.renderer.tileentity.HBNestTileEntityRenderer;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.tileentity.WingsTileEntities;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.DED_NEST, DEDNestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.HB_NEST, HBNestTileEntityRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.DUMPY_EGG_DRAKE, DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.HATCHET_BEAK, HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ICY_PLOWHEAD, IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.PLOWHEAD_EGG, PlowheadEggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO, MimangoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO_EGG, MimangoEggRenderer::new);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @OnlyIn(Dist.CLIENT)
    public static Item.Properties getWithISTER(Item.Properties properties, Supplier<Callable<Object>> ister) {
        return properties.setISTER((Supplier) ister);
    }
}
