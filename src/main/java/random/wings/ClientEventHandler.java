package random.wings;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import random.wings.client.renderer.DumpyEggDrakeRenderer;
import random.wings.client.renderer.NestTileEntityRenderer;
import random.wings.entity.DumpyEggDrakeEntity;
import random.wings.tileentity.NestTileEntity;

public class ClientEventHandler {
    public static boolean isOverDED() {
        return Minecraft.getInstance().objectMouseOver.getType() == RayTraceResult.Type.ENTITY && ((EntityRayTraceResult) Minecraft.getInstance().objectMouseOver).getEntity() instanceof DumpyEggDrakeEntity;
    }

    public static void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(NestTileEntity.class, NestTileEntityRenderer.INSTANCE);
        RenderingRegistry.registerEntityRenderingHandler(DumpyEggDrakeEntity.class, DumpyEggDrakeRenderer::new);
    }
}
