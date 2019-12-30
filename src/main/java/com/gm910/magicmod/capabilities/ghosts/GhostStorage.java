package com.gm910.magicmod.capabilities.ghosts;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class GhostStorage implements IStorage<IGhost> {

	@Override
	public NBTBase writeNBT(Capability<IGhost> capability, IGhost instance, EnumFacing side) {
		NBTTagCompound sp = new NBTTagCompound();
		sp.setBoolean("IsGhost", instance.isGhost());
		if (instance.getEntity() != null) {
			sp.setUniqueId("ID", instance.getEntity().getPersistentID());
		}
		if (instance.getKiller() != null) {
			sp.setUniqueId("Killer", instance.getKiller());
		}
		return sp;
	}

	@Override
	public void readNBT(Capability<IGhost> capability, IGhost instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound)nbt;
		instance.setGhost(tag.getBoolean("IsGhost"));
		if (tag.getUniqueId("ID") != null) {
			instance.uniqueID(tag.getUniqueId("ID"));
		}
		if (tag.getUniqueId("Killer") != null) {
			instance.setKiller(tag.getUniqueId("Killer"));
		}
	}

}
