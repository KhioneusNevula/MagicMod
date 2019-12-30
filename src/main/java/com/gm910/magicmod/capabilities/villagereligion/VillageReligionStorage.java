package com.gm910.magicmod.capabilities.villagereligion;

import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.ServerPos;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class VillageReligionStorage implements IStorage<IVillageReligion> {

	@Override
	public NBTBase writeNBT(Capability<IVillageReligion> capability, IVillageReligion instance, EnumFacing side) {
		NBTTagCompound sp = new NBTTagCompound();
		if (instance.getPriestID() != null) {
			sp.setUniqueId("Priest", instance.getPriestID());
		}
		if (instance.getReligion() != null) {
			sp.setString("Religion", instance.getReligion().toString());
		}
		
		if (instance.getVillage() != null) {
			sp.setTag("Village", (new ServerPos(instance.getVillage(), instance.getDimension())).toNBT());
		}
		sp.setBoolean("HBV", instance.hasBeenVisited());
		return sp;
	}

	@Override
	public void readNBT(Capability<IVillageReligion> capability, IVillageReligion instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound)nbt;
		if (tag.hasKey("Priest")) {
			instance.setPriestID(tag.getUniqueId("Priest"));
		}
		if (tag.hasKey("Religion")) {
			instance.setReligion(Deities.fromString(tag.getString("Religion")));
		}
		
		if (tag.hasKey("Village")) {
			instance.setVillage(ServerPos.fromNBT(tag.getCompoundTag("Village")).getPos());
			instance.setDimension(ServerPos.fromNBT(tag.getCompoundTag("Village")).getD());
		}
		instance.setHasBeenVisited(tag.getBoolean("HBV"));
	}

}
