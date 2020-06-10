package net.msrandom.wings.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public abstract class NestTileEntity extends TileEntity implements ITickableTileEntity {
    public NestTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public abstract boolean addEgg();

    public abstract boolean removeEgg();

    public abstract int getEggCount();

    public static void render(NestTileEntity tileEntityIn, Model model, ResourceLocation[] textures, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5, 1.5, 0.5);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
        RenderSystem.enableRescaleNormal();
        model.render(matrixStackIn, bufferIn.getBuffer(model.getRenderType(textures[tileEntityIn.getEggCount()])), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStackIn.pop();
    }
}
