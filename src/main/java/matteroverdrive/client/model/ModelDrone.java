package matteroverdrive.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by Simeon on 1/22/2016.
 */
public class ModelDrone extends ModelBase
{
    ModelRenderer Main_head;
    ModelRenderer FlapUpper;
    ModelRenderer FlapBottom;
    ModelRenderer FlapLeft;
    ModelRenderer FlapRight;
    ModelRenderer ExhaustLeft;
    ModelRenderer ExhaustRight;
    ModelRenderer EYE;

    public ModelDrone()
    {
        textureWidth = 64;
        textureHeight = 32;

        Main_head = new ModelRenderer(this, 0, 0);
        Main_head.addBox(-3F, -3F, -3F, 6, 5, 6);
        Main_head.setRotationPoint(0F, 16F, 0F);
        Main_head.setTextureSize(64, 32);
        Main_head.mirror = true;
        setRotation(Main_head, 0F, 0F, 0F);
        FlapUpper = new ModelRenderer(this, 0, 19);
        FlapUpper.addBox(-1F, -3F, -1F, 2, 4, 1);
        FlapUpper.setRotationPoint(0F, -3F, -3F);
        FlapUpper.setTextureSize(64, 32);
        FlapUpper.mirror = true;
        setRotation(FlapUpper, -0.7853982F, 0F, 0F);
        Main_head.addChild(FlapUpper);
        FlapBottom = new ModelRenderer(this, 7, 19);
        FlapBottom.addBox(-1F, -1F, -1F, 2, 4, 1);
        FlapBottom.setRotationPoint(0F, 2F, -3F);
        FlapBottom.setTextureSize(64, 32);
        FlapBottom.mirror = true;
        setRotation(FlapBottom, 0.7853982F, 0F, 0F);
        Main_head.addChild(FlapBottom);
        FlapLeft = new ModelRenderer(this, 0, 13);
        FlapLeft.addBox(-1F, -1F, -1F, 2, 4, 1);
        FlapLeft.setRotationPoint(3F, 0F, -3F);
        FlapLeft.setTextureSize(64, 32);
        FlapLeft.mirror = true;
        setRotation(FlapLeft, 0.7853982F, 0F, -1.570796F);
        Main_head.addChild(FlapLeft);
        FlapRight = new ModelRenderer(this, 7, 13);
        FlapRight.addBox(-1F, -3F, -1F, 2, 4, 1);
        FlapRight.setRotationPoint(-3F, 0F, -3F);
        FlapRight.setTextureSize(64, 32);
        FlapRight.mirror = true;
        setRotation(FlapRight, -0.7853982F, 0F, -1.570796F);
        Main_head.addChild(FlapRight);
        ExhaustLeft = new ModelRenderer(this, 26, 0);
        ExhaustLeft.addBox(-1F, -1F, 0F, 2, 3, 3);
        ExhaustLeft.setRotationPoint(3F, 0F, 2F);
        ExhaustLeft.setTextureSize(64, 32);
        ExhaustLeft.mirror = true;
        setRotation(ExhaustLeft, -0.6108652F, 0F, 0F);
        Main_head.addChild(ExhaustLeft);
        ExhaustRight = new ModelRenderer(this, 26, 7);
        ExhaustRight.addBox(-1F, -1F, 0F, 2, 3, 3);
        ExhaustRight.setRotationPoint(-3F, 0F, 2f);
        ExhaustRight.setTextureSize(64, 32);
        ExhaustRight.mirror = true;
        setRotation(ExhaustRight, -0.6108652F, 0F, 0F);
        Main_head.addChild(ExhaustRight);
        EYE = new ModelRenderer(this, 14, 13);
        EYE.addBox(-1F, -1F, -0.5F, 2, 2, 1);
        EYE.setRotationPoint(0F, 0F, -3F);
        EYE.setTextureSize(64, 32);
        EYE.mirror = true;
        setRotation(EYE, 0F, 0F, 0F);
        Main_head.addChild(EYE);
    }

    @Override
    public void render(Entity entityIn, float f, float f1, float f2, float f3, float f4, float scale)
    {
        super.render(entityIn, f, f1, f2, f3, f4, scale);
        setRotationAngles(f, f1, f2, f3, f4, scale,entityIn);
        Main_head.render(scale);
        //FlapUpper.render(scale);
        //FlapBottom.render(scale);
        //FlapLeft.render(scale);
        //FlapRight.render(scale);
        //ExhaustLeft.render(scale);
        //ExhaustRight.render(scale);
        //EYE.render(scale);
        //overlay.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotationTimer, float yawHead, float pitch, float f5,Entity entity)
    {
        this.Main_head.rotateAngleX = pitch / (180F / (float)Math.PI);
        this.Main_head.rotateAngleY = yawHead / (180F / (float)Math.PI);
        float flapSwitng = limbSwingAmount * 0.6f;
        setRotation(FlapUpper, -0.7853982F - flapSwitng, 0F, 0F);
        setRotation(FlapBottom, 0.7853982F + flapSwitng, 0F, 0F);
        setRotation(FlapLeft, 0.7853982F + flapSwitng, 0F, -1.570796F);
        setRotation(FlapRight, -0.7853982F - flapSwitng, 0F, -1.570796F);

        float exaustWing = limbSwingAmount;
        setRotation(ExhaustLeft, -1 + exaustWing - this.Main_head.rotateAngleX, 0F, 0F);
        setRotation(ExhaustRight, -1 + exaustWing - this.Main_head.rotateAngleX, 0F, 0F);
        //this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
