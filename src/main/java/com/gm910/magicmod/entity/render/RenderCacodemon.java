package com.gm910.magicmod.entity.render;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.entity.classes.demons.EntityCacodemon;
import com.gm910.magicmod.entity.models.ModelCacodemon;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCacodemon extends RenderLiving<EntityCacodemon> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(MagicMod.MODID + ":textures/entity/cacodemon.png");
	
	public RenderCacodemon(RenderManager manager) {
		super(manager, new ModelCacodemon(), 0.5f);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityCacodemon entity) {
		// TODO Auto-generated method stub
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityCacodemon entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

}
