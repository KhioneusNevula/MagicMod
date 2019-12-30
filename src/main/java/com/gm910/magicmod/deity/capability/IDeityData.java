package com.gm910.magicmod.deity.capability;

import java.util.ArrayList;
import java.util.function.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public interface IDeityData {
	
	public NBTTagCompound getData();
	public void setData(NBTTagCompound comp);
	public boolean afterWorldFirstLoad();
	public boolean setWorldFirstLoad(boolean b);
}
