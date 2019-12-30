package com.gm910.magicmod.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemSoul extends ItemBase {
	
	public ItemSoul(String name) {
		super(name);
		this.setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey("Soul")) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.hasTagCompound()) {
			entityIn.replaceItemInInventory(itemSlot, ItemStack.EMPTY);
			return;
		}
		if (!stack.getTagCompound().hasKey("Soul")) {
			entityIn.replaceItemInInventory(itemSlot, ItemStack.EMPTY);
			return;
		}
	}
	
	public static ItemStack setSoul(Entity en, ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		stack.getTagCompound().setTag("Soul", en.serializeNBT());
		
		return stack;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (stack.getTagCompound().hasKey("Soul")) {
			tooltip.add((new TextComponentTranslation("soul.soulof", EntityList.createEntityFromNBT(stack.getTagCompound().getCompoundTag("Soul"), worldIn).getName())).getFormattedText());
		} else {
			tooltip.add((new TextComponentTranslation("soul.soulless")).getFormattedText());
		}
	}


}
