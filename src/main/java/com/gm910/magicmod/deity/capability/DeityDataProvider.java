package com.gm910.magicmod.deity.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class DeityDataProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IDeityData.class)
	public static final Capability<IDeityData> DEITY_CAP = null;
	
	private IDeityData instance = DEITY_CAP.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == DEITY_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == DEITY_CAP ? DEITY_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		// TODO Auto-generated method stub
		return DEITY_CAP.getStorage().writeNBT(DEITY_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		// TODO Auto-generated method stub
		DEITY_CAP.getStorage().readNBT(DEITY_CAP, this.instance, null, nbt);
	}
	
}
