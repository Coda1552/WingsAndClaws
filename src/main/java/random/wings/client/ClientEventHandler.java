package random.wings.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import random.wings.client.renderer.entity.*;
import random.wings.client.renderer.tileentity.DEDNestTileEntityRenderer;
import random.wings.client.renderer.tileentity.HBNestTileEntityRenderer;
import random.wings.entity.WingsEntities;
import random.wings.tileentity.WingsTileEntities;

public class ClientEventHandler {
    public static void init() {
        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.DED_NEST, DEDNestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.HB_NEST, HBNestTileEntityRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.DUMPY_EGG_DRAKE, DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.HATCHET_BEAK, HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ICY_PLOWHEAD, IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.PLOWHEAD_EGG, PlowheadEggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO, MimangoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO_EGG, MimangoEggRenderer::new);
        //RenderingRegistry.registerEntityRenderingHandler(MimangoEggEntity.class, MimangoEggRenderer::new);
    }
}
