package net.msrandom.wings.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.client.renderer.entity.HaroldsGreendrakeRenderer;
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
        RenderTypeLookup.setRenderLayer(WingsBlocks.MANGO_BUNCH, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(WingsBlocks.ROASTED_HAROLDS_GREENDRAKE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WingsBlocks.ROASTED_HAROLDS_GREENDRAKE, RenderType.getTranslucent());

        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.DED_NEST, DEDNestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.HB_NEST, HBNestTileEntityRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.DUMPY_EGG_DRAKE, DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.HATCHET_BEAK, HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ICY_PLOWHEAD, IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.PLOWHEAD_EGG, PlowheadEggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO, MimangoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO_EGG, MimangoEggRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ST_PROJECTILE_ENTITY, SpearProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.SUGARSCALE, SugarscaleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.HAROLDS_GREENDRAKE, HaroldsGreendrakeRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.LEAPING_GRUB_SHRIMP, LeapingGrubShrimpRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.SONGVERN, SongvernRenderer::new);

        ClientRegistry.registerKeyBinding(WingsAndClaws.callHatchetBeakKey = new KeyBinding("key.callHatchetBeak", 75, "key.categories.gameplay"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @OnlyIn(Dist.CLIENT)
    public static Item.Properties getWithISTER(Item.Properties properties, Supplier<Callable<Object>> ister) {
        return properties.setISTER((Supplier) ister);
    }
}
