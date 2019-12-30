package com.gm910.magicmod.effects;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.text.TextComponentTranslation;

public class DeityPotionType extends PotionType {

	public final DeityPotion effect;
	
	public DeityPotionType(DeityPotion effect, int time, String prefix) {
		super(prefix + effect.getRegistryName().getResourcePath(), new PotionEffect(effect, time));
		setRegistryName(prefix + effect.getRegistryName().getResourcePath());
		this.effect = effect;
	}
	
	@Override
	public String getNamePrefixed(String prefix) {
		String pref = prefix;
		if (pref.equalsIgnoreCase("potion.effect.")) pref = (new TextComponentTranslation("deity.potion", effect.getName())).getFormattedText();
		if (pref.equalsIgnoreCase("splash_potion.effect.")) pref = (new TextComponentTranslation("deity.splashpotion", effect.getName())).getFormattedText();
		if (pref.equalsIgnoreCase("lingering_potion.effect.")) pref = (new TextComponentTranslation("deity.lingeringpotion", effect.getName())).getFormattedText();
		if (pref.equalsIgnoreCase("tipped_arrow.effect.")) pref = (new TextComponentTranslation("deity.tippedarrow." + (effect.isBadEffect() ? "cursed" : "blessing"), effect.getName())).getFormattedText();
		return pref;
	}
	
}
