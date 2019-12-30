package com.gm910.magicmod.init;

import java.util.HashMap;
import java.util.Map;

import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.effects.DeityPotion;
import com.gm910.magicmod.effects.DeityPotionType;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PotionInit {

	public static final Map<Deity, DeityPotion> BLESSINGS = new HashMap<Deity, DeityPotion>();
	public static final Map<Deity, DeityPotion> CURSES = new HashMap<Deity, DeityPotion>();
	
	public static final Map<Deity, PotionType> BLESSING_BOTTLES = new HashMap<Deity, PotionType>();
	public static final Map<Deity, PotionType> CURSE_BOTTLES = new HashMap<Deity, PotionType>();
	
	public static final Map<Deity, PotionType> LONG_BLESSING_BOTTLES = new HashMap<Deity, PotionType>();
	public static final Map<Deity, PotionType> LONG_CURSE_BOTTLES = new HashMap<Deity, PotionType>();
	
	
	public static void initDeityEffectsAndBottles() {
		for (Deity d : Deities.deities) {
			DeityPotion blessing = new DeityPotion(d, false);
			DeityPotion curse = new DeityPotion(d, true);
			BLESSINGS.put(d, blessing);
			CURSES.put(d, curse);
			
			BLESSING_BOTTLES.put(d, new DeityPotionType(blessing, 2400, ""));
			CURSE_BOTTLES.put(d, new DeityPotionType(curse, 2400, ""));
			LONG_BLESSING_BOTTLES.put(d, new DeityPotionType(blessing, 4800, "long_"));
			LONG_CURSE_BOTTLES.put(d, new DeityPotionType(curse, 4800, "long_"));
			
		}
	}
	
	public static void registerPotions() {
		for (Deity d : Deities.deities) {
			registerPotion(BLESSING_BOTTLES.get(d), LONG_BLESSING_BOTTLES.get(d), BLESSINGS.get(d));
			registerPotion(CURSE_BOTTLES.get(d), LONG_CURSE_BOTTLES.get(d), CURSES.get(d));
		}
		
		registerPotionMixes();
	}
	
	public static void registerPotion(PotionType defaultPotion, PotionType longPotion, Potion effect) {
		registerPotion(effect);
		if (defaultPotion != null) {
			ForgeRegistries.POTION_TYPES.register(defaultPotion);
		}
		if (defaultPotion != null) {
			ForgeRegistries.POTION_TYPES.register(longPotion);
		}
	}
	
	public static void registerPotion(Potion effect) {
		
		if (effect != null) {
			ForgeRegistries.POTIONS.register(effect);
		}
	}
	
	public static void registerPotionMixes() {
		
	}
	
	public static void registerPotionMix(PotionType potionToReturn, PotionType potionToMakeFrom, Item reagent) {
		PotionHelper.addMix(potionToMakeFrom, reagent, potionToReturn);
	}
	
	public static void registerLongPotionMix(PotionType short_, PotionType long_) {
		PotionHelper.addMix(short_, Items.REDSTONE, long_);
	}
	
}
