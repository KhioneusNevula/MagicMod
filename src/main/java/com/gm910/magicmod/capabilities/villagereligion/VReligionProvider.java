package com.gm910.magicmod.capabilities.villagereligion;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class VReligionProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IVillageReligion.class)
	public static final Capability<IVillageReligion> REL_CAP = null;
	
	private IVillageReligion instance = REL_CAP.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == REL_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return capability == REL_CAP ? REL_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		// TODO Auto-generated method stub
		return REL_CAP.getStorage().writeNBT(REL_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		// TODO Auto-generated method stub
		REL_CAP.getStorage().readNBT(REL_CAP, this.instance, null, nbt);
	}
	
}
