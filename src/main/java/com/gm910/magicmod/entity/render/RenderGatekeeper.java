package com.gm910.magicmod.entity.render;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.entity.classes.demons.EntityGatekeeper;
import com.gm910.magicmod.entity.models.ModelGatekeeper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.ResourceLocation;

public class RenderGatekeeper extends RenderLiving<EntityGatekeeper> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(MagicMod.MODID + ":textures/entity/goeturge.png");
	
	public RenderGatekeeper(RenderManager manager) {
		super(manager, new ModelGatekeeper(), 0.5f);
		
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityGatekeeper entity) {
		// TODO Auto-generated method stub
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityGatekeeper entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

	/*protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(2, 2, 2);
    }*/
	
}
