package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.item.SpearProjectileEntity;


@OnlyIn(Dist.CLIENT)
public class SpearProjectileModel extends SegmentedModel<SpearProjectileEntity> {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(WingsAndClaws.MOD_ID,"textures/entity/st_spear/st_projectile_entity.png");
    private final ModelRenderer modelRenderer = new ModelRenderer(14,27,0,0);
    ModelRenderer tip;
    ModelRenderer tip2;
    ModelRenderer fabric;
    ModelRenderer handle;


    public SpearProjectileModel() {

        this.textureWidth = 14;
        this.textureHeight = 27;

        tip = new ModelRenderer(this, 4, 0);
        tip.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3, 0.0F);
        tip.setRotationPoint(0.0F, -13.0F, 0.0F);

        fabric = new ModelRenderer(this, 4, 12);
        fabric.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        fabric.setRotationPoint(0.0F, 0.0F, 0.0F);

        tip2 = new ModelRenderer(this, 4, 8);
        tip2.addBox(-0.5F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
        tip2.setRotationPoint(0.0F, -4.0F, 0.0F);

        handle = new ModelRenderer(this, 0, 0);
        handle.addBox(-0.5F, -13.0F, -0.5F, 1, 26, 1, 0.0F);
        handle.setRotationPoint(0.0F, 11.0F, 0.0F);

        this.handle.addChild(this.tip);
        this.tip.addChild(this.fabric);
        this.tip.addChild(this.tip2);

    }

    @Override
    public void setRotationAngles(SpearProjectileEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }


    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(handle);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
