package com.gm910.magicmod.capabilities.souls;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SoulProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ISouls.class)
	public static final Capability<ISouls> SOUL_CAP = null;
	
	private ISouls instance = SOUL_CAP.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == SOUL_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == SOUL_CAP ? SOUL_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		// TODO Auto-generated method stub
		return SOUL_CAP.getStorage().writeNBT(SOUL_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		// TODO Auto-generated method stub
		SOUL_CAP.getStorage().readNBT(SOUL_CAP, this.instance, null, nbt);
	}
	
}
