package com.gm910.magicmod.capabilities.villagereligion;

import java.util.UUID;

import com.gm910.magicmod.deity.util.Deity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;

public interface IVillageReligion {
	public IVillageReligion setReligion(Deity deity);
	public IVillageReligion removeReligion();
	public Deity getReligion();
	public UUID getPriestID();
	public IVillageReligion setPriestID(UUID id);
	public EntityLivingBase getPriest(MinecraftServer server);
	public IVillageReligion setPriest(EntityLivingBase base);
	public IVillageReligion setVillage(BlockPos vill);
	public IVillageReligion setVillage(Village vill);
	public BlockPos getVillage();
	public Village getVillage(MinecraftServer server);
	public int getDimension();
	public IVillageReligion setDimension(int d);
	public boolean hasBeenVisited();
	public IVillageReligion setHasBeenVisited(boolean h);
}
