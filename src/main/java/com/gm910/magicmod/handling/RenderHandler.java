package com.gm910.magicmod.handling;

import com.gm910.magicmod.deity.entities.EntityDeity;
import com.gm910.magicmod.deity.entities.EntityDeityProjection;
import com.gm910.magicmod.deity.entities.RenderDeity;
import com.gm910.magicmod.deity.entities.RenderDeityProjection;
import com.gm910.magicmod.entity.classes.demons.EntityAzraelite;
import com.gm910.magicmod.entity.classes.demons.EntityCacodemon;
import com.gm910.magicmod.entity.classes.demons.EntityForgeDemon;
import com.gm910.magicmod.entity.classes.demons.EntityGatekeeper;
import com.gm910.magicmod.entity.classes.demons.EntityGoeturge;
import com.gm910.magicmod.entity.classes.demons.EntityImp;
import com.gm910.magicmod.entity.classes.magical.EntityEye;
import com.gm910.magicmod.entity.classes.magical.EntityHellPortal;
import com.gm910.magicmod.entity.render.RenderAzraelite;
import com.gm910.magicmod.entity.render.RenderCacodemon;
import com.gm910.magicmod.entity.render.RenderEye;
import com.gm910.magicmod.entity.render.RenderForgeDemon;
import com.gm910.magicmod.entity.render.RenderGatekeeper;
import com.gm910.magicmod.entity.render.RenderGoeturge;
import com.gm910.magicmod.entity.render.RenderHellPortal;
import com.gm910.magicmod.entity.render.RenderImp;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class RenderHandler {
	
	@SideOnly(Side.CLIENT)
	public static void registerEntRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityForgeDemon.class, new IRenderFactory<EntityForgeDemon>() {
			
			@Override
			public Render<? super EntityForgeDemon> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderForgeDemon(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, new IRenderFactory<EntityImp>() {
			
			@Override
			public Render<? super EntityImp> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderImp(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityCacodemon.class, new IRenderFactory<EntityCacodemon>() {
			
			@Override
			public Render<? super EntityCacodemon> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderCacodemon(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityGoeturge.class, new IRenderFactory<EntityGoeturge>() {
			
			@Override
			public Render<? super EntityGoeturge> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderGoeturge(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityAzraelite.class, new IRenderFactory<EntityAzraelite>() {
			
			@Override
			public Render<? super EntityAzraelite> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderAzraelite(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityGatekeeper.class, new IRenderFactory<EntityGatekeeper>() {
			
			@Override
			public Render<? super EntityGatekeeper> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderGatekeeper(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityHellPortal.class, new IRenderFactory<EntityHellPortal> () {
			@Override
			public Render<? super EntityHellPortal> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderHellPortal(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDeity.class, new IRenderFactory<EntityDeity> () {
			@Override
			public Render<? super EntityDeity> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderDeity(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDeityProjection.class, new IRenderFactory<EntityDeityProjection> () {
			@Override
			public Render<? super EntityDeityProjection> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderDeityProjection(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityEye.class, new IRenderFactory<EntityEye> () {
			@Override
			public Render<? super EntityEye> createRenderFor(RenderManager manager) {
				// TODO Auto-generated method stub
				return new RenderEye(manager);
			}
		});
		
	}

}
