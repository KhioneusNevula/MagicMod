package com.gm910.magicmod.deity.entities;

import com.gm910.magicmod.MagicMod;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDeity extends RenderLiving<EntityDeity> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(MagicMod.MODID + ":textures/entity/goeturge.png");
	
	public RenderDeity(RenderManager manager) {
		super(manager, new ModelBiped(), 0.5f);
		
	}
	
	@Override
	public void doRender(EntityDeity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.deity != null) {
			
			if (entity.deity.getOverrideRender() != null) {
				entity.deity.getOverrideRender().setManager(this.renderManager);
				entity.deity.getOverrideRender().doRender(entity, x, y, z, entityYaw, partialTicks);
			}
		} else {
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityDeity entity) {
		if (entity.deity != null) {
			System.out.println("Deity exists");
			return entity.deity.getTextureOverride();
		}
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityDeity entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

	/*protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(2, 2, 2);
    }*/
	
}
