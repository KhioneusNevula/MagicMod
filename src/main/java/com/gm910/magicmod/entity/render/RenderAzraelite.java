package com.gm910.magicmod.entity.render;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.entity.classes.demons.EntityAzraelite;
import com.gm910.magicmod.entity.models.ModelAzraelite;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAzraelite extends RenderLiving<EntityAzraelite> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(MagicMod.MODID + ":textures/entity/azraelite.png");
	
	public RenderAzraelite(RenderManager manager) {
		super(manager, new ModelAzraelite(), 0.5f);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityAzraelite entity) {
		// TODO Auto-generated method stub
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityAzraelite entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

}
