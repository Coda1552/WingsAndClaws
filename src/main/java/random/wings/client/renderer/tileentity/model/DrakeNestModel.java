package random.wings.client.renderer.tileentity.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class DrakeNestModel extends Model {
    public RendererModel bottom;
    public RendererModel top;
    public RendererModel egg1;
    public RendererModel egg2;
    public RendererModel egg3;

    public DrakeNestModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.egg1 = new RendererModel(this, 42, 0);
        this.egg1.setRotationPoint(-2.0F, 21.0F, 1.5F);
        this.egg1.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(egg1, -0.1884955592153876F, 0.1884955592153876F, -0.3141592653589793F);
        this.bottom = new RendererModel(this, 0, 0);
        this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bottom.addBox(-7.0F, -2.0F, -7.0F, 14, 2, 14, 0.0F);
        this.egg2 = new RendererModel(this, 30, 16);
        this.egg2.setRotationPoint(0.0F, 21.0F, 0.5F);
        this.egg2.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(egg2, -0.439822971502571F, 0.9424777960769379F, 0.25132741228718347F);
        this.top = new RendererModel(this, 0, 16);
        this.top.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.top.addBox(-5.0F, -2.0F, -5.0F, 10, 2, 10, 0.0F);
        this.egg3 = new RendererModel(this, 46, 16);
        this.egg3.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.egg3.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(egg3, 0.7740535232594852F, 0.18675022996339324F, 0.0F);
    }

    public void render() {
        this.egg1.render(0.0625f);
        this.bottom.render(0.0625f);
        this.egg2.render(0.0625f);
        this.top.render(0.0625f);
        this.egg3.render(0.0625f);
    }

    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
