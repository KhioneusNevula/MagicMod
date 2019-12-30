package com.gm910.magicmod.capabilities.wizard;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants.NBT;

public class WizardStorage implements IStorage<IWizard> {

	@Override
	public NBTBase writeNBT(Capability<IWizard> capability, IWizard instance, EnumFacing side) {
		NBTTagCompound sp = new NBTTagCompound();
		sp.setDouble("Power", instance.getMagicPower());
		sp.setDouble("FullAmount", instance.getFullAmount());
		sp.setString("Spell", instance.getActiveSpell().toString());
		sp.setBoolean("IsWizard", instance.isWizard());
		return sp;
	}

	@Override
	public void readNBT(Capability<IWizard> capability, IWizard instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound)nbt;
		instance.setActiveSpell(IWizard.Spell.fromString(tag.getString("Spell")));
		instance.setFullAmountTo(tag.getDouble("FullAmount"));
		instance.setMagicPower(tag.getDouble("Power"));
		instance.setIsWizard(tag.getBoolean("IsWizard"));
	}



}
