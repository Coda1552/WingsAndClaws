package coda.wingsandclaws.client;

import coda.wingsandclaws.client.renderer.entity.*;
import coda.wingsandclaws.client.renderer.tileentity.*;
import coda.wingsandclaws.init.WingsEntities;
import coda.wingsandclaws.entity.HatchetBeakEntity;
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
import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.init.WingsBlocks;
import coda.wingsandclaws.init.WingsItems;
import coda.wingsandclaws.init.WingsTileEntities;

@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        RenderTypeLookup.setRenderLayer(WingsBlocks.MANGO_BUNCH.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(WingsBlocks.ROASTED_HAROLDS_GREENDRAKE.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WingsBlocks.ROASTED_HAROLDS_GREENDRAKE.get(), RenderType.getTranslucent());

        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.DED_NEST.get(), DEDNestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WingsTileEntities.HB_NEST.get(), HBNestTileEntityRenderer::new);

        WingsISTER.get().addDelegate(WingsItems.ICY_PLOWHEAD_SHIELD.get(), new PlowheadShieldRenderer());
        WingsISTER.get().addDelegate(WingsBlocks.DED_NEST.get().asItem(), new NestItemRenderer(WingsTileEntities.DED_NEST.get()));
        WingsISTER.get().addDelegate(WingsBlocks.HB_NEST.get().asItem(), new NestItemRenderer(WingsTileEntities.HB_NEST.get()));

        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.DUMPY_EGG_DRAKE.get(), DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.HATCHET_BEAK.get(), HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ICY_PLOWHEAD.get(), IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WingsEntities.ICY_PLOWHEAD_EGG.get(), PlowheadEggRenderer::new);
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
