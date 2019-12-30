package com.gm910.magicmod.entity.render;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.entity.classes.demons.EntityImp;
import com.gm910.magicmod.entity.models.ModelImp;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderImp extends RenderLiving<EntityImp> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(MagicMod.MODID + ":textures/entity/imp.png");
	
	public RenderImp(RenderManager manager) {
		super(manager, new ModelImp(), 0.5f);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityImp entity) {
		// TODO Auto-generated method stub
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityImp entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

}
