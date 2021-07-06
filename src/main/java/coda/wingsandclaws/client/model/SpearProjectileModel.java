package coda.wingsandclaws.client.model;

import coda.wingsandclaws.entity.item.SpearProjectileEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class SpearProjectileModel extends SegmentedModel<SpearProjectileEntity> {
    public ModelRenderer tip;
    public ModelRenderer tip2;
    public ModelRenderer fabric;
    public ModelRenderer handle;
    private Iterable<ModelRenderer> parts;

    public SpearProjectileModel() {
        this.texWidth = 14;
        this.texHeight = 27;
        this.tip = new ModelRenderer(this, 4, 0);
        this.tip.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3, 0.0F);
        this.tip.setPos(0.0F, -13.0F, 0.0F);
        this.fabric = new ModelRenderer(this, 4, 12);
        this.fabric.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.fabric.setPos(0.0F, 0.0F, 0.0F);
        this.tip2 = new ModelRenderer(this, 4, 8);
        this.tip2.addBox(-0.5F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
        this.tip2.setPos(0.0F, -4.0F, 0.0F);
        this.handle = new ModelRenderer(this, 0, 0);
        this.handle.addBox(-0.5F, -13.0F, -0.5F, 1, 26, 1, 0.0F);
        this.handle.setPos(0.0F, 11.0F, 0.0F);
        this.handle.addChild(this.tip);
        this.tip.addChild(this.fabric);
        this.tip.addChild(this.tip2);
    }

    @Override
    public void setupAnim(SpearProjectileEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return parts == null ? parts = ImmutableList.of(handle) : parts;
    }
}
