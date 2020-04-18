package random.wings.client.renderer.tileentity.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class HBNestModel extends Model {
    public RendererModel bottom;
    public RendererModel top;
    public RendererModel egg1;

    public HBNestModel() {
        this.textureWidth = 128;
        this.textureHeight = 32;
        this.top = new RendererModel(this, 48, 0);
        this.top.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.top.addBox(-6.0F, -2.0F, -6.0F, 12, 2, 12, 0.0F);
        this.egg1 = new RendererModel(this, 96, 0);
        this.egg1.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.egg1.addBox(-3.5F, -12.0F, -3.5F, 7, 12, 7, 0.0F);
        this.setRotateAngle(egg1, 0.08726646259971647F, 0.08726646259971647F, 0.08726646259971647F);
        this.bottom = new RendererModel(this, 0, 0);
        this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bottom.addBox(-8.0F, -4.0F, -8.0F, 16, 4, 16, 0.0F);
    }

    public void render() {
        float f = 0.0625f;
        this.top.render(f);
        this.egg1.render(f);
        this.bottom.render(f);
    }

    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
