package com.gm910.magicmod.deity;

import java.util.ArrayList;
import java.util.List;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.deities.DeityLeviathan;
import com.gm910.magicmod.deity.deities.DeityVillager;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.Deity.Quest;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;

//-4341446870352739196
public class Deities {
	
	
	
	public static ArrayList<Class<? extends Quest>> questTypes = new ArrayList<Class<? extends Quest>>();
	
	public static final List<Deity> deities = new ArrayList<Deity>();
	public static ArrayList<Class<? extends EntityLivingBase>> naturallyDevout = new ArrayList<Class<? extends EntityLivingBase>>();
	
	//public static final Deity SIMPLE_TRADER = (new DeityTrader(new ResourceLocation(MagicMod.MODID, "simpler_trader"), "Simple Trader", false, new ItemStack(Items.APPLE, 1))).putTrade(new ItemStack(Items.APPLE, 1), new ItemStack(Items.BANNER, 1));
	public static final Deity LEVIATHAN = new DeityLeviathan(new ResourceLocation(MagicMod.MODID, "leviathan"), "Leviathan", false, false, new ItemStack(Items.WATER_BUCKET, 1));
	public static final Deity SEAR = new DeityVillager(new ResourceLocation(MagicMod.MODID, "sear"), "Sear", false, false, new ItemStack(Items.EMERALD, 10));
		
	public static Deity getByResourceLocation(ResourceLocation rl) {
		for (Deity d : deities) {
			if (d.getUnlocName().equals(rl)) {
				return d;
			}
		}
		return null;
	}
	
	public static void registerQuestType(Class<? extends Quest> questType) {
		questTypes.add(questType);
	}
	
	public static void registerNaturallyDevout(Class<? extends EntityLivingBase> dev) {
		if (!naturallyDevout.contains(dev)) {
			naturallyDevout.add(dev);
		}
	}

	public static Deity fromString(String unlocName) {
		if (!deities.isEmpty()) {
			for (Deity d : deities) {
				if (d.getUnlocName().equals(new ResourceLocation(unlocName))) {
					return d;
				}
			}
		}
		return null;
	}
	
	public static void summonDeity(WorldServer world, EntityLivingBase summoner, BlockPos pos, ArrayList<EntityItem> itemEntities) {
		if (deities.isEmpty()) return;
		for (Deity d : deities) {
			//System.out.println(d);
			if (d.canSummon(world, summoner, pos, itemEntities)) {
				d.summon(world, summoner, pos, itemEntities);
				return;
			}
		}
	}
	
	public static void initDeities() {
		//registerDeity(SIMPLE_TRADER);
		registerDeity(LEVIATHAN);
		registerDeity(SEAR);
	}
	
	public static void registerDeity(Deity d) {
		deities.add(d);
	}

		
		public static void readFromNBT(NBTTagCompound nbt) {
			//resetDeities();
			//System.out.println("Reading from nbt");
			NBTTagList list = nbt.getTagList("DeityList", NBT.TAG_COMPOUND);
			if (list == null) return;
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				Deity deity = getByResourceLocation(new ResourceLocation(tag.getString("Deity")));
				if (deity != null) {
					//System.out.println(deity);
					deity.deserializeNBT(tag.getCompoundTag("Data"));
					
				}
				
			}
		}
		
		public static void resetDeities() {
			System.out.println("Resetting deitydata");
			for (Deity d : deities) {
				d.reset();
			}
		}
		
		
		public static NBTTagCompound writeToNBT(NBTTagCompound compound) {
			NBTTagList list = new NBTTagList();
			
			for (Deity deity : deities) {
				NBTTagCompound deityTag = new NBTTagCompound();
				deityTag.setTag("Data", deity.serializeNBT());
				deityTag.setString("Deity", deity.getUnlocName().toString());
				list.appendTag(deityTag);
			}
			compound.setTag("DeityList", list);
			//resetDeities();
			return compound;
		}

	
}
