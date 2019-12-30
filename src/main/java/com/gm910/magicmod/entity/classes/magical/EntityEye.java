package com.gm910.magicmod.entity.classes.magical;

import com.gm910.magicmod.entity.classes.demons.IDemonWithOwner;
import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@EventBusSubscriber
public class EntityEye extends Entity implements IDemonWithOwner {
	
	private EntityPlayer owner;
	

	public EntityEye(World worldIn) {
		super((WorldServer) worldIn);
	}
	
	public void setGameProfile(EntityPlayer player) {
		
	}
	
	@SubscribeEvent
	public static void event(PlayerTickEvent event) {
		
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		
		if (compound.hasKey("Owner")) {
			this.owner = (EntityPlayer) CommonProxy.getServer().getEntityFromUuid(compound.getUniqueId("Owner"));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		
		if (owner != null) {
			compound.setUniqueId("Owner", owner.getUniqueID());
		}
	}

	@Override
	public EntityLivingBase getOwner() {
		
		return owner;
	}

	@Override
	public void setOwner(EntityLivingBase owner) {
		this.owner = (EntityPlayer)owner;
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub
		
	}

}
