package com.gm910.magicmod.capabilities.souls;

import java.util.ArrayList;
import java.util.function.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public interface ISouls {
	public static int MAX_SOULS = 50;
	
	public void addSoul(EntityLivingBase b, WorldServer world);
	public void addSoul(EntityLivingBase b);
	public void addSoul(NBTTagCompound tags);
	public <T extends EntityLivingBase> ArrayList<T> getSoulsByClass(Class<? extends T> clazz, WorldServer world);
	public ArrayList<EntityLivingBase> getSoulsByPredicate(WorldServer world, Predicate<? super EntityLivingBase> pred);
	public ArrayList<NBTTagCompound> getSoulsByPredicate(Predicate<? super NBTTagCompound> pred);
	public ArrayList<NBTTagCompound> getSoulTagsByPredicate(WorldServer world, Predicate<? super EntityLivingBase> pred);
	public void removeSoul(WorldServer world, EntityLivingBase b);
	public void removeSoul(NBTTagCompound uuid);
	public ArrayList<NBTTagCompound> getSouls();
	public ArrayList<EntityLivingBase> getSouls(WorldServer world);
	public ArrayList<NBTTagCompound> getTagList();
}
