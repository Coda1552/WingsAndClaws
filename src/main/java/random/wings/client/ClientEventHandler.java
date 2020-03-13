package random.wings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import random.wings.client.renderer.DumpyEggDrakeRenderer;
import random.wings.client.renderer.HatchetBeakRenderer;
import random.wings.client.renderer.IcyPlowheadRenderer;
import random.wings.client.renderer.NestTileEntityRenderer;
import random.wings.entity.DumpyEggDrakeEntity;
import random.wings.entity.HatchetBeakEntity;
import random.wings.entity.IcyPlowheadEntity;
import random.wings.entity.PlowheadEggEntity;
import random.wings.item.WingsItems;
import random.wings.tileentity.NestTileEntity;

public class ClientEventHandler {
    public static void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(NestTileEntity.class, NestTileEntityRenderer.INSTANCE);
        RenderingRegistry.registerEntityRenderingHandler(DumpyEggDrakeEntity.class, DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HatchetBeakEntity.class, HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(IcyPlowheadEntity.class, IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PlowheadEggEntity.class, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
    }
}
