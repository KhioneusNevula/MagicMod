package com.gm910.magicmod.capabilities.villagereligion;

import java.util.List;
import java.util.UUID;

import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.ServerPos;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;

public class VillageReligion implements IVillageReligion {

	private Deity deity = null;
	
	private UUID priest = null;
	private BlockPos vill = null;
	private int dimension = -1;
	private boolean hbf;
	
	@Override
	public IVillageReligion setReligion(Deity deity) {
		this.deity = deity;
		if (deity == null) {
			if (vill != null) {
				deity.getVillReligion().remove(new ServerPos(vill, dimension));
			}
		} else {
			if (vill != null) {
				deity.getVillReligion().add(new ServerPos(vill, dimension));
			}
		}
		return this;
	}

	@Override
	public IVillageReligion removeReligion() {
		this.deity = null;
		return this;
	}

	@Override
	public Deity getReligion() {
		
		return deity;
	}

	@Override
	public UUID getPriestID() {
		
		return priest;
	}

	@Override
	public IVillageReligion setPriestID(UUID id) {
		this.priest = id;
		return this;
	}

	@Override
	public EntityLivingBase getPriest(MinecraftServer server) {
		if (priest != null) {
			return (EntityLivingBase) server.getEntityFromUuid(priest);
		}
		return null;
	}

	@Override
	public IVillageReligion setPriest(EntityLivingBase base) {
		priest = base.getPersistentID();
		return this;
	}

	@Override
	public IVillageReligion setVillage(Village vill) {
		this.vill = vill.getCenter();
		return this;
	}

	@Override
	public int getDimension() {
		// TODO Auto-generated method stub
		return dimension;
	}

	@Override
	public IVillageReligion setDimension(int d) {
		dimension = d;
		return this;
	}

	@Override
	public IVillageReligion setVillage(BlockPos vill) {
		this.vill = vill;
		return this;
	}

	@Override
	public BlockPos getVillage() {
		
		return this.vill;
	}

	@Override
	public Village getVillage(MinecraftServer server) {
		List<Village> villages = server.getWorld(dimension).getVillageCollection().getVillageList();
		for (Village g : villages) {
			if (g.getCenter().equals(this.vill)) {
				return g;
			}
		}
		return null;
	}

	@Override
	public boolean hasBeenVisited() {
		// TODO Auto-generated method stub
		return hbf;
	}

	@Override
	public IVillageReligion setHasBeenVisited(boolean h) {
		hbf = h;
		return this;
	}

}
