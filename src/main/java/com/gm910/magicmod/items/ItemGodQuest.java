package com.gm910.magicmod.items;

import java.util.List;

import com.gm910.magicmod.deity.util.Deity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemGodQuest extends ItemBase implements IDivineItem {

	public final Deity deity;
	
	public ItemGodQuest(String name, Deity deity) {
		super(name);
		this.deity = deity;
	}

	@Override
	public Deity getDeity() {
		
		return deity;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add((new TextComponentTranslation("deity.questitemtooltip", deity)).getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
}
