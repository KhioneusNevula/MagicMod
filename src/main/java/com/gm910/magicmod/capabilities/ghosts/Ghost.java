package com.gm910.magicmod.capabilities.ghosts;

import java.util.List;
import java.util.UUID;

import com.gm910.magicmod.capabilities.souls.ISouls;
import com.gm910.magicmod.capabilities.souls.SoulProvider;
import com.gm910.magicmod.init.ItemInit;
import com.gm910.magicmod.items.ItemSoul;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class Ghost implements IGhost {

	private boolean isGhost = false;
	private EntityLivingBase entity = null;
	private boolean isPlayer = false;
	private UUID killer = null;
	
	@Override
	public IGhost setGhost(boolean isGhost) {
		this.isGhost = isGhost;
		return this;
	}

	@Override
	public boolean isGhost() {
		
		return isGhost;
	}

	@Override
	public EntityLivingBase getEntity() {
		
		return entity;
	}

	@Override
	public IGhost setEntity(EntityLivingBase entity) {
		this.entity = entity;
		return this;
	}

	@SubscribeEvent
	@Override
	public void onTick(LivingUpdateEvent event) {
		if (!event.getEntityLiving().getCapability(GhostProvider.GHOST_CAP, null).isGhost()) return;
		if (!event.getEntityLiving().isGlowing()) event.getEntityLiving().setGlowing(true);
		if (!event.getEntityLiving().isInvisible()) {
			event.getEntityLiving().setInvisible(true);
		}
		if (killer != null) {
			EntityPlayer play = event.getEntity().world.getMinecraftServer().getPlayerList().getPlayerByUUID(killer);
			if (play != null) {
				event.getEntityLiving().setRevengeTarget(play);
				
				if (event.getEntityLiving() instanceof EntityLiving) {
					((EntityLiving)event.getEntityLiving()).setAttackTarget(play);
				}
				if (event.getEntityLiving().getDistanceSq(play) > 100) {
					event.getEntityLiving().setPosition(play.posX + (play.getRNG().nextBoolean() ? 1 : -1)*play.getRNG().nextInt(6), play.posY + play.getRNG().nextInt(6), play.posZ + (play.getRNG().nextBoolean() ? 1 : -1)*play.getRNG().nextInt(6));
				}
			}
		}
		
	}

	@SubscribeEvent
	@Override
	public void onDeath(LivingDeathEvent event) {
		if (!event.getEntityLiving().getCapability(GhostProvider.GHOST_CAP, null).isGhost()) return;
		event.getEntityLiving().deathTime = 0;
		if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) {
			event.setCanceled(true);
			event.getEntityLiving().setHealth(2);
			return;
		}
		ISouls cap = event.getSource().getTrueSource().getCapability(SoulProvider.SOUL_CAP, null);
		List<NBTTagCompound> l = cap.getSouls();
		boolean dead = false;
		for (NBTTagCompound comp : l) {
			if (comp.getUniqueId("UUID").equals(event.getEntityLiving().getPersistentID())) {
				dead = true;
				cap.removeSoul(comp);
			}
		}
		if (!dead) {
			event.getEntityLiving().setHealth(2);
			event.setCanceled(true);
		}
		
		if (!event.isCanceled() && event.getEntityLiving() instanceof EntityLiving) {
			((EntityLiving) event.getEntityLiving()).spawnExplosionParticle();
		}
	}

	@Override
	public boolean isPlayer() {
		
		return isPlayer;
	}

	@Override
	public void onDrops(LivingDropsEvent event) {
		if (!event.getEntityLiving().getCapability(GhostProvider.GHOST_CAP, null).isGhost()) return;
		event.getDrops().clear();
		
		event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, ItemSoul.setSoul(event.getEntityLiving(), new ItemStack(ItemInit.SOUL, 1))));
		
	}
	
	@Override
	public void uniqueID(UUID uuid) {
		if (FMLCommonHandler.instance() == null) return;
		if (FMLCommonHandler.instance().getMinecraftServerInstance() == null) {
			System.out.println("No server");
			return;
		}
		entity = (EntityLivingBase) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(uuid);
	}

	@Override
	public void onHurt(LivingAttackEvent event) {
		if (!event.getEntityLiving().getCapability(GhostProvider.GHOST_CAP, null).isGhost()) return;
		
	}

	@Override
	public IGhost setKiller(UUID killer) {
		this.killer = killer;
		return this;
	}

	@Override
	public UUID getKiller() {
		// TODO Auto-generated method stub
		return killer;
	}

}
