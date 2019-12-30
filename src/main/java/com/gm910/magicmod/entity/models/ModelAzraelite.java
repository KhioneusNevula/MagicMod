package com.gm910.magicmod.entity.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelEnderman - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelAzraelite extends ModelBase {
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedHead;
    public ModelRenderer shape8;

    public ModelAzraelite() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.bipedHeadwear = new ModelRenderer(this, 0, 16);
        this.bipedHeadwear.setRotationPoint(0.0F, -13.0F, -0.0F);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
        this.bipedBody = new ModelRenderer(this, 32, 16);
        this.bipedBody.setRotationPoint(0.0F, -14.0F, -0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, -13.0F, -0.0F);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 56, 0);
        this.bipedRightArm.setRotationPoint(-5.0F, -12.0F, 0.0F);
        this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, 0.0F);
        this.setRotateAngle(bipedRightArm, -0.0018247716361656785F, 0.0F, 0.0947020947933197F);
        this.shape8 = new ModelRenderer(this, 0, 0);
        this.shape8.setRotationPoint(-3.5F, -1.2F, -2.1F);
        this.shape8.addBox(0.0F, 0.0F, 0.0F, 7, 20, 4, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 56, 0);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(5.0F, -12.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, 0.0F);
        this.setRotateAngle(bipedLeftArm, 0.0018247716361656785F, 0.0F, -0.0947020947933197F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.bipedHeadwear.render(f5);
        this.bipedBody.render(f5);
        this.bipedHead.render(f5);
        this.bipedRightArm.render(f5);
        this.shape8.render(f5);
        this.bipedLeftArm.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
