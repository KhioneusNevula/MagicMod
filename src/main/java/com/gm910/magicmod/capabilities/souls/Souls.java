package com.gm910.magicmod.capabilities.souls;

import java.util.ArrayList;
import java.util.function.Predicate;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class Souls implements ISouls {

	private ArrayList<NBTTagCompound> souls =  new ArrayList<NBTTagCompound>();

	@Override
	public void addSoul(EntityLivingBase b, WorldServer world) {
		souls.add(b.serializeNBT());
	}

	@Override
	public void addSoul(EntityLivingBase b) {
		souls.add(b.serializeNBT());
	}

	@Override
	public void addSoul(NBTTagCompound tag) {
		souls.add(tag);
	}

	@Override
	public <T extends EntityLivingBase> ArrayList<T> getSoulsByClass(Class<? extends T> type, WorldServer world) {
		ArrayList<T> list = new ArrayList<T>();
		if (souls.isEmpty()) return list;
		ArrayList<NBTTagCompound> soulsCopy = new ArrayList<NBTTagCompound>(souls);
		for (NBTTagCompound tag : soulsCopy) {
			EntityLivingBase b = (EntityLivingBase)EntityList.createEntityFromNBT(tag, world);
			if (b == null) {
				souls.remove(tag);
				continue;
			}
			if (b.getClass().equals(type)) {
				list.add((T)b);
			}
		}
		return list;
	}

	@Override
	public ArrayList<EntityLivingBase> getSoulsByPredicate(WorldServer world,
			Predicate<? super EntityLivingBase> pred) {
		ArrayList<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
		
		if (souls.isEmpty()) return list;
		ArrayList<NBTTagCompound> soulsCopy = new ArrayList<NBTTagCompound>(souls);
		
		for (NBTTagCompound tag : soulsCopy) {
			EntityLivingBase b = (EntityLivingBase)EntityList.createEntityFromNBT(tag, world);
			if (b == null) {
				souls.remove(tag);
				continue;
			}
			list.add(b);
		}
		list.removeIf(pred.negate());
		
		return null;
	}

	@Override
	public ArrayList<NBTTagCompound> getSoulsByPredicate(Predicate<? super NBTTagCompound> pred) {
		ArrayList<NBTTagCompound> list = new ArrayList<NBTTagCompound>(souls);
		list.removeIf(pred.negate());
		return null;
	}

	@Override
	public ArrayList<NBTTagCompound> getSoulTagsByPredicate(WorldServer world,
			Predicate<? super EntityLivingBase> pred) {
		ArrayList<NBTTagCompound> list = new ArrayList<NBTTagCompound>();
		
		if (souls.isEmpty()) return list;
		ArrayList<NBTTagCompound> soulsCopy = new ArrayList<NBTTagCompound>(souls);
		
		for (NBTTagCompound tag : soulsCopy) {
			EntityLivingBase b = (EntityLivingBase)EntityList.createEntityFromNBT(tag, world);
			if (b == null) {
				souls.remove(tag);
				continue;
			}
			if (pred.test(b)) list.add(tag);
		}
		return list;
	}

	@Override
	public void removeSoul(WorldServer world, EntityLivingBase b) {
		if (souls.isEmpty()) return;
		ArrayList<NBTTagCompound> list = new ArrayList<NBTTagCompound>();
		for (NBTTagCompound tag : list) {
			if (EntityList.createEntityFromNBT(tag, world) == null) {
				souls.remove(tag);
				continue;
			} else {
				if (EntityList.createEntityFromNBT(tag, world).getUniqueID().equals(b.getUniqueID())) {
					souls.remove(tag);
				}
			}
		}
	}

	@Override
	public void removeSoul(NBTTagCompound uuid) {
		souls.remove(uuid);
	}

	@Override
	public ArrayList<NBTTagCompound> getSouls() {
		// TODO Auto-generated method stub
		return new ArrayList<NBTTagCompound>(souls);
	}

	@Override
	public ArrayList<EntityLivingBase> getSouls(WorldServer world) {
		ArrayList<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
		if (souls.isEmpty()) return list;
		ArrayList<NBTTagCompound> soulCopy = new ArrayList<NBTTagCompound>(souls);
		for (NBTTagCompound tag : soulCopy) {
			EntityLivingBase en = (EntityLivingBase)EntityList.createEntityFromNBT(tag, world);
			if (en == null) {
				souls.remove(en);
			} else {
				list.add(en);
			}
		}
		return list;
	}

	@Override
	public ArrayList<NBTTagCompound> getTagList() {
		// TODO Auto-generated method stub
		return souls;
	}

}
