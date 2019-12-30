package com.gm910.magicmod.entity.render;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.entity.classes.demons.EntityForgeDemon;
import com.gm910.magicmod.entity.models.ModelForgeDemon;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderForgeDemon extends RenderLiving<EntityForgeDemon> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(MagicMod.MODID + ":textures/entity/demon.png");
	
	public RenderForgeDemon(RenderManager manager) {
		super(manager, new ModelForgeDemon(), 0.5f);
		
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityForgeDemon entity) {
		// TODO Auto-generated method stub
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityForgeDemon entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

}
