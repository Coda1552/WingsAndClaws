package random.wings.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import random.wings.client.renderer.entity.DumpyEggDrakeRenderer;
import random.wings.client.renderer.entity.HatchetBeakRenderer;
import random.wings.client.renderer.entity.IcyPlowheadRenderer;
import random.wings.client.renderer.entity.PlowheadEggRenderer;
import random.wings.client.renderer.tileentity.NestTileEntityRenderer;
import random.wings.entity.item.PlowheadEggEntity;
import random.wings.entity.monster.IcyPlowheadEntity;
import random.wings.entity.passive.DumpyEggDrakeEntity;
import random.wings.entity.passive.HatchetBeakEntity;
import random.wings.tileentity.NestTileEntity;

public class ClientEventHandler {
    public static void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(NestTileEntity.class, NestTileEntityRenderer.INSTANCE);
        RenderingRegistry.registerEntityRenderingHandler(DumpyEggDrakeEntity.class, DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HatchetBeakEntity.class, HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(IcyPlowheadEntity.class, IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PlowheadEggEntity.class, PlowheadEggRenderer::new);
    }
}
