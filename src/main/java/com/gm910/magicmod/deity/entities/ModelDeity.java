package com.gm910.magicmod.deity.entities;

import java.util.Random;

import com.gm910.magicmod.entity.classes.demons.EntityDemon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDeity extends ModelBase {
    public ModelRenderer demonrightarm;
    public ModelRenderer demonleftleg;
    public ModelRenderer demonstomach;
    public ModelRenderer demonleftarm;
    public ModelRenderer demonrightleg;
    public ModelRenderer demonhead;
    private int times;

    public ModelDeity() {
    	times = 0;
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.demonleftleg = new ModelRenderer(this, 37, 0);
        this.demonleftleg.setRotationPoint(-2.0F, 1.0F, 8.0F);
        this.demonleftleg.addBox(-7.6F, -3.0F, -3.0F, 4, 16, 5, 0.0F);
        this.demonrightleg = new ModelRenderer(this, 60, 0);
        this.demonrightleg.mirror = true;
        this.demonrightleg.setRotationPoint(4.9F, 1.0F, -5.0F);
        this.demonrightleg.addBox(0.7F, -3.0F, -3.0F, 4, 16, 5, 0.0F);
        this.setRotateAngle(demonrightleg, 0.0F, 1.3962634015954636F, 0.0F);
        this.demonrightarm = new ModelRenderer(this, 60, 21);
        this.demonrightarm.setRotationPoint(2.0F, -7.0F, 0.0F);
        this.demonrightarm.addBox(-13.0F, -2.5F, -11.7F, 4, 20, 6, 0.0F);
        this.setRotateAngle(demonrightarm, 0.0F, 0.7976154681614086F, 0.0F);
        this.demonhead = new ModelRenderer(this, 0, 0);
        this.demonhead.setRotationPoint(0.0F, -7.0F, 1.0F);
        this.demonhead.addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, 0.0F);
        this.demonleftarm = new ModelRenderer(this, 60, 58);
        this.demonleftarm.setRotationPoint(-2.0F, -7.0F, 0.0F);
        this.demonleftarm.addBox(9.0F, -2.5F, 5.7F, 4, 20, 6, 0.0F);
        this.setRotateAngle(demonleftarm, 0.0F, 0.7958701389094143F, 0.0F);
        this.demonstomach = new ModelRenderer(this, 0, 70);
        this.demonstomach.setRotationPoint(0.0F, -17.9F, 0.0F);
        this.demonstomach.addBox(-4.5F, 10.0F, -3.0F, 9, 19, 6, 0.5F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.demonleftleg.render(f5);
        this.demonrightleg.render(f5);
        this.demonrightarm.render(f5);
        this.demonleftarm.render(f5);
        this.demonstomach.render(f5);
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
    		float headPitch, float scaleFactor, Entity entityIn) {
    	// TODO Auto-generated method stub
    	this.demonhead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.demonhead.rotateAngleX = headPitch * 0.017453292F;
        this.demonleftleg.rotateAngleX = -1.5F  * limbSwingAmount;
        this.demonrightleg.rotateAngleX = 1.5F  * limbSwingAmount;
        this.demonleftleg.rotateAngleY = 0.0F;
        this.demonrightleg.rotateAngleY = 0.0F;
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
