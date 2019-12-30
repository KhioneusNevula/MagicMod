package com.gm910.magicmod.entity.models;

import com.gm910.magicmod.entity.classes.demons.EntityDemon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelEnderman - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelCacodemon extends ModelBase {
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedHead;

    public ModelCacodemon() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, -13.0F, -0.0F);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 56, 0);
        this.bipedRightArm.setRotationPoint(-5.0F, -12.0F, 0.0F);
        this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, 0.0F);
        this.setRotateAngle(bipedRightArm, 0.0F, 0.0F, 0.10000000149011613F);
        this.bipedLeftLeg = new ModelRenderer(this, 56, 0);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.setRotationPoint(2.0F, -5.0F, 0.0F);
        this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 0, 16);
        this.bipedHeadwear.setRotationPoint(0.0F, -13.0F, -0.0F);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
        this.bipedRightLeg = new ModelRenderer(this, 56, 0);
        this.bipedRightLeg.setRotationPoint(-2.0F, -5.0F, 0.0F);
        this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 56, 0);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(5.0F, -12.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, 0.0F);
        this.setRotateAngle(bipedLeftArm, 0.0F, 0.0F, -0.10000000149011613F);
        this.bipedBody = new ModelRenderer(this, 32, 16);
        this.bipedBody.setRotationPoint(0.0F, -14.0F, -0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
    	if (((EntityDemon)entity).getItem() != null) return;
        this.bipedHead.render(f5);
        this.bipedRightArm.render(f5);
        this.bipedLeftLeg.render(f5);
        this.bipedHeadwear.render(f5);
        this.bipedRightLeg.render(f5);
        this.bipedLeftArm.render(f5);
        this.bipedBody.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
    		float headPitch, float scaleFactor, Entity entityIn) {
    	this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
        this.bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
        this.bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
        this.bipedLeftArm.rotateAngleX = -1.3F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedRightArm.rotateAngleX = 1.6F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedLeftArm.rotateAngleY = -1.3F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedRightArm.rotateAngleY = 1.2F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedLeftArm.rotateAngleZ = -1.7F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedRightArm.rotateAngleZ = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleZ = -1.2F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedRightLeg.rotateAngleZ = 1.4F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleX = -1.9F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedRightLeg.rotateAngleX = 1.0F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleY = -1.8F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.bipedRightLeg.rotateAngleY = -1.4F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        }
    
    
    
    private float triangleWave(float p_78172_1_, float p_78172_2_)
    {
        return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
    }
}
