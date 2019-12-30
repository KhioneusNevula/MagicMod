package com.gm910.magicmod.deity.entities;

import com.gm910.magicmod.MagicMod;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDeityProjection extends RenderLiving<EntityDeityProjection> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(MagicMod.MODID + ":textures/entity/goeturge.png");
	
	public RenderDeityProjection(RenderManager manager) {
		super(manager, new ModelBiped(), 0.5f);
		
	}
	
	@Override
	public void doRender(EntityDeityProjection entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.getDeity() != null) {
			if (entity.getDeity().getOverrideRender() != null) {
				entity.getDeity().getOverrideRender().setManager(this.renderManager);
				entity.getDeity().getOverrideRender().doRender(entity, x, y, z, entityYaw, partialTicks);
			}
		} else {
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityDeityProjection entity) {
		if (entity.getDeity() != null) {
			System.out.println("Deity exists");
			return TEXTURES;
		}
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityDeityProjection entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

	/*protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(2, 2, 2);
    }*/
	
}
