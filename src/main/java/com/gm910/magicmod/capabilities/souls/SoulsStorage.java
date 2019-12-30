package com.gm910.magicmod.capabilities.souls;

import com.gm910.magicmod.capabilities.wizard.IWizard;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants.NBT;

public class SoulsStorage implements IStorage<ISouls> {

	@Override
	public NBTBase writeNBT(Capability<ISouls> capability, ISouls instance, EnumFacing side) {
		NBTTagCompound sp = new NBTTagCompound();
		NBTTagList soulList = new NBTTagList();
		if (!instance.getTagList().isEmpty()) {
			for (NBTTagCompound tg : instance.getTagList()) {
				soulList.appendTag(tg);
			}
		}
		sp.setTag("Souls", soulList);
		return sp;
	}

	@Override
	public void readNBT(Capability<ISouls> capability, ISouls instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound)nbt;
		NBTTagList soulList = tag.getTagList("Souls", NBT.TAG_COMPOUND);
		for (int i = 0; i < soulList.tagCount(); i++) {
			NBTTagCompound comp = soulList.getCompoundTagAt(i);
			instance.getTagList().add(comp);
		}
	}

}
