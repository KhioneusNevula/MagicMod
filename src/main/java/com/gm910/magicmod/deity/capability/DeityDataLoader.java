package com.gm910.magicmod.deity.capability;

import com.gm910.magicmod.deity.Deities;

import net.minecraft.nbt.NBTTagCompound;

public class DeityDataLoader implements IDeityData {

	public boolean worldFirstLoad = false;
	
	@Override
	public NBTTagCompound getData() {
		
		NBTTagCompound comp = Deities.writeToNBT(new NBTTagCompound());
		System.out.println("Saved deity data");
		return comp;
	}

	@Override
	public void setData(NBTTagCompound comp) {
		Deities.readFromNBT(comp);
		System.out.println("Loaded deity data");
		
	}

	@Override
	public boolean afterWorldFirstLoad() {
		
		return worldFirstLoad;
	}

	@Override
	public boolean setWorldFirstLoad(boolean b) {
		worldFirstLoad = b;
		return b;
	}

	

}
