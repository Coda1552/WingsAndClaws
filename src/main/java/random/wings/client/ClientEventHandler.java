package random.wings.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import random.wings.client.renderer.DumpyEggDrakeRenderer;
import random.wings.client.renderer.NestTileEntityRenderer;
import random.wings.entity.DumpyEggDrakeEntity;
import random.wings.tileentity.NestTileEntity;

public class ClientEventHandler {
    public static void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(NestTileEntity.class, NestTileEntityRenderer.INSTANCE);
        RenderingRegistry.registerEntityRenderingHandler(DumpyEggDrakeEntity.class, DumpyEggDrakeRenderer::new);
    }
}
