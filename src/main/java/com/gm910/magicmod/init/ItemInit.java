package com.gm910.magicmod.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.items.ItemBase;
import com.gm910.magicmod.items.ItemBloodOrb;
import com.gm910.magicmod.items.ItemDemonOrb;
import com.gm910.magicmod.items.ItemGodKillerDebug;
import com.gm910.magicmod.items.ItemHoler;
import com.gm910.magicmod.items.ItemHolyBook;
import com.gm910.magicmod.items.ItemSoul;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=MagicMod.MODID)
public class ItemInit {
	
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item DEMON_ORB = new ItemDemonOrb("demon_orb");
	public static final Item CHASMATOR = new ItemHoler("chasmator");
	public static final Item BLOOD_ORB = new ItemBloodOrb("blood_orb");
	public static final Item GODKILLER = new ItemGodKillerDebug("god_killer");
	public static final Item SOUL = new ItemSoul("soul");
	public static final Map<Deity, Item> ITEM_STATUES = new HashMap<Deity, Item>();
	public static final Map<Deity, Item> ITEM_HOLY_BOOKS = new HashMap<Deity, Item>();
	
	public static void createHolyBooks() {
		for (Deity d : MagicMod.proxy.getDeities().deities) {
			ITEM_HOLY_BOOKS.put(d, new ItemHolyBook(d));
		}
	}
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		ITEMS.addAll(ITEM_HOLY_BOOKS.values());
		/*for (Deity deity : Deities.deities) {
			for (Item item : deity.questItems.values()) {
				if (!ITEMS.contains(item)) {
					ITEMS.add(item);
				}
			}
		}*/
		event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
		
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		for (Item it : ITEMS) {
			registerRender(it);
		}
	}
	
	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
	}

}
