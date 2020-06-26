package net.msrandom.wings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.*;
import net.msrandom.wings.client.renderer.tileentity.DEDNestTileEntityRenderer;
import net.msrandom.wings.client.renderer.tileentity.HBNestTileEntityRenderer;
import net.msrandom.wings.client.renderer.tileentity.model.PlowheadHornModel;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.item.WingsItems;
import net.msrandom.wings.tileentity.WingsTileEntities;

@Mod.EventBusSubscriber(modid = WingsAndClaws.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {
    private static final ResourceLocation HORN_TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/horn.png");
    private static final PlowheadHornModel HORN_MODEL = new PlowheadHornModel();

    @SubscribeEvent
    public static void playerRenderPre(RenderPlayerEvent.Pre event) {
        if(isUsingHorn(event.getPlayer())) {
            PlayerModel<AbstractClientPlayerEntity> player = event.getRenderer().getEntityModel();
            player.bipedLeftArm.showModel = false;
            player.bipedRightArm.showModel = false;
        }
    }

    @SubscribeEvent
    public static void playerRenderPost(RenderPlayerEvent.Post event) {
        if(isUsingHorn(event.getPlayer())) {
            //The rotations here are not done
            PlayerModel<AbstractClientPlayerEntity> player = event.getRenderer().getEntityModel();
            handleArm(event, true, player.bipedLeftArm, event.getPlayer(), event.getPartialRenderTick());
            handleArm(event, false, player.bipedRightArm, event.getPlayer(), event.getPartialRenderTick());
            event.getMatrixStack().push();
            PlayerEntity p = event.getPlayer();
            float rotation = (p.prevRenderYawOffset + (p.renderYawOffset - p.prevRenderYawOffset) * event.getPartialRenderTick());
            float radians = (float) Math.toRadians(rotation);
            HORN_MODEL.shape1.rotationPointZ = MathHelper.sin(radians) * 4.75F;
            HORN_MODEL.shape1.rotationPointX = MathHelper.cos(-radians) * 4.75F;
            HORN_MODEL.render(event.getMatrixStack(), event.getBuffers().getBuffer(HORN_MODEL.getRenderType(HORN_TEXTURE)), event.getLight(), OverlayTexture.getPackedUV(OverlayTexture.getU(0), OverlayTexture.getV(false)), 1, 1, 1, 1);
            event.getMatrixStack().scale(1, -1, -1);
            event.getMatrixStack().pop();
        }
    }

    @SubscribeEvent
    public static void renderArm(RenderHandEvent event) {
        if(isUsingHorn(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
    }

    public static boolean isUsingHorn(PlayerEntity playerEntity) {
        return playerEntity != null && (playerEntity.getHeldItemMainhand().getItem() == WingsItems.HORN_HORN || playerEntity.getHeldItemOffhand().getItem() == WingsItems.HORN_HORN);
    }

    private static void handleArm(RenderPlayerEvent.Post event, boolean flip, ModelRenderer arm, PlayerEntity player, float partial) {
        float rotation = (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partial);
        arm.showModel = true;
        float radians = (float) Math.toRadians(flip ? -rotation : rotation);
        arm.rotationPointZ = MathHelper.sin(radians) * 4.75F;
        arm.rotationPointY = player.isSneaking() ? 17 : 20;
        arm.rotationPointX = MathHelper.cos(radians) * 4.75F;

        arm.rotateAngleX = flip ? 2.1f : 2.1f;
        arm.rotateAngleY = radians + (flip ? -0.3f : 0.3f);
        arm.rotateAngleZ = flip ? 0.21f : -0.21f;

        event.getMatrixStack().push();
        arm.render(event.getMatrixStack(), event.getBuffers().getBuffer(event.getRenderer().getEntityModel().getRenderType(((AbstractClientPlayerEntity) player).getLocationSkin())), event.getLight(), OverlayTexture.getPackedUV(OverlayTexture.getU(0), OverlayTexture.getV(player.hurtTime > 0 || player.deathTime > 0)));
        event.getMatrixStack().pop();

        arm.rotationPointY = 2;
    }

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
}
