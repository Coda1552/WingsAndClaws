package net.coda.wings.client;

import net.coda.wings.client.renderer.entity.*;
import net.coda.wings.client.renderer.tileentity.*;
import net.coda.wings.entity.WingsEntities;
import net.coda.wings.entity.monster.HatchetBeakEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.coda.wings.WingsAndClaws;
import net.coda.wings.block.WingsBlocks;
import net.coda.wings.item.WingsItems;
import net.coda.wings.tileentity.WingsTileEntities;

@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        RenderTypeLookup.setRenderLayer(WingsBlocks.MANGO_BUNCH, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(WingsBlocks.ROASTED_HAROLDS_GREENDRAKE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WingsBlocks.ROASTED_HAROLDS_GREENDRAKE, RenderType.getTranslucent());

        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.DED_NEST, DEDNestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.HB_NEST, HBNestTileEntityRenderer::new);

        WingsISTER.get().addDelegate(WingsItems.ICY_PLOWHEAD_SHIELD.get(), new PlowheadShieldRenderer());
        WingsISTER.get().addDelegate(WingsBlocks.DED_NEST.asItem(), new NestItemRenderer(WingsTileEntities.DED_NEST));
        WingsISTER.get().addDelegate(WingsBlocks.HB_NEST.asItem(), new NestItemRenderer(WingsTileEntities.HB_NEST));

        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.DUMPY_EGG_DRAKE.get(), DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.HATCHET_BEAK.get(), HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ICY_PLOWHEAD.get(), IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.PLOWHEAD_EGG.get(), PlowheadEggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO.get(), MimangoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.MIMANGO_EGG.get(), MimangoEggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.SUGARSCALE.get(), SugarscaleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.HAROLDS_GREENDRAKE.get(), HaroldsGreendrakeRenderer::new);
        // RenderingRegistry.registerEntityRenderingHandler(WingsEntities.SADDLED_THUNDER_TAIL, SaddledThunderTailRenderer::new);
        // RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ST_PROJECTILE_ENTITY.get(), SpearProjectileRenderer::new);
        // RenderingRegistry.registerEntityRenderingHandler(WingsEntities.SADDLED_THUNDER_TAIL_EGG, SaddledThunderTailEggRenderer::new);
        // RenderingRegistry.registerEntityRenderingHandler(WingsEntities.LEAPING_GRUB_SHRIMP, LeapingGrubShrimpRenderer::new);
        // RenderingRegistry.registerEntityRenderingHandler(WingsEntities.SONGVERN, SongvernRenderer::new);

        WingsKeyBindings.LIST.forEach(ClientRegistry::registerKeyBinding);
    }

    @SubscribeEvent
    public static void renderPlayer(RenderPlayerEvent.Pre event) {
        if (event.getPlayer().getRidingEntity() instanceof HatchetBeakEntity) {
            final HatchetBeakEntity hb = (HatchetBeakEntity) event.getPlayer().getRidingEntity();
            event.getMatrixStack().rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(event.getPartialRenderTick(), hb.prevTilt, hb.tilt)));
        }
    }
}
