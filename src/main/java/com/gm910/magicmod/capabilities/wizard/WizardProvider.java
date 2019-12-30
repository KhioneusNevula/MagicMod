package com.gm910.magicmod.capabilities.wizard;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class WizardProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IWizard.class)
	public static final Capability<IWizard> WIZ_CAP = null;
	
	private IWizard instance = WIZ_CAP.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == WIZ_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == WIZ_CAP ? WIZ_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		// TODO Auto-generated method stub
		return WIZ_CAP.getStorage().writeNBT(WIZ_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		// TODO Auto-generated method stub
		WIZ_CAP.getStorage().readNBT(WIZ_CAP, this.instance, null, nbt);
	}
	
}
