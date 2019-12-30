package com.gm910.magicmod.handling.util;

import java.util.UUID;

import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public abstract class TextUtil {
	
	public static String translation(String key, Object...args) {
		return TextFormatting.getTextWithoutFormattingCodes((new TextComponentTranslation(key, args)).getFormattedText());
	}
	
	public static String jsonize(String tc) {
		TextComponentString string = new TextComponentString(tc);
		return ITextComponent.Serializer.componentToJson(string);
	}
	
	public static String jsonize(ITextComponent tc) {
		return ITextComponent.Serializer.componentToJson(tc);
	}
	
	public static ITextComponent dejsonize(String str) {
		return ITextComponent.Serializer.fromJsonLenient(str);
	}
	
	public static String nameFromUUID(UUID uu) {
		Entity en = CommonProxy.getServer().getEntityFromUuid(uu);
		if (en != null) {
			return TextFormatting.getTextWithoutFormattingCodes(en.getDisplayName().getFormattedText());
		}
		return "";
	}
}
