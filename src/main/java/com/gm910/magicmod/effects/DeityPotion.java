package com.gm910.magicmod.effects;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.util.Deity;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class DeityPotion extends Potion {

	public final Deity deity;
	
	public DeityPotion(Deity deity, boolean isCurse) {
		super(isCurse, isCurse ? deity.getCurseColor() : deity.getBlessingColor());
		setPotionName("effect." + (isCurse ? "curse" : "blessing") + "_"+ deity.getUnlocName().getResourcePath());
		setIconIndex(isCurse ? 1 : 0, 0);
		setRegistryName(new ResourceLocation(MagicMod.MODID, (isCurse ? "curse_" : "blessing_") + deity.getUnlocName().getResourcePath()));
		this.deity = deity;
	}
	
	@Override
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MagicMod.MODID + ":" + "textures/gui/potion_effects.png"));
		return true;
	}
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return (new TextComponentTranslation((this.isBadEffect() ? "deity.curseeffect" : "deity.blessingeffect"), deity.domain.toString())).getFormattedText();
	}
	
}
