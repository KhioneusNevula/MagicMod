package com.gm910.magicmod.deity.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class DeityDataStorage implements IStorage<IDeityData> {

	@Override
	public NBTBase writeNBT(Capability<IDeityData> capability, IDeityData instance, EnumFacing side) {
		NBTTagCompound c = new NBTTagCompound();
		c.setTag("Data", instance.getData());
		c.setBoolean("WorldLoaded", instance.afterWorldFirstLoad());
		System.out.println("" + instance.afterWorldFirstLoad());
		return c;
	}

	@Override
	public void readNBT(Capability<IDeityData> capability, IDeityData instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound)nbt;
		instance.setData(tag.getCompoundTag("Data"));
		instance.setWorldFirstLoad(tag.getBoolean("WorldLoaded"));
		System.out.println(""+instance.afterWorldFirstLoad());
	}

}
