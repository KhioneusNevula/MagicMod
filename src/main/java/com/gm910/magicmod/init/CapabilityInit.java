package com.gm910.magicmod.init;


import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.capabilities.ghosts.Ghost;
import com.gm910.magicmod.capabilities.ghosts.GhostProvider;
import com.gm910.magicmod.capabilities.ghosts.GhostStorage;
import com.gm910.magicmod.capabilities.ghosts.IGhost;
import com.gm910.magicmod.capabilities.souls.ISouls;
import com.gm910.magicmod.capabilities.souls.SoulProvider;
import com.gm910.magicmod.capabilities.souls.Souls;
import com.gm910.magicmod.capabilities.souls.SoulsStorage;
import com.gm910.magicmod.capabilities.villagereligion.IVillageReligion;
import com.gm910.magicmod.capabilities.villagereligion.VReligionProvider;
import com.gm910.magicmod.capabilities.villagereligion.VillageReligion;
import com.gm910.magicmod.capabilities.villagereligion.VillageReligionStorage;
import com.gm910.magicmod.deity.capability.DeityDataLoader;
import com.gm910.magicmod.deity.capability.DeityDataProvider;
import com.gm910.magicmod.deity.capability.DeityDataStorage;
import com.gm910.magicmod.deity.capability.IDeityData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class CapabilityInit {
	
	public static final ResourceLocation SOUL_CAP = new ResourceLocation(MagicMod.MODID, "cap_soul");
	public static final ResourceLocation REL_CAP = new ResourceLocation(MagicMod.MODID, "cap_village_religion");
	public static final ResourceLocation DEITY_CAP = new ResourceLocation(MagicMod.MODID, "cap_deitydata");
	public static final ResourceLocation GHOST_CAP = new ResourceLocation(MagicMod.MODID, "cap_ghost");
	
	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if ((event.getObject() instanceof EntityPlayer)) {
			event.addCapability(SOUL_CAP, new SoulProvider());
		}
		if (event.getObject() instanceof EntityLivingBase) {
			event.addCapability(GHOST_CAP, new GhostProvider());
		}
	}
	
	@SubscribeEvent
	public static void capabilityVillage(AttachCapabilitiesEvent<Village> event) {
		event.addCapability(REL_CAP, new VReligionProvider());
		
	}
	
	@SubscribeEvent
	public static void capabilityDD(AttachCapabilitiesEvent<World> event) {
		if (event.getObject().provider.getDimension() != 0) return;
		
		event.addCapability(DEITY_CAP, new DeityDataProvider());
	}
	
	@SubscribeEvent
	public static void capClone(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			ISouls cap = event.getOriginal().getCapability(SoulProvider.SOUL_CAP, null);
			ISouls newCap = event.getEntityPlayer().getCapability(SoulProvider.SOUL_CAP, null);
			newCap.getTagList().clear();
			for (NBTTagCompound comp : cap.getTagList()) {
				newCap.addSoul(comp);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void preInit() {
		CapabilityManager.INSTANCE.register(ISouls.class, new SoulsStorage(), Souls.class);
		CapabilityManager.INSTANCE.register(IVillageReligion.class, new VillageReligionStorage(), VillageReligion.class);
		CapabilityManager.INSTANCE.register(IDeityData.class, new DeityDataStorage(), DeityDataLoader.class);
		CapabilityManager.INSTANCE.register(IGhost.class, new GhostStorage(), Ghost.class);
	}

}
