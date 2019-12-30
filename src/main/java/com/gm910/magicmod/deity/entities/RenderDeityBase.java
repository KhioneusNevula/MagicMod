package com.gm910.magicmod.deity.entities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderDeityBase extends RenderLiving<EntityLiving> {

	public static final ResourceLocation TEXTURES = Minecraft.getMinecraft().player.getLocationSkin();
	
	
	public RenderDeityBase(RenderManager manager) {
		super(manager, new ModelBiped(), 0.5f);

	}
	
	public void setManager(RenderManager manager) {
		Field f = null;
		try {
			f = this.getClass().getSuperclass().getSuperclass().getDeclaredField("renderManager");
		} catch (NoSuchFieldException e1) {

			e1.printStackTrace();
		} catch (SecurityException e1) {

			e1.printStackTrace();
		}
		if (f != null) {
			f.setAccessible(true);
			try {
				f.setInt(this, f.getModifiers() & Modifier.FINAL);
			} catch (IllegalArgumentException e1) {

				e1.printStackTrace();
			} catch (IllegalAccessException e1) {

				e1.printStackTrace();
			}
			try {
				f.set(this, manager);
			} catch (IllegalArgumentException e) {

				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		if (entity instanceof EntityDeity) {
			if (((EntityDeity) entity).deity != null) {
				return ((EntityDeity) entity).deity.getTextureOverride();
			}
		} else if (entity instanceof EntityDeityProjection) {
			if (((EntityDeityProjection) entity).getDeity() != null) {
				return ((EntityDeityProjection) entity).getDeity().getTextureOverride();
			}
		}
		return TEXTURES;
	}
	
	@Override
	public void doRender(EntityLiving entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected void applyRotations(EntityLiving entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		// TODO Auto-generated method stub
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

	/*protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(2, 2, 2);
    }*/
	
}
