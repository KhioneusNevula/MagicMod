package com.gm910.magicmod.entity.models;

import com.gm910.magicmod.entity.classes.demons.EntityDemon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelIronGolem - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelForgeDemon extends ModelBase {
    public ModelRenderer demonleftarm;
    public ModelRenderer demonrightleg;
    public ModelRenderer demonrightarm;
    public ModelRenderer demonleftleg;
    public ModelRenderer demonchest;
    public ModelRenderer demonstomach;
    public ModelRenderer demonhead;

    public ModelForgeDemon() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.demonstomach = new ModelRenderer(this, 0, 70);
        this.demonstomach.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.demonstomach.addBox(-4.5F, 10.0F, -3.0F, 9, 9, 6, 0.5F);
        this.demonleftarm = new ModelRenderer(this, 60, 58);
        this.demonleftarm.setRotationPoint(-2.0F, -7.0F, 0.0F);
        this.demonleftarm.addBox(9.0F, -2.5F, -3.0F, 4, 20, 6, 0.0F);
        this.demonchest = new ModelRenderer(this, 0, 40);
        this.demonchest.setRotationPoint(2.0F, -7.0F, 0.5F);
        this.demonchest.addBox(-9.0F, -2.0F, -6.0F, 14, 9, 9, 0.0F);
        this.demonhead = new ModelRenderer(this, 0, 0);
        this.demonhead.setRotationPoint(0.0F, -7.0F, 1.0F);
        this.demonhead.addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, 0.0F);
        this.demonleftleg = new ModelRenderer(this, 37, 0);
        this.demonleftleg.setRotationPoint(-2.0F, 11.0F, 0.0F);
        this.demonleftleg.addBox(-3.5F, -3.0F, -3.0F, 4, 16, 5, 0.0F);
        this.demonrightleg = new ModelRenderer(this, 60, 0);
        this.demonrightleg.mirror = true;
        this.demonrightleg.setRotationPoint(4.8F, 11.0F, 0.0F);
        this.demonrightleg.addBox(-3.5F, -3.0F, -3.0F, 4, 16, 5, 0.0F);
        this.demonrightarm = new ModelRenderer(this, 60, 21);
        this.demonrightarm.setRotationPoint(2.0F, -7.0F, 0.0F);
        this.demonrightarm.addBox(-13.0F, -2.5F, -3.0F, 4, 20, 6, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	if (((EntityDemon)entity).getItem() != null) return;
        this.demonstomach.render(f5);
        this.demonleftarm.render(f5);
        this.demonchest.render(f5);
        this.demonhead.render(f5);
        this.demonleftleg.render(f5);
        this.demonrightleg.render(f5);
        this.demonrightarm.render(f5);
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
    	this.demonhead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.demonhead.rotateAngleX = headPitch * 0.017453292F;
        this.demonleftleg.rotateAngleX = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.demonrightleg.rotateAngleX = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.demonleftleg.rotateAngleY = 0.0F;
        this.demonrightleg.rotateAngleY = 0.0F;
        }
    
    private float triangleWave(float p_78172_1_, float p_78172_2_)
    {
        return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
    }
}
