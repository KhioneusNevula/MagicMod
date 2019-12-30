package com.gm910.magicmod.entity.models;

import java.util.Random;

import com.gm910.magicmod.entity.classes.demons.EntityDemon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

/**
 * ModelVex - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelImp extends ModelBase {
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedBody;
    public ModelRenderer leftWing;
    public ModelRenderer bipedHead;
    public ModelRenderer rightWing;

    public ModelImp() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.setRotationPoint(-1.8F, 12.0F, 0.1F);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(bipedRightArm, 0.0F, 0.0F, 0.10000000149011613F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(bipedLeftArm, 0.0F, 0.0F, -0.10000000149011613F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.leftWing = new ModelRenderer(this, 0, 32);
        this.leftWing.mirror = true;
        this.leftWing.setRotationPoint(0.0F, 1.0F, 2.0F);
        this.leftWing.addBox(0.0F, 0.0F, 0.0F, 20, 12, 1, 0.0F);
        this.setRotateAngle(leftWing, 0.47123894095420843F, -0.6283185482025146F, -0.47123894095420843F);
        this.rightWing = new ModelRenderer(this, 0, 32);
        this.rightWing.setRotationPoint(0.0F, 1.0F, 2.0F);
        this.rightWing.addBox(-20.0F, 0.0F, 0.0F, 20, 12, 1, 0.0F);
        this.setRotateAngle(rightWing, 0.47123894095420843F, 0.6283185482025146F, 0.47123894095420843F);
        this.bipedRightLeg = new ModelRenderer(this, 32, 0);
        this.bipedRightLeg.setRotationPoint(0.6F, 12.2F, -0.5F);
        this.bipedRightLeg.addBox(-1.0F, -1.0F, -2.0F, 4, 10, 5, 0.0F);
        this.setRotateAngle(bipedRightLeg, -0.015707963267948967F, 0.0F, -0.03665191429188092F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
    	if (((EntityDemon)entity).getItem() != null) return;
        this.bipedLeftLeg.render(f5);
        this.bipedBody.render(f5);
        this.bipedHeadwear.render(f5);
        this.bipedRightArm.render(f5);
        this.bipedLeftArm.render(f5);
        this.bipedHead.render(f5);
        this.leftWing.render(f5);
        this.rightWing.render(f5);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.bipedRightLeg.offsetX, this.bipedRightLeg.offsetY, this.bipedRightLeg.offsetZ);
        GlStateManager.translate(this.bipedRightLeg.rotationPointX * f5, this.bipedRightLeg.rotationPointY * f5, this.bipedRightLeg.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 1.3D, 1.0D);
        GlStateManager.translate(-this.bipedRightLeg.offsetX, -this.bipedRightLeg.offsetY, -this.bipedRightLeg.offsetZ);
        GlStateManager.translate(-this.bipedRightLeg.rotationPointX * f5, -this.bipedRightLeg.rotationPointY * f5, -this.bipedRightLeg.rotationPointZ * f5);
        this.bipedRightLeg.render(f5);
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
    		float headPitch, float scaleFactor, Entity entityIn) {
    	this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
        this.bipedHeadwear.rotateAngleX = (new Random()).nextFloat()*2-1;
        this.bipedHeadwear.rotateAngleY = (new Random()).nextFloat()*2-1;
        this.bipedHeadwear.rotateAngleZ = (new Random()).nextFloat()*2-1;
        this.bipedLeftArm.rotateAngleX = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedRightArm.rotateAngleX = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        this.bipedRightArm.rotateAngleY = 0.0F;
        }
    
    
    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    private float triangleWave(float p_78172_1_, float p_78172_2_)
    {
        return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
    }
}
