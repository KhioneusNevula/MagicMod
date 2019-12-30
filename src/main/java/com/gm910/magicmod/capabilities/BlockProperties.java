package com.gm910.magicmod.capabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.util.ServerPos;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

//-4341446870352739196
public class BlockProperties extends WorldSavedData {
	
	public final Map<ServerPos, NBTTagCompound> blockProperties = new HashMap<ServerPos, NBTTagCompound>();
	
	private static final String NAME = MagicMod.MODID + "_blockProperties";
	
	public final ArrayList<UUID> litEntities = new ArrayList<UUID>();
		
	public BlockProperties() {
			super(NAME);
	}

		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			blockProperties.clear();
			NBTTagList list = nbt.getTagList("List", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				blockProperties.put(ServerPos.fromNBT(tag.getCompoundTag("Position")), tag.getCompoundTag("Properties"));
			}
			
			list = nbt.getTagList("Light", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				litEntities.add(tag.getUniqueId("Entity"));
			}
			markDirty();
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound compound) {
			NBTTagList list = new NBTTagList();
			for (ServerPos pos : blockProperties.keySet()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setTag("Position", pos.toNBT());
				tag.setTag("Properties", blockProperties.get(pos));
				list.appendTag(tag);
			}
			compound.setTag("List", list);
			list = new NBTTagList();
			for (UUID uuid : litEntities) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("Entity", uuid);
				list.appendTag(tag);
			}
			compound.setTag("Light", list);
			markDirty();
			return compound;
		}
		
		public NBTTagCompound getProperties(ServerPos pos) {
			if (blockProperties.containsKey(pos)) {
				markDirty();
				return blockProperties.get(pos);
			}
			else {
				blockProperties.put(pos, new NBTTagCompound());
				markDirty();
				return blockProperties.get(pos);
			}
		}
		
		public void setProperties(ServerPos pos, NBTTagCompound co) {
			blockProperties.put(pos, co);
			markDirty();
		}
		
		public NBTTagCompound getProperties(World world, BlockPos pos) {
			markDirty();
			return getProperties(new ServerPos(pos, world.provider.getDimension()));
		}
		
		public ArrayList<ServerPos> getAllWithKey(String key) {
			ArrayList<ServerPos> poses = new ArrayList<ServerPos>();
			for (ServerPos pos : blockProperties.keySet()) {
				if (blockProperties.get(pos).hasKey(key)) {
					poses.add(pos);
				}
			}
			markDirty();
			return poses;
		}
		
		public ArrayList<ServerPos> getAllWithUUID(String key, UUID value) {
			ArrayList<ServerPos> poses = new ArrayList<ServerPos>();
			for (ServerPos pos : blockProperties.keySet()) {
				if (blockProperties.get(pos).hasKey(key)) {
					if (blockProperties.get(pos).getUniqueId(key).equals(value)) {
						poses.add(pos);
					}
				}
			}
			markDirty();
			return poses;
		}
		
		public ArrayList<ServerPos> getAllWithBoolean(String key, boolean value) {
			ArrayList<ServerPos> poses = new ArrayList<ServerPos>();
			for (ServerPos pos : blockProperties.keySet()) {
				if (blockProperties.get(pos).hasKey(key)) {
					if (blockProperties.get(pos).getBoolean(key) == value) {
						poses.add(pos);
					}
				}
			}
			markDirty();
			return poses;
		}
		
		public ArrayList<ServerPos> getAllWithInteger(String key, int value) {
			ArrayList<ServerPos> poses = new ArrayList<ServerPos>();
			for (ServerPos pos : blockProperties.keySet()) {
				if (blockProperties.get(pos).hasKey(key)) {
					if (blockProperties.get(pos).getInteger(key) == value) {
						poses.add(pos);
						
					}
				}
			}
			markDirty();
			return poses;
		}
		
		public ArrayList<ServerPos> getAllWithDouble(String key, double value) {
			ArrayList<ServerPos> poses = new ArrayList<ServerPos>();
			for (ServerPos pos : blockProperties.keySet()) {
				if (blockProperties.get(pos).hasKey(key)) {
					if (blockProperties.get(pos).getDouble(key) == value) {
						poses.add(pos);
						
					}
				}
			}
			markDirty();
			return poses;
		}
		
		public ArrayList<UUID> getLitEntities(){ 
			markDirty();
			return this.litEntities;
		}
		
		public boolean isLit(UUID entity) {
			markDirty();
			return this.litEntities.contains(entity);
		}
		
		public void addLitEntity(UUID base) {
			this.litEntities.add(base);
			markDirty();
		}
		
		public void removeLitEntity(UUID base) {
			this.litEntities.remove(base);
			markDirty();
		}
		
		public boolean isLit(Entity entity) {
			return this.isLit(entity.getUniqueID());
		}
		
		public void addLitEntity(Entity entity) {
			addLitEntity(entity.getUniqueID());
		}
		
		public void removeLitEntity(Entity entity) {
			this.removeLitEntity(entity.getUniqueID());
		}
		
		public BlockProperties get(World w) {
			MapStorage storage = w.getMapStorage();
			BlockProperties instance = (BlockProperties) storage.getOrLoadData(BlockProperties.class, NAME);

			if (instance == null) {
			  instance = new BlockProperties();
			  storage.setData(NAME, instance);
			}
			
			return instance;
		}
		
	
}
