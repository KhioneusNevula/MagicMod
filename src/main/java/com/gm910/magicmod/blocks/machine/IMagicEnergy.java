package com.gm910.magicmod.blocks.machine;

public interface IMagicEnergy {

	public int receiveEnergy(int en);
	public int extractEnergy(int en);
	public int getEnergyStored();
	public int getMaxEnergyStored();
	
}
