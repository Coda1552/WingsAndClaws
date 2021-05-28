package coda.wingsandclaws.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import coda.wingsandclaws.entity.SugarscaleEntity;

@OnlyIn(Dist.CLIENT)
public class SugarscaleModel<T extends Entity> extends EntityModel<SugarscaleEntity> {
    public ModelRenderer body;
    public ModelRenderer tail;
    public ModelRenderer finLeft;
    public ModelRenderer finRight;
    public ModelRenderer tailFin;

    public SugarscaleModel() {
        this.textureWidth = 29;
        this.textureHeight = 16;
        this.finLeft = new ModelRenderer(this, -2, 0);
        this.finLeft.mirror = true;
        this.finLeft.setRotationPoint(2.0F, 1.5F, 0.5F);
        this.finLeft.addBox(0.0F, 0.0F, -1.0F, 3.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 22.5F, -3.0F);
        this.body.addBox(-2.0F, -1.5F, -4.5F, 4.0F, 3.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.finRight = new ModelRenderer(this, -2, 0);
        this.finRight.setRotationPoint(-2.0F, 1.5F, 0.5F);
        this.finRight.addBox(-3.0F, 0.0F, -1.0F, 3.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.tailFin = new ModelRenderer(this, 0, 7);
        this.tailFin.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.tailFin.addBox(0.0F, -2.0F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.tail = new ModelRenderer(this, 17, 0);
        this.tail.setRotationPoint(0.0F, 0.5F, 4.5F);
        this.tail.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.body.addChild(this.finLeft);
        this.body.addChild(this.finRight);
        this.tail.addChild(this.tailFin);
        this.body.addChild(this.tail);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.body).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
    public void setRotationAngles(SugarscaleEntity entityIn, float f, float f1, float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = 5.0f;
        float degree = 1.0f;
        this.body.rotateAngleY = MathHelper.cos(f * speed * 0.4F) * degree * 0.5F * f1;
        this.tail.rotateAngleY = MathHelper.cos(f * speed * 0.4F) * degree * -0.5F * f1;
        this.tailFin.rotateAngleY = MathHelper.cos(f * speed * 0.4F) * degree * 0.5F * f1;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
